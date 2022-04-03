package com.example.facebookclone.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.facebookclone.databinding.ActivityPaylasBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class PaylasActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;
    private StorageReference storageReference;


    Uri imageData;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> perrmisionLauncher;
    private ActivityPaylasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaylasBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        registerLauncher();

        firebaseStorage =FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = firebaseStorage.getReference();

    }

    public void PaylasClic(View view){
        //Paylaş
        if (imageData != null){
            if (binding.paylasActivityEditTextYorum.getText() != null){
                //rastgele id atamak için
                UUID uuid = UUID.randomUUID();
                String imageName = "images/" + uuid + ".jpg";

                //Upload
                storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //Başarılı
                        StorageReference newReference = firebaseStorage.getReference(imageName);
                        newReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                //resimUri
                                String downloadUrl = uri.toString();
                                //kullanıcı adi
                                FirebaseUser user = auth.getCurrentUser();
                                String email = user.getEmail();
                                //Yorum
                                String yorum = binding.paylasActivityEditTextYorum.getText().toString();

                                //HasMap
                                HashMap<String, Object> postData = new HashMap<>();
                                postData.put("useremail", email);
                                postData.put("downloadUrl", downloadUrl);
                                postData.put("yorum", yorum);
                                postData.put("Date", FieldValue.serverTimestamp());

                                firebaseFirestore.collection("Gönderiler").add(postData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                        Intent anasayfaIntent = new Intent(PaylasActivity.this, AnasayfaActivity.class);
                                        anasayfaIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(anasayfaIntent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Başarısız

                    }
                });


            }else {
            showMesage("Yorum yazınız");
            }
        }else {
            showMesage("Göresl yükleyiniz");
        }



    }

    public void ResimSecClic(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view, "Resim için galeriye gitmek için izin veriyor musun ? ", Snackbar.LENGTH_INDEFINITE).setAction("İzin Ver", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //İzin istenecek
                        perrmisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                }).show();
            }else{
                //İzin istenecek
                perrmisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else{
            //Galeriye git
            Intent galeriIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(galeriIntent);


        }


    }

    public void registerLauncher(){

    activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == RESULT_OK){
                Intent intentFromResult = result.getData();

                if (intentFromResult != null){
                    imageData = intentFromResult.getData();
                    binding.paylasActivityImageViewPost.setImageURI(imageData);

                }
            }


        }
    });

    perrmisionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result){
                //izin verildi
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGallery);

            }else {
                //İzin vermedi
                Toast.makeText(PaylasActivity.this, "Fotoğraf yüklemek için izin lazım", Toast.LENGTH_LONG).show();

            }
        }
    });

    }

    public void showMesage(String messaj){
        Toast.makeText(getApplicationContext(), messaj, Toast.LENGTH_LONG).show();
    }
}