package com.example.facebookclone.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.facebookclone.Adapter.GönderiAdapter;
import com.example.facebookclone.R;
import com.example.facebookclone.databinding.ActivityAnasayfaBinding;
import com.example.facebookclone.model.Gönderi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AnasayfaActivity extends AppCompatActivity {

    private ActivityAnasayfaBinding binding;

    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;

    ArrayList<Gönderi> gönderiArrayList;

    GönderiAdapter gönderiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnasayfaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        getData();

        gönderiArrayList = new ArrayList<>();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        gönderiAdapter = new GönderiAdapter(gönderiArrayList);
        binding.recyclerView.setAdapter(gönderiAdapter);



    }

    public void getData(){

        firebaseFirestore.collection("Gönderiler").orderBy("date", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null){
                    Toast.makeText(AnasayfaActivity.this , error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }

                if(value != null){

                    for (DocumentSnapshot snapshot: value.getDocuments()){

                        Map<String, Object> data = snapshot.getData();

                        String userEmail = (String) data.get("useremail");
                        String downloadUrl = (String) data.get("downloadUrl");
                        String yorum = (String) data.get("yorum");

                        Gönderi gönderi = new Gönderi(userEmail, yorum, downloadUrl);
                        gönderiArrayList.add(gönderi);
                    }

                    //recycler view yeni veri geldi haber ver. göstersin
                    gönderiAdapter.notifyDataSetChanged();

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_post){
            //Paylaşma sayfasına geçecek
            Intent uploadIntent = new Intent(AnasayfaActivity.this, PaylasActivity.class );
            startActivity(uploadIntent);

        }else if (item.getItemId() == R.id.signout){
            //Çıkış işlemi
            auth.signOut();

            Intent mainIntent = new Intent(AnasayfaActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}