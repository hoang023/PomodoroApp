package com.example.pomodoro;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pomodoro.SetTimeFunctions.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class SignUpActivity extends AppCompatActivity {
    private EditText edtemail;
    private TextInputEditText edtpassword, edtconfirmpassword;
    private Button btnsignup, btnSigninnow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth=FirebaseAuth.getInstance();
        edtemail = findViewById(R.id.edtEmail);
        edtpassword = findViewById(R.id.edtPassword);
        edtconfirmpassword = findViewById(R.id.edtConfirmPassword);
        btnsignup = findViewById(R.id.signup_btn);
        btnSigninnow = findViewById(R.id.signin_now_btn);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        btnSigninnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signup() {
        String email, password, confirmpassword;
        email= edtemail.getText().toString();
        password = edtpassword.getText().toString();
        confirmpassword = edtconfirmpassword.getText().toString();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.equals(password,confirmpassword)){
            Toast.makeText(this,"Passwords don't macth", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseUser currentU = FirebaseAuth.getInstance().getCurrentUser();
                                String UId = currentU.getUid();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Toast.makeText(getApplicationContext(), "Account successfully created. Please check your email for verification", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                startActivity(intent);
                                DatabaseReference databaseReference = database.getReference().child("User").child(UId).child("SetTime");
                                Status status = new Status("0","0","0");
                                databaseReference.setValue(status);
                            }
                            else{
                                Toast.makeText(getApplicationContext(), "Account failed created", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    Toast.makeText(getApplicationContext(), "Account failed created", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
