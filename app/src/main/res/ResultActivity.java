package com.example.listview1;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String item1 = "";

        Bundle extras = getIntent().getExtras();

        item1 = extras.getString("item1");

        TextView textView = (TextView) findViewById(R.id.textView_newActivity_contentString);

        String str = item1;
        textView.setText(str);
    }
}
