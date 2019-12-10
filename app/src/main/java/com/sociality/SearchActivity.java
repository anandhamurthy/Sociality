package com.sociality;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sociality.Adapter.EventsAdapter;
import com.sociality.Models.Events;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView Search_List;
    private EventsAdapter eventsAdapter;
    private List<Events> eventsList;

    private DatabaseReference mEventsDatabase;
    private EditText Search_Edit_Text;
    private ImageView Search_Button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEventsDatabase = FirebaseDatabase.getInstance().getReference("Events");
        mEventsDatabase.keepSynced(true);

        Search_List = findViewById(R.id.search_list);
        Search_Edit_Text = findViewById(R.id.search_edit_text);

        Search_List.setHasFixedSize(true);
        Search_List.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        eventsList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getApplicationContext(), eventsList, true);
        Search_List.setAdapter(eventsAdapter);

        Search_Button = findViewById(R.id.search_search_icon);

        Search_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        Search_Edit_Text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchEvents(charSequence.toString().toUpperCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //  readUsers();
    }

    private void searchEvents(String s) {
        Query query = FirebaseDatabase.getInstance().getReference("Events").orderByChild("event_name").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Events events = snapshot.getValue(Events.class);
                    eventsList.add(events);
                }


                eventsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readEvens() {

        mEventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Search_Edit_Text.getText().toString().equals("")) {
                    eventsList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Events user = snapshot.getValue(Events.class);
                        eventsList.add(user);

                    }

                    eventsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
