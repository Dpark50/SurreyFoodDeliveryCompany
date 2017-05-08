package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private Intent intent;
    private EditText idInput;
    private EditText passInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        idInput = (EditText) findViewById(R.id.id_input);
        passInput = (EditText) findViewById(R.id.password_input);
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    */

    public void login(View view) {
        RadioGroup radioGroup_login_type = (RadioGroup) findViewById(R.id.typedt);
        RadioButton selected = (RadioButton)findViewById(radioGroup_login_type.getCheckedRadioButtonId());
        final String loginType = selected.getText().toString();
        String id = idInput.getText().toString();
        String password = passInput.getText().toString();

        // ******Commented out for ease of access for now******
        /*
        if (isEmptyInput(id)) {
            idInput.setError("Enter your ID");
            return;
        }

        if (isEmptyInput(password)) {
            passInput.setError("Enter your password");
            return;
        }
        */
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

        /* ***Commented out to login without validation for now ***
        mAuth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // Sign in success, update UI with the signed-in user's information
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String accountUID = user.getUid();
                            Query accountQuery = mDatabase.child("users").child(accountUID);
                            accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Account account = dataSnapshot.getValue(Account.class);
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Invalid username or password",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                */
    }

    public Boolean isEmptyInput(String input) {
        if (TextUtils.isEmpty(input)) {
            return true;
        }

        return false;
    }
}
