package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ProfileActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    protected void signOut (View view) {
        //sign out
        finish();
    }

    public void Orders(View view) {
        intent = new Intent(this, DispatcherNewOrdersActivity.class);
        finish();
        startActivity(intent);
    }

    public void DriversList(View view) {
        intent = new Intent(this, DispatcherDriversListActivity.class);
        finish();
        startActivity(intent);
    }

    public void Settings(View view) {
        intent = new Intent(this, DispatcherPendingOrdersActivity.class);
        finish();
        startActivity(intent);
    }
}
