package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the title bar and action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Make the status bar transparent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_signup);
        FirebaseApp.initializeApp(this);


        EditText edtSignupName = findViewById(R.id.edtSignupName);
        EditText edtSignupEmail = findViewById(R.id.edtSignupEmail);
        EditText edtSignupPassword = findViewById(R.id.edtSignupPassword);
        Button btnSignup = findViewById(R.id.btnSignup);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtSignupName.getText().toString().trim();
                String email = edtSignupEmail.getText().toString().trim();
                String password = edtSignupPassword.getText().toString();

                // Check if fields are empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return; // Stop further execution
                }

                // Create a new user account in Firebase Authentication
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // User account created successfully
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    // Save user details to the Firebase Realtime Database
                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                    User user = new User(userId, name, email);
                                    usersRef.child(userId).setValue(user);

                                    // Show a success message or navigate to the next activity
                                    Toast.makeText(Signup.this, "Signup successful!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Signup failed, display an error message
                                    Toast.makeText(Signup.this, "Signup failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });






    }
}