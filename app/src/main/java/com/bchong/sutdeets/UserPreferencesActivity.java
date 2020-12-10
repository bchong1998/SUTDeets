package com.bchong.sutdeets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserPreferencesActivity extends AppCompatActivity {

    EditText mUsername, mStudentID;
    CheckBox mTagsISTD, mTagsAI, mTagsFinTech, mTagsTest1, mTagsTest2;
    Button mSaveChangesButton;
    DatabaseReference mDatabase;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mCurrentUser;
    Iterable mSnapshot;
    HashMap<String, String> userTags = new HashMap<>();

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        mTagsISTD = findViewById(R.id.tagsISTD);
        mTagsAI = findViewById(R.id.tagsAI);
        mTagsFinTech = findViewById(R.id.tagsFinTech);
        mTagsTest1 = findViewById(R.id.tagsESD);
        mTagsTest2 = findViewById(R.id.tagsEPD);


        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.tagsISTD:
                if (checked){
                    if (mTagsISTD.isChecked()){
                        userTags.put(mTagsISTD.getText().toString(), "");
                    }

                    //                    System.out.println(mTagsISTD.getText().toString());
                }
                else {
                    userTags.remove(mTagsISTD.getText().toString());
                }
                System.out.println(userTags);
                break;

            case R.id.tagsAI:
                if (checked){
                    userTags.put(mTagsAI.getText().toString(), "");
                    //                    System.out.println(mTagsISTD.getText().toString());
                }
                else {
                        userTags.remove(mTagsAI.getText().toString());
                }
                System.out.println(userTags);
                break;

            case R.id.tagsFinTech:
                if (checked) {
                    userTags.put(mTagsFinTech.getText().toString(), "");
                }
                else {
                    userTags.remove(mTagsFinTech.getText().toString());
                }
                System.out.println(userTags);
                break;

            case R.id.tagsESD:
                if (checked){
                    userTags.put(mTagsTest1.getText().toString(), "");
                    //                    System.out.println(mTagsISTD.getText().toString());
                }
                else {
                    userTags.remove(mTagsTest1.getText().toString());
                }
//                System.out.println(userTags);
                break;

            case R.id.tagsEPD:
                if (checked){
                    userTags.put(mTagsTest2.getText().toString(), "");
                    //                    System.out.println(mTagsISTD.getText().toString());
                }
                else {
                    userTags.remove(mTagsTest2.getText().toString());
                }
//                System.out.println(userTags);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_preferences);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        mUsername = findViewById(R.id.preferencesName);
        mStudentID = findViewById(R.id.preferencesStudentID);



        mSaveChangesButton = findViewById(R.id.preferencesSaveChangesButton);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsername.setText(snapshot.child("Users").child(mCurrentUser.getUid()).child("name").getValue().toString());
                mStudentID.setText(snapshot.child("Users").child(mCurrentUser.getUid()).child("studentID").getValue().toString());
                for (DataSnapshot child: snapshot.child("Users").child(mCurrentUser.getUid()).child("tags").getChildren()){
                    userTags.put(child.getKey(),"");
                };

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //save changes button
        mSaveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> userDetails = new HashMap<>();


                userDetails.put("name", mUsername.getText().toString());
                userDetails.put("studentID", mStudentID.getText().toString());
                userDetails.put("email", mCurrentUser.getEmail());

                mDatabase.child("Users").child(mCurrentUser.getUid()).setValue(userDetails);
                mDatabase.child("Users").child(mCurrentUser.getUid()).child("tags").setValue(userTags);

                Intent homepage = new Intent(UserPreferencesActivity.this, MainActivity.class);
                startActivity(homepage);
            }
        });



    }

}