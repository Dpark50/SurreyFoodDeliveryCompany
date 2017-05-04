package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        EditText idEditText = (EditText) findViewById(R.id.id_input);
        String loginType = idEditText.getText().toString();
        if (loginType.compareTo("dispatcher") == 0)
            intent = new Intent(this, DispatcherNewOrdersActivity.class);
        else if (loginType.compareTo("driver") == 0)
            intent = new Intent(this, DriverHomeActivity.class);
        else if (loginType.compareTo("admin") == 0)
            intent = new Intent(this, AdminHomeActivity.class);
        else {
            Toast.makeText(getApplicationContext(), "Invalid user name or password.", Toast.LENGTH_LONG).show();
            return;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void Admin(View view) {
        Intent intent = new Intent(this, DispatcherNewOrdersActivity.class);
        startActivity(intent);
    }
}
