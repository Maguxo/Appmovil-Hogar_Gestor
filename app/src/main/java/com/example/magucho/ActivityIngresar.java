package com.example.magucho;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class ActivityIngresar extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;

     Button btn_in, btn_circle;
    private TextInputEditText email, password;
    String marca1;
    String marca2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

                mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [END config_signin]
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        btn_in= findViewById(R.id.boton_in);
        btn_circle=  findViewById(R.id.button_circle);
        email=  findViewById(R.id.email_in);
        password= findViewById(R.id.password_in);
        marca1= "ednemesis-006@hotmail.com";
        marca2= "EscorpionS6";

        btn_in.setOnClickListener(v -> ingresa());

        btn_circle.setOnClickListener(v ->
                resultLauncher.launch(new Intent(mGoogleSignInClient.getSignInIntent())));
    }

    public void ingresa() {
       if((marca1.contentEquals(Objects.requireNonNull(email.getText())) && marca2.contentEquals(Objects.requireNonNull(password.getText())))){
                 Intent intent = new Intent(ActivityIngresar.this,ActivityPresentacion.class);
                 startActivity(intent);
        }else{
           Toast.makeText(ActivityIngresar.this,R.string.IN_P,Toast.LENGTH_SHORT).show();
       }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    // [END auth_with_google]

    // [START signin]

    ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent in = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(in);
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.w(TAG, "Google sign in failed", e);
                    }
                }
            }
    );


    // [END signin]
    private void updateUI(FirebaseUser user) {
        FirebaseUser user1= FirebaseAuth.getInstance().getCurrentUser();

        if(user1 != null){
            Intent i= new Intent(ActivityIngresar.this,ActivityPerfil.class);
            startActivity(i);
            finish();
            Toast.makeText(ActivityIngresar.this,R.string.register,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ActivityIngresar.this,R.string.register_NO,Toast.LENGTH_SHORT).show();
        }

    }
}