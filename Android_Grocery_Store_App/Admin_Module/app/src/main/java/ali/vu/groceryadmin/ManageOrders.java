package ali.vu.groceryadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);


        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ordersRef = database.getReference("orders");


        // Create the adapter
        OrderPagerAdapter adapter = new OrderPagerAdapter(getSupportFragmentManager());

        // Add your fragments
        adapter.addFragment(new PndingOrdersFragment(), "Pending Orders");
        adapter.addFragment(new CompletedOrdersFragment(), "Completed Orders");

        // Set the adapter to your ViewPager
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        // Set up your TabLayout with the ViewPager
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
}
