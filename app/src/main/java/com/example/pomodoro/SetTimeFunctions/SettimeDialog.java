package com.example.pomodoro.SetTimeFunctions;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.pomodoro.R;

public class SettimeDialog extends Dialog {

    interface SetTimeListener {
        public void settimeEntered(String focusValue, String breakValue, String stagesValue);
    }

    public Context context;

    private NumberPicker focusNumb, breakNumb, stagesNumb;
    private String focusValue, breakValue, stagesValue;
    private Button buttonOK, buttonCancel;

    private SettimeDialog.SetTimeListener listener;

    public SettimeDialog(Context context, SettimeDialog.SetTimeListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_settime);

        this.focusNumb = (NumberPicker) findViewById(R.id.focusValue);
        focusNumb.setMaxValue(60);
        focusNumb.setMinValue(0);
        focusNumb.setValue(0);
        focusNumb.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                focusValue = String.valueOf(newVal);
            }
        });

        this.breakNumb = (NumberPicker) findViewById(R.id.breakValue);
        breakNumb.setMaxValue(20);
        breakNumb.setMinValue(0);
        breakNumb.setValue(0);
        breakNumb.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                breakValue = String.valueOf(newVal);
            }
        });

        this.stagesNumb = (NumberPicker) findViewById(R.id.stagesValue);
        stagesNumb.setMaxValue(6);
        stagesNumb.setMinValue(0);
        stagesNumb.setValue(0);
        stagesNumb.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stagesValue = String.valueOf(newVal);
            }
        });

        this.buttonOK = (Button) findViewById(R.id.buttonOK);
        this.buttonCancel  = (Button) findViewById(R.id.buttonCancel);

        this.buttonOK.setOnClickListener(new View.OnClickListener() {
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
        String focusTime, breakTime, stagesTime;

        focusTime = focusValue;
        breakTime = breakValue;
        stagesTime = stagesValue;

        if (focusTime==null || breakTime==null ||stagesTime==null){
            Toast.makeText(this.context, "The value is invalid" +
                    "\nPlease set all three values", Toast.LENGTH_LONG).show();
            return;
        }

        this.dismiss(); // Close Dialog

        if (this.listener != null) {
            this.listener.settimeEntered(focusTime, breakTime, stagesTime);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}