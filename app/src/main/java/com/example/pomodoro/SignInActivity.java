package com.example.pomodoro;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends AppCompatActivity {
    private EditText edtemail;
    private TextInputEditText edtpassword;
    private Button btnsignin,btnsignup;
    private FirebaseAuth mAuth;
    private TextView forgotTextLink;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mAuth=FirebaseAuth.getInstance();
        edtemail = findViewById(R.id.edtEmail);
        edtpassword = findViewById(R.id.edtPassword);
        btnsignin = findViewById(R.id.signin_btn);
        btnsignup = findViewById(R.id.signup_btn);
        forgotTextLink =(TextView) findViewById(R.id.Forgotpassword);
        forgotTextLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText reset_Mail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password?");
                passwordResetDialog.setMessage("Enter your email to received reset link.");
                passwordResetDialog.setView(reset_Mail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String mail =reset_Mail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(SignInActivity.this,"Reset link sent to your email",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(SignInActivity.this,"Error! Reset link is not sent "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                passwordResetDialog.create().show();
            }
        });


        btnsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin();
            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup();
            }
        });


    }

    private void Signup() {
        Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(i);
    }

    private void Signin() {
        String email, password;
        email= edtemail.getText().toString();
        password = edtpassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(getApplicationContext(),"Login successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    Toast.makeText(getApplicationContext(), "Your email address or password you entered is incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}


