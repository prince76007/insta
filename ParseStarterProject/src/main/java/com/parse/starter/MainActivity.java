/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {
  EditText ursName, pass;
  TextView forgetPass, singUp, instaLogo;
  RelativeLayout loginLayout;
  Button loginButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Objects.requireNonNull(getSupportActionBar()).hide();
    ParseUser parseUser = ParseUser.getCurrentUser();
    if (parseUser != null) {
     gotoUserList();
    }
      setContentView(R.layout.activity_main);
    //Server password :   KX9gxOZxCHoy
    loginButton = findViewById(R.id.loginButton);
    ursName = findViewById(R.id.username);
    pass = findViewById(R.id.password);
    forgetPass = findViewById(R.id.forgetLink);
    singUp = findViewById(R.id.singUp);
    instaLogo = findViewById(R.id.instaLogo);
    loginLayout = findViewById(R.id.logInLayout);
    instaLogo.setOnClickListener(this);
    loginLayout.setOnClickListener(this);
    forgetPass.setText(Html.fromHtml("Forgotten your login details?<font color=#121D5A><b> Get help with logging in.</b></font>"));
    singUp.setText(Html.fromHtml("Don\'t have an account?<font color=#121D5A><b> Sign up.</b></font>"));
    forgetPass.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(MainActivity.this, "forget Password Clicked", Toast.LENGTH_SHORT).show();
      }
    });
    pass.setOnKeyListener(this);
    ursName.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 4 && pass.getText().length() > 4) {
          loginButton.setClickable(true);
          loginButton.setBackgroundColor(getResources().getColor(R.color.logInButtonEnable));
        } else {
          loginButton.setClickable(false);
          loginButton.setBackgroundColor(getResources().getColor(R.color.logInButtonDisabled));
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });


    pass.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 4 && ursName.getText().length() > 4) {
          loginButton.setClickable(true);
          loginButton.setBackgroundColor(getResources().getColor(R.color.logInButtonEnable));
        } else {
          loginButton.setClickable(false);
          loginButton.setBackgroundColor(getResources().getColor(R.color.logInButtonDisabled));
        }
      }

      @Override
      public void afterTextChanged(Editable s) {

      }
    });

    singUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
      }
    });


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }


  public void logIn(View view){
    try {
      ParseUser parseUser = new ParseUser();
      parseUser.logInInBackground(ursName.getText().toString().toLowerCase(), pass.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (e == null && user != null) {
            gotoUserList();
          } else if (e != null) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
          } else {
            Toast.makeText(MainActivity.this, "Something Went Wrong! Plese try again.", Toast.LENGTH_SHORT);
          }
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode==KeyEvent.KEYCODE_ENTER && event.getAction()==KeyEvent.ACTION_DOWN){
      logIn(v);
    }
    return false;
  }

  @Override
  public void onClick(View v) {
    if (v.getId()==R.id.logInLayout || v.getId()==R.id.instaLogo){
      InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }

  public void gotoUserList(){
    Intent intent = new Intent(getApplicationContext(),UserList.class);
    startActivity(intent);
  }
}
