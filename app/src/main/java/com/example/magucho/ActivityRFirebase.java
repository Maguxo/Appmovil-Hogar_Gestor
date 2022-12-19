package com.example.magucho;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magucho.Adapter.LibroAdapter;
import com.example.magucho.Modelo.Libro;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ActivityRFirebase extends AppCompatActivity {

    private ImageView regreso;
    private Button cerrar, firebase_in;
     RecyclerView mRecycleV;
    LibroAdapter libroAdapter;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;
    protected Dialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rfirebase);

        cerrar= findViewById(R.id.boton_close);
        firebase_in= findViewById(R.id.boton_firebase);
        regreso= findViewById(R.id.back);
        mRecycleV= findViewById(R.id.recicleViewSingle);
        mFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        cerrar.setOnClickListener(v -> {
            cierre();
        });
        regreso.setOnClickListener(v -> {
            Intent in= new Intent(ActivityRFirebase.this,ActivityPresentacion.class );
            startActivity(in);
        });
        firebase_in.setOnClickListener(v -> {
            Intent intent= new Intent(ActivityRFirebase.this,ActivityRegisterFirebase.class);
            startActivity(intent);
        });

       setUpRecyclerView();
    }

    private void cierre() {
        dialog= null;
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
       builder.setMessage(R.string.salir_out);
       builder.setCancelable(false);
       builder.setPositiveButton(R.string.yes_bo, (dialog, which) -> {
           Intent i= new Intent(ActivityRFirebase.this,MainActivity.class);
           startActivity(i);
           finish();
           Toast.makeText(getApplication(),"OK", Toast.LENGTH_SHORT).show();
       });
       builder.setNegativeButton("NO", (dialog, which) -> {
           builder.create();
           Toast.makeText(getApplication(), R.string.no_bo,Toast.LENGTH_SHORT).show();
       });
       builder.show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        mRecycleV.setLayoutManager(new LinearLayoutManager(this));
        query = mFirestore.collection("book");

        FirestoreRecyclerOptions<Libro> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Libro>().setQuery(query, Libro.class).build();

        libroAdapter = new LibroAdapter(firestoreRecyclerOptions, ActivityRFirebase.this, getSupportFragmentManager());
        libroAdapter.notifyDataSetChanged();
        mRecycleV.setAdapter(libroAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        libroAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        libroAdapter.stopListening();
    }
}