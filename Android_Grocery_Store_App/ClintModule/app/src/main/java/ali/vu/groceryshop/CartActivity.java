package ali.vu.groceryshop;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartRecyclerView;
    private TextView totalBillTextView;

    private CartManager cartManager;
    private Button proceedToCheckoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        totalBillTextView = findViewById(R.id.totalBillTextView);
        proceedToCheckoutButton = findViewById(R.id.proceedToCheckoutButton);

        cartManager = CartManager.getInstance(); // Get an instance of CartManager

        ArrayList<CartItem> cartItems = cartManager.getCartItems();

        // Set up the RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter cartAdapter = new CartAdapter(this, cartItems);
        cartRecyclerView.setAdapter(cartAdapter);

        double totalBill = calculateTotalBill(cartItems);

        totalBillTextView.setText(String.format("Total Bill: %.2f", totalBill));

        proceedToCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an ArrayList to hold product details
                ArrayList<String> productDetails = new ArrayList<>();

                // Populate the ArrayList with product details
                for (CartItem item : cartItems) {
                    String productName = item.getProduct().getTitle();
                    int quantity = item.getQuantity();
                    productDetails.add(productName + ": " + quantity);
                }

                // Show shipping address input dialog
                Intent intent = new Intent(CartActivity.this, OrderFormActivity.class);
                intent.putExtra("TOTAL_BILL", totalBill);

                // Pass the list of product details as an extra
                intent.putStringArrayListExtra("PRODUCT_DETAILS", productDetails);

                startActivity(intent);
            }
        });



    }

    private double calculateTotalBill(ArrayList<CartItem> cartItems) {
        double totalBill = 0;
        for (CartItem item : cartItems) {
            double price = Double.parseDouble(item.getProduct().getPrice()); // Convert price to double
            totalBill += price * item.getQuantity();
        }
        return totalBill;
    }




}
