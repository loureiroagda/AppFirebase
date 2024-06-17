package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appscomfirebase.activity.CadastroActivity;
import com.example.appscomfirebase.activity.LoginActivity;
import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private FirebaseAuth autentica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }

    public void btnEntrar(View v){startActivities(new Intent[]{new Intent(this, LoginActivity.class)});}

    public void btnCadastrar(View v){startActivities(new Intent[]{new Intent(this, CadastroActivity.class)});}
    public void verificarUsuarioLogado(){

        autentica = ConfiguracaoFirebase.getAutentificacao();
        if(autentica.getCurrentUser() != null){
            abrirTelaPrincipal();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void abrirTelaPrincipal(){startActivities(new Intent[]{new Intent(this, PrincipalActivity.class)});}
}