package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class DriversListActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_list);
    }

    public void Orders(View view) {
        intent = new Intent(this, DispatcherActivity.class);
        startActivity(intent);
    }

    public void Settings(View view) {
        intent = new Intent(this, DispatcherSettingsActivity.class);
        startActivity(intent);
    }

    public void Profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
}
