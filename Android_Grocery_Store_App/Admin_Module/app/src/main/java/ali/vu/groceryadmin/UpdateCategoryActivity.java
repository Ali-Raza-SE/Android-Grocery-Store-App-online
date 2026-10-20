package ali.vu.groceryadmin;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class UpdateCategoryActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private Uri updatedImageUri;
    private Category categoryToUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);
        setTitle("Update Category");

        EditText edtUpdateCategoryTitle = findViewById(R.id.edtUpdateCategoryTitle);
        EditText editTextUpdateDescription = findViewById(R.id.editUpdateCategoryDescription);
        ImageView imageViewUpdateCategory = findViewById(R.id.imageViewUpdateCategory);

        Button btnUpdateImage = findViewById(R.id.btnUpdateCategoryImage);
        Button btnUpdateCategory = findViewById(R.id.btnUpdateCategory);




        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        String categoryJson = getIntent().getStringExtra("category");
        Gson gson = new Gson();
        categoryToUpdate = gson.fromJson(categoryJson, Category.class);

        if (categoryToUpdate != null) {
            edtUpdateCategoryTitle.setText(categoryToUpdate.getTitle());
            editTextUpdateDescription.setText(categoryToUpdate.getDescription());
            // Load and display the existing image using Glide
            Glide.with(this)
                    .load(categoryToUpdate.getImageUrl())
                    .into(imageViewUpdateCategory);
        }

        btnUpdateCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String updatedTitle = edtUpdateCategoryTitle.getText().toString();
                String updatedDescription = editTextUpdateDescription.getText().toString();


                // Create an updated Category object
                Category updatedCategory = new Category(categoryToUpdate.getId(), updatedTitle, updatedDescription, categoryToUpdate.getImageUrl());

                // Update the category in Firebase
                updateCategoryInFirebase(updatedCategory);
            }
        });
    }

    private void updateCategoryInFirebase(Category updatedCategory) {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories");

        Map<String, Object> updates = new HashMap<>();
        updates.put(updatedCategory.getId() + "/title", updatedCategory.getTitle());
        updates.put(updatedCategory.getId() + "/description", updatedCategory.getDescription());

        // Update the image URL if a new image was selected
        if (updatedImageUri != null) {
            updates.put(updatedCategory.getId() + "/imageUrl", updatedImageUri.toString());
        }

        categoriesRef.updateChildren(updates)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed to update category.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            updatedImageUri = data.getData();

            // Display the selected image in the ImageView
            ImageView imageViewUpdateCategory = findViewById(R.id.imageViewUpdateCategory);
            imageViewUpdateCategory.setImageURI(updatedImageUri);
            uploadImageToFirebaseStorage();
        }
    }

    private void uploadImageToFirebaseStorage() {
        if (updatedImageUri != null) {
            String categoryIdToUpdate = categoryToUpdate.getId();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(categoryIdToUpdate);

            storageReference.putFile(updatedImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the updated image URL
                        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            String updatedImageUrl = uri.toString();

                            // Update the image URL in the category object
                            categoryToUpdate.setImageUrl(updatedImageUrl);

                            // Update the category in Firebase Realtime Database
                            updateCategoryInFirebase(categoryToUpdate);
                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(), "Failed to upload image to Firebase.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
