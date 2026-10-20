package ali.vu.groceryshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MyCartFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView emptyCartText;
    private String userID;
    private ArrayList<Order> pendingOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // Get the current user's ID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userID = currentUser.getUid();

            // Retrieve pending orders for the specified user from Firebase
            retrievePendingOrdersFromFirebase();

            // Set up ViewPager and attach it to the TabLayout
            MyCartPagerAdapter pagerAdapter = new MyCartPagerAdapter(getChildFragmentManager());
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }

        return view;
    }

    // Retrieve pending orders for the specified user from Firebase
    private void retrievePendingOrdersFromFirebase() {
        pendingOrders = new ArrayList<>();

        // Get a reference to the "orders" node in Firebase Realtime Database
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Query to fetch pending orders for the specified user
        Query pendingOrdersQuery = ordersRef.orderByChild("userID").equalTo(userID);

        pendingOrdersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    if (order != null && "Pending".equals(order.getOrderStatus())) {
                        pendingOrders.add(order);
                    }
                }

                // Update your UI with the list of pending orders
                //updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }

//    private void updateUI() {
//        // Check if there are no orders, and show a message if the cart is empty
//        if (pendingOrders.isEmpty()) {
//            emptyCartText.setVisibility(View.VISIBLE);
//        } else {
//            emptyCartText.setVisibility(View.GONE);
//        }
//    }
}
