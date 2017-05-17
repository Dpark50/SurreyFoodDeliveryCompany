package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import static t27.surreyfooddeliverycompany.R.id.uid;

public class DriverHomeActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private ListView listview;
    private String accountUID;
    private AlertDialog alert;
    private Button accept;
    private Button complete;
    private String orderStatus;
    private String orderUid;

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
                TextView statusText = (TextView) view.findViewById(R.id.order_status);
                TextView uidText = (TextView) view.findViewById(uid);

                if (order.getState().compareTo("processing") == 0 ||
                        order.getState().compareTo("delivering") == 0) {
                    String orderStatus = "Order Status: <font color=\"blue\">" +
                            order.getState() + "</font>";
                    text.setText(order.orderDetail());
                    statusText.setText(Html.fromHtml(orderStatus), TextView.BufferType.SPANNABLE);
                    uidText.setText("Order UID: " + order.getOrderUid() + "\n" +
                            order.getTimeStamp());
                }
            }
        };

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textviewStatus = (TextView) view.findViewById(R.id.order_status);
                TextView textviewUid = (TextView) view.findViewById(uid);
                String status = textviewStatus.getText().toString();
                String uid = textviewUid.getText().toString();

                // Parse textview string to order state
                orderStatus = status.substring(14);

                // Parse textview string to uid
                orderUid = uid.substring(11);

                displayDialog();
            }
        });

        listview.setAdapter(adapter);
    }

    private void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.order_completion, null);
        builder.setView(dialogView);

        TextView title = (TextView) dialogView.findViewById(R.id.title);
        accept = (Button) dialogView.findViewById(R.id.order_accept);
        complete = (Button) dialogView.findViewById(R.id.order_complete);
        String dialogTitle = "Order Status";
        title.setText(dialogTitle);

        if (orderStatus.compareTo("delivering") == 0) {
            accept.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            accept.setTextColor(Color.GRAY);
            accept.setEnabled(false);
            complete.getBackground().setColorFilter(null);
            complete.setEnabled(true);
        } else {
            complete.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
            complete.setTextColor(Color.GRAY);
            complete.setEnabled(false);
        }

        alert = builder.create();
        alert.show();
    }

    public void profile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    public void accept(View view) {
        mDatabaseRef.child("order").child(orderUid).child("state").setValue("delivering");
        alert.dismiss();
    }

    public void complete(View view) {
        mDatabaseRef.child("order").child(orderUid).child("state").setValue("finished");

        // Append to driverUID to detect changes
        mDatabaseRef.child("order").child(orderUid).child("driverUID").setValue(accountUID +
                " (Finished)");
        alert.dismiss();
    }
}
