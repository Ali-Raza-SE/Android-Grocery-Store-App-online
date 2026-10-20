package ali.vu.groceryadmin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    FragmentManager fragmentManager = getSupportFragmentManager();
    private TextView adminNameTextView;
    private TextView adminNameTextView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // Find the NavigationView in your layout
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Find the header view of the NavigationView
        View headerView = navigationView.getHeaderView(0);

        // Access the TextViews in the header view
        adminNameTextView = headerView.findViewById(R.id.adminNameTextView);
        adminNameTextView2 = headerView.findViewById(R.id.adminNameTextView2);


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

                    adminNameTextView.setText("Name: " + name);
                    adminNameTextView2.setText("Email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });


        // Set a listener for the navigation item clicks
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                int id = menuItem.getItemId();

                Intent intent;
                switch (id) {
                    case R.id.menu_home:
                        Toast.makeText(Dashboard.this, "home", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                        finish();
                        break;

                    case R.id.menu_my_profile:
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, new AdminProfileFragment())
                                .commit();
                        break;

                    case R.id.menu_offers:
                        intent = new Intent(Dashboard.this, OffersActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_categories:
                        intent = new Intent(Dashboard.this, ManageCategories.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_new_products:

                    case R.id.menu_popular_products:
                        intent = new Intent(Dashboard.this, NewProducts.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_search:
                        intent = new Intent(Dashboard.this, SearchProductActivity.class);
                        startActivity(intent);
                        break;

                    case R.id.menu_manage_orders:
                        intent = new Intent(Dashboard.this, ManageOrders.class);
                        startActivity(intent);
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


        CardView btn_manage_categories = findViewById(R.id.cardview_manage_categories);
        CardView btnManageCategoryItems = findViewById(R.id.cardviewManageCategoryItems);
        CardView newProducts = findViewById(R.id.cardviewManageNewProducts);
        CardView cardViewLogout = findViewById(R.id.cardview_logout);
        CardView cardReturnProductRequest = findViewById(R.id.cardview_Return_Request);
        CardView cardView_offers = findViewById(R.id.cardView_offers);
        CardView cardviewManageOrders = findViewById(R.id.cardviewManageOrders);
        CardView btnManageCustomers = findViewById(R.id.cardviewManageUsers);

        btn_manage_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ManageCategories.class);
                startActivity(intent);
            }
        });

        btnManageCategoryItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ManageCategoryItems.class);
                startActivity(intent);
            }
        });

        btnManageCustomers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ManageCustomersActivity.class);
                startActivity(intent);
            }
        });

        cardView_offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, OffersActivity.class);
                startActivity(intent);
            }
        });

        cardReturnProductRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ReturnProductRequest.class);
                startActivity(intent);
            }
        });

        newProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, NewProducts.class);
                startActivity(intent);
            }
        });

        cardviewManageOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, ManageOrders.class);
                startActivity(intent);
            }
        });



        cardViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
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