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
import com.google.gson.Gson;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private Context context;
    private DatabaseReference databaseReference;

    public CategoryAdapter(List<Category> categoryList, Context context, DatabaseReference databaseReference) {
        this.categoryList = categoryList;
        this.context = context;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryImageView;
        private TextView titleTextView;
        private TextView descriptionTextView;
        private ImageView editCategoryButton;
        private ImageView deleteButton;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.categoryImageView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            editCategoryButton = itemView.findViewById(R.id.categoryEditButton);
            deleteButton = itemView.findViewById(R.id.deleteCategoryButton);
        }

        void bind(Category category) {
            titleTextView.setText(category.getTitle());
            descriptionTextView.setText(category.getDescription());

            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.logo_image)
                    .into(categoryImageView);

            // Edit button click listener
            editCategoryButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, UpdateCategoryActivity.class);
                String categoryJson = new Gson().toJson(category);
                intent.putExtra("category", categoryJson);
                context.startActivity(intent);
            });

            // Delete button click listener
            deleteButton.setOnClickListener(v -> {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle("Confirm Deletion");
                alertDialogBuilder.setMessage("Are you sure you want to delete this category?");
                alertDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    DatabaseReference categoryRef = databaseReference.child(category.getId());
                    categoryRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context.getApplicationContext(), "Category deleted successfully!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context.getApplicationContext(), "Failed to delete category.", Toast.LENGTH_SHORT).show();
                                }
                            });
                });
                alertDialogBuilder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            });
        }
    }
}
