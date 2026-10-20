package ali.vu.groceryadmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class ReturnRequestAdapter extends RecyclerView.Adapter<ReturnRequestAdapter.ReturnRequestViewHolder> {

    private Context context;
    private List<ReturnRequest> returnRequests;
    private DatabaseReference returnRequestRef;

    public ReturnRequestAdapter(Context context, List<ReturnRequest> returnRequests) {
        this.context = context;
        this.returnRequests = returnRequests;
        returnRequestRef = FirebaseDatabase.getInstance().getReference().child("ReturnRequests");
    }

    @NonNull
    @Override
    public ReturnRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.return_request_item, parent, false);
        return new ReturnRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReturnRequestViewHolder holder, int position) {
        ReturnRequest returnRequest = returnRequests.get(position);
        holder.bind(returnRequest);
    }

    @Override
    public int getItemCount() {
        return returnRequests.size();
    }

    class ReturnRequestViewHolder extends RecyclerView.ViewHolder {
        private TextView orderNameTextView;
        private TextView orderStatusTextView;
        private TextView phoneNumberTextView;
        private TextView addressTextView;
        private TextView cityTextView;
        private TextView deliveryTimeSlotTextView;
        private TextView paymentMethodTextView;
        private TextView totalBillTextView;
        private Button resendOrderButton;

        public ReturnRequestViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNameTextView = itemView.findViewById(R.id.orderNameTextView);
            orderStatusTextView = itemView.findViewById(R.id.orderStatusTextView);
            phoneNumberTextView = itemView.findViewById(R.id.phoneNumberTextView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            deliveryTimeSlotTextView = itemView.findViewById(R.id.deliveryTimeSlotTextView);
            paymentMethodTextView = itemView.findViewById(R.id.paymentMethodTextView);
            totalBillTextView = itemView.findViewById(R.id.totalBillTextView);
            resendOrderButton = itemView.findViewById(R.id.resendOrderButton);
        }

        public void bind(final ReturnRequest returnRequest) {
            orderNameTextView.setText("Order Name: " + returnRequest.getOrderName());
            orderStatusTextView.setText("Order Status: " + returnRequest.getOrderStatus());
            phoneNumberTextView.setText("Phone Number: " + returnRequest.getPhoneNumber());
            addressTextView.setText("Address: " + returnRequest.getAddress());
            cityTextView.setText("City: " + returnRequest.getCity());
            deliveryTimeSlotTextView.setText("Delivery Time Slot: " + returnRequest.getDeliveryTimeSlot());
            paymentMethodTextView.setText("Payment Method: " + returnRequest.getPaymentMethod());
            totalBillTextView.setText("Total Bill: " + returnRequest.getTotalBill());

            // Handle the "Resend Order" button click
            resendOrderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Delete the return request from Firebase
                    returnRequestRef.child(returnRequest.getReturnRequestID()).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Remove the item from the RecyclerView
                                        int position = getAdapterPosition();
                                        if (position != RecyclerView.NO_POSITION) {
                                            returnRequests.remove(position);
                                            notifyItemRemoved(position);
                                            notifyItemRangeChanged(position, getItemCount());
                                            Toast.makeText(context, "Return request deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "Failed to delete return request", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }
    }
}
