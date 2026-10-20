package ali.vu.groceryadmin;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class AddNewCategory extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText edtCategoryTitle, editTextDescription;
    private ImageView imageViewCategory;
    private Uri selectedImageUri;

    private String productID;

    ProgressBar bar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_category);

        edtCategoryTitle = findViewById(R.id.edtNewCategoryName);
        editTextDescription = findViewById(R.id.edtNewCategoryDes);
        imageViewCategory = findViewById(R.id.selectedNewCategoryImageView);

        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnAddCategory = findViewById(R.id.btnAddCategory);

        bar = findViewById(R.id.progressBar);





        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                bar.setVisibility(View.VISIBLE);
                addCategoryToFirebase();
            }
        });





    }

    private void updateCategoryInFirebase(Category updatedCategory) {
        DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories").child(updatedCategory.getId());
        categoriesRef.setValue(updatedCategory).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Category updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to update category.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static String makeCategoryID(String input) {
        // Step 1: Replace white spaces with underscores
        String step1 = input.replace(" ", "_");

        // Step 2: Capitalize each word
        String[] words = step1.split("_");
        StringBuilder finalResult = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            String capitalizedWord = word.substring(0, 1).toUpperCase(Locale.getDefault()) + word.substring(1).toLowerCase();
            finalResult.append(capitalizedWord);
            if (i < words.length - 1) {
                finalResult.append("_");
            }
        }
        return finalResult.toString();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    private void addCategoryToFirebase() {
        String title = edtCategoryTitle.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        productID = edtCategoryTitle.getText().toString();
        String finalCategoryID =  makeCategoryID(productID);

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || selectedImageUri == null) {
            Toast.makeText(this, "Please enter category details and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Upload the image to Firebase Storage
        String categoryId = FirebaseDatabase.getInstance().getReference("Categories").push().getKey();
        assert categoryId != null;
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images").child(categoryId);
        storageReference.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
            // Get the download URL of the uploaded image
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();


                // Save the category data to Firebase Realtime Database
                DatabaseReference categoriesRef = FirebaseDatabase.getInstance().getReference("Categories").child(finalCategoryID);
                Category category = new Category(finalCategoryID, title, description, imageUrl);
                categoriesRef.setValue(category).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Category added successfully!", Toast.LENGTH_SHORT).show();
                        // Finish the activity and go back to the AdminDashboardActivity
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to add category to Firebase.", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to upload image to Firebase.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imageViewCategory.setImageURI(selectedImageUri);
        }
    }
}
