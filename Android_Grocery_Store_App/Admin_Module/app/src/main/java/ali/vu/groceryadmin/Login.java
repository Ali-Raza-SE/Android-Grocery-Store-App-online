package ali.vu.groceryadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnLogin;
    private TextView tvForgetPassword;
    private Button btnSignup;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the title bar and action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        // Make the status bar transparent
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgetPassword = findViewById(R.id.tvForgetPassword);
        btnSignup = findViewById(R.id.dontHaveAccountSignup);

        tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPasswordRecoveryDialog();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Signup.class));
            }
        });
    }

    private void openPasswordRecoveryDialog() {
        Dialog passwordRecoveryDialog = new Dialog(this);
        passwordRecoveryDialog.setContentView(R.layout.dialog_forget_password);

        // Check if the IDs match the IDs in dialog_forget_password.xml
        EditText emailEditText = passwordRecoveryDialog.findViewById(R.id.resetPasswordEmail);
        Button resetPasswordButton = passwordRecoveryDialog.findViewById(R.id.btnRecoverPassword);

        resetPasswordButton.setOnClickListener(v -> {
            String userEmail = emailEditText.getText().toString();
            if(userEmail.isEmpty()){
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
            }
            else{
                String toastMessage = "Password reset email sent to " + userEmail;
                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                passwordRecoveryDialog.dismiss();
            }
        });

        passwordRecoveryDialog.show();
    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString();

        // Check if fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return; // Stop further execution
        }

        // Authenticate the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User authentication successful
                            Toast.makeText(Login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Dashboard.class));
                            finish();
                        } else {
                            // Login failed, display an error message
                            Toast.makeText(Login.this, "Login failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
        {
            startActivity(new Intent(getApplicationContext(),Dashboard.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        showExitConfirmationDialog();
    }

    private void showExitConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to close the app?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the app
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


}
