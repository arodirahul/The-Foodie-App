package com.airwatch.thefoodieapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by rarodi on 8/18/2015.
 */

public class LoginActivity extends AppCompatActivity {

    private Button mLogin;
    private EditText mUserName,mEmail;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        TextView textViewCustom = (TextView)findViewById(R.id.title);
        Typeface wednesdayFont = Typeface.createFromAsset(getAssets(),"fonts/Wednesday.ttf");
        textViewCustom.setTypeface(wednesdayFont);

        final Intent intent = new Intent(context,MainActivity.class);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        String user = pref.getString(PreferenceConstants.Key_name, "");
        String mail = pref.getString(PreferenceConstants.Key_mail,"");

            if(user.isEmpty() && mail.isEmpty()){

                mLogin = (Button)findViewById(R.id.login);
                mUserName = (EditText)findViewById(R.id.userName);
                mEmail = (EditText)findViewById(R.id.email);

                mLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(context, MainActivity.class);

                        if((mUserName.getText().toString().isEmpty())|(mEmail.getText().toString().isEmpty())){
                            alertUser();
                        }

                        if(mUserName.getText().toString().isEmpty()){
                            mUserName.setError("Empty Username!");
                        }

                        if(mEmail.getText().toString().isEmpty()){
                            mEmail.setError("Empty Email!");
                        }

                        if((mUserName.getText().toString().compareTo("Rahul")==0) &&(mEmail.getText().toString().compareTo("a@gmail.com")==0)) {
                            saveDetails();
                            startActivity(intent);
                            finish();
                        }
                        else{
                            if((mUserName.getText().toString().compareTo("Rahul")!=0) && !(mUserName.getText().toString().isEmpty())){
                                mUserName.setError("Incorrect Username!");
                            }
                            if((mEmail.getText().toString().compareTo("a@gmail.com")!=0) && !(mEmail.getText().toString().isEmpty())){
                                mEmail.setError("Incorrect Email!");
                            }
                        }

                }
            });
        }

        else
        {
            startActivity(intent);
            finish();
        }
    }

    private  void saveDetails(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PreferenceConstants.Key_name, mUserName.getText().toString());
        editor.putString(PreferenceConstants.Key_mail, mEmail.getText().toString());
        editor.commit();
    }

    private  void alertUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert!");
        builder.setMessage("Either Email or username is empty");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

}
