package com.example.magucho;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ActivityPresentacion extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private FloatingActionButton actionButton,actionButton2,actionButton3;
    private Animation fabOpen, fabclose, rotateForward,rotateBackward;
    boolean isOpen= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentacion);

        drawerLayout= findViewById(R.id.drawer_layout);
        toolbar= findViewById(R.id.toolbar);
        navigationView= findViewById(R.id.view_on);
        actionButton= findViewById(R.id.fab);
        actionButton2= findViewById(R.id.fab2);
        actionButton3= findViewById(R.id.fab3);
        fabOpen= AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabclose= AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotateForward= AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward= AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        setSupportActionBar(toolbar);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        actionButton.setOnClickListener(v -> animationFab());
        actionButton2.setOnClickListener(v -> {
            animationFab();
            Intent intent= new Intent(ActivityPresentacion.this,ActivityRFirebase.class);
            startActivity(intent);
        });
        actionButton3.setOnClickListener(v -> {
            animationFab();
            Intent in= new Intent(ActivityPresentacion.this, ActivityPresentacion.class);
            startActivity(in);
        });
    }

    private void animationFab(){
        if(isOpen){
            actionButton.startAnimation(rotateForward);
            actionButton2.startAnimation(fabclose);
            actionButton3.startAnimation(fabclose);
            actionButton2.setClickable(false);
            actionButton3.setClickable(false);
            isOpen=false;
        }else{
            actionButton.startAnimation(rotateBackward);
            actionButton2.startAnimation(fabOpen);
            actionButton3.startAnimation(fabOpen);
            actionButton2.setClickable(true);
            actionButton3.setClickable(true);
            isOpen=true;

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        selectItemNav(item);
        return true;
    }

    private void selectItemNav(MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_presentacion:
                fm = getSupportFragmentManager();
                ft= fm.beginTransaction();
                ft.replace(R.id.content, new ActivityFragmentAbaut());
                ft.commit();
                break;

            case R.id.nav_nota:
                fm = getSupportFragmentManager();
                ft= fm.beginTransaction();
                ft.replace(R.id.content, new ActivityFragmentNota());
                ft.commit();
                break;

            case R.id.nav_abaut:
                fm = getSupportFragmentManager();
                ft= fm.beginTransaction();
                ft.replace(R.id.content, new ActivityFragmentDeveloper());
                ft.commit();
                break;
        }
    }
}