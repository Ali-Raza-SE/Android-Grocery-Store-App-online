package ali.vu.groceryadmin;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private List<Customer> customerList;
    private Context context;

    public CustomerAdapter(Context context, List<Customer> customerList) {
        this.context = context;
        this.customerList = customerList;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.bind(customer);
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    class CustomerViewHolder extends RecyclerView.ViewHolder {
        private TextView customerName;
        private TextView customerEmail;
        private ImageView customerDeleteButton;
        private ImageView customerEditButton;

        CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerEmail = itemView.findViewById(R.id.customerEmail);
            customerDeleteButton = itemView.findViewById(R.id.customerDeleteButton);
            customerEditButton = itemView.findViewById(R.id.customerEditButton);

            customerDeleteButton.setOnClickListener(v -> {
                showDeleteConfirmationDialog(getAdapterPosition());
            });

            customerEditButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Customer customer = customerList.get(position);
                    editCustomer(customer);
                }
            });
        }




        private void showDeleteConfirmationDialog(final int position) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Confirm Deletion");
            alertDialogBuilder.setMessage("Are you sure you want to delete this customer?");
            alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                deleteCustomer(position);
            });
            alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        private void editCustomer(Customer customer) {
            Intent intent = new Intent(context, EditCustomerActivity.class);
            intent.putExtra("customerId", customer.getId());
            context.startActivity(intent);
        }


        private void deleteCustomer(int position) {
            Customer customer = customerList.get(position);
            DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("users").child(customer.getId());
            customerRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context.getApplicationContext(), "Customer deleted successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context.getApplicationContext(), "Failed to delete customer.", Toast.LENGTH_SHORT).show();
                    });
        }

        void bind(Customer customer) {
            customerName.setText(customer.getName());
            customerEmail.setText(customer.getEmail());

        }
    }
}
