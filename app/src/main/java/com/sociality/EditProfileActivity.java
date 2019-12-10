package com.sociality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.sociality.Models.Users;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private FirebaseUser mFirebaseUser;
    private String mCurrentUserId;
    private Uri mImageUri;
    private String Gender="Male";
    private StorageTask mUploadTask;
    private StorageReference mProfileImageStorage;
    private DatabaseReference mUsersDatabase;

    private RadioButton Edit_Profile_Male, Edit_Profile_Female, Edit_Profile_Others;
    private FloatingActionButton Edit_Profile_Save;
    private TextView Edit_Profile_Email_Id, Edit_Profile_Change_Profile_Image;
    private CircleImageView Edit_Profile_Image;
    private ImageView Edit_Profile_Back;
    private EditText Edit_Profile_Name, Edit_Profile_Description, Edit_Profile_Website, Edit_Profile_Phone_Number, Edit_Profile_Place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        Edit_Profile_Image = findViewById(R.id.edit_profile_profile_image);
        Edit_Profile_Change_Profile_Image = findViewById(R.id.edit_profile_change_profile_image_text);
        Edit_Profile_Name = findViewById(R.id.edit_profile_name);
        Edit_Profile_Description = findViewById(R.id.edit_profile_description);
        Edit_Profile_Website = findViewById(R.id.edit_profile_website);
        Edit_Profile_Phone_Number = findViewById(R.id.edit_profile_phone_number);
        Edit_Profile_Place = findViewById(R.id.edit_profile_place);
        Edit_Profile_Email_Id = findViewById(R.id.edit_profile_email_address);
        Edit_Profile_Male = findViewById(R.id.edit_profile_male);
        Edit_Profile_Female = findViewById(R.id.edit_profile_female);
        Edit_Profile_Others = findViewById(R.id.edit_profile_others);
        Edit_Profile_Save = findViewById(R.id.edit_profile_save);
        Edit_Profile_Back = findViewById(R.id.edit_profile_back);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mCurrentUserId = mFirebaseUser.getUid();
        mProfileImageStorage = FirebaseStorage.getInstance().getReference("profile_images");

        mUsersDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mCurrentUserId);
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                Edit_Profile_Name.setText(user.getName());
                Edit_Profile_Description.setText(user.getDescription());
                Edit_Profile_Place.setText(user.getPlace());
                Gender = user.getGender();
                if (!Gender.isEmpty()){
                    if(Gender.equals("Male"))
                        Edit_Profile_Male.setChecked(true);
                    else if(Gender.equals("Female"))
                        Edit_Profile_Female.setChecked(true);
                    else
                        Edit_Profile_Others.setChecked(true);
                }

                Edit_Profile_Phone_Number.setText(user.getPhone_number());
                Edit_Profile_Website.setText(user.getWebsite());
                Edit_Profile_Email_Id.setText(user.getEmail_id());

                Glide.with(getApplicationContext()).load(user.getLogo()).into(Edit_Profile_Image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Edit_Profile_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Edit_Profile_Male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Gender="Male";
                }
            }
        });
        Edit_Profile_Female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Gender="Female";
                }
            }
        });
        Edit_Profile_Others.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Gender="Others";
                }
            }
        });

        Edit_Profile_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isEmpty(Edit_Profile_Name.getText().toString())) {

                    UpdateProfile(Edit_Profile_Name.getText().toString(),
                            Edit_Profile_Website.getText().toString(),
                            Edit_Profile_Place.getText().toString(), Edit_Profile_Description.getText().toString(), Gender, Edit_Profile_Phone_Number.getText().toString());
                }

            }
        });

        Edit_Profile_Change_Profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(EditProfileActivity.this);
            }
        });

        Edit_Profile_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .start(EditProfileActivity.this);
            }
        });

    }

    private void UpdateProfile(String name, String web, String place, String des, String gender, String phone) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("website", web);
        map.put("place", place);
        map.put("phone_number", phone);
        map.put("gender", gender);
        map.put("description", des);
        map.put("user_id", mCurrentUserId);

        mUsersDatabase.updateChildren(map);

        Toast.makeText(EditProfileActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void UploadImage() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if (mImageUri != null) {
            final StorageReference fileReference = mProfileImageStorage.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri);
            mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String miUrlOk = downloadUri.toString();

                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("logo", "" + miUrlOk);
                        mUsersDatabase.updateChildren(map1);

                        pd.dismiss();

                    } else {
                        Toast.makeText(EditProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(EditProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {


            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri = result.getUri();

            UploadImage();

        } else {
            Toast.makeText(this, "Something gone wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(String name) {
        if (name.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "User Name Should Not Be Empty!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
