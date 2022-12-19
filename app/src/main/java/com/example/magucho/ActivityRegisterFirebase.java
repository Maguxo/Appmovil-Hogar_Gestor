package com.example.magucho;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityRegisterFirebase extends AppCompatActivity {

    private ImageView regreso_tool;
    private CircleImageView photolib;
    private EditText titulo, costo,genero, año;
    private  Button Add_photo, delete_photo,Add_fire;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;

    StorageReference storageReference;
    String storage_path = "book/*";

    private static final int COD_SEL_IMAGE= 300;

    private Uri image_uri;
    String  photo = "photo";
    String idd;
    ProgressDialog progressDialog;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_firebase);

    regreso_tool= findViewById(R.id.back);
    regreso_tool.setOnClickListener(v -> {
        Intent intent= new Intent(ActivityRegisterFirebase.this, ActivityRFirebase.class);
        startActivity(intent);
    });

    progressDialog= new ProgressDialog(this);
     String id= getIntent().getStringExtra("id_book");
    mfirestore= FirebaseFirestore.getInstance();
    mAuth= FirebaseAuth.getInstance();
    storageReference= FirebaseStorage.getInstance().getReference();

    photolib= findViewById(R.id.imagen_fire);
    Add_photo= findViewById(R.id.btn_photo);
    delete_photo= findViewById(R.id.btn_delete);
    Add_fire= findViewById(R.id.btn_add);
    titulo= findViewById(R.id.title_register);
    costo= findViewById(R.id.precio_register);
    genero=findViewById(R.id.genero_register);
    año=findViewById(R.id.año_register);

    Add_photo.setOnClickListener(v ->
            resultLauncher.launch(new Intent(MediaStore.ACTION_PICK_IMAGES)));
    delete_photo.setOnClickListener(v -> {
        HashMap<String, Object> map= new HashMap<>();
        map.put(photo,"");
        mfirestore.collection("book").document(idd).update(map);
        Toast.makeText(ActivityRegisterFirebase.this,R.string.delete_photo, Toast.LENGTH_SHORT).show();
    });

    if(id == null || id == ""){
        Add_fire.setOnClickListener(v -> {
            String title=  titulo.getText().toString().trim();
            String cash=  costo.getText().toString().trim();
            String generd= genero.getText().toString().trim();
            String year=  año.getText().toString().trim();
     if(title.isEmpty() || cash.isEmpty() || generd.isEmpty() || year.isEmpty()){
         Toast.makeText(getApplicationContext(),R.string.enter_d, Toast.LENGTH_SHORT).show();
     }else{
         postBook(title,cash,generd,year);
     }
        });
    }else{
        idd = id;
        Add_fire.setText("Update");
        getBoock(id);
        Add_fire.setOnClickListener(v -> {
            String title=  titulo.getText().toString().trim();
            String cash=  costo.getText().toString().trim();
            String generd= genero.getText().toString().trim();
            String year=  año.getText().toString().trim();
            if(title.isEmpty() && cash.isEmpty() && generd.isEmpty() && year.isEmpty()){
                Toast.makeText(getApplicationContext(),R.string.enter_d, Toast.LENGTH_SHORT).show();
            }else{
                updateBook(title,cash,generd,year,id);
            }
        });
    }
    }

    private void updateBook(String title, String cash, String generd, String year, String id) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tittle",title);
        map.put("cost",cash);
        map.put("gender",generd);
        map.put("year",year);
        mfirestore.collection("book").document(id).update(map).addOnSuccessListener(unused -> {
            Toast.makeText(getApplicationContext(),R.string.actualiza,Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(),R.string.actualiza_no,Toast.LENGTH_SHORT).show());
    }

    private void postBook(String title, String cash, String generd, String year) {
         //String idUser= mAuth.getCurrentUser().getUid();
        DocumentReference id= mfirestore.collection("book").document();
        Map<String ,Object> map= new HashMap<>();
        //map.put("id",idUser);
        map.put("id_book",id);
        map.put("tittle",title);
        map.put("cost",cash);
        map.put("gender",generd);
        map.put("year",year);
        mfirestore.collection("book").document(id.getId()).set(map).addOnSuccessListener(unused -> {
            Toast.makeText(getApplicationContext(),R.string.to_register,Toast.LENGTH_SHORT).show();
            finish();
        }).addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(),R.string.error_register,Toast.LENGTH_SHORT).show());
    }

    ActivityResultLauncher<Intent> resultLauncher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent in = new Intent(Intent.ACTION_PICK);
                        try {
                            in.setType("book");
                        }catch (Exception e){
                            Toast.makeText(ActivityRegisterFirebase.this,"no se pudo",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode == RESULT_OK || requestCode == COD_SEL_IMAGE){
                image_uri = data.getData();
                subirPhotho(image_uri);
        Toast.makeText(ActivityRegisterFirebase.this,R.string.up_pload,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ActivityRegisterFirebase.this,R.string.up_error,Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("SuspiciousIndentation")
    private void subirPhotho(Uri image_uri) {
    progressDialog.setMessage("Actualizar foto");
    progressDialog.show();

    String rute_storage_photo= storage_path + "" + photolib + "" + mAuth.getUid() + "" + idd;
    StorageReference reference = storageReference.child(rute_storage_photo);
    reference.putFile(image_uri).addOnSuccessListener(taskSnapshot -> {
        Task<Uri>uriTask= taskSnapshot.getStorage().getDownloadUrl();
        while (!uriTask.isSuccessful());
            if(uriTask.isSuccessful()){
                uriTask.addOnSuccessListener(uri -> {
                    String download_uri= uri.toString();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put(photo, download_uri);
                    mfirestore.collection("book").document(idd).update(map);
                Toast.makeText(ActivityRegisterFirebase.this,R.string.load_ok,Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                });
            }
    }).addOnFailureListener(e ->
            Toast.makeText(ActivityRegisterFirebase.this,R.string.load_error,Toast.LENGTH_SHORT).show());
    }

    private void getBoock(String id){
        mfirestore.collection("book").document(id).get().addOnSuccessListener(documentSnapshot -> {
            String num_lib= documentSnapshot.getString("tittle");
            String p_lib= documentSnapshot.getString("cost");
            String g_lib= documentSnapshot.getString("gender");
            String A_lib= documentSnapshot.getString("year");
            String photo_lib= documentSnapshot.getString("photo");

            titulo.setText(num_lib);
            costo.setText(p_lib);
            genero.setText(g_lib);
            año.setText(A_lib);
            try{
                if(!photo_lib.equals("")){
                    Toast toast= Toast.makeText(getApplicationContext(),R.string.load_photo,Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,200);
                    toast.show();
                    Picasso.with(ActivityRegisterFirebase.this)
                            .load(photo_lib)
                            .resize(150,150)
                            .into(photolib);
                }
            }catch(Exception e){
                Log.v("Error","e: "+ e);
            }
        }).addOnFailureListener(e ->
                Toast.makeText(ActivityRegisterFirebase.this,R.string.not_date,Toast.LENGTH_SHORT).show());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}