package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import objectstodb.Account;

public class ForgotPasswordActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private EditText text;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        text = (EditText) findViewById(R.id.input);
    }

    public void sendEmail(View view) {
        final String emailAddress = text.getText().toString();
        intent = new Intent(this, LoginActivity.class);

        auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Query dispatcherQuery = mDatabase.child("dispatcher");
                            Query adminQuery = mDatabase.child("admin");
                            queryAccount(dispatcherQuery, emailAddress);
                            queryAccount(adminQuery, emailAddress);
                            text.setError("Dispatcher or Admin email only");
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Invalid email",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void queryAccount(Query accountQuery, final String emailAddress) {
        accountQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Account account;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    account = snapshot.getValue(Account.class);
                    if (account.getEmail().compareTo(emailAddress) == 0) {
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent
                                .FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        Toast.makeText(ForgotPasswordActivity.this, "Email sent",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ForgotPasswordActivity.this, "Error reading email, please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
