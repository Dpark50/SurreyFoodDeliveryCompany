package t27.surreyfooddeliverycompany;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import objectstodb.Account;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Intent intent;
    private EditText idInput;
    private EditText passInput;
    SharedPreferences userPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginRedirect();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        idInput = (EditText) findViewById(R.id.id_input);
        passInput = (EditText) findViewById(R.id.password_input);
    }

    public void login(View view) {
        RadioGroup radioGroup_login_type = (RadioGroup) findViewById(R.id.typedt);
        RadioButton selected = (RadioButton)findViewById(radioGroup_login_type.getCheckedRadioButtonId());
        //type of the employee
        final String loginType = selected.getText().toString();
        String id = idInput.getText().toString();
        String password = passInput.getText().toString();

        if (InputValidation.isEmptyInput(id)) {
            idInput.setError("Enter your ID");
            return;
        }

        if (InputValidation.isEmptyInput(password)) {
            passInput.setError("Enter your password");
            return;
        }

        if (loginType.compareTo("dispatcher") == 0)
            intent = new Intent(this, DispatcherNewOrdersActivity.class);
        else if (loginType.compareTo("driver") == 0)
            intent = new Intent(this, DriverHomeActivity.class);
        else if (loginType.compareTo("admin") == 0)
            intent = new Intent(this, AdminHomeActivity.class);
        else {
            Toast.makeText(getApplicationContext(), "Invalid user name or password", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            signInEmployee(loginType);

                            //refresh the notifi token
                            if(loginType.equals("dispatcher")) {
                                mDatabase.child("dispatch_token").setValue(FirebaseInstanceId.getInstance().getToken());
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Invalid username or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInEmployee(final String loginType) {
        FirebaseUser user = mAuth.getCurrentUser();
        final String accountUID = user.getUid();
        //Query to add one employee
        Query accountQuery = mDatabase.child(loginType).child(accountUID);

        accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account = dataSnapshot.getValue(Account.class);
                userPreferences = getApplicationContext().getSharedPreferences(
                        getString(R.string.user_preference), Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = userPreferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(account);
                prefsEditor.putString("accountUID", accountUID);
                prefsEditor.putString("userObject", json);
                prefsEditor.putString("loginType", loginType);
                prefsEditor.apply();

                if (account.getAccountType().compareTo(loginType) == 0) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                            .FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Invalid username or password",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginRedirect() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(
                getString(R.string.user_preference), Context.MODE_PRIVATE);

        if (preferences.getString("accountUID", null) != null) {
            if (preferences.getString("loginType", null).compareTo("driver") == 0) {
                intent = new Intent(this, DriverHomeActivity.class);
                startActivity(intent);
                finish();
            } else if (preferences.getString("loginType", null).compareTo("dispatcher") == 0) {
                intent = new Intent(this, DispatcherNewOrdersActivity.class);
                startActivity(intent);
                finish();
            } else if (preferences.getString("loginType", null).compareTo("admin") == 0) {
                intent = new Intent(this, AdminHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    public void forgotPassword(View view) {
        intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}
