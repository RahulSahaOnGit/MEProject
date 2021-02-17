package com.example.meproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class CustomerHomePage extends AppCompatActivity {

    Button logoutButton, scanAndPayButton;
    TextView balanceTextView, pointsTextView;

    double customerBalance;
    int customerPoints;

    SharedPreferences sharedPreferences;

    DBHelper DB;
    Cursor customerCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home_page);

        logoutButton=findViewById(R.id.logOutButton);
        scanAndPayButton=findViewById(R.id.scanAndPayButton);
        balanceTextView=findViewById(R.id.balanceTextView);
        pointsTextView=findViewById(R.id.pointsTextView);
        DB=new DBHelper(this);

        sharedPreferences=getSharedPreferences("appSharedPreference",MODE_PRIVATE);
        customerCursor=DB.getData(sharedPreferences.getString("email", ""));

        customerBalance=customerCursor.getDouble(customerCursor.getColumnIndex("balance"));
        customerPoints=customerCursor.getInt(customerCursor.getColumnIndex("points"));

        balanceTextView.setText("Your Balance is "+customerBalance);
        pointsTextView.setText("You have "+customerPoints+" points");
    }

    public void logOutUser(View view) {
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putBoolean("loggedIn", false);
        edit.commit();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openCustomerScanActivity(View view) {
        IntentIntegrator intentIntegrator=new IntentIntegrator(CustomerHomePage.this);
        intentIntegrator.setPrompt("For Flash Use Volume Up Key");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult.getContents()!=null){
            openCustomerPayActivity(intentResult.getContents());
        }else{
            Toast.makeText(CustomerHomePage.this, "Couldn't find any QR", Toast.LENGTH_LONG).show();
        }
    }

    private void openCustomerPayActivity(String contents) {
        Intent intent=new Intent(this, CustomerPayActivity.class);
        intent.putExtra("fromUser", sharedPreferences.getString("email", ""));
        intent.putExtra("toUser", contents);
        startActivity(intent);
    }

    public void openCustomerAddBalanceActivity(View view) {
        Intent intent=new Intent(this, CustomerAddBalanceActivity.class);
        startActivity(intent);
    }
}

