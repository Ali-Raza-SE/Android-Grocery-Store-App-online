package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditCustomerActivity extends AppCompatActivity {
    private EditText editName;
    private EditText editEmail;
    private String customerId; // To store the customer's ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        // Initialize UI elements
        editName = findViewById(R.id.editNameCustomer);
        editEmail = findViewById(R.id.editEmailCustomer);

        // Get the customer's ID from the intent
        customerId = getIntent().getStringExtra("customerId");

        // Fetch customer's details from Firebase using customerId
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("users").child(customerId);
        customerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve customer's details
                Customer customer = dataSnapshot.getValue(Customer.class);
                if (customer != null) {
                    // Update the UI elements with customer's details
                    editName.setText(customer.getName());
                    editEmail.setText(customer.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
            }
        });

        // Implement a button click listener to save changes
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the updated name and email from the input fields
                String updatedName = editName.getText().toString();
                String updatedEmail = editEmail.getText().toString();

                // Update the customer's details in Firebase
                customerRef.child("name").setValue(updatedName);
                customerRef.child("email").setValue(updatedEmail);

                // Provide feedback to the user
                Toast.makeText(EditCustomerActivity.this, "Customer details updated successfully!", Toast.LENGTH_SHORT).show();

                // Finish the activity and go back to the previous screen
                finish();
            }
        });
    }
}
