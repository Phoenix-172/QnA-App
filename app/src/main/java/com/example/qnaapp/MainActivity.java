package com.example.qnaapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView answer;
    private EditText query;
    private String url;
    private String getAIResponse(String MyURL) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(MyURL);
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpurlconnection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            httpurlconnection.disconnect();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            button = (Button) findViewById(R.id.button);
            answer = (TextView) findViewById(R.id.textView3);
            query = (EditText) findViewById(R.id.editTextText);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = ((EditText) findViewById(R.id.editTextText)).getText().toString();
                    if (query.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Please enter a query", Toast.LENGTH_SHORT).show();
                    } else {

                        url = "http://10.0.2.2:5000/getData?prompt=" + query;
                        String result = getAIResponse(url);
                        answer.setText(result);
                    }

                }
            });
            return insets;
        });

    }
}