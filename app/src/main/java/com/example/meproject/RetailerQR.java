package com.example.meproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class RetailerQR extends AppCompatActivity {

    ImageView qrCodeImageView;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_q_r);

        qrCodeImageView=findViewById(R.id.qrCodeImageView);

        sharedPreferences=getSharedPreferences("appSharedPreference",MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");

        MultiFormatWriter writer=new MultiFormatWriter();
        try {
            BitMatrix matrix=writer.encode(email, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder=new BarcodeEncoder();
            Bitmap bitmap=encoder.createBitmap(matrix);
            qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}