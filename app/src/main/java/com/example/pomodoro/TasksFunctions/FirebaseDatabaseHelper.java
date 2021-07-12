package com.example.pomodoro.TasksFunctions;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {

    private FirebaseDatabase database;
    private DatabaseReference mData;
    private FirebaseUser currentU;
    private String UId;
    private List<TodoTASK> mList = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<TodoTASK> mList);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        database = FirebaseDatabase.getInstance();
        currentU = FirebaseAuth.getInstance().getCurrentUser();
        UId = currentU.getUid();
        mData = database.getReference("User").child(UId).child("Task");
    }

    public void showData(final DataStatus dataStatus) {
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                mList.clear();
                List<String> keys = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    keys.add(dataSnapshot.getKey());
                    TodoTASK data = dataSnapshot.getValue(TodoTASK.class);
                    data.setId(dataSnapshot.getKey());
                    mList.add(data);
                }
                dataStatus.DataIsLoaded(mList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void updateData (String key, String content, final  DataStatus dataStatus){
        mData.child(key).child("Content").setValue(content).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsUpdated();
            }
        });
    }

    public void deleteData(String key, final DataStatus dataStatus){
        mData.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}