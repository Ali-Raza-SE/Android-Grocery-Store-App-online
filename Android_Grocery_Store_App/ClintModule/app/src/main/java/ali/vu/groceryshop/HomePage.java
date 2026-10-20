package ali.vu.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePage extends AppCompatActivity {


    private DatabaseReference databaseReference;
    private RecyclerView recyclerViewCategories;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    //for product view
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private DatabaseReference productsRef;

    private TextView userNameTextView;
    private TextView userNameTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Find the NavigationView in your layout
        NavigationView navigationView = findViewById(R.id.navigation_view);


        // Find the header view of the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Access the TextViews in the header view
        userNameTextView = headerView.findViewById(R.id.userNameTextView);
        userNameTextView2 = headerView.findViewById(R.id.userNameTextView2);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = currentUser.getUid();

// Attach a ValueEventListener to listen for changes to the user's data
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();

                    userNameTextView.setText("Name: " + name);
                    userNameTextView2.setText("Email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });


        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Categories");

        recyclerViewCategories = findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, this, FirebaseDatabase.getInstance().getReference("Categories"));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategories.setLayoutManager(linearLayoutManager);
        recyclerViewCategories.setAdapter(categoryAdapter);


        // Attach ValueEventListener to retrieve categories
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                categoryList.clear();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    Category category = categorySnapshot.getValue(Category.class);
                    categoryList.add(category);
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });



        //showing Products in recycler view main Activity
        productsRef = FirebaseDatabase.getInstance().getReference("Products");

        recyclerViewProducts = findViewById(R.id.recyclerViewProductsMainActivity);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this);

        recyclerViewProducts.setAdapter(productAdapter);

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
                // Handle error
            }
        });

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(getApplicationContext(), ProductDetailsActivity.class);
                // Pass relevant attributes as extras
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getTitle());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("imageUrl", product.getImageUrl());


                startActivity(intent);
            }
        });





        // Set a listener for the navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();

                switch (id) {
                    case R.id.menu_home:
                        Toast.makeText(HomePage.this, "home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomePage.class));
                        finish();
                        break;

                    case R.id.menu_my_profile:

                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, new UserProfileFragment())
                                .commit();
                        break;

                    case R.id.menu_offers:
                        startActivity(new Intent(getApplicationContext(), OffersActivity.class));
                        break;

                    case R.id.menu_my_cart:
                         fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, new MyCartFragment())
                                .commit();
                        break;

//                        case R.id.menu_return_product:
//                         startActivity(new Intent(getApplicationContext(), ReturnProductRequest.class));
//                        break;

                    case R.id.menu_search:
                        startActivity(new Intent(getApplicationContext(), SearchProductActivity.class));
                        break;

                    case R.id.menu_popular_products:

                    case R.id.menu_new_products:
                        startActivity(new Intent(getApplicationContext(), NewProducts.class));
                        break;

                    case R.id.menu_logout:
                        logout();
                        break;
                }

                DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

    }


    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        // Navigate to the login screen or main screen
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Session End!", Toast.LENGTH_SHORT).show();
    }

    public void openDrawer(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.openDrawer(GravityCompat.START);
    }




}