package com.example.mypocketnavv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TripHistoryActivity extends AppCompatActivity {
    ListView listView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    DatabaseReference databaseTrips;
    List<TripHandler>tripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        //databaseTrips= FirebaseDatabase.getInstance().getReference("TripHistory").child("IxxzZgtbc8fHiUqEiinPVyksrNz1");
        databaseTrips= FirebaseDatabase.getInstance().getReference("TripHistory").child(currentUser.getUid());

        listView=findViewById(R.id.lv_tripHistory);

        tripList=new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        databaseTrips.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    tripList.clear();
                    for (DataSnapshot tripSnapshot : dataSnapshot.getChildren()) {
                        TripHandler tripHandler = tripSnapshot.getValue(TripHandler.class);
                        tripList.add(tripHandler);
                    }

                    TripList adapter = new TripList(TripHistoryActivity.this, tripList);
                    listView.setAdapter(adapter);

                }else {
                    Toast.makeText(TripHistoryActivity.this, "0 trips", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
