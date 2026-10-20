package ali.vu.groceryadmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ReturnProductRequest extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference returnRequestRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_product_request);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentUserId = currentUser.getUid();

        returnRequestRef = FirebaseDatabase.getInstance().getReference().child("ReturnRequests");

        recyclerView = findViewById(R.id.returnRequestsRecyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Query to retrieve return requests
        Query query = returnRequestRef.orderByChild("orderID");

        FirebaseRecyclerOptions<ReturnRequest> options = new FirebaseRecyclerOptions.Builder<ReturnRequest>()
                .setQuery(query, ReturnRequest.class)
                .build();

        final FirebaseRecyclerAdapter<ReturnRequest, ReturnRequestViewHolder> adapter =
                new FirebaseRecyclerAdapter<ReturnRequest, ReturnRequestViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ReturnRequestViewHolder holder, final int position, @NonNull ReturnRequest model) {
                        holder.setDetails(model);

                        // Handle Resend Order button click
                        holder.resendOrderButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the selected return request ID
                                String selectedReturnRequestID = getRef(position).getKey();

                                // Delete the selected return request
                                returnRequestRef.child(selectedReturnRequestID).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(ReturnProductRequest.this, "Return request processed.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(ReturnProductRequest.this, "Failed to process return request.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ReturnRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.return_request_item, parent, false);
                        return new ReturnRequestViewHolder(view);
                    }
                };


        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class ReturnRequestViewHolder extends RecyclerView.ViewHolder {

        TextView orderNameTextView, orderStatusTextView, phoneNumberTextView, addressTextView,
                cityTextView, deliveryTimeSlotTextView, paymentMethodTextView, totalBillTextView, tvIssueInProduct;
        ImageView productImageView;
        Button resendOrderButton;

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
            productImageView = itemView.findViewById(R.id.productImageView);
            resendOrderButton = itemView.findViewById(R.id.resendOrderButton);
            tvIssueInProduct = itemView.findViewById(R.id.tvIssueInProduct);
        }

        public void setDetails(ReturnRequest returnRequest) {
            orderNameTextView.setText("Order Name: " + returnRequest.getOrderName());
            orderStatusTextView.setText("Order Status: " + returnRequest.getOrderStatus());
            phoneNumberTextView.setText("Phone Number: " + returnRequest.getPhoneNumber());
            addressTextView.setText("Address: " + returnRequest.getAddress());
            cityTextView.setText("City: " + returnRequest.getCity());
            deliveryTimeSlotTextView.setText("Delivery Time Slot: " + returnRequest.getDeliveryTimeSlot());
            paymentMethodTextView.setText("Payment Method: " + returnRequest.getPaymentMethod());
            tvIssueInProduct.setText("User Feedback: " + returnRequest.getElaboration());

            // Load and display the product image using Picasso
            Picasso.get().load(returnRequest.getImageURL()).into(productImageView);
        }
    }
}
