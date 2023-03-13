package com.example.sutdlife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.android.gms.tasks.OnCompleteListener;


import java.sql.Timestamp;

public class MainActivity extends AppCompatActivity { //implement

    TextView textViewMsg;

    final String node = "current_msg";
    DatabaseReference mRootDatabaseRef;
    DatabaseReference mNodeRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewMsg = findViewById(R.id.textViewMesg); // search for text id in xml file
        mRootDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mNodeRef = mRootDatabaseRef.child(node);

        mNodeRef.addValueEventListener(new ValueEventListener() { // database changes in parallel with client interactions
            // event listener registers to database node reference, capture what is changed for the new current value
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String change = snapshot.getValue(String.class);
                textViewMsg.setText(change); // update
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        mNodeRef.setValue(timestamp.toString()); // convert current timestamp into a string
    }

public void buildActionCodeSettings() {

    ActionCodeSettings actionCodeSettings =
            ActionCodeSettings.newBuilder()
                    // URL you want to redirect back to. The domain (www.example.com) for this
                    // URL must be whitelisted in the Firebase Console.
                    .setUrl("") // TODO Create a URL to allow users to install the app if not yet installed
                    // This must be true
                    .setHandleCodeInApp(true)
                    .setIOSBundleId("com.example.ios")
                    .setAndroidPackageName(
                            "com.example.android",
                            true, /* installIfNotAvailable */
                            "12"    /* minimumVersion */)
                    .build();
}

public void signInLink(String email, ActionCodeSettings actionCodeSettings) {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener(new OnCompleteListener<Void>() {

                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email sent.");
                    }
                }
            });
}

}