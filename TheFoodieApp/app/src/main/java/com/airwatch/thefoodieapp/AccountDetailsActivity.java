package com.airwatch.thefoodieapp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * Created by rarodi on 8/18/2015.
 */
public class AccountDetailsActivity extends Activity {

    private TextView mCustomerDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        mCustomerDetails = (TextView) findViewById(R.id.details);
        mCustomerDetails.setText(getResources().getString(R.string.details, getCustomerName(),getCustomerMail()));
    }

    private String getCustomerName() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString(PreferenceConstants.Key_name, null);
    }

    private String getCustomerMail() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString(PreferenceConstants.Key_mail, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==R.id.account_history){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
