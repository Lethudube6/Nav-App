package com.example.mypocketnavv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText username,password;
    Button login;
    TextView register,TourG;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mAuth.signOut();
        mAuth = FirebaseAuth.getInstance();



        username= findViewById(R.id.username);
        password=findViewById(R.id.password);
        login=findViewById(R.id.buttonLoginL);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailVerify=username.getText().toString();
                String passwordVerify=password.getText().toString();

                if(!emailVerify.isEmpty()){
                    if(emailVerify.contains("@")){
                        if(!passwordVerify.isEmpty()){
                            if(passwordVerify.length()>5){
                                loginmethod(username.getText().toString(),password.getText().toString());
                            }else{
                                password.setError("The Password is too short, please make it longer 6 characters a minimum");
                                password.requestFocus();
                            }

                        }else{
                            password.setError("Please enter password, this feild is empty");
                            password.requestFocus();
                        }

                    }else{
                        username.setError("Please enter email address format this is wrong '@'");
                        username.requestFocus();
                    }

                }else{
                    username.setError("Please enter email, this feild is empty");
                    username.requestFocus();
                }






            }
        });

        register=findViewById(R.id.textViewRegister);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                //loginmethod(username.getText().toString(),password.getText().toString());
            }
        });

    }

    public void loginmethod(String email1,String password1){
        mAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        final String TAG="MainActivity";
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            String currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            startActivity(new Intent(MainActivity.this, MapActivity.class).putExtra("logeduser",currentuser));
                            //String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            //String currentuser=user.getUid();
                            //Toast.makeText(MainActivity.this, "the id is"+currentuser, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.Incorrect Email or Password.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void updateUI(FirebaseUser user) {

        if (user != null) {

        } else {

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
