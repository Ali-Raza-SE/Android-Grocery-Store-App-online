package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText editTextCustomerName;
    private EditText editTextCustomerEmail;
    private EditText editTextCustomerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        editTextCustomerName = findViewById(R.id.editTextCustomerName);
        editTextCustomerEmail = findViewById(R.id.editTextCustomerEmail);
        editTextCustomerPassword = findViewById(R.id.editTextCustomerPassword);

        Button buttonSaveCustomer = findViewById(R.id.buttonSaveCustomer);


        buttonSaveCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextCustomerName.getText().toString().trim();
                String email = editTextCustomerEmail.getText().toString().trim();
                String password = editTextCustomerPassword.getText().toString();

                // Check if fields are empty
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "Signup successful!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    // Signup failed, display an error message
                                    Toast.makeText(getApplicationContext(), "Signup failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }


}
