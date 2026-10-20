package ali.vu.groceryadmin;

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


public class AdminProfileFragment extends Fragment {

    private TextView tvAdminName;
    private TextView tvAdminEmail;
    private Button btnEditInProfileFragment;
    private Button logout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        // Find the TextView elements in your layout
        tvAdminName = rootView.findViewById(R.id.tvAdminName);
        tvAdminEmail = rootView.findViewById(R.id.tvAdminEmail);
        btnEditInProfileFragment = rootView.findViewById(R.id.btnEditInProfileFragment);
        logout = rootView.findViewById(R.id.btnLogoutInProfileFragment);

        btnEditInProfileFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),EditAdminProfile.class));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(),Login.class));
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


