package com.parse.starter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseException;

import com.parse.ParseUser;
import com.parse.SignUpCallback;


import java.util.Objects;

public class SignUp extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener{

    EditText confPass,userNmae,emailText,passText;
    Button next;
    TextView chooseUName,youMay;
    ConstraintLayout signUpLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_sign_up);
        userNmae=findViewById(R.id.newUsrName);
        next=findViewById(R.id.signUpButton);
        emailText=findViewById(R.id.emailText);
        passText=findViewById(R.id.passText);
        confPass=findViewById(R.id.confPassText);
        chooseUName=findViewById(R.id.chooseUName);
        youMay=findViewById(R.id.canChangeLater);
        signUpLayout=findViewById(R.id.signUp);
        signUpLayout.setOnClickListener(this);
        chooseUName.setOnClickListener(this);
        youMay.setOnClickListener(this);
        confPass.setOnKeyListener(this);
        userNmae.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>4 && emailText.getText().toString().length()>7 && passText.getText().toString().length()>5){
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonEnable));
                    next.setClickable(true);
                }else{
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonDisabled));
                    next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>7 && userNmae.getText().toString().length()>4 && passText.getText().toString().length()>5){
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonEnable));
                    next.setClickable(true);
                }else{
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonDisabled));
                    next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length()>5 && userNmae.getText().toString().length()>4 && emailText.getText().toString().length()>7){
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonEnable));
                    next.setClickable(true);
                }else{
                    next.setBackgroundColor(getResources().getColor(R.color.logInButtonDisabled));
                    next.setClickable(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    public void signUp(View view){
        if (passText.getText().toString().matches(confPass.getText().toString())) {
          ParseUser parseUser = new ParseUser();
          parseUser.setUsername(userNmae.getText().toString().toLowerCase());
          parseUser.setPassword(passText.getText().toString());
          parseUser.setEmail(emailText.getText().toString());
          parseUser.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {
                  if (e==null){
                      Toast.makeText(SignUp.this,"SignUp Success",Toast.LENGTH_LONG).show();
                      finish();
                  }else{
                      Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                  }
              }
          });
        }else{
            Toast.makeText(this,"Password does not match",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
            signUp(v);
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.signUp || v.getId()==R.id.chooseUName || v.getId()==R.id.canChangeLater){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }
}