package com.example.google_auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInAct extends AppCompatActivity {

    EditText emailET;
    EditText passwordET;
    Button Signin,Signout;
    ProgressBar objectprogressbar;

    TextView recoveryTV;

    FirebaseAuth objectfirebaseauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        convertxmltojava();
        objectfirebaseauth=FirebaseAuth.getInstance();

        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectfirebaseauth.signOut();
                Signout.setVisibility(View.INVISIBLE);

                Toast.makeText(SignInAct.this, "SignOut Sucessfully", Toast.LENGTH_SHORT).show();
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinuser();
            }
        });

        recoveryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showrecoverdialogbox();
            }
        });
    }

    private void showrecoverdialogbox()
    {

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);

        final EditText emailText=new EditText(this);
        emailText.setHint("Email");
        emailText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        linearLayout.addView(emailText);

        linearLayout.setPadding(10,10,10,10);

        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String email=emailText.getText().toString().trim();

                beginRecovery(email);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });

        builder.create().show();

    }

    private void beginRecovery(String email)
    {
        objectfirebaseauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    Toast.makeText(SignInAct.this, "Recovery Email is send", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(SignInAct.this, "Could'nt send Recovery Email", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {

                Toast.makeText(SignInAct.this, "Failed to recover"+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void convertxmltojava()
    {
        emailET=findViewById(R.id.EmailET);
        passwordET=findViewById(R.id.passwordET);
        Signin=findViewById(R.id.signup_btn);
        Signout=findViewById(R.id.signout_btn);
        objectprogressbar=findViewById(R.id.progress_bar);
        recoveryTV=findViewById(R.id.Forgetpassword);
    }


    public void signinuser()
    {
        try
        {
            if (!emailET.getText().toString().isEmpty()&&!passwordET.getText().toString().isEmpty())
            {
                if (objectfirebaseauth!=null)
                {
                    if (objectfirebaseauth.getCurrentUser()!=null) {
                        objectfirebaseauth.signOut();

                        Toast.makeText(this, "Signout Sucessfully", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
//                        objectprogressbar.setVisibility(View.VISIBLE);
                        Signin.setEnabled(false);


                        objectfirebaseauth.signInWithEmailAndPassword(emailET.getText().toString(),
                                passwordET.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(SignInAct.this, "Sucessfully Sign In", Toast.LENGTH_SHORT).show();

                                Signin.setEnabled(true);
                                Signout.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignInAct.this, "Unsucessfull to Signin", Toast.LENGTH_SHORT).show();

//                                objectprogressbar.setVisibility(View.INVISIBLE);
                                Signin.setEnabled(true);
                            }
                        });

                    }

                }


            }

            else if (emailET.getText().toString().isEmpty())
            {
                Toast.makeText(this, "You have not Entered an Email", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "SignInUser"+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
