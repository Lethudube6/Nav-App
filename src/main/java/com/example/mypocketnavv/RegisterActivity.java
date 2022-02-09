
package com.example.mypocketnavv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

        EditText fname,lname,email,email2,password,password2;
        Button register;
        DatabaseReference mydatabase;
        Boolean registered=true;

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mAuth = FirebaseAuth.getInstance();
            mydatabase= FirebaseDatabase.getInstance().getReference("userdetails");

            setContentView(R.layout.activity_register);
            fname=findViewById(R.id.editTextFirstNameR);
            lname=findViewById(R.id.editTextLastName);
            email=findViewById(R.id.editTextEmailR);
            email2=findViewById(R.id.editTextconfirmEmail);
            password=findViewById(R.id.editTextPasswordR);
            password2=findViewById(R.id.editTextconfirmPasswordR);

            register=findViewById(R.id.buttonRegister);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    String fnameVerify=fname.getText().toString(),
                            lnameVerify=lname.getText().toString(),
                            email2Verify=email2.getText().toString(),
                            password2Verify=password2.getText().toString();
                    String emailVerify=email.getText().toString().trim().toLowerCase();
                    String passwordVerify=password.getText().toString().trim();

                    if(!fnameVerify.isEmpty()){
                        if(!lnameVerify.isEmpty()){
                            if(!emailVerify.isEmpty()){
                                if(emailVerify.contains("@")){
                                    if(!email2Verify.isEmpty()){
                                        if(email2Verify.equals(emailVerify)){
                                            if(!passwordVerify.isEmpty()){
                                                if(passwordVerify.length()>5){
                                                    if(!password2Verify.isEmpty()){
                                                        if(password2Verify.equals(passwordVerify)){
                                                            regMethod(emailVerify,passwordVerify);
                                                            if(registered){
                                                                regmethod2(fname.getText().toString(),lname.getText().toString(),email.getText().toString());
                                                            }

                                                        }else {
                                                            password2.setError("Passwords Dont match");
                                                            password2.requestFocus();
                                                        }
                                                    }else{
                                                        password2.setError("Please Confirm Password this field is empty");
                                                        password2.requestFocus();
                                                    }
                                                }else{
                                                    password.setError("The Password is too short, please make it longer 6 characters a minimum");
                                                    password.requestFocus();
                                                }

                                            }else{
                                                password.setError("Please enter password, this field is empty");
                                                password.requestFocus();
                                            }

                                        }else{
                                            email2.setError("Emails don't Match ");
                                            email2.requestFocus();
                                        }
                                    }else{
                                        email2.setError("Please confirm Email field is Empty");
                                        email2.requestFocus();
                                    }
                                }else{
                                    email.setError("Please enter email address format this is wrong '@");
                                    email.requestFocus();
                                }

                            }else{
                                email.setError("Please enter email, this feild is empty");
                                email.requestFocus();
                            }


                        }else {
                            lname.setError("Please enter Last, this field is empty");
                            lname.requestFocus();
                        }
                    }else {
                        fname.setError("Please enter First Name, this field is empty");
                        fname.requestFocus();
                    }

                }
            });
        }

        public void regMethod(String email1,String password1){
            mAuth.createUserWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            String TAG = "RegisterActivity";
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");

                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                //startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                registered=true;


                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                registered=false;
                            }

                            // ...
                        }
                    });
            //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        }
        public void regmethod2(String fname22,String lname22,String email22){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            String id = mydatabase.push().getKey();
            newuserdatareg nudr= new newuserdatareg(id,fname22,lname22,email22);
            mydatabase.child(id).setValue(nudr);
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        }

        @Override
        public void onStart(){
            super.onStart();

            FirebaseUser currentUser = mAuth.getCurrentUser();
            //updateUI(currentUser);



        }

        private void updateUI(FirebaseUser user) {

            if (user != null) {

            } else {

            }
        }
    }
