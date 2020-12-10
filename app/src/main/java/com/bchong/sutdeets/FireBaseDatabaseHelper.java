package com.bchong.sutdeets;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

//manipuplating real time database
public class FireBaseDatabaseHelper {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferanceEvents;
    private List<Events> events=new ArrayList<>();
    private List<String> tags=new ArrayList<>();
    private DatabaseReference mReferanceUsers;
    FirebaseAuth fAuth;
    String userId;

    public interface DataStatus{
        void DataIsLoaded(List<Events> events,List<String> keys);
        void DataIsInserted();
        void DataIsDeleted();
        void DataIsUpdated();
    }
    public FireBaseDatabaseHelper() {
        mDatabase=FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
       mReferanceUsers= mDatabase.getReference("Users");//.child(userId).child("tags");
        mReferanceEvents=mDatabase.getReference("Events");
    }
    public void readEvents(final DataStatus datastatus){
        final List<String> tags=new ArrayList<>();
        List<String> keys=new ArrayList<>();
        datastatus.DataIsLoaded(events,keys);
        mReferanceUsers= mDatabase.getInstance().getReference("Users").child(userId).child("tags");

        mReferanceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot keyNode:snapshot.getChildren()){
                    tags.add(keyNode.getKey());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mReferanceEvents.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                events.clear();
                List<String> keys=new ArrayList<>();
                for (DataSnapshot keyNode:snapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Events event=keyNode.getValue(Events.class);
                   if(tags.contains(event.getTAG())){
                        events.add(event);}
         //          }
                }
                datastatus.DataIsLoaded(events,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
