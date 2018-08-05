package com.udacity.getfit.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.udacity.getfit.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private Button btnRegister;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnGoogle;
    private Button btnFacebook;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_login);
        checkCurrentAuth();
    }

    private void checkCurrentAuth() {
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser == null){
//            Toast.makeText(this, "User NOT Logged In!!", Toast.LENGTH_SHORT).show();
            initComponents();
            setListeners();
        }else{
            Toast.makeText(this, "User Logged In!!"+currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            Intent getFitHomeActivity = new Intent(LoginActivity.this, GetFitHomeActivity.class);
            startActivity(getFitHomeActivity);
            finish();
        }
    }

    private void initComponents() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.tvError);
    }

    private void setListeners() {
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnFacebook.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                    createNewUser();
                break;

            case R.id.btnLogin:
                    existingUserLogin();
                break;

            case R.id.btnGoogle:
                // Configure Google Sign In
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                break;

            case R.id.btnFacebook:

                break;
        }
    }

    private void createNewUser() {
        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                              existingUserLogin();
                            }else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                                tvError.setVisibility(View.VISIBLE);
                                tvError.setText(task.getException().getMessage());
                                updateUI(null);
                            }
                        }

                    }
                });
    }

    private void existingUserLogin(){
        mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Welcome back. Lets get fit again!", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                }else{
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(LoginActivity.this, "It seems this Email is already in use.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

  /*  private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/
}

