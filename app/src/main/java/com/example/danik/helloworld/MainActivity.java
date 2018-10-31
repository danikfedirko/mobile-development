package com.example.danik.helloworld;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

public class MainActivity extends AppCompatActivity {
    public Button buttonSubmit;
    public Button buttonShowList;
    public EditText inputFirstName;
    public EditText inputLastName;
    public EditText inputPhone;
    public EditText inputEmail;
    public AwesomeValidation inputValidation;
    private ArrayList<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputValidation = new AwesomeValidation(ValidationStyle.BASIC);
        setupUI();
        validateUI();
        checkUI();
        userList = new ArrayList<>();
    }

    private void setupUI() {
        inputFirstName = findViewById(R.id.inputFirstName);
        inputLastName = findViewById(R.id.inputLastName);
        inputEmail = findViewById(R.id.inputEmail);
        inputEmail.setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        inputPhone = findViewById(R.id.inputPhone);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonShowList = findViewById(R.id.buttonShowList);
        buttonShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openListActivity = new Intent(MainActivity.this,
                        UserListActivity.class);
                startActivity(openListActivity);
            }
        });
    }

    private void validateUI() {
        inputValidation.addValidation(MainActivity.this, R.id.inputFirstName,
                "[a-zA-Z\\s]+", R.string.FirstName);
        inputValidation.addValidation(MainActivity.this, R.id.inputLastName,
                "[a-zA-Z\\s]+", R.string.LastName);
        inputValidation.addValidation(MainActivity.this, R.id.inputEmail,
                Patterns.EMAIL_ADDRESS, R.string.Email);
        inputValidation.addValidation(MainActivity.this, R.id.inputPhone,
                RegexTemplate.TELEPHONE, R.string.PhoneNumber);
    }


    private void checkUI() {
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputValidation.validate()){
                    Toast.makeText(MainActivity.this, "Data received", Toast.LENGTH_SHORT).show();
                    saveInfo();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveInfo() {
        User user = new User(inputLastName.getText().toString(),
                inputFirstName.getText().toString(),
                inputPhone.getText().toString());
        userList.add(user);
        Gson gson = new Gson();
        String json = gson.toJson(userList);
        Log.i("Users", json);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                "UsersList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UsersList", json);
        editor.apply();
    }
}