package ali.vu.groceryadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryProductName;
        private TextView categoryProductDescription;
        private TextView categoryProductPrice;
        private ImageView categoryProductImageView;
        private ImageView categoryProductDeleteButton;
        private ImageView categoryProductEditButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryProductName = itemView.findViewById(R.id.categoryProductName);
            categoryProductDescription = itemView.findViewById(R.id.categoryProductDescription);
            categoryProductPrice = itemView.findViewById(R.id.categoryProductPrice);
            categoryProductImageView = itemView.findViewById(R.id.categoryProductImageView);
            categoryProductDeleteButton = itemView.findViewById(R.id.categoryProductDeleteButton);
            categoryProductEditButton = itemView.findViewById(R.id.categoryProductEditButton);

            categoryProductDeleteButton.setOnClickListener(v -> {
                showDeleteConfirmationDialog(getAdapterPosition());
            });

            categoryProductEditButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Product product = productList.get(position);
                    editProduct(product);
                }
            });
        }

        private void showDeleteConfirmationDialog(final int position) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Confirm Deletion");
            alertDialogBuilder.setMessage("Are you sure you want to delete this product?");
            alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                deleteProduct(position);
            });
            alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        private void editProduct(Product product) {
            Intent intent = new Intent(context, UpdateProductActivity.class);
            intent.putExtra("productKey", product.getId());
            context.startActivity(intent);
        }


        private void deleteProduct(int position) {
            Product product = productList.get(position);
            DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Products").child(product.getId());
            productRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context.getApplicationContext(), "Product deleted successfully!", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context.getApplicationContext(), "Failed to delete product.", Toast.LENGTH_SHORT).show();
                    });
        }

        void bind(Product product) {
            categoryProductName.setText(product.getTitle());
            categoryProductDescription.setText(product.getDescription());
            categoryProductPrice.setText("Price: " + product.getPrice());

            Glide.with(itemView.getContext())
                    .load(product.getImageUrl())
                    .placeholder(R.drawable.logo_image)
                    .into(categoryProductImageView);
        }
    }
}