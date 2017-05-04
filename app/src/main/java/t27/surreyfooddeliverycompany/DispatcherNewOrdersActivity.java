package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

public class DispatcherNewOrdersActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TabHost tabHost;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_new_orders);
        tabHost = (TabHost)findViewById(R.id.tabhost);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("Tab1").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.orders_btn, null))
                .setContent(R.id.orders_btn);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Tab2").setIndicator(null,
                ResourcesCompat.getDrawable(getResources(),
                        R.drawable.inprogress_btn, null))
                .setContent(R.id.inprogress_btn);
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Tab3").setIndicator(null,
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
    }
}