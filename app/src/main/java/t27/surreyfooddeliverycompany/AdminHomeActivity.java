package t27.surreyfooddeliverycompany;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Comment;

import java.util.Collections;

import objectstodb.Account;

public class AdminHomeActivity extends AppCompatActivity {
    private static final String TAG = "AdminHomeActivity";
    private Intent intent;
    private DatabaseReference mDatabaseRef;

    private ListView listview;
    private static final int NUM_ITEMS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        listview = (ListView) findViewById(R.id.addedAccount_listview);

        //query the recent added accounts
        Query recentAccounts_query = mDatabaseRef.child("users").orderByChild("timestampCreated").limitToLast(NUM_ITEMS);

        //list adapter for added accounts
        FirebaseListAdapter<Account> adapter =new FirebaseListAdapter<Account>(this,Account.class,R.layout.admin_account_item_layout,recentAccounts_query) {
            @Override
            protected void populateView(View view, Account account, int i) {
                TextView text1=(TextView)view.findViewById(R.id.email);
                TextView text2=(TextView)view.findViewById(R.id.name);
                TextView text3=(TextView)view.findViewById(R.id.type);
                TextView text4=(TextView)view.findViewById(R.id.phone);
                String email_text = "Email: " + account.getEmail();
                String name_text = "Name: " + account.getName();
                String accountType_text = "Type: " + account.getAccountType();
                String phone_text = "Phone: " + account.getNumber();
                text1.setText(email_text);
                text2.setText(name_text);
                text3.setText(accountType_text);
                text4.setText(phone_text);
            }
        };
        listview.setAdapter(adapter);
    }

    public void addAccount(View view) {
        intent = new Intent(this, AdminAddAccountActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {

        //ad logout function

        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
