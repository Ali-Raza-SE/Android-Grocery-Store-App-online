package ali.vu.groceryshop;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfileFragment extends Fragment {

    private TextView tvAdminName;
    private TextView tvAdminEmail;
    private Button btnEditInProfileFragment;
    private Button logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // Find the TextView elements in your layout
        tvAdminName = rootView.findViewById(R.id.tvUserName);
        tvAdminEmail = rootView.findViewById(R.id.tvUserEmail);
        btnEditInProfileFragment = rootView.findViewById(R.id.btnEditInProfileFragment);
        logout = rootView.findViewById(R.id.btnLogoutInProfileFragment);

        btnEditInProfileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),EditUserProfile.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                // Navigate to the login screen or main screen
                Intent intent = new Intent(getContext(), Login.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(getContext(), "Session End!", Toast.LENGTH_SHORT).show();

            }
        });

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        String userId = currentUser.getUid();

// Attach a ValueEventListener to listen for changes to the user's data
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    String name = userProfile.getName();
                    String email = userProfile.getEmail();

                    tvAdminName.setText("Name: " + name);
                    tvAdminEmail.setText("Email: " + email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors here
            }
        });



        return rootView;
    }


}


