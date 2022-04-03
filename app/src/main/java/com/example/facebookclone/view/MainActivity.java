package com.example.facebookclone.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.facebookclone.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();

        //Kullanıcı önceden oturum açtıysa direkt anasayfaya git.
        FirebaseUser user = auth.getCurrentUser();

        if (user != null){
            Intent intent = new Intent(MainActivity.this , AnasayfaActivity.class);
            startActivity(intent);
            finish();
        }



    }

    public void HesapOlustur (View view){

        Intent kayıtIntent = new Intent(MainActivity.this, KayitolActivity.class);
        startActivity(kayıtIntent);


    }

    public void girisYap (View view){
        String email = binding.mainActivityEmailText.getText().toString();
        String password = binding.mainActivityPasswordText.getText().toString();

        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // kullanıcı başarılı Anasayfa ya gidecek.
                Intent intent = new Intent(MainActivity.this, AnasayfaActivity.class);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // kullanıcı hata yaptı
                Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}