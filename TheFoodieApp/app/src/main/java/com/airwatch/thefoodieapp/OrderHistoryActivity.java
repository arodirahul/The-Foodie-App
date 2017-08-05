package com.airwatch.thefoodieapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import static com.airwatch.thefoodieapp.MainActivity.KEY_DISH;
import static com.airwatch.thefoodieapp.MainActivity.KEY_COUNT;
import static com.airwatch.thefoodieapp.MainActivity.KEY_COST;

/**
 * Created by rarodi on 8/18/2015.
 */


public class OrderHistoryActivity extends Activity {
    SQLiteDatabase db;
    TextView tv;
    TextView et1,et2,et3;
    public String stringData,numberData,costData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        tv=(TextView)findViewById(R.id.textView1);
        et1=(TextView)findViewById(R.id.editText1);
        et2=(TextView)findViewById(R.id.editText2);
        et3=(TextView)findViewById(R.id.text3);


        Intent intent = getIntent();
        if (intent!=null) {
            String stringData= getIntent().getStringExtra(KEY_DISH);
            String numberData = getIntent().getStringExtra(KEY_COUNT);
            String costData = getIntent().getStringExtra(KEY_COST);

            et1.setText(stringData);
            et2.setText(numberData);
            et3.setText(costData);
        }



        db= openOrCreateDatabase("Mydb", MODE_PRIVATE, null);

        db.execSQL("create table if not exists mytable(name varchar, quantity varchar, cost varchar)");
    }

    //Confirm order button. Only after confirmation, the order will be saved
    public void insert(View v)
    {
        String name = et1.getText().toString();
        String dishQuantity = et2.getText().toString();
        String dishCost = et3.getText().toString();
        et1.setText(stringData);
        et2.setText(numberData);
        et3.setText(costData);

        //insert data into table
        db.execSQL("insert into mytable values('"+name+"','"+dishQuantity+"','"+dishCost+"')");

        Toast.makeText(this, "Order placed successfully.", Toast.LENGTH_LONG).show();
    }

    //To display order history
    public void display(View v)
    {
        Cursor c=db.rawQuery("select * from mytable", null);
        tv.setText("");
        //move cursor to first position
        c.moveToFirst();
        //fetch all data one by one
        do
        {
            String name=c.getString(c.getColumnIndex("name"));
            String dishQuantity=c.getString(1);
            String dishCost = c.getString(2);
            tv.append("  Dish Name : "+name+" \n  Count          : "+dishQuantity+" \n  Cost            : Rs."+dishCost+"\n\n");
        }while(c.moveToNext());
    }
}
