package com.example.pomodoro.TasksFunctions;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pomodoro.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TasksAdd extends BottomSheetDialogFragment {

    public static  final String TAG = "TasksAdd";

    private EditText tasksEdt;
    private Button saveBtn;
    DatabaseReference mData;
    private FirebaseAuth auth;
    private Context context;
    private Integer status = 0;

    public static TasksAdd newInstance(){
        return new TasksAdd();

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
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
                    saveBtn.setBackgroundColor(Color.GRAY);
                }else{
                    saveBtn.setEnabled(true);
                    saveBtn.setBackgroundColor(getResources().getColor(R.color.purple_500));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = tasksEdt.getText().toString();

                if (task.isEmpty()){
                    Toast.makeText(context,"Empty task not allowed !!!", Toast.LENGTH_LONG).show();
                }else{

                    String UId=auth.getCurrentUser().getUid();

                    Date date = new Date();
                    String d = (String) android.text.format.DateFormat.format("dd/MM/yyyy",date);
                    Map<String , Integer> taskMap = new HashMap<>();

                    taskMap.put(task,status);

                }
            }
        });

    }
}
