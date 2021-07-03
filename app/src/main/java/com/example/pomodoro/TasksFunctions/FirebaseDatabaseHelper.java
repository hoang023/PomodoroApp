package com.example.pomodoro.TasksFunctions;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase database;
    private DatabaseReference mData;
    private  FirebaseUser currentU;
    private String UId, year, month;
    private List<Data> mList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Data> mList, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        mData = database.getReference("User");
        currentU = FirebaseAuth.getInstance().getCurrentUser();
        UId = currentU.getUid();
        year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        month = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());
    }


    private void showData(final DataStatus dataStatus) {
        mData.child(UId).child(year).child(month).child("Task").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keys.add(dataSnapshot.getKey());
                    Data data = dataSnapshot.getValue(Data.class);
                    mList.add(data);
                }
                dataStatus.DataIsLoaded(mList,keys);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}
