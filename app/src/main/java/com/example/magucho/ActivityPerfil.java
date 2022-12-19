package com.example.magucho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityPerfil extends AppCompatActivity {

    CircleImageView ima;
    TextView txname, texemail;
    Button btn_salir;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ima= findViewById(R.id.imagfoto);
        txname= findViewById(R.id.idname);
        texemail= findViewById(R.id.idcorreo);
        btn_salir= findViewById(R.id.bottom_out);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            String name= user.getDisplayName();
            String email= user.getEmail();
            Uri imadf= user.getPhotoUrl();
            txname.setText("usuario:\n"+ name);
            texemail.setText("Correo:\n"+ email);
            Picasso.with(ActivityPerfil.this)
                    .load(user.getPhotoUrl())
                    .resize(150,150)
                    .into(ima);
        }else{
            getApplicationContext();
        }

        btn_salir.setOnClickListener(v -> salga());
    }

    private void salga() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Toast.makeText(ActivityPerfil.this,R.string.salir,Toast.LENGTH_SHORT).show();
        Intent i= new Intent(ActivityPerfil.this,ActivityIngresar.class);
        startActivity(i);
    }
}