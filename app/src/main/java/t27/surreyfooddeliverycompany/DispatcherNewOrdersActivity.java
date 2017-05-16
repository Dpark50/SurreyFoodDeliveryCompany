package t27.surreyfooddeliverycompany;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LocalOrders.CachedOrderPrefrence;
import LocalOrders.InProgressAdapter;
import LocalOrders.NewOrderAdapter;
import LocalOrders.newAndInProgressFun;
import objectstodb.Account;
import objectstodb.Order;

public class DispatcherNewOrdersActivity extends AppCompatActivity {
    private Intent intent;
    private TabHost tabHost;
    private DatabaseReference mDatabaseRef;
    private ListView listview;
    private TextView name;
    private TextView address;
    private TextView email;
    private TextView phoneNumber;


    //all the order records grabbed from db when this activity is loaded the first time
    private List<Order> orders_list;
    private HashMap<String,Order> map_uid_to_order;
    //two lists in two tabs
    private ArrayList<Order> newOrder_list;
    private ArrayList<Order> inProgress_list;

    private NewOrderAdapter new_ordersAdapter;
    private InProgressAdapter inprogress_orderAdapter;

    boolean initialOrdersLoad;

    private FirebaseListAdapter<Account> driversAdapter;

    private ListView neworderListView;
    private ListView inprogressListView;
    private ChildEventListener childAddedListener;
    private Query queryOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_new_orders);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        listview = (ListView) findViewById(R.id.drivers_list);
        name = (TextView) findViewById(R.id.name);
        address = (TextView) findViewById(R.id.edit_address);
        email = (TextView) findViewById(R.id.email);
        phoneNumber = (TextView) findViewById(R.id.phone);

        neworderListView = (ListView) findViewById(R.id.new_order_list);
        inprogressListView =(ListView)findViewById(R.id.inprogress_order_list);

        setTabs(tabHost);
        Query queryDrivers = mDatabaseRef.child("driver").orderByChild("status").equalTo("online");


        driversAdapter = new FirebaseListAdapter<Account>(
                DispatcherNewOrdersActivity.this, Account.class,
                R.layout.dispatcher_drivers_list, queryDrivers) {
            @Override
            protected void populateView(View view, Account account, int i) {
                TextView text = (TextView) view.findViewById(R.id.driver);
                String driverDetails;
                if (account.getStatus().compareTo("online") == 0) {
                    driverDetails = account.getName() + "\nStatus: " +
                            account.getIdle() + "\nPhone Number: " + account.getNumber();
                    text.setText(driverDetails);
                }
            }
        };

        listview.setAdapter(driversAdapter);
        setProfileInfo();

        //new order page and in-progress page

        map_uid_to_order = new HashMap<String,Order>();
        initialOrdersLoad = false;

        //-----------start---------------------get orders from db and merge them with local records
        queryOrders = mDatabaseRef.child("order");

        queryOrders.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //after the initial is loaded
                if(initialOrdersLoad) {
                    Order neworder = dataSnapshot.getValue(Order.class);
                    //add new order to the new order list ui
                    map_uid_to_order.put(neworder.getOrderUid(),neworder);
                    newOrder_list.add(neworder);
                    // Sorting
                    sortList(newOrder_list);


                    new_ordersAdapter.notifyDataSetChanged();

                    childAddedListener = this;


                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        queryOrders.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<HashMap<String,Order>> type_orders_list =
                        new GenericTypeIndicator<HashMap<String,Order>>() {};
                map_uid_to_order = dataSnapshot.getValue(type_orders_list);
                //store as a map

                //get current email for saving orders to sharedPreference
                String curEmail = getApplicationContext().getSharedPreferences(
                        getString(R.string.user_preference), Context.MODE_PRIVATE).getString("curEmail",null);
                //store orders as hashMap
                //merge with the old ones
                CachedOrderPrefrence.saveOrderMapToAppByEmail(getApplicationContext(),curEmail,map_uid_to_order);
                //get the orders in correct state
                map_uid_to_order.putAll(CachedOrderPrefrence.getOrderByEmail(getApplicationContext(),curEmail)) ;
                newOrder_list = newAndInProgressFun.getNewOrdersFromMap(map_uid_to_order);
                inProgress_list = newAndInProgressFun.getInProgressOrdersFromMap(map_uid_to_order);
                // Sorting new orders
                sortList(newOrder_list);
                sortList(inProgress_list);

                new_ordersAdapter = new NewOrderAdapter(DispatcherNewOrdersActivity.this,newOrder_list,R.layout.new_order_item_layout,
                                                        R.id.tvType,R.id.tvDetail,R.id.tvStatus);
                inprogress_orderAdapter = new InProgressAdapter(DispatcherNewOrdersActivity.this,inProgress_list,R.layout.in_progress_item_layout,
                        R.id.tvType,R.id.tvDetail,R.id.tvStatus);
                neworderListView.setAdapter(new_ordersAdapter);
                inprogressListView.setAdapter(inprogress_orderAdapter);
                initialOrdersLoad  =true;
                neworderListView.setOnItemClickListener(new NewOrderItemOnClickListener());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(DispatcherNewOrdersActivity.this,"Fail to load orders",Toast.LENGTH_LONG).show();
            }
        });
        //--------------end------get orders from db and merge them with local records
    }

    public void setTabColor(TabHost tabhost) {
        int i = 0;

        for(i = 0; i<tabhost.getTabWidget().getChildCount(); i++) {
            tabhost.getTabWidget().getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }

        // Change colour of the selected tab
        tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab())
                .setBackgroundColor(Color.parseColor("#DBE4FB"));
    }

    private void setTabs(TabHost tabhost) {
        tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab1").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.orders_btn, null))
                .setContent(R.id.orders_btn);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab2").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.inprogress_btn, null))
                .setContent(R.id.inprogress_btn);
        final TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab3").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.drivers_btn, null))
                .setContent(R.id.drivers_btn);
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Tab4").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.profile_btn, null))
                .setContent(R.id.profile_btn);
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);
        tabHost.addTab(tab3);
        tabHost.addTab(tab4);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String arg0) {
                setTabColor(tabHost);
            }
        });

        setTabColor(tabHost);
    }

    private void setProfileInfo() {
        SharedPreferences userPreference = getApplicationContext().getSharedPreferences(
                getString(R.string.user_preference), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = userPreference.getString("userObject", null);
        if (json != null) {
            Account account = gson.fromJson(json, Account.class);
            String accountName = account.getName();
            String accountAddress = account.getAddress();
            String accountEmail = account.getEmail();
            String accountPhone = account.getNumber();

            name.setText(accountName);
            address.setText(accountAddress);
            email.setText(accountEmail);
            phoneNumber.setText(accountPhone);
        }
    }

    public void SignOut(View view) {
        SharedPreferences preferences = getSharedPreferences(getString(
                R.string.user_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void mapButton (View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    private void sortList(ArrayList<Order> order_list) {
        Collections.sort(order_list, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if(o1.getTimestampCreated()==null||o2.getTimestampCreated()==null) {
                    return 1;
                }
                Long otime1 = o1.getDateCreatedLong();
                Long otime2 = o2.getDateCreatedLong();
                return otime1.compareTo(otime2);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //detach
        queryOrders.removeEventListener(childAddedListener);
    }

    //onclick for new orders
    private class NewOrderItemOnClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Order selectedOrder = (Order)parent.getItemAtPosition(position);

            // driver list to be selected
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(DispatcherNewOrdersActivity.this);
            //builderSingle.setIcon(R.drawable.ic_launcher);
            builderSingle.setTitle("Select One driver:");

            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builderSingle.setAdapter(driversAdapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Account driver = driversAdapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(DispatcherNewOrdersActivity.this);
                    builderInner.setMessage(driver.getName());
                    builderInner.setTitle("Your Selected Item is");
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();
                }
            });
            builderSingle.show();
        }
    }
}
