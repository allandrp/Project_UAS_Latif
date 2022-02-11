package com.example.project_uas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    Button buttonSignUp;
    FirebaseDatabase fbDatabase;
    DatabaseReference dbReference;
    FirebaseAuth fbAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fbDatabase = FirebaseDatabase.getInstance(getString(R.string.INSTANCE_FIREBASE));
        dbReference = fbDatabase.getReference();
        fbAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.et_email_register);
        editTextPassword = findViewById(R.id.et_password_register);
        editTextConfirmPassword = findViewById(R.id.et_confirm_password);
        buttonSignUp = findViewById(R.id.button_sign_up);
        buttonSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == buttonSignUp.getId()){
            if(editTextPassword.getText().toString().equals(editTextConfirmPassword.getText().toString())){
                fbAuth.createUserWithEmailAndPassword(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                        }else{
                            Toast.makeText(RegisterActivity.this, "Terjadi Error Saat Registrasi atau email sudah terdaftar", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }else{
                editTextConfirmPassword.setError("Harus sama dengan password");
            }
        }
    }
}