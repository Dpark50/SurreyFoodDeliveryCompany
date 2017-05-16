package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import objectstodb.Order;

public class DriverHomeActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private ListView listview;
    private String accountUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_home);
        final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.order_layout);
        listview = (ListView) findViewById(R.id.order_list);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        accountUID = user.getUid();

        Query queryOrders = mDatabaseRef.child("order").orderByChild("driverUID")
                .equalTo(accountUID);

        FirebaseListAdapter<Order> adapter = new FirebaseListAdapter<Order>(
                DriverHomeActivity.this, Order.class,
                R.layout.drivers_order__list, queryOrders) {
            @Override
            protected void populateView(View view, Order order, int i) {
                TextView text = (TextView) view.findViewById(R.id.order);
                text.setText(order.orderDetail());
            }
        };

        listview.setAdapter(adapter);
    }

    public void profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
