package com.example.facebookclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.facebookclone.databinding.ActivityKayitolBinding;
import com.example.facebookclone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitolActivity extends AppCompatActivity {
    private ActivityKayitolBinding binding;
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKayitolBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
    }

    public void KayitOl (View view){
        String email = binding.kayitolActivityEmailText.getText().toString();
        String password = binding.kayitolActivityPasswordText.getText().toString();

        if (email.equals("") || password.equals("")){
            Toast.makeText(KayitolActivity.this, "Email ve şifre boş olamaz", Toast.LENGTH_LONG).show();
        }else {

            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Intent anasayfaIntent = new Intent(KayitolActivity.this, AnasayfaActivity.class);
                    startActivity(anasayfaIntent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KayitolActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });

        }



    }

    public void GeriBack (View view){
        Intent backIntent = new Intent(KayitolActivity.this, MainActivity.class);
        startActivity(backIntent);
        finish();

    }
}