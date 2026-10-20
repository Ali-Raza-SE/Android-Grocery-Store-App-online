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

public class ManageCustomersActivity extends AppCompatActivity {

    private RecyclerView recyclerViewToDisplayCustomers;
    private CustomerAdapter customerAdapter;
    private List<Customer> customerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_customers);
        setTitle("Manage Customers");

        FloatingActionButton fabAddCustomer = findViewById(R.id.fabAddCustomer);
        fabAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageCustomersActivity.this, AddCustomerActivity.class));
            }
        });

        recyclerViewToDisplayCustomers = findViewById(R.id.recyclerViewToDisplayCustomers);
        customerList = new ArrayList<>();
        customerAdapter = new CustomerAdapter(this, customerList);
        recyclerViewToDisplayCustomers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewToDisplayCustomers.setAdapter(customerAdapter);

        fetchAndPopulateCustomers();
    }

    private void fetchAndPopulateCustomers() {
        DatabaseReference customersRef = FirebaseDatabase.getInstance().getReference("users");

        customersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerList.clear();
                for (DataSnapshot customerSnapshot : dataSnapshot.getChildren()) {
                    Customer customer = customerSnapshot.getValue(Customer.class);
                    customerList.add(customer);
                }
                customerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ManageCustomersActivity.this, "Failed to fetch customers.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
