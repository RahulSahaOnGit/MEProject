package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.regex.*;

public class SignUp extends AppCompatActivity {
    EditText fullNameEditText, emailEditText, passwordEditText, accountBalanceEditText;
    Button signUpButton;
    RadioGroup userTypeRadioGroup;
    RadioButton customerRadioButton, retailerRadioButton;
    DBHelper DB;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullNameEditText=findViewById(R.id.fullNameEditText);
        emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        accountBalanceEditText=findViewById(R.id.accountBalanceEditText);
        signUpButton=findViewById(R.id.signUpButton);
        userTypeRadioGroup=findViewById(R.id.userTypeRadioGroup);
        customerRadioButton=findViewById(R.id.customerRadioButton);
        retailerRadioButton=findViewById(R.id.retailerRadioButton);

        sharedPreferences=getSharedPreferences("appSharedPreference",MODE_PRIVATE);

        DB=new DBHelper(this);
    }

    public void createUser(View view) {

        String fullName=fullNameEditText.getText().toString();
        String email=emailEditText.getText().toString();
        String password=passwordEditText.getText().toString();
        String accountBalance=accountBalanceEditText.getText().toString();
        if(email.equals("") || password.equals("") || accountBalance.equals(""))
            Toast.makeText(SignUp.this, "Please enter all the Values", Toast.LENGTH_LONG).show();
        else
        {
            if(checkEmailFormat(email)==false){
                Toast.makeText(SignUp.this, "Invalid Email Format!", Toast.LENGTH_LONG).show();
            }else{
                Boolean checkUser=DB.checkUserExist(email);
                if(checkUser==false) {
                    String userType = "";
                    if (customerRadioButton.isChecked())
                        userType = "customer";
                    else
                        userType = "retailer";
                    Boolean insert = DB.insertUsersData(email, password, fullName, userType, Double.parseDouble(accountBalance));
                    if (insert == true) {
                        if (userType.equals("customer")) {
                            openCustomerHomePage(email);
                        } else {
                            openRetailerHomePage(email);
                        }
                    }else{
                        Toast.makeText(SignUp.this, "Error! Please Try Again.", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(SignUp.this, "Email Already Exists", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private boolean checkEmailFormat(String email) {
        String emailRegex="^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
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

}