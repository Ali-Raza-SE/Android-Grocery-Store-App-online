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

public class CompletedOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCompletedOrders);
        orderList = new ArrayList<>();
        orderAdapter = new OrderAdapter(getActivity(), orderList, false);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(orderAdapter);

        // Fetch and populate completed orders
        fetchAndPopulateCompletedOrders();

        return view;
    }


    private void fetchAndPopulateCompletedOrders() {
        // Get a reference to the "orders" node in Firebase Realtime Database
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Query to fetch completed orders
        Query completedOrdersQuery = ordersRef.orderByChild("orderStatus").equalTo("Completed");

        completedOrdersQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Order> completedOrders = new ArrayList<>();
                for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                    Order order = orderSnapshot.getValue(Order.class);
                    completedOrders.add(order);
                }
                // Update your UI with the list of completed orders
                // You can update the RecyclerView or any other UI element here
                orderAdapter.setOrders(completedOrders);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors, if any
            }
        });
    }
}
