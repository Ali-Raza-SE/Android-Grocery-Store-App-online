package ali.vu.groceryshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class ReturnProduct extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1001;
    private static final int PERMISSION_CODE = 1002;

    private EditText elaborationEditText;
    private ImageView productImageView;
    private Button requestReturnButton;

    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference returnRequestRef;
    private StorageReference productImagesRef;
    private String currentUserID;
    private Uri imageUri;

    private String orderName;
    private String orderStatus;
    private String phoneNumber;
    private String address;
    private String city;
    private String deliveryTimeSlot;
    private String paymentMethod;
    private String totalBill;
    private String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_returen_product);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        returnRequestRef = FirebaseDatabase.getInstance().getReference().child("ReturnRequests");
        productImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImages");

        // Get data from the intent
        orderName = getIntent().getStringExtra("orderName");
        orderStatus = getIntent().getStringExtra("orderStatus");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        address = getIntent().getStringExtra("address");
        city = getIntent().getStringExtra("city");
        deliveryTimeSlot = getIntent().getStringExtra("deliveryTimeSlot");
        paymentMethod = getIntent().getStringExtra("paymentMethod");
        totalBill = getIntent().getStringExtra("totalBill");
        orderID = getIntent().getStringExtra("orderID");

        // Initialize UI elements
        elaborationEditText = findViewById(R.id.elaborationEditText);
        productImageView = findViewById(R.id.productImageView);
        requestReturnButton = findViewById(R.id.requestReturnButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        // Set a click listener for the product image view
        productImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the app has permission to access the gallery
                if (ContextCompat.checkSelfPermission(ReturnProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, open the gallery
                    pickImageFromGallery();
                } else {
                    // Permission not granted, request it
                    ActivityCompat.requestPermissions(ReturnProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                }
            }
        });

        // Set a click listener for the request return button
        requestReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the request to return the product
                sendReturnRequest();
            }
        });
    }

    // Method to pick an image from the gallery
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Method to handle permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open the gallery
                pickImageFromGallery();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to handle image selection from the gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            // Image selected
            imageUri = data.getData();
            productImageView.setImageURI(imageUri);
        }
    }

    // Method to send the return request
    private void sendReturnRequest() {
        // Get the elaboration text from the EditText
        String elaboration = elaborationEditText.getText().toString().trim();

        // Check if the elaboration text and image are provided
        if (TextUtils.isEmpty(elaboration) || imageUri == null) {
            Toast.makeText(this, "Please provide elaboration and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        // Upload the selected image to Firebase Storage
        uploadImageToFirebase(elaboration);
    }

    // Method to upload the selected image to Firebase Storage
    private void uploadImageToFirebase(final String elaboration) {
        // Generate a unique key for the image
        final String returnRequestID = returnRequestRef.push().getKey();

        // Convert the image to a byte array
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageData = baos.toByteArray();

            // Upload the byte array to Firebase Storage
            UploadTask uploadTask = productImagesRef.child(returnRequestID + ".jpg").putBytes(imageData);

            // Handle the upload success or failure
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image uploaded successfully, get the download URL
                        productImagesRef.child(returnRequestID + ".jpg").getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        if (task.isSuccessful()) {
                                            // Get the download URL
                                            String downloadUrl = task.getResult().toString();

                                            // Create a return request object
                                            HashMap<String, Object> returnRequestMap = new HashMap<>();
                                            returnRequestMap.put("elaboration", elaboration);
                                            returnRequestMap.put("imageURL", downloadUrl);
                                            returnRequestMap.put("orderName", orderName);
                                            returnRequestMap.put("orderStatus", orderStatus);
                                            returnRequestMap.put("phoneNumber", phoneNumber);
                                            returnRequestMap.put("address", address);
                                            returnRequestMap.put("city", city);
                                            returnRequestMap.put("deliveryTimeSlot", deliveryTimeSlot);
                                            returnRequestMap.put("paymentMethod", paymentMethod);
                                            returnRequestMap.put("totalBill", totalBill);
                                            returnRequestMap.put("orderID", orderID);

                                            // Save the return request to Firebase Realtime Database
                                            returnRequestRef.child(returnRequestID).setValue(returnRequestMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            progressDialog.dismiss();
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(ReturnProduct.this, "Return request sent successfully.", Toast.LENGTH_SHORT).show();

                                                                // Delete the order from Firebase
                                                                DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");
                                                                ordersRef.child(orderID).removeValue();

                                                               startActivity(new Intent(getApplicationContext(), RequestReceived.class));
                                                               finish();
                                                            } else {
                                                                Toast.makeText(ReturnProduct.this, "Failed to send return request.", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(ReturnProduct.this, "Failed to get image URL.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ReturnProduct.this, "Failed to upload image.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("ReturnProduct", "Error: " + e.getMessage());
        }
    }
}
