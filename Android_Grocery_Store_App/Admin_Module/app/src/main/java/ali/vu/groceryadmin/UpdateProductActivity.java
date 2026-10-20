package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UpdateProductActivity extends AppCompatActivity {

    private EditText edtUpdateProductTitle, edtUpdateProductDescription, edtUpdateProductPrice;
    private ImageView UpdateProductImage;

    private DatabaseReference productRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        edtUpdateProductTitle = findViewById(R.id.edtUpdateProductTitle);
        edtUpdateProductDescription = findViewById(R.id.edtUpdateProductDescription);
        edtUpdateProductPrice = findViewById(R.id.edtUpdateProductPrice);
        UpdateProductImage = findViewById(R.id.UpdateProductImage);

        String productKey = getIntent().getStringExtra("productKey");

        // Get a reference to the specific product in the database

        if (productKey != null) {
            productRef = FirebaseDatabase.getInstance().getReference("Products").child(productKey);
            loadProductForEditing(productRef);
        } else {
            // Handle the case where productKey is null (e.g., show an error message)
        }


        // Load the existing product details for editing
        loadProductForEditing(productRef);

        Button btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnUpdateProduct.setOnClickListener(v -> updateProduct());
    }

    private void loadProductForEditing(DatabaseReference productRef) {
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        edtUpdateProductTitle.setText(product.getTitle());
                        edtUpdateProductDescription.setText(product.getDescription());
                        edtUpdateProductPrice.setText(product.getPrice());

                        // Load the product image using Glide and display it in UpdateProductImage ImageView
                        Glide.with(getApplicationContext())
                                .load(product.getImageUrl())
                                .placeholder(R.drawable.logo_image)
                                .into(UpdateProductImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }


    private void updateProduct() {
        // Retrieve the updated details from EditText fields
        String updatedTitle = edtUpdateProductTitle.getText().toString();
        String updatedDescription = edtUpdateProductDescription.getText().toString();
        String updatedPrice = edtUpdateProductPrice.getText().toString();

        // Update the product details in the database
        productRef.child("title").setValue(updatedTitle);
        productRef.child("description").setValue(updatedDescription);
        productRef.child("price").setValue(updatedPrice);

        // Display a success message or handle any errors
        // ...

        finish();
    }
}