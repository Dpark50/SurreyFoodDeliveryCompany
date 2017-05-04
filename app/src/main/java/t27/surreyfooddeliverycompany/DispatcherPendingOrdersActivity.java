package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DispatcherPendingOrdersActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_pending_orders);
    }

    public void newOrders(View view) {
        intent = new Intent(this, DispatcherNewOrdersActivity.class);
        finish();
        startActivity(intent);
    }

    public void driversList(View view) {
        intent = new Intent(this, DispatcherDriversListActivity.class);
        finish();
        startActivity(intent);
    }

    public void profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
