package com.example.danik.helloworld;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
        Button buttonHi, buttonClear;
        EditText inputName;
        TextView myName;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            inputName = findViewById(R.id.inputName);
            buttonClear = findViewById(R.id.buttonClear);
            buttonHi = findViewById(R.id.buttonHi);
            myName = findViewById(R.id.myname);
        }

        public void clearMessage(View view) {
            inputName.getText().clear();
        }

        @SuppressLint("StringFormatInvalid")
        public void showMessage(View view) {
            myName.setText(String.format("%s %s", getString(R.string.hello), inputName.getText()));
            inputName.getText().clear();
        }
}