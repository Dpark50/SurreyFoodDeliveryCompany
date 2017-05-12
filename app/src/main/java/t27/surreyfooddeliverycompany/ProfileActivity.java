package t27.surreyfooddeliverycompany;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    public void signOut (View view) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        SharedPreferences preferences = getSharedPreferences(getString(
                R.string.user_preference), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        final String accountUID = user.getUid();

        database.child("driver").child(accountUID).child("status").setValue("offline");
        database.child("driver").child(accountUID).child("idle").setValue("idle");
        mAuth.signOut();
        editor.clear();
        editor.apply();

        intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
