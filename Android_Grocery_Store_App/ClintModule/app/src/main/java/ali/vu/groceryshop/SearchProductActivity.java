package ali.vu.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SearchProductActivity extends AppCompatActivity {

    private EditText edtSearchProduct;
    private Button btnSearchProducts;
    private ImageView searchedProductImage1;
    private ImageView searchedProductImage2;
    private ImageView searchedProductImage3;
    private TextView searchedProductNamed;
    private TextView searchedProductDes;
    private TextView textProductNotFound;
    private LinearLayout linearLayout;

    private DatabaseReference databaseReference;
    private Product product; // Declare the product variable here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        // Initialize views
        edtSearchProduct = findViewById(R.id.edtSearchProduct);
        btnSearchProducts = findViewById(R.id.btnSearchProducts);
        searchedProductImage1 = findViewById(R.id.searchedProductImage1);
        searchedProductImage2 = findViewById(R.id.searchedProductImage2);
        searchedProductImage3 = findViewById(R.id.searchedProductImage3);
        searchedProductNamed = findViewById(R.id.searchedProductNamed);
        searchedProductDes = findViewById(R.id.searchedProductPrice); // Corrected ID
        textProductNotFound = findViewById(R.id.textProductNotFound);
        linearLayout = findViewById(R.id.linearLayoutContainingSearchedData);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Products");

        btnSearchProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchQuery = edtSearchProduct.getText().toString().trim();
                searchProduct(searchQuery);
            }
        });

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product != null) {
                    Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                    // Pass relevant attributes as extras
                    intent.putExtra("productId", product.getId());
                    intent.putExtra("productName", product.getTitle());
                    intent.putExtra("productPrice", product.getPrice());
                    intent.putExtra("imageUrl", product.getImageUrl());
                    startActivity(intent); // Start the ProductDetailsActivity
                }
            }
        });
    }

    private void searchProduct(final String query) {
        textProductNotFound.setVisibility(View.GONE);

        databaseReference.orderByChild("title").equalTo(query).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        linearLayout.setVisibility(View.VISIBLE);
                        product = snapshot.getValue(Product.class); // Assign the product here
                        displayProductDetails(product);
                    }
                } else {
                    // Product not found
                    textProductNotFound.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors here
            }
        });
    }

    private void displayProductDetails(Product product) {
        searchedProductNamed.setText(product.getTitle());
        searchedProductDes.setText("Price: " +product.getPrice());

        // Load product images using a library like Picasso
        Picasso.get().load(product.getImageUrl()).into(searchedProductImage1);
        Picasso.get().load(product.getImageUrl()).into(searchedProductImage2);
        Picasso.get().load(product.getImageUrl()).into(searchedProductImage3);
    }
}
