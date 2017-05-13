package t27.surreyfooddeliverycompany;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import objectstodb.Account;

public class DispatcherNewOrdersActivity extends AppCompatActivity {
    private Intent intent;
    private TabHost tabHost;
    private DatabaseReference mDatabaseRef;
    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_new_orders);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        listview = (ListView) findViewById(R.id.drivers_list);
        Query queryDrivers = mDatabaseRef.child("driver").orderByChild("status").equalTo("online");
        setTabs(tabHost);

        FirebaseListAdapter<Account> adapter = new FirebaseListAdapter<Account>(
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

        listview.setAdapter(adapter);
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

    protected void mapButton (View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
