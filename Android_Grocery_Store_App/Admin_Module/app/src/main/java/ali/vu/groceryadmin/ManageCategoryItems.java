package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ManageCategoryItems extends AppCompatActivity {

    private RecyclerView recyclerViewToDisplayProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category_items);
        setTitle("Manage Products");

        FloatingActionButton fabAddCategoryProducts = findViewById(R.id.fabAddCategoryProducts);
        fabAddCategoryProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddCategoryItem.class);
                startActivity(intent);
            }
        });

        recyclerViewToDisplayProducts = findViewById(R.id.recyclerViewToDisplayProducts);
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(this, productList); // Pass the context here
        recyclerViewToDisplayProducts.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewToDisplayProducts.setAdapter(productAdapter);

        fetchAndPopulateProducts();
    }

    private void fetchAndPopulateProducts() {
        DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products");

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product product = productSnapshot.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageCategoryItems.this, "Failed to fetch products.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
