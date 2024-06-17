package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autentica = ConfiguracaoFirebase.getAutentificacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
    }

    public void addDespesas(View v){

        startActivities(new Intent[]{new Intent(this, DespesasActivity.class)});
    }

    public void addReceita(View v){

        startActivities(new Intent[]{new Intent(this, ReceitaActivity.class)});
    }

    public void logout(View v){
        autentica.signOut();
        startActivities(new Intent[]{new Intent(this, MainActivity.class)});
    }
}