package com.example.magucho.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.magucho.ActivityRegisterFirebase;
import com.example.magucho.Modelo.Libro;
import com.example.magucho.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class LibroAdapter extends FirestoreRecyclerAdapter<Libro, LibroAdapter.ViewHolder>{

    private final FirebaseFirestore mFirestore= FirebaseFirestore.getInstance();
    Activity activity;
    FragmentManager fm;

    public LibroAdapter(@NonNull FirestoreRecyclerOptions<Libro> options,Activity activity,FragmentManager fm) {
        super(options);
        this.activity=activity;
        this.fm=fm;
    }

    @Override
    protected void onBindViewHolder(@NonNull LibroAdapter.ViewHolder viewHolder, int position, @NonNull Libro Libro) {
        DocumentSnapshot documentSnapshot= getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
        final String id= documentSnapshot.getId();

        viewHolder.titulo_lb.setText(Libro.getTittle());
        viewHolder.precio_lb.setText(Libro.getCost());
        viewHolder.genero_lb.setText(Libro.getGender());
        viewHolder.año_lb.setText(Libro.getYear());
         String photoLib= Libro.getPhoto();
        try{
            if(!photoLib.equals("")){
                Picasso.with(activity.getApplicationContext())
                        .load(photoLib)
                        .resize(150,150)
                        .into(viewHolder.photo_lb);
            }
        }catch (Exception ex){
            Log.v("Excption","e: " + ex);
        }
        viewHolder.btn_edi.setOnClickListener(v -> {
            Intent i= new Intent(activity, ActivityRegisterFirebase.class);
            i.putExtra("id_book",id);
            activity.startActivity(i);
        });

        viewHolder.btn_delete.setOnClickListener(v -> deleteLibro(id));
    }

    private  void deleteLibro(String id){
        mFirestore.collection("book").document(id).delete()
                .addOnSuccessListener(unused ->
          Toast.makeText(activity,R.string.ok_delete,Toast.LENGTH_SHORT)
                  .show()).addOnFailureListener(e ->
          Toast.makeText(activity,R.string.not_delete, Toast.LENGTH_SHORT).show());
    }
    @NonNull
    @Override
    public LibroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista, parent,false);
        return new ViewHolder(v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titulo_lb, precio_lb, genero_lb,año_lb;
        ImageView btn_delete, btn_edi, photo_lb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo_lb= itemView.findViewById(R.id.title_list);
            precio_lb= itemView.findViewById(R.id.precio_list);
            genero_lb= itemView.findViewById(R.id.genero_list);
            año_lb= itemView.findViewById(R.id.año_list);
            btn_edi= itemView.findViewById(R.id.editcard);
            btn_delete= itemView.findViewById(R.id.deletecard);
            photo_lb= itemView.findViewById(R.id.photocard);
        }
    }
}
 