package com.example.pomodoro.SetTimeFunctions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pomodoro.R;

public class FocusDialog extends Dialog {

    interface SetTimeListener {
        public void settimeEntered(String settime);
    }

    public Context context;

    private EditText edt_settime;
    private Button buttonOK;
    private Button buttonCancel;

    private FocusDialog.SetTimeListener listener;

    public FocusDialog(Context context, FocusDialog.SetTimeListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_focustime);

        this.edt_settime = (EditText) findViewById(R.id.edt_settime);
        this.buttonOK = (Button) findViewById(R.id.btnOk);
        this.buttonCancel  = (Button) findViewById(R.id.btnCancel);

        this.buttonOK .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOKClick();
            }
        });
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });
    }

    // User click "OK" button.
    private void buttonOKClick()  {
        String settime = this.edt_settime.getText().toString();

        if(settime== null || settime.isEmpty())  {
            Toast.makeText(this.context, "The value can not be empty" +
                    "\nPlease input the valid number", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            int val = Integer.parseInt(settime);
            if ((val<1) || (val>60)){
                Toast.makeText(this.context, "The value is invalid" +
                        "\nPlease correct by the following rule", Toast.LENGTH_LONG).show();
                return;}
        }
        this.dismiss(); // Close Dialog

        if(this.listener!= null)  {
            this.listener.settimeEntered(settime);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}