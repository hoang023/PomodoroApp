package com.example.pomodoro.SetTimeFunctions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.R;
import com.example.pomodoro.StatisticalActivity;
import com.example.pomodoro.TasksFunctions.TasksActivity;

public class SettimeActivity extends AppCompatActivity{

    private Button tasksbutton, detail, save;
    private TextView tx_focus, tx_break, tx_stages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settime);
        //Chuyển qua màn hình tasks
        tasksbutton = (Button) findViewById(R.id.tasksbutton);
        tasksbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettimeActivity.this, TasksActivity.class);
                startActivity(intent);
            }
        });
        //Chuyển qua màn hình statiscal
        detail = (Button) findViewById(R.id.detailtbutton );
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(SettimeActivity.this, StatisticalActivity.class);
                startActivity(intent);
            }
        });
        /*Dialog add item
        tx_focus chứa giá trị focus người dùng nhập vào, 2 cái dưới tương tự
        lần lượt theo thứ tự là break vs stages*/
        this.tx_focus = (TextView) findViewById(R.id.tx_focustime);
        this.tx_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BreakTimeDialog();
            }
        });
        this.tx_break = (TextView) findViewById(R.id.tx_breaktime);
        this.tx_break.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BreakTimeDialog();
            }
        });
        this.tx_stages = (TextView) findViewById(R.id.tx_stagestime);
        this.tx_stages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BreakTimeDialog();
            }
        });
    }
    //3 dialog add item
    private void BreakTimeDialog(){
        SettimeDialog.SetTimeListener listener = new SettimeDialog.SetTimeListener() {
            @Override
            public void settimeEntered(String focusValue, String breakValue, String stagesValue) {
                tx_focus.setText(focusValue);
                tx_break.setText(breakValue);
                tx_stages.setText(stagesValue);
            }
        };
        final SettimeDialog dialog = new SettimeDialog(this, listener);
        dialog.show();
    }
}
