package com.sociality;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.theartofdev.edmodo.cropper.CropImage;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText Register_Email_Address;
    private EditText Register_Password;
    private EditText Register_Confirm_Password;
    private EditText Register_Name, Register_Founder_Name;
    private FloatingActionButton Register_Button;
    private TextView Register_Login_Button;

    private FirebaseAuth mAuth;

    private DatabaseReference mUsersDatabase;

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_register);

        final String user_key = getIntent().getStringExtra("user_key");

        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        Register_Email_Address = findViewById(R.id.register_email);
        Register_Founder_Name = findViewById(R.id.register_founder_name);
        Register_Password = findViewById(R.id.register_password);
        Register_Confirm_Password = findViewById(R.id.register_confirm_password);
        Register_Name = findViewById(R.id.register_name);
        Register_Button = findViewById(R.id.register_button);
        Register_Login_Button = findViewById(R.id.register_login_text);
        Register_Founder_Name.setVisibility(View.GONE);

        if (user_key.equals("Organization")){
            Register_Email_Address.setHint("Organization Email id");
            Register_Name.setHint("Organization Name");
            Register_Founder_Name.setVisibility(View.VISIBLE);
        }else if(user_key.equals("Individual")){
            Register_Email_Address.setHint("Email id");
            Register_Name.setHint("Name");
            Register_Founder_Name.setVisibility(View.GONE);
        }

        Register_Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                loginIntent.putExtra("user_key",user_key);
                startActivity(loginIntent);
                finish();

            }
        });

        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = Register_Email_Address.getText().toString();
                final String pass = Register_Password.getText().toString();
                String confirm_pass = Register_Confirm_Password.getText().toString();
                final String name = Register_Name.getText().toString();
                String founder_name ="";
                if (user_key.equals("Organization"))
                    founder_name=Register_Founder_Name.getText().toString();

                if (isEmpty(email, name, pass, confirm_pass)) {

                    if (pass.length() > 5 && confirm_pass.length() > 5) {

                        if (pass.equals(confirm_pass)) {

                            mProgressDialog.setTitle("Registering");
                            mProgressDialog.setMessage("Creating User..");
                            mProgressDialog.show();
                            mProgressDialog.setCanceledOnTouchOutside(false);

                            final String finalFounder_name = founder_name;
                            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        mProgressDialog.setMessage("Creating The Profile..");

                                        String device_token = FirebaseInstanceId.getInstance().getToken();
                                        HashMap userMap = new HashMap<>();
                                        userMap.put("email_id", email);
                                        userMap.put("name", name);
                                        userMap.put("place", "");
                                        userMap.put("website", "");
                                        userMap.put("phone_number", "");
                                        userMap.put("founder", finalFounder_name);
                                        userMap.put("verified", "False");
                                        userMap.put("gender", "");
                                        userMap.put("description", "");
                                        userMap.put("user_id", mAuth.getCurrentUser().getUid());
                                        userMap.put("logo", "");
                                        userMap.put("device_token", device_token);
                                        userMap.put("user_key", user_key);


                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        String uid = current_user.getUid();

                                        mUsersDatabase.child(uid).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    mProgressDialog.dismiss();

                                                    Toast.makeText(RegisterActivity.this, "Account Created & Logging in Sucessfully", Toast.LENGTH_LONG).show();
                                                    Intent setupIntent = new Intent(RegisterActivity.this, EditProfileActivity.class);
                                                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(setupIntent);
                                                    finish();

                                                }

                                            }
                                        });


                                    } else {
                                        mProgressDialog.dismiss();
                                        String errorMessage = task.getException().getMessage();
                                        Toast.makeText(RegisterActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();

                                    }

                                }
                            });

                        } else {

                            Toast.makeText(RegisterActivity.this, "Confirm Password and Password Field doesn't match.", Toast.LENGTH_LONG).show();

                        }

                    } else {

                        Register_Password.setError("Atleast 6 Characters");
                        Register_Confirm_Password.setError("Atleast 6 Characters");
                        Toast.makeText(RegisterActivity.this, "Password must contain atleast 6 characters.", Toast.LENGTH_LONG).show();

                    }

                } else {

                    Toast.makeText(RegisterActivity.this, "Enter all the details.", Toast.LENGTH_LONG).show();

                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {

            Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish();

        }

    }

    private boolean isEmpty(String email, String name, String password, String confirm_pass) {
        if (email.isEmpty() || name.isEmpty() || password.isEmpty() || confirm_pass.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Complete All the Details", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
