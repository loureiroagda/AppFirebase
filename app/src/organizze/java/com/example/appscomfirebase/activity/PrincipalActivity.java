package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityMainBinding;
import com.example.appscomfirebase.databinding.ActivityPrincipalBinding;
import com.google.firebase.auth.FirebaseAuth;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private TextView saldo, pessoa;

    private FirebaseAuth autentica = ConfiguracaoFirebase.getAutentificacao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        saldo = findViewById(R.id.p_valor);
        pessoa = findViewById(R.id.p_titulo);
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