package com.example.pomodoro.TasksFunctions;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TasksAdd extends BottomSheetDialogFragment {

    public static final String TAG = "TasksAdd";

    private EditText tasksEdt;
    private Button saveBtn;
    private FirebaseAuth auth;
    private Context context;

    public static TasksAdd newInstance() {
        return new TasksAdd();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tasksEdt = view.findViewById(R.id.tasksEdt);
        saveBtn = view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(v -> {
            String task = tasksEdt.getText().toString();
                if (task.isEmpty()) {
                    Toast.makeText(context, "Empty task not allowed !!!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    String month = new SimpleDateFormat("MMM", Locale.getDefault()).format(new Date());
                    FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
                    String UId = currentU.getUid();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference mData = database.getReference("User").child(UId).child("Task");
                    Map<String, Object> taskMap = new HashMap<>();

                    taskMap.put("Content", task);
                    taskMap.put("Status", 0);
                    taskMap.put("month", month);

                    TodoTASK newTask=new TodoTASK();
                    newTask.setContent(task);
                    newTask.setStatus(0);

                    // thì ngay chỗ này mình push là push một cái object TodoTASK lên luôn k phải 1 cái map
                    //vì sẽ k lấy data về được

                    newTask.setId(mData.push().getKey());
                    newTask.setMonth(month);

                    mData.child(newTask.getId()).setValue(taskMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                Toast.makeText(context, "Task Saved", Toast.LENGTH_SHORT).show();
                                Log.d("NEW TASK ID",newTask.getId());
                            } else {
                                Toast.makeText(context, "Task Not Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
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