package com.bchong.sutdeets;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewProfile extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener {
    TextView mfullName,memail, mStudent_id,mtag;
    Button changeProfile;
    FirebaseAuth fAuth;
    String userId;
    String tag;
    DatabaseReference mref;
    private DrawerLayout drawer;

    protected void onCreate(Bundle savedInstanceState)  {
        Log.d("start"," create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);

        ArrayList<String> TAGArray = new ArrayList<>();



        mStudent_id = (TextView) findViewById(R.id.profilePhone);
        mfullName =(TextView) findViewById(R.id.profileName);
        memail    = (TextView) findViewById(R.id.profileEmail);
        changeProfile=(Button) findViewById(R.id.changeProfile);
        mtag=(TextView)findViewById(R.id.Viewprofile_tag);

        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updatePreferences = new Intent(ViewProfile.this, UserPreferencesActivity.class);
                startActivity(updatePreferences);
            }
        });



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


// make some animation when opening menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Log.d("error","error");
//        start the app, first state is here + if quit app, return here
        if (savedInstanceState == null) {
   //         getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MessageFragment()).commit();

            navigationView.setCheckedItem(R.id.nav_profile);
        }

        Log.d("database"," database");
        fAuth = FirebaseAuth.getInstance();
        userId = fAuth.getCurrentUser().getUid();
        mref= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        Log.d(userId," create-----------------------");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("dam THIS", snapshot.toString());
                String name=snapshot.child("name").getValue().toString();
                tag=snapshot.child("tags").getValue().toString().replace("{","").replace("}","").replace("=","");

                System.out.println(tag);
                String emails=snapshot.child("email").getValue().toString();
                String Studentid1=snapshot.child("studentID").getValue().toString();
                mtag.setText(tag);
                mfullName.setText(name);
                memail.setText(emails);
                mStudent_id.setText(Studentid1);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//switch screen
        switch (item.getItemId()) {
            case R.id.nav_message:
                Intent gohome = new Intent(ViewProfile.this, MainActivity.class);
                startActivity(gohome);
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ChatFragment()).commit();
                break;
            case R.id.nav_profile:
                Intent viewprofile = new Intent(ViewProfile.this, ViewProfile.class);
                startActivity(viewprofile);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(ViewProfile.this, LoginActivity.class);
                startActivity(login);
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }
}
