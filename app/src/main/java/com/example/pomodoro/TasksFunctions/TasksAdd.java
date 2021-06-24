package com.example.pomodoro.TasksFunctions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pomodoro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class TasksAdd extends BottomSheetDialogFragment {

    public static  final String TAG = "TasksAdd";

    private EditText tasksEdt;
    private Button saveBtn;
    private FirebaseAuth auth;
    private Context context;
    private final Integer status = 0;

    public static TasksAdd newInstance(){
        return new TasksAdd();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tasksEdt = view.findViewById(R.id.tasksEdt);
        saveBtn = view.findViewById(R.id.saveBtn);


        tasksEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    saveBtn.setEnabled(false);
                    saveBtn.setBackgroundColor(Color.DKGRAY);
                }else{
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn.setOnClickListener(v -> {
            String task = tasksEdt.getText().toString();

            if (task.isEmpty()) {
                Toast.makeText(context, "Empty task not allowed !!!", Toast.LENGTH_LONG).show();
            } else {
                //Date date = new Date();
                //String year = (String) DateFormat.format("yyyy", date);
                //String month = (String) DateFormat.format("MM", date);
                String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());

                Map<String, Integer> taskMap = new HashMap<>();

                taskMap.put(task, status);

                //String UId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                //String UId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
                String UId = currentU.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference mData = database.getReference("User");
                DatabaseReference mData1 = mData.child(UId);
                DatabaseReference mData2 = mData1.child(year);
                DatabaseReference mData3 = mData2.child(month+1);

                mData3.child("Task").setValue(taskMap
                        , (OnCompleteListener<DocumentReference>) task1 -> {
                    if (task1.isSuccessful()) {
                        Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        dismiss();
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof  OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }
}