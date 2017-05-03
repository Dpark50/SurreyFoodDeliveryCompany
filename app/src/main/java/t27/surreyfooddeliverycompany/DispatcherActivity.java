package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DispatcherActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatcher);
    }

    public void DriversList(View view) {
        intent = new Intent(this, DriversListActivity.class);
        finish();
        startActivity(intent);
    }

    public void Settings(View view) {
        intent = new Intent(this, DispatcherSettingsActivity.class);
        finish();
        startActivity(intent);
    }

    public void Profile(View view) {
        intent = new Intent(this, ProfileActivity.class);
        finish();
        startActivity(intent);
    }
}
