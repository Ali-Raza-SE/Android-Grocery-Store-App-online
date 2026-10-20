package ali.vu.groceryadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private Context context;
    private Button btnMarkAsComplete;
    private Button btnMarkAsCancel;

    private boolean isPendingFragment;



    public OrderAdapter(Context context, List<Order> orderList, boolean isPendingFragment) {
        this.context = context;
        this.orderList = orderList;
        this.isPendingFragment = isPendingFragment;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use the updated item_order layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.bind(order);

        // Update the visibility of layoutWithButtons based on isPendingFragment
        if (isPendingFragment) {
            holder.layoutWithButtons.setVisibility(View.VISIBLE);
        } else {
            holder.layoutWithButtons.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Method to update the list of orders
    public void setOrders(List<Order> orders) {
        this.orderList = orders;
        notifyDataSetChanged(); // Notify the adapter of changes
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNameTextView;
        private TextView orderStatusTextView;
        private TextView totalBillTextView;
        private TextView phoneNumberTextView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView deliveryTimeSlotTextView;
        private TextView paymentMethodTextView;
        private LinearLayout layoutWithButtons;



        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            totalBillTextView = itemView.findViewById(R.id.totalBillTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            deliveryTimeSlotTextView = itemView.findViewById(R.id.deliveryTimeSlotTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);

            btnMarkAsComplete = itemView.findViewById(R.id.btnMarkAsComplete);
            btnMarkAsCancel = itemView.findViewById(R.id.btnMarkAsCancel);
            layoutWithButtons = itemView.findViewById(R.id.layoutWithButtons);



            btnMarkAsComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle marking the order as complete
                    markOrderAsComplete(orderList.get(getAdapterPosition()));
                    Toast.makeText(context.getApplicationContext(), "Order Completed", Toast.LENGTH_SHORT).show();
                }
            });

            btnMarkAsCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle marking the order as canceled
                    markOrderAsCanceled(orderList.get(getAdapterPosition()));
                    Toast.makeText(context.getApplicationContext(), "Order Cancel", Toast.LENGTH_SHORT).show();

                }
            });
        }

        void bind(Order order) {
            orderNameTextView.setText("Order Person Name: " + order.getName());
            orderStatusTextView.setText("Order Status: " + order.getOrderStatus());
            phoneNumberTextView.setText("Phone Number: " + order.getNumber());
            addressTextView.setText("Address: " + order.getAddress());
            cityTextView.setText("City: " + order.getCity());
            deliveryTimeSlotTextView.setText("Delivery Time Slot: " + order.getDeliveryTimeSlot());
            paymentMethodTextView.setText("Payment Method: " + order.getPaymentMethod());
            totalBillTextView.setText("Total Bill: " + order.getTotalBill());
        }
    }

    private void markOrderAsComplete(Order order) {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = order.getOrderId();
        ordersRef.child(orderId).child("orderStatus").setValue("Completed");

    }

    private void markOrderAsCanceled(Order order) {

        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");
        String orderId = order.getOrderId(); // Assuming you have a method to get the order ID
        ordersRef.child(orderId).removeValue();

        // Notify any listeners or update UI as needed
    }


}
