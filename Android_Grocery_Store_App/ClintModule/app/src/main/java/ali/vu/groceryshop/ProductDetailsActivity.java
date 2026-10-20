package ali.vu.groceryshop;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView productImageView;
    private TextView productNameTextView;
    private TextView productPriceTextView;
    private Button decreaseQuantityButton;
    private TextView quantityTextView;
    private Button increaseQuantityButton;
    private Button adToCart;
    private List<CartItem> cartItems;
    private TextView totalPriceTextView;
    private String productPrice;

    private int selectedQuantity = 1;
    private Product selectedProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Retrieve selected product from intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String productId = intent.getStringExtra("productId");
        productPrice = intent.getStringExtra("productPrice");
        String imageUrl = intent.getStringExtra("imageUrl");

        cartItems = new ArrayList<>();

        productImageView = findViewById(R.id.productImageView);
        productNameTextView = findViewById(R.id.productNameTextView);
        productPriceTextView = findViewById(R.id.productPriceTextView);
        adToCart = findViewById(R.id.btnAddToCart);
        decreaseQuantityButton = findViewById(R.id.decreaseQuantityButton);
        quantityTextView = findViewById(R.id.quantityTextView);
        increaseQuantityButton = findViewById(R.id.increaseQuantityButton);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        // Display product details
        productNameTextView.setText("Name: " + productName);
        productPriceTextView.setText("Price: " + productPrice);

        selectedProduct = new Product(productId, productName, imageUrl,productPrice);

        Glide.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.logo_image)
                .into(productImageView);

        // Handle quantity buttons
        decreaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedQuantity > 1) {
                    selectedQuantity--;
                    updateQuantityAndTotalPrice();
                }
            }
        });

        increaseQuantityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedQuantity++;
                updateQuantityAndTotalPrice();
            }
        });

        // Handle "Add to Cart" button click
        adToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add the selected product to the cart
            }
        });

        // Initialize the total price
        updateQuantityAndTotalPrice();

        adToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartItem cartItem = new CartItem(selectedProduct, selectedQuantity);
                CartManager.getInstance().addToCart(cartItem);

                // Assuming you want to navigate to the cart activity after adding to cart
                Intent cartIntent = new Intent(ProductDetailsActivity.this, CartActivity.class);
                startActivity(cartIntent);
            }
        });

    }

    private void updateQuantityAndTotalPrice() {
        quantityTextView.setText(String.valueOf(selectedQuantity));
        double total = Double.parseDouble(productPrice) * selectedQuantity;
        totalPriceTextView.setText(String.format("Total Price: %.2f", total));
    }


}
