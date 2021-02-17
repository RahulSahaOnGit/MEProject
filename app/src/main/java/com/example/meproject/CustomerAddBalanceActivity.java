package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomerAddBalanceActivity extends AppCompatActivity {

    EditText addBalanceEditText;
    Button addBalanceButton;

    String customerEmail, balanceToAdd;
    double customerBalance;

    DBHelper DB;
    Cursor customerCursor;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_balance);

        addBalanceEditText=findViewById(R.id.addBalanceEditText);
        addBalanceButton=findViewById(R.id.addBalanceButton);

        sharedPref=getSharedPreferences("appSharedPreference",MODE_PRIVATE);
        customerEmail=sharedPref.getString("email", "");

        DB=new DBHelper(this);
        customerCursor=DB.getData(customerEmail);

    }

    public void addBalanceAndOpenCustomerHomePage(View view) {
        balanceToAdd=addBalanceEditText.getText().toString();
        if(balanceToAdd.equals("")){
            Toast.makeText(CustomerAddBalanceActivity.this, "Please Provide the Amount to Add", Toast.LENGTH_LONG).show();
        }
        else{
            customerBalance=customerCursor.getDouble(customerCursor.getColumnIndex("balance"));
            customerBalance+=Double.parseDouble(balanceToAdd);
            Boolean customerUpdate=DB.updateBalance(customerEmail, customerBalance);

            if(customerUpdate!=false){
                openCustomerHomePageActivity();
            }

        }
    }

    private void openCustomerHomePageActivity() {
        Toast.makeText(CustomerAddBalanceActivity.this, "Added Succesfully!", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this, CustomerHomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}