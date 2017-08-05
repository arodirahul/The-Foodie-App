package com.airwatch.thefoodieapp;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by rarodi on 8/18/2015.
 */


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button mOrderNowButton, mIncrement, mDecrement;
    private TextView mValue, mPing;
    private TextView mCustomerName;
    private String mStringVal;
    private TextView mCost;
    String remember="cupcount";
    String remember2="dish";
    String remember3="dishCost";

    public static final String KEY_DISH = "dishname";
    public static final String KEY_COUNT = "count";
    public static final String KEY_COST = "cost";

    private TextView salTextView;
    TextView selMenu;
    ProgressDialog pd;
    Spinner spinnerMenu;
    String selState ="Cupcake";
    int mNumberOfCups= 2, totalCost = 0 ,dishPosition=0;
    private String[] state = { "Cupcake - Rs.20", "Donut - Rs.10", "Eclair - Rs.25", "Vada Pav - Rs.15",
            "Cheese Sandwich - Rs.30", "Pizza - Rs.80", "Croissant - Rs.40", "North Indian Thali - Rs.65",
            "Pasta - Rs.55" };
    private int[] itemCost = {20,10,25,15,30,80,40,65,55};

    Thread backgroundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Spinner code----------------------------------------------------------------
        System.out.println(state.length);
        selMenu = (TextView) findViewById(R.id.expanded_menu);
        spinnerMenu = (Spinner) findViewById(R.id.spinnerstate);
        ArrayAdapter<String> adapter_state;
        adapter_state = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, state);
        adapter_state.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMenu.setAdapter(adapter_state);
        spinnerMenu.setOnItemSelectedListener(this);
        //spinner code ends here-------------------------------------------------------


        salTextView = (TextView) findViewById(R.id.summary1);
        salTextView.setText(getResources().getString(R.string.summary1, mNumberOfCups, selState ));

        mIncrement = (Button) findViewById(R.id.incr);
        mDecrement = (Button) findViewById(R.id.decr);
        mOrderNowButton = (Button) findViewById(R.id.order);
        mPing = (TextView) findViewById(R.id.ping);

        mCost = (TextView)findViewById(R.id.cost);
        mValue = (TextView)findViewById(R.id.value);
        mValue.setText("2");
        mCustomerName = (TextView) findViewById(R.id.custName);
        mCustomerName.setText(getResources().getString(R.string.cusName, getCustomerName()));
        mCost.setText(getResources().getString(R.string.cost, 0,0,0));

            if (savedInstanceState != null) {
               mNumberOfCups = savedInstanceState.getInt(remember);
               totalCost  = savedInstanceState.getInt(remember3);
               selState = savedInstanceState.getString(remember2);
               mValue.setText(Integer.toString(mNumberOfCups));
               salTextView.setText(getResources().getString(R.string.summary1, mNumberOfCups, selState));
               mCost.setText(getResources().getString(R.string.cost, itemCost[dishPosition],mNumberOfCups, totalCost));
            }

        View.OnClickListener orderNowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerOrderReceiver();
                Log.i("FoodieApp", "Success");
                totalCost = mNumberOfCups*itemCost[dishPosition];
                salTextView.setText(getResources().getString(R.string.summary1, mNumberOfCups, selState));
                mCost.setText(getResources().getString(R.string.cost,itemCost[dishPosition],mNumberOfCups, totalCost));
                Intent intentOrder = new Intent(MainActivity.this, OrderHistoryActivity.class);
                intentOrder.putExtra(KEY_DISH, selState);
                intentOrder.putExtra(KEY_COUNT, String.valueOf(mNumberOfCups));
                intentOrder.putExtra(KEY_COST, String.valueOf(totalCost));
                startActivity(intentOrder);
            }
        };
        mOrderNowButton.setOnClickListener(orderNowListener);

        View.OnClickListener rechargeNowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Your Balance is recharging..", Toast.LENGTH_LONG).show();
            }
        };
        mOrderNowButton.setOnClickListener(orderNowListener);

        View.OnClickListener incrNowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNumberOfCups++;
                mStringVal = Integer.toString(mNumberOfCups);
                mValue.setText(mStringVal);
            }
        };
        mIncrement.setOnClickListener(incrNowListener);

        View.OnClickListener decrNowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNumberOfCups > 2) {
                    mNumberOfCups--;
                }
                mStringVal = Integer.toString(mNumberOfCups);
                mValue.setText(mStringVal);
            }
        };
        mDecrement.setOnClickListener(decrNowListener);

        View.OnClickListener pingNowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Pinging..", Toast.LENGTH_LONG).show();
                pingAServer(v);
            }
        };
        mPing.setOnClickListener(pingNowListener);

    }

    private String getCustomerName() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString(PreferenceConstants.Key_name, null);
    }


    private void showOrderProgress(){
        if(pd==null){
            pd=new ProgressDialog(this);
        }
        pd.setIcon(R.drawable.notification_template_icon_bg);
        pd.setTitle("Please Wait...");
        pd.setMessage("Order in progress..");
        pd.setCancelable(true);
        pd.show();
        Log.i("CoffeeApp", "pd shown");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000); //Do your work here.
                } catch (Exception e) {
                }pd.dismiss();
            }
        }).start();
        MyIntentService.startActionOrder(this, selState, Integer.toString(mNumberOfCups));
        Log.i("CoffeeApp", "startActionOrder sent");
                pd.dismiss();
    }

    private  void orderNow(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void orderByEmail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"arodirahul@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Order by " + mCustomerName.getText());
        intent.putExtra(Intent.EXTRA_TEXT, "Number of : " + selState + " : " + mNumberOfCups);
        startActivity(intent);
        Log.i("CoffeeApp", "Order now clicked");
       /*Intent intent2 = new Intent(Intent.ACTION_VIEW);
            intent2.setData(Uri.parse("http://www.royalenfield.com"));
            startActivity(intent2);*/
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerMenu.setSelection(position);
        dishPosition = position;
        selState = (String) spinnerMenu.getSelectedItem();
        selMenu.setText("Selected Dish:" + selState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            //return true;
            startActivity(new Intent(MainActivity.this, AccountDetailsActivity.class));
        }

        if (id == R.id.account_history) {
            //return true;
            startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(remember, mNumberOfCups);
        savedInstanceState.putString(remember2, selState);
        savedInstanceState.putInt(remember3, totalCost);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //do sth
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void pingAServer(View view){
        NetworkThread customThread = new NetworkThread();
        customThread.start();
        Intent intent2 = new Intent(Intent.ACTION_VIEW);
        intent2.setData(Uri.parse("http://www.foodpanda.in"));
        startActivity(intent2);
    }

    // Broadcast related blocks----------------------------------------------------

    BroadcastReceiver orderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("OrderResult", "status returned from service");
            if(intent!=null && intent.getAction().equalsIgnoreCase("action.youraction")){
                dismissOrderProgress();
                showOrderResult(intent.getBooleanExtra("result",false));
            }
        }
    };

    private  void dismissOrderProgress(){
        pd.dismiss();
    }

    private void registerOrderReceiver(){
        IntentFilter orderFilter = new IntentFilter();
        orderFilter.addAction("action.orderPlaced");
        orderFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(orderReceiver, orderFilter);
    }

    private void showOrderResult(boolean result){
        if(result==true)
        {
            Toast.makeText(MainActivity.this,"Order Successful", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Order Unsuccessful", Toast.LENGTH_LONG).show();
        }
    }

    private void unregisterOrderReceiver(){
        unregisterReceiver(orderReceiver);
    }

    // Broadcast related blocks end here-----------------------------------------------
}
