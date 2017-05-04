package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DispatcherNewOrdersActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_new_orders);
    }

    public void DriversList(View view) {
        intent = new Intent(this, DispatcherDriversListActivity.class);
        finish();
        startActivity(intent);
    }

    public void PendingOrders(View view) {
        intent = new Intent(this, DispatcherPendingOrdersActivity.class);
        finish();
        startActivity(intent);
    }

    public void Profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
