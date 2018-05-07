package com.example.zion.workoutwithme;

// Profile Edit Java
// Lynne Diep
// Chuong Truong

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;

public class Profile_Edit extends AppCompatActivity {
    EditText nameText;
    EditText bioText;
    EditText interestText;
    Button changeActivityButton;
    User currentUser;
    public static final String CURRENT_USER_ID = "";
    private static final String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        // Current User
        Intent userInfo = getIntent();
        final String cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);

        nameText = (EditText) findViewById(R.id.Name_editText);
        bioText = (EditText) findViewById(R.id.Bio_editText);
        interestText = (EditText) findViewById(R.id.Interest_editText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User").child(cruzID);

        /*
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        */

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            currentUser = dataSnapshot.getValue(User.class);
                            nameText.setText(currentUser.getName());
                            bioText.setText(currentUser.getBio());

                            changeActivityButton = (Button) findViewById(R.id.save);
                            changeActivityButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Profile_Edit.this, activity_newsfeed.class);
                                    currentUser.setName(nameText.getText().toString());
                                    currentUser.setBio(bioText.getText().toString());
                                    userRef.setValue(currentUser);

                                    intent.putExtra(CURRENT_USER_ID, cruzID);
                                    startActivity(intent);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "It's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
