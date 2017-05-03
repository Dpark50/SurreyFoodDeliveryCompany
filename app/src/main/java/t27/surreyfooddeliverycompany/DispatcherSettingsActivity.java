package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DispatcherSettingsActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher_settings);
    }

    public void Orders(View view) {
        intent = new Intent(this, DispatcherActivity.class);
        finish();
        startActivity(intent);
    }

    public void DriversList(View view) {
        intent = new Intent(this, DriversListActivity.class);
        finish();
        startActivity(intent);
    }

    public void Profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
