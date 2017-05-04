package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DispatcherDriversListActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_drivers_list);
    }

    public void newOrders(View view) {
        intent = new Intent(this, DispatcherNewOrdersActivity.class);
        finish();
        startActivity(intent);
    }

    public void pendingOrders(View view) {
        intent = new Intent(this, DispatcherPendingOrdersActivity.class);
        finish();
        startActivity(intent);
    }

    public void profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
