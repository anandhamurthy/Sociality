package com.sociality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sociality.Adapter.SelectedImageAdapter;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class NewEventActivity extends AppCompatActivity {

    private RelativeLayout New_Event_Donation_Layout, New_Event_Volunteer_Layout, New_Event_Join_Hands_Layout;
    private EditText New_Event_Donation_Web_Page;
    private EditText New_Event_Volunteer_Members;
    private EditText New_Event_Join_Hands_Members;
    private ImageView New_Event_Close;
    private CheckBox New_Event_Donations, New_Event_Join_Hands, New_Event_Volunteer;
    private FloatingActionButton New_Event_Done, New_Event_Add_Image;
    private GridView New_Event_Selected_Images;
    private EditText New_Event_Name, New_Event_Description, New_Event_Website, New_Event_Contact_Number, New_Event_Incharge_Person, New_Event_Incharge_Person_Email_Id;

    private static final int CustomGallerySelectId = 1;
    public static final String CustomGalleryIntentKey = "ImageArray";
    boolean images_got=false;
    ArrayList<String> image_url = new ArrayList<>(3);

    private StorageReference mEventsStorage;

    private String mCurrentUserId;
    private FirebaseAuth mAuth;
    private DatabaseReference mEventsDatabase;

    private EditText year;
    private Spinner day, month;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUserId = mAuth.getCurrentUser().getUid();

        New_Event_Close = findViewById(R.id.new_event_close);

        New_Event_Name = findViewById(R.id.new_event_name);
        New_Event_Description = findViewById(R.id.new_event_description);
        New_Event_Incharge_Person = findViewById(R.id.new_event_incharge_person);
        New_Event_Incharge_Person_Email_Id = findViewById(R.id.new_event_incharge_person_email_id);
        New_Event_Contact_Number = findViewById(R.id.new_event_contact_number);
        New_Event_Website = findViewById(R.id.new_event_website);

        New_Event_Donations = findViewById(R.id.new_event_donation);
        New_Event_Join_Hands = findViewById(R.id.new_event_join_hands);
        New_Event_Volunteer = findViewById(R.id.new_event_volunteer);
        New_Event_Selected_Images = findViewById(R.id.new_events_selected_images);
        New_Event_Donation_Web_Page = findViewById(R.id.new_event_donation_web_page);
        New_Event_Volunteer_Members = findViewById(R.id.new_event_volunteer_members);
        New_Event_Join_Hands_Members = findViewById(R.id.new_event_join_hands_members);
        New_Event_Donation_Web_Page = findViewById(R.id.new_event_donation_web_page);

        New_Event_Add_Image = findViewById(R.id.new_event_add_image);
        New_Event_Done = findViewById(R.id.new_event_done);

        New_Event_Donation_Layout = findViewById(R.id.new_event_donation_layout);
        New_Event_Volunteer_Layout = findViewById(R.id.new_event_volunteer_layout);
        New_Event_Join_Hands_Layout = findViewById(R.id.new_event_join_hands_layout);

        New_Event_Donation_Layout.setVisibility(View.GONE);
        New_Event_Volunteer_Layout.setVisibility(View.GONE);
        New_Event_Join_Hands_Layout.setVisibility(View.GONE);

        mEventsStorage = FirebaseStorage.getInstance().getReference().child("Events");
        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mEventsDatabase.keepSynced(true);

        day = findViewById(R.id.new_event_day);
        month = findViewById(R.id.new_event_month);
        year = findViewById(R.id.new_event_year);

        New_Event_Donations.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    New_Event_Donation_Layout.setVisibility(View.VISIBLE);
                }else{
                    New_Event_Donation_Layout.setVisibility(View.GONE);
                }
            }
        });

        New_Event_Volunteer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    New_Event_Volunteer_Layout.setVisibility(View.VISIBLE);
                }else{
                    New_Event_Volunteer_Layout.setVisibility(View.GONE);
                }
            }
        });

        New_Event_Join_Hands.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    New_Event_Join_Hands_Layout.setVisibility(View.VISIBLE);
                }else{
                    New_Event_Join_Hands_Layout.setVisibility(View.GONE);
                }
            }
        });

        New_Event_Add_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewEventActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }else{
                    startActivityForResult(new Intent(NewEventActivity.this, ImagePickerActivity.class), CustomGallerySelectId);
                }

            }
        });
        New_Event_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        New_Event_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewEvent();
            }
        });

        getSharedImages();
    }


    private void AddNewEvent() {

        String donation_web_page = New_Event_Donation_Web_Page.getText().toString();
        String volunteer_members = New_Event_Volunteer_Members.getText().toString();
        String join_hands_members = New_Event_Join_Hands_Members.getText().toString();

        final String name = New_Event_Name.getText().toString();
        final String desc = New_Event_Description.getText().toString();
        final String incharge_person = New_Event_Incharge_Person.getText().toString();
        final String incharge_person_email_id = New_Event_Incharge_Person_Email_Id.getText().toString();
        final String website = New_Event_Website.getText().toString();
        final String contact_number = New_Event_Contact_Number.getText().toString();

        if(isEmpty(name,desc,incharge_person,incharge_person_email_id,website,contact_number)){

            if (!day.getSelectedItem().toString().equals("Day") && !month.getSelectedItem().toString().equals("Month") && !year.getText().toString().isEmpty()){
                String date = day.getSelectedItem().toString()+"-"+month.getSelectedItem().toString()+"-"+year.getText().toString();
                if (images_got){
                    String key = mEventsDatabase.push().getKey();
                    String user_id = mAuth.getCurrentUser().getUid();
                    int count=0;
                    HashMap userMap = new HashMap<>();
                    userMap.put("description", desc);
                    userMap.put("event_key", key);
                    userMap.put("event_name", name);
                    userMap.put("incharge_person", incharge_person);
                    userMap.put("incharge_email_id", incharge_person_email_id);
                    userMap.put("website", website);
                    userMap.put("event_date", date);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy" );
                    Calendar calendar = Calendar.getInstance();
                    userMap.put("timestamp", dateFormat.format(calendar.getTime()));
                    userMap.put("contact_number", contact_number);

                    userMap.put("donation_web_page", donation_web_page);
                    userMap.put("volunteer_members", volunteer_members);
                    userMap.put("join_hands_members", join_hands_members);


                    if(image_url.size()>2){
                        userMap.put("image_one", image_url.get(0));
                        userMap.put("image_two", image_url.get(1));
                        userMap.put("image_three", image_url.get(2));
                        count=3;
                    }
                    else if(image_url.size()==2){
                        userMap.put("image_one", image_url.get(0));
                        userMap.put("image_two", image_url.get(1));
                        count=2;
                    }
                    else if(image_url.size()==1){
                        userMap.put("image_one", image_url.get(0));
                        count=1;
                    }
                    userMap.put("image_count", count);
                    userMap.put("user_id", user_id);

                    mEventsDatabase.child(key).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                finish();
                            }
                        }
                    });
                }else{
                    Toast.makeText(this, "Select Image from button", Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(this, "Select Date from button", Toast.LENGTH_SHORT).show();
            }

        }


    }

    protected void onActivityResult(int requestcode, int resultcode,
                                    final Intent intent) {
        super.onActivityResult(requestcode, resultcode, intent);
        switch (requestcode) {
            case CustomGallerySelectId:
                if (resultcode == RESULT_OK) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Upload");
                    builder.setMessage("Do you want to upload these images ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    final ProgressDialog pd = new ProgressDialog(NewEventActivity.this);
                                    pd.setMessage("Uploading");
                                    pd.setCanceledOnTouchOutside(false);
                                    pd.show();
                                    String imagesArray = intent.getStringExtra(CustomGalleryIntentKey);
                                    List<String> selectedImages = Arrays.asList(imagesArray.substring(1, imagesArray.length() - 1).split(", "));
                                    loadGridView(new ArrayList<String>(selectedImages));
                                    int count=0;

                                    if (selectedImages.size()>=3){
                                        count=2;
                                    }else if(selectedImages.size()<3){
                                        count=selectedImages.size();
                                    }
                                    for(int i=0;i<count;i++){
                                        Uri s = Uri.fromFile(new File(selectedImages.get(i)));
                                        if(s!=null) {
                                            final StorageReference fileReference = mEventsStorage.child(System.currentTimeMillis() + ".jpg");

                                            fileReference.putFile(s).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            image_url.add(uri.toString());
                                                            images_got=true;
                                                            pd.dismiss();
                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Upload");
                    alert.show();
                }
                break;

        }
    }

    private void loadGridView(ArrayList<String> imagesArray) {
        SelectedImageAdapter adapter = new SelectedImageAdapter(NewEventActivity.this, imagesArray, false);
        New_Event_Selected_Images.setAdapter(adapter);
    }

    private void getSharedImages() {

        if (Intent.ACTION_SEND_MULTIPLE.equals(getIntent().getAction())
                && getIntent().hasExtra(Intent.EXTRA_STREAM)) {
            ArrayList<Parcelable> list =
                    getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            final ArrayList<String> selectedImages = new ArrayList<>();

            for (Parcelable parcel : list) {
                Uri uri = (Uri) parcel;
                String sourcepath = getPath(uri);
                selectedImages.add(sourcepath);
            }
            loadGridView(selectedImages);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Upload");
            builder.setMessage("Do you want to upload these images ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            final ProgressDialog pd = new ProgressDialog(NewEventActivity.this);
                            pd.setMessage("Uploading");
                            pd.setCanceledOnTouchOutside(false);
                            pd.show();
                            int count=0;

                            if (selectedImages.size()>3){
                                count=3;
                            }else if(selectedImages.size()<3){
                                count=selectedImages.size();
                            }
                            for(int i=0;i<count;i++){
                                Uri s = Uri.fromFile(new File(selectedImages.get(i)));
                                if(s!=null) {
                                    final StorageReference fileReference = mEventsStorage.child(System.currentTimeMillis() + ".jpg");

                                    fileReference.putFile(s).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    image_url.add(uri.toString());
                                                    images_got=true;
                                                    pd.dismiss();
                                                }
                                            });
                                        }
                                    });

                                }
                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Upload");
            alert.show();

        }
    }


    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private boolean isEmpty(String name, String desc, String incharge_person, String incharge_person_email_id, String website, String contact_number) {
        if (name.isEmpty() || desc.isEmpty() || incharge_person.isEmpty() || incharge_person_email_id.isEmpty() || website.isEmpty() || contact_number.isEmpty()) {
            Toast.makeText(NewEventActivity.this, "Complete the form!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
