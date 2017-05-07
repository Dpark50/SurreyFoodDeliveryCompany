package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import objectstodb.Account;

public class AdminAddAccountActivity extends AppCompatActivity {
    private static final String TAG = "AdminAddAccountActivity" ;
    private Intent intent;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabaseRef;

    //inputs
    private EditText email_EditText;
    private EditText password_EditText;
    private EditText password2_EditText;
    private EditText name_EditText;
    private EditText phone_EditText;
    private EditText address_EditText;
    private RadioGroup type_RadioGroup;
    private RadioButton accountType_RadioButton;

    private String accountUID;
    private String email;
    private String password;
    private String password2;
    private String name;
    private String phone;
    private String address;
    private String accountType;

    private Account addedAccount;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_account);

        email_EditText = (EditText) findViewById(R.id.email_et);
        password_EditText = (EditText) findViewById(R.id.password_et);
        password2_EditText = (EditText) findViewById(R.id.password2_et);
        name_EditText = (EditText) findViewById(R.id.name_et);
        phone_EditText = (EditText) findViewById(R.id.phone_et);
        address_EditText = (EditText) findViewById(R.id.address_et);
        type_RadioGroup = (RadioGroup) findViewById(R.id.type_radioGroup);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        intent = new Intent(this, AdminHomeActivity.class);
        mAuth = FirebaseAuth.getInstance();




    }

    public void submitAddAccount(View view) {

        accountType_RadioButton = (RadioButton) findViewById(type_RadioGroup.getCheckedRadioButtonId());
        accountType = accountType_RadioButton.getText().toString();
        email = email_EditText.getText().toString();
        password = password_EditText.getText().toString();
        password2 = password2_EditText.getText().toString();
        name = name_EditText.getText().toString() ;
        phone = phone_EditText.getText().toString();
        address = address_EditText.getText().toString();




        //validate inputs

        Log.d(TAG, "submitAddAccount: email " + email);
        Log.d(TAG, "submitAddAccount: pass " + password);
        //add account to firebase db
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(AdminAddAccountActivity.this, R.string.register_failed,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    //store to the database
                                    FirebaseUser user = task.getResult().getUser();
                                    accountUID = user.getUid();
                                    Log.d(TAG, "onComplete: uid=" + accountUID);

                                    if(user != null) {
                                        addedAccount = new Account(accountUID,
                                                accountType,
                                                email,
                                                password,
                                                name,
                                                phone,
                                                address);

                                        mDatabaseRef.child("users").child(accountUID).setValue(addedAccount);

                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }



                                }

                                // ...
                            }
                        });


    }

}
