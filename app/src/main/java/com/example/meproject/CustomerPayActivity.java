package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerPayActivity extends AppCompatActivity {

    EditText amountEditText;
    TextView retailerNameTextView, balanceTextView, pointsTextView;
    Button payButton;
    String customerEmail, retailerEmail, retailerName;
    double customerBalance, retailerBalance;
    int customerPoints;

    DBHelper DB;
    Cursor customerCursor, retailerCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_pay);

        amountEditText=findViewById(R.id.amountEditText);
        retailerNameTextView=findViewById(R.id.retailerNameTextView);
        balanceTextView=findViewById(R.id.balanceTextView);
        pointsTextView=findViewById(R.id.pointsTextView);
        payButton=findViewById(R.id.payButton);

        DB=new DBHelper(this);

        customerEmail=getIntent().getStringExtra("fromUser");
        retailerEmail=getIntent().getStringExtra("toUser");

        customerCursor=DB.getData(customerEmail);
        retailerCursor=DB.getData(retailerEmail);

        retailerName=retailerCursor.getString(retailerCursor.getColumnIndex("fullName"));
        customerBalance=customerCursor.getDouble(customerCursor.getColumnIndex("balance"));
        retailerBalance=retailerCursor.getDouble(retailerCursor.getColumnIndex("balance"));
        customerPoints=customerCursor.getInt(customerCursor.getColumnIndex("points"));

        retailerNameTextView.setText("Paying to "+retailerName);
        balanceTextView.setText("Your Balance is "+customerBalance);
        pointsTextView.setText("You have "+customerPoints+" points");

    }

    public void payUser(View view) {
        String amountString=amountEditText.getText().toString();
        if(amountString.equals("")){
            Toast.makeText(CustomerPayActivity.this, "Enter Amount", Toast.LENGTH_LONG).show();
        }else{

            double amountToPay=Double.parseDouble(amountString);

            if((customerBalance+customerPoints)<amountToPay){
                Toast.makeText(CustomerPayActivity.this, "Insufficient Balance And Points", Toast.LENGTH_LONG).show();
            }else{
                commitTransaction(customerCursor, retailerCursor, amountToPay, customerBalance, customerPoints, retailerBalance);
            }
        }

    }

    private void commitTransaction(Cursor customerCursor, Cursor retailerCursor, double amountToPay, double customerBalance, int customerPoints, double retailerBalance) {
        if(customerBalance<amountToPay){
            customerPoints= (int) (customerPoints-(amountToPay-customerBalance)+(amountToPay/100));
            customerBalance=0;
            retailerBalance+=amountToPay;
        }else{
            customerPoints= (int) (customerPoints+(amountToPay/100));
            customerBalance=customerBalance-amountToPay;
            retailerBalance+=amountToPay;
        }
        Boolean customerUpdate=DB.updateBalanceAndPoints(customerEmail, customerBalance, customerPoints);
        Boolean retailerUpdate=DB.updateBalanceAndPoints(retailerEmail, retailerBalance, 0);
        if(customerUpdate==true && retailerUpdate==true)
        {
            openCustomerHomePageActivity();
        }
    }

    private void openCustomerHomePageActivity() {
        Toast.makeText(CustomerPayActivity.this, "Paid Succesfully!", Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this, CustomerHomePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}