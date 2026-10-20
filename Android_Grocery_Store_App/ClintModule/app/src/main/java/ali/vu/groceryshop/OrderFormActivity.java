package ali.vu.groceryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import ali.vu.groceryshop.CartManager;


public class OrderFormActivity extends AppCompatActivity {

    private EditText nameEditText, numberEditText, addressEditText, cityEditText, timeSlotEditText;
    private Button placeOrderButton;
    private CartManager cartManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_form);
        setTitle("Order Details");

        cartManager = CartManager.getInstance();


        // Get the total bill and product details from the Intent extras
        double totalBill = getIntent().getDoubleExtra("TOTAL_BILL", 0.0);
        ArrayList<String> productDetails = getIntent().getStringArrayListExtra("PRODUCT_DETAILS");

        // Get the user's ID from the Intent extras
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userID = currentUser.getUid();

        nameEditText = findViewById(R.id.nameEditText);
        numberEditText = findViewById(R.id.phoneNumberEditText);
        addressEditText = findViewById(R.id.addressEditText);
        cityEditText = findViewById(R.id.cityEditText);
        timeSlotEditText = findViewById(R.id.deliveryTimeSlotEditText);
        placeOrderButton = findViewById(R.id.placeOrderButton);

        placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input from the form
                String name = nameEditText.getText().toString().trim();
                String number = numberEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String city = cityEditText.getText().toString().trim();
                String deliveryTimeSlot = timeSlotEditText.getText().toString().trim();

                // Validate input fields
                if (name.isEmpty() || number.isEmpty() || address.isEmpty() || city.isEmpty() || deliveryTimeSlot.isEmpty()) {
                    Toast.makeText(OrderFormActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Create an instance of the Order class with all order details
                    Order order = new Order(
                            null,
                            userID,
                            name,
                            number,
                            address,
                            city,
                            deliveryTimeSlot,
                            "Cash on Delivery",
                            "Pending",
                            productDetails,
                            totalBill
                    );

                    // Store the order in Firebase
                    saveOrderToFirebase(order);

                    // Clear the cart after placing the order
                    cartManager.clearCart();

                    // Display a success message
                    Toast.makeText(OrderFormActivity.this, "Order placed successfully!", Toast.LENGTH_SHORT).show();

                    // Clear input fields
                    clearForm();

                    startActivity(new Intent(getApplicationContext(),HomePage.class));
                    finish();
                }
            }
        });

    }

    private void saveOrderToFirebase(Order order) {
        // Initialize Firebase database reference (you need to set up Firebase in your project)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("orders");

        // Push the order to the Firebase database to generate a unique key
        DatabaseReference newOrderRef = ordersRef.push();

        // Set the unique key as the order ID
        order.setOrderId(newOrderRef.getKey());

        // Save the order to Firebase using the unique key
        newOrderRef.setValue(order);
    }

    private void clearForm() {
        nameEditText.getText().clear();
        numberEditText.getText().clear();
        addressEditText.getText().clear();
        cityEditText.getText().clear();
        timeSlotEditText.getText().clear();
    }
}
