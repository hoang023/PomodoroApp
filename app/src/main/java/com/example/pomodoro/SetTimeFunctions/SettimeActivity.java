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
                FoscusTimeDialog();
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
                SatgesDialog();
            }
        });
    }
    //3 dialog add item
    private void FoscusTimeDialog()  {
        FocusDialog.SetTimeListener listener = new FocusDialog.SetTimeListener() {
            @Override
            public void settimeEntered(String settime) {
                tx_focus.setText(settime);
            }
        };
        final FocusDialog dialog = new FocusDialog(this, listener);
        dialog.show();
    }

    private void BreakTimeDialog(){
        BreakDialog.SetTimeListener listener = new BreakDialog.SetTimeListener() {
            @Override
            public void settimeEntered(String settime) {
                tx_break.setText(settime);
            }
        };
        final BreakDialog dialog = new BreakDialog(this, listener);
        dialog.show();
    }

    private void SatgesDialog(){
        StagesDialog.SetTimeListener listener = new StagesDialog.SetTimeListener(){
            @Override
            public void settimeEntered(String settime) {
                tx_stages.setText(settime);
            }
        };
        final StagesDialog dialog = new StagesDialog(this, listener);
        dialog.show();
    }
}
