package com.example.magucho;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView llamada_o;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    llamada_o= findViewById(R.id.llamada);

    llamada_o.setOnClickListener(v -> {
        mensaje();
    });
    }

    private void mensaje() {

        final AlertDialog.Builder dialog= new AlertDialog.Builder(this);
        dialog.setMessage(R.string.bienvenida_in);
        dialog.setCancelable(false);
        dialog.setPositiveButton(R.string.thank, (dialog1, which) -> {
            Intent in= new Intent(MainActivity.this,ActivityIngresar.class);
            startActivity(in);
            Toast.makeText(MainActivity.this,R.string.option  ,Toast.LENGTH_SHORT).show();
        });
        dialog.show();
    }
}