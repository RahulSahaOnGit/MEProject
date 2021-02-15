package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText emailEditText, passwordEditText;
    Button loginButton;
    TextView signUpLinkTextView;
    DBHelper DB;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        loginButton=findViewById(R.id.logInButton);
        signUpLinkTextView=findViewById(R.id.signUpLinkTextView);

        DB=new DBHelper(this);

        sharedPreferences=getSharedPreferences("appSharedPreference",MODE_PRIVATE);
        checkLogInStatusAndOpenHomePage();
    }

    private void checkLogInStatusAndOpenHomePage() {
        if(sharedPreferences.getBoolean("loggedIn",false)){
            String email=sharedPreferences.getString("email", "");
            String userType=sharedPreferences.getString("userType", "");
            if(userType.equals("customer")) {
                openCustomerHomePage(email);
            }else{
                openRetailerHomePage(email);
            }
        }
    }

    public void checkUserCredentials(View view) {
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        if(email.equals("") || password.equals(""))
            Toast.makeText(MainActivity.this, "Please enter all the Values", Toast.LENGTH_LONG).show();
        else
        {
            Boolean checkUser=DB.checkEmailAndPassword(email, password);
            if(checkUser==true) {
                String userType=DB.getUserType(email);
                //Toast.makeText(MainActivity.this, "User Type is "+userType, Toast.LENGTH_LONG).show();
                if(userType.equals("customer")) {
                    openCustomerHomePage(email);
                }else{
                    openRetailerHomePage(email);
                }
            }else{
                Toast.makeText(MainActivity.this, "Incorrect Email Or Password", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void openRetailerHomePage(String email) {
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("email", email);
        edit.putString("userType", "retailer");
        edit.putBoolean("loggedIn", true);
        edit.commit();
        Intent intent=new Intent(this, RetailerHomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void openCustomerHomePage(String email) {
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putString("email", email);
        edit.putString("userType", "customer");
        edit.putBoolean("loggedIn", true);
        edit.commit();
        Intent intent=new Intent(this, CustomerHomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openSignUpPage(View view) {
        Intent intent=new Intent(this, SignUp.class);
        startActivity(intent);
    }
}