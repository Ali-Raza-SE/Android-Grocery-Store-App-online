package ali.vu.groceryshop;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OrdersManager {
    private DatabaseReference ordersRef;

    public OrdersManager() {
        // Initialize Firebase references
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ordersRef = database.getReference("orders");
    }

    public void addOrder(Order order) {
        // Add the order to Firebase
        String orderId = ordersRef.push().getKey(); // Generate a unique key for the order
        ordersRef.child(orderId).setValue(order);
    }

    // Other methods for retrieving and updating orders
}
