package com.example.artflow;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus++;

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(() -> progressBar.setProgress(progressStatus));
            }


            runOnUiThread(() -> {
                Intent intent = new Intent(MainActivity.this, Select.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}
