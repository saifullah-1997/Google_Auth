package com.example.google_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class SignUpAct extends AppCompatActivity {

    FirebaseAuth objectFirebaseauth;

    EditText emailET;
    EditText passwordET;
    Button signupbtn;
    ProgressBar objectprogressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        objectFirebaseauth=FirebaseAuth.getInstance();
        convertxmltojava();

    }

    private void convertxmltojava()
    {
        emailET=findViewById(R.id.EmailET);
        passwordET=findViewById(R.id.passwordET);

        signupbtn=findViewById(R.id.signup_btn);
        objectprogressbar=findViewById(R.id.signUpProgressBar);

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkifuserexsist();
            }
        });
    }

    private void signupuser()
    {
        try {

            if(!emailET.getText().toString().isEmpty()&&!passwordET.getText().toString().isEmpty())
            {
                if (objectFirebaseauth!=null)
                {
                    objectprogressbar.setVisibility(View.VISIBLE);
                    signupbtn.setEnabled(false);
                    objectFirebaseauth.createUserWithEmailAndPassword(emailET.getText().toString(),passwordET.getText()
                            .toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            objectprogressbar.setVisibility(View.INVISIBLE);
                            signupbtn.setEnabled(true);
                            if (authResult.getUser()!=null)
                            {
                                objectprogressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignUpAct.this,
                                        "Success to signup", Toast.LENGTH_SHORT).show();
                                signupbtn.setEnabled(true);
                                objectFirebaseauth.signOut();
                                startActivity(new Intent(SignUpAct.this,MainActivity.class));
                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            objectprogressbar.setVisibility(View.INVISIBLE);

                            Toast.makeText(SignUpAct.this, "Fail to sign up", Toast.LENGTH_SHORT).show();
                            signupbtn.setEnabled(true);

                        }
                    });
                }

            }

            else if (emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this,
                        "Enter your Email", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            }

            else if (passwordET.getText().toString().isEmpty())
            {
                Toast.makeText(this,
                        "Enter your password", Toast.LENGTH_SHORT).show();
                passwordET.requestFocus();
            }
            else
            {
                Toast.makeText(this,
                        "Wrong Input Email or password", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, "SignUp USer" +
                    e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private void checkifuserexsist()
    {
        try {
            if (objectFirebaseauth != null)
            {
                objectprogressbar.setVisibility(View.VISIBLE);
                signupbtn.setEnabled(false);
                objectFirebaseauth.fetchSignInMethodsForEmail(emailET.getText()
                        .toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check=task.getResult().getSignInMethods().isEmpty();

                        if (check)
                        {
                            signupuser();
                        }
                        else
                        {
                            objectprogressbar.setVisibility(View.INVISIBLE);
                            signupbtn.setEnabled(true);
                            Toast.makeText(SignUpAct.this, "Already have an Account", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        signupbtn.setEnabled(true);
                        objectprogressbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignUpAct.this, "Fail to check user exsist", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else if (emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "You have not enter the email", Toast.LENGTH_SHORT).show();
                emailET.requestFocus();
            }
            else if (passwordET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "You have not entered your Password", Toast.LENGTH_SHORT).show();
                passwordET.requestFocus();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, "Check if user Exist"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}