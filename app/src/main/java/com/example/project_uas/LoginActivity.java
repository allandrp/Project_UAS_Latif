package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageGoogle;
    private FirebaseAuth fbAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    EditText editTextEmail, editTextPassword;
    TextView textViewSignUp;
    Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        imageGoogle = findViewById(R.id.img_google);
        editTextEmail = findViewById(R.id.et_email);
        editTextPassword = findViewById(R.id.et_password);
        buttonSignIn = findViewById(R.id.button_sign_in);
        textViewSignUp = findViewById(R.id.Tv_sign_up);
        textViewSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);

        fbAuth = FirebaseAuth.getInstance();

        if(fbAuth.getCurrentUser() != null){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        imageGoogle.setOnClickListener(this);

        // konfigurasi client google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("827736177418-g772mdo3g0lh4v026vbtne81i6toroa3.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }


    @Override
    public void onClick(View view) {
        if(view.getId() == imageGoogle.getId()){
            signIn();
        }

        if(view.getId() == buttonSignIn.getId()){
            fbAuth.signInWithEmailAndPassword(editTextEmail.getText().toString(), editTextPassword.getText().toString()).addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intentHome = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intentHome);
                                finish();
                            }
                        }
                    }
            );

        }

        if(view.getId() == textViewSignUp.getId()){
            Intent intentSignUp = new Intent (this, RegisterActivity.class);
            startActivity(intentSignUp);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.d("TES_GOOGLE_SIGN_IN", e.getMessage());
            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fbAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    Log.d("TES_HASIL", "firebaseAuthWithGoogle: "+user.getEmail());
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                })
                .addOnFailureListener(this, e -> Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}