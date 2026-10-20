package ali.vu.groceryshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private ArrayList<Order> orders;
    private boolean isPendingFragment;

    public OrderAdapter(Context context, ArrayList<Order> orders, boolean isPendingFragment) {
        this.context = context;
        this.orders = orders;
        this.isPendingFragment = isPendingFragment;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);

        // Update the visibility of layoutWithButtons based on isPendingFragment
        if (isPendingFragment) {
            holder.returnProductButton.setVisibility(View.GONE);
        } else {
            holder.returnProductButton.setVisibility(View.VISIBLE);
        }

        // Add an OnClickListener to the button
        holder.returnProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent to start the ReturnProduct activity
                Intent intent = new Intent(context, ReturnProduct.class);

                // Put the data into the Intent
                intent.putExtra("orderName", order.getName());
                intent.putExtra("orderStatus", order.getOrderStatus());
                intent.putExtra("phoneNumber", order.getNumber());
                intent.putExtra("address", order.getAddress());
                intent.putExtra("city", order.getCity());
                intent.putExtra("deliveryTimeSlot", order.getDeliveryTimeSlot());
                intent.putExtra("paymentMethod", order.getPaymentMethod());
                intent.putExtra("totalBill", order.getTotalBill());
                intent.putExtra("orderID", order.getOrderId());

                // Start the activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNameTextView;
        private TextView orderStatusTextView;
        private TextView phoneNumberTextView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView deliveryTimeSlotTextView;
        private TextView paymentMethodTextView;
        private TextView totalBillTextView;
        private Button returnProductButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            deliveryTimeSlotTextView = itemView.findViewById(R.id.deliveryTimeSlotTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
            totalBillTextView = itemView.findViewById(R.id.totalBillTextView);
            returnProductButton = itemView.findViewById(R.id.returnProductButton);
        }

        public void bind(Order order) {
            // Populate the TextViews with order details
            orderNameTextView.setText("Order Name: " + order.getName());
            orderStatusTextView.setText("Order Status: " + order.getOrderStatus());
            phoneNumberTextView.setText("Phone Number: " + order.getNumber());
            addressTextView.setText("Address: " + order.getAddress());
            cityTextView.setText("City: " + order.getCity());
            deliveryTimeSlotTextView.setText("Delivery Time Slot: " + order.getDeliveryTimeSlot());
            paymentMethodTextView.setText("Payment Method: " + order.getPaymentMethod());
            totalBillTextView.setText("Total Bill: " + order.getTotalBill());
        }
    }
}
