package ali.vu.groceryadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PndingOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPendingOrders);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getActivity(), orderList, true);



        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderAdapter);

        // Fetch and populate pending orders
        fetchAndPopulatePendingOrders();

        return view;
    }


    private void fetchAndPopulatePendingOrders() {
        // Get a reference to the "orders" node in Firebase Realtime Database
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Query to fetch pending orders
        Query pendingOrdersQuery = ordersRef.orderByChild("orderStatus").equalTo("Pending");

        pendingOrdersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> pendingOrders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    pendingOrders.add(order);
                }
                // Update your UI with the list of pending orders
                // You can update the RecyclerView or any other UI element here
                orderAdapter.setOrders(pendingOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }
}
