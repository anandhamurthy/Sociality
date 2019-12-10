package com.sociality.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.sociality.DetailedActivity;
import com.sociality.Models.Events;
import com.sociality.R;
import com.sociality.ZoomImageActivity;


public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH> {

    private Context context;
    private DatabaseReference mEventsDatabase;
    private int ImageCount=0;

    public ImageSliderAdapter(final Context context, String event_id, int count) {
        this.context = context;
        mEventsDatabase = FirebaseDatabase.getInstance().getReference().child("Events").child(event_id);
        mEventsDatabase.keepSynced(true);
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_layout_image_slider, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(final SliderAdapterVH viewHolder, final int position) {

        mEventsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final Events events = dataSnapshot.getValue(Events.class);

//                if (!events.getImage_one().isEmpty() && !events.getImage_two().isEmpty() && !events.getImage_three().isEmpty()){
//                    ImageCount=3;
//                }else if (!events.getImage_one().isEmpty() && !events.getImage_two().isEmpty()){
//                    ImageCount=2;
//                }else{
//                    ImageCount=1;
//                }
                switch (position) {
                    case 0:
                        Glide.with(context)
                                .load(events.getImage_one())
                                .into(viewHolder.imageViewBackground);
                        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent detailedIntent = new Intent(context, ZoomImageActivity.class);
                                detailedIntent.putExtra("image_url",events.getImage_one());
                                context.startActivity(detailedIntent);
                            }
                        });
                        break;
                    case 1:
                        Glide.with(context)
                                .load(events.getImage_two())
                                .into(viewHolder.imageViewBackground);
                        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent detailedIntent = new Intent(context, ZoomImageActivity.class);
                                detailedIntent.putExtra("image_url",events.getImage_two());
                                context.startActivity(detailedIntent);
                            }
                        });
                        break;
                    case 2:
                        Glide.with(context)
                                .load(events.getImage_three())
                                .into(viewHolder.imageViewBackground);
                        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent detailedIntent = new Intent(context, ZoomImageActivity.class);
                                detailedIntent.putExtra("image_url",events.getImage_three());
                                context.startActivity(detailedIntent);
                            }
                        });
                        break;
                    default:
                        Glide.with(context)
                                .load(events.getImage_one())
                                .into(viewHolder.imageViewBackground);
                        viewHolder.imageViewBackground.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent detailedIntent = new Intent(context, ZoomImageActivity.class);
                                detailedIntent.putExtra("image_url",events.getImage_one());
                                context.startActivity(detailedIntent);
                            }
                        });
                        break;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getCount() {
        return 3;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image);
            this.itemView = itemView;
        }
    }
}