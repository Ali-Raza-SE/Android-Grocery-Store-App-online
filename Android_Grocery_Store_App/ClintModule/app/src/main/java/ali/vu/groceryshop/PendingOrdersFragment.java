package ali.vu.groceryshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> pendingOrders;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pending_orders, container, false);

        recyclerView = view.findViewById(R.id.pendingOrdersRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Retrieve the currently authenticated user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            pendingOrders = new ArrayList<>();
            orderAdapter = new OrderAdapter(getActivity(), pendingOrders, true);
            recyclerView.setAdapter(orderAdapter);

            // Retrieve all orders for the current user from Firebase
            retrieveOrdersFromFirebase(currentUser.getUid());
        }

        return view;
    }

    private void retrieveOrdersFromFirebase(String userID) {
        // Get a reference to the "orders" node in Firebase Realtime Database
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Query to fetch all orders for the specified user
        ordersRef.orderByChild("userID").equalTo(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        pendingOrders.clear();

                        for (DataSnapshot orderSnapshot : dataSnapshot.getChildren()) {
                            Order order = orderSnapshot.getValue(Order.class);
                            if ("Pending".equals(order.getOrderStatus())) {
                                pendingOrders.add(order);
                            }
                        }

                        // Notify the adapter of changes
                        orderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors, if any
                    }
                });
    }
}
