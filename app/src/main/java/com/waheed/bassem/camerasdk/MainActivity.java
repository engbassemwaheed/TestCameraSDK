package com.waheed.bassem.camerasdk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.waheed.bassem.ocr.OcrInterface;
import com.waheed.bassem.ocr.OcrManager;
import com.waheed.bassem.ocr.display.CameraFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();

        String apiKey = "8e326757f088957";
        OcrManager ocrManager = OcrManager.getInstance(apiKey);

        ocrManager.startOCR(fragmentManager, R.id.frame_layout, new OcrInterface() {
            @Override
            public void onOcrResult(ArrayList<String> text) {
                Log.e(TAG, "onOcrResult: text = " + text);
            }

            @Override
            public void onBackPressed() {
                Log.e(TAG, "onBackPressed:");
            }

            @Override
            public void onError(int errorCode) {
                Log.e(TAG, "onError: errorCode = " + errorCode);
            }
        });

    }
}