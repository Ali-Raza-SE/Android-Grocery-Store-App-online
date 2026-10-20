package ali.vu.groceryadmin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class AddCategoryItem extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;
    private EditText editProductName, editProductDescription, editProductPrice;
    private AutoCompleteTextView autoCompleteCategory;
    private ImageView addProductImage;

    private DatabaseReference categoriesRef; // Firebase reference to categories

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category_item);
        setTitle("Add Product");

        editProductName = findViewById(R.id.edtAddProduct);
        editProductDescription = findViewById(R.id.edtAddProductDescription);
        editProductPrice = findViewById(R.id.edtAddProductPrice);
        autoCompleteCategory = findViewById(R.id.auto_complete_txt);
        addProductImage = findViewById(R.id.addProductImage);

        Button btnAddProduct = findViewById(R.id.btnAddProduct);
        Button btnSelectAddProductImage = findViewById(R.id.btnSelectAddProductImage);

        btnSelectAddProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        categoriesRef = FirebaseDatabase.getInstance().getReference("Categories"); // Reference to categories in Firebase

        // Fetch categories from Firebase and populate AutoCompleteTextView
        fetchAndPopulateCategories();

        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = editProductName.getText().toString();
                String productDescription = editProductDescription.getText().toString();
                String productPrice = editProductPrice.getText().toString();
                String selectedCategory = autoCompleteCategory.getText().toString();

                if (productName.isEmpty() || productDescription.isEmpty() || productPrice.isEmpty() || selectedCategory.isEmpty()) {
                    Toast.makeText(AddCategoryItem.this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference("Products"); // Reference to "Products" node

                String productId = productsRef.push().getKey();
                if (productId != null) {
                    StorageReference storageRef = FirebaseStorage.getInstance().getReference("product_images");
                    StorageReference imageRef = storageRef.child(productId);

                    // Upload the selected image to Firebase Storage
                    BitmapDrawable drawable = (BitmapDrawable) addProductImage.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] imageData = baos.toByteArray();

                    imageRef.putBytes(imageData)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get the download URL of the uploaded image
                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUri) {
                                            String imageUrl = downloadUri.toString();

                                            // Create a Product object with all the data
                                            Product product = new Product(productId, productName, productDescription, productPrice, selectedCategory, imageUrl);

                                            // Add the product to the "Products" node in the Firebase database
                                            productsRef.child(productId).setValue(product)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Toast.makeText(AddCategoryItem.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                                                            finish();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(AddCategoryItem.this, "Failed to add product.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddCategoryItem.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    private void fetchAndPopulateCategories() {
        categoriesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> categories = new ArrayList<>();
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    String categoryName = categorySnapshot.child("title").getValue(String.class);
                    categories.add(categoryName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCategoryItem.this,
                        android.R.layout.simple_dropdown_item_1line, categories);
                autoCompleteCategory.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddCategoryItem.this, "Failed to fetch categories.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedProductImageUri = data.getData();
            addProductImage.setImageURI(selectedProductImageUri);
        }
    }


}
