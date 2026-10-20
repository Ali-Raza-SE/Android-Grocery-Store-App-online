package ali.vu.groceryshop;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
        CategoryViewHolder viewHolder = new CategoryViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Category category = categoryList.get(viewHolder.getAdapterPosition());

                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("category_id", category.getId());
                context.startActivity(intent);
            }
        });
        return viewHolder;
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


        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryImageView = itemView.findViewById(R.id.categoryImageView);
            titleTextView = itemView.findViewById(R.id.categoryNameTextView);
           // descriptionTextView = itemView.findViewById(R.id.categoryDescriptionTextView);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the clicked category
                    Category clickedCategory = categoryList.get(getAdapterPosition());

                    // Create an intent to open the CategoryProductsActivity
                    Intent intent = new Intent(context, ProductListActivity.class);
                    intent.putExtra("category_id", clickedCategory.getId()); // Pass the category ID
                    context.startActivity(intent);
                }
            });


        }

        void bind(Category category) {
            titleTextView.setText(category.getTitle());

            Glide.with(context)
                    .load(category.getImageUrl())
                    .placeholder(R.drawable.logo_image)
                    .into(categoryImageView);
        }
    }
}