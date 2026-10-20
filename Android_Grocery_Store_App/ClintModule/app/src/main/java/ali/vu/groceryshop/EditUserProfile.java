package ali.vu.groceryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUserProfile extends AppCompatActivity {

    private TextView edtEditUserName;
    private TextView edtEditUserEmail;
    private Button btnSaveEdits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);


        edtEditUserName = findViewById(R.id.tvEditUserName);
        edtEditUserEmail = findViewById(R.id.tvEditUserEmail);
        btnSaveEdits = findViewById(R.id.btnSaveEdits);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = currentUser.getUid();



        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if(userProfile != null){
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();

                    edtEditUserName.setText(name);
                    edtEditUserEmail.setText(email);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btnSaveEdits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtEditUserName.getText().toString().trim();
                String email = edtEditUserEmail.getText().toString().trim();

                if (name != null && email != null) {
                    // Get the current user's ID
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = currentUser.getUid();

                    // Get a reference to the "users" node in the database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

                    // Create a User object with the updated data
                    User updatedUser = new User(userId, name, email);

                    // Use the DatabaseReference to update the user's data
                    databaseReference.child(userId).setValue(updatedUser)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(EditUserProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();


                                    edtEditUserName.setText("Name: " + name);
                                    edtEditUserEmail.setText("Email: " + email);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(EditUserProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(EditUserProfile.this, "Fill in all the fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}