package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RetailerHomePage extends AppCompatActivity {

    Button logOutButton, qrCodeButton;
    TextView balanceTextView, pointsTextView;

    double retailerBalance;

    DBHelper DB;
    Cursor retailerCursor;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_home_page);

        logOutButton=findViewById(R.id.logOutButton);
        qrCodeButton=findViewById(R.id.openQRCodeActivityButton);
        balanceTextView=findViewById(R.id.balanceTextView);

        DB=new DBHelper(this);

        sharedPreferences=getSharedPreferences("appSharedPreference",MODE_PRIVATE);
        retailerCursor=DB.getData(sharedPreferences.getString("email", ""));

        retailerBalance=retailerCursor.getDouble(retailerCursor.getColumnIndex("balance"));

        balanceTextView.setText("Your Balance is "+retailerBalance);
    }

    public void logOutUser(View view) {
        SharedPreferences.Editor edit=sharedPreferences.edit();
        edit.putBoolean("loggedIn", false);
        edit.commit();
        Intent intent=new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openQRCodeActivity(View view) {
        Intent intent=new Intent(this, RetailerQR.class);
        startActivity(intent);
    }
}