package com.velt.libraryapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.velt.libraryapplication.DataClasses.UserData;

public class AuthenticationActivity extends AppCompatActivity implements View.OnClickListener {

    EditText signInEmail, signInPassword, signUpEmail, signUpPassword, signUpName, signUpHomeAddress;
    TextView signInDoneBtn,signUpDoneBtn, titleText, signIn, signUp;
    LinearLayout signInLayout, signUpLayout;
    FirebaseAuth userAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        signInEmail = findViewById(R.id.SignIn_Email_EditText);
        signInPassword = findViewById(R.id.SignIn_Password_EditText);
        signUpEmail = findViewById(R.id.SignUp_Email_EditText);
        signUpPassword = findViewById(R.id.SignUp_Password_EditText);
        signUpName = findViewById(R.id.SignUp_Full_Name_EditText);
        signUpHomeAddress = findViewById(R.id.SignUp_Home_Address_EditText);
        signInDoneBtn = findViewById(R.id.SignIn_Button);
        signUpDoneBtn = findViewById(R.id.SignUp_Button);
        titleText = findViewById(R.id.SignIn_Title);
        signUp = findViewById(R.id.SignUp_Link_Btn);
        signIn = findViewById(R.id.SignIn_Link_Btn);
        signInLayout = findViewById(R.id.SignIn_Layout);
        signUpLayout = findViewById(R.id.SignUp_Layout);

        userAuthentication = FirebaseAuth.getInstance();

        signUpDoneBtn.setOnClickListener(this);
        signInDoneBtn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        signIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.SignUp_Button:
                //Todo Add Authentication
                final String email = signUpEmail.getText().toString();
                final String password = signUpPassword.getText().toString();
                final String address = signUpHomeAddress.getText().toString();
                final String fullName = signUpName.getText().toString();

                if (email.length() != 0 && password.length() != 0 && address.length()!=0 && fullName.length()!=0){
                    userAuthentication.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        int books = 0;
                                        String reserves = "none";
                                        addUserData(email,address,fullName,books, reserves);
                                        Toast.makeText(AuthenticationActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AuthenticationActivity.this, LibraryActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(AuthenticationActivity.this, "Something went wrong, check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(this, "All fields should be filled", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.SignIn_Button:
                //Todo Add Authentication
                 String emailStr = signInEmail.getText().toString();
                 String passwordStr = signInPassword.getText().toString();
                if (emailStr.length() != 0 && passwordStr.length() != 0){
                    userAuthentication.signInWithEmailAndPassword(emailStr,passwordStr)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AuthenticationActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AuthenticationActivity.this, LibraryActivity.class));
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(AuthenticationActivity.this, "Something went wrong, check your internet connection and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                break;
            case R.id.SignUp_Link_Btn:
                signInLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.VISIBLE);
                String title = "Sign\nUp";
                titleText.setText(title);
                break;
            case R.id.SignIn_Link_Btn:
                signInLayout.setVisibility(View.VISIBLE);
                signUpLayout.setVisibility(View.GONE);
                String titleTwo = "Sign\nIn";
                titleText.setText(titleTwo);
                break;
        }
    }

    private void addUserData(String email, String address, String fullName, int books, String reserves) {
        UserData data = new UserData(fullName,email,address,books,reserves);
        DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference("users");
        String[] key = email.split("@");
        userDetails.child(key[0]).setValue(data);
    }
}
