package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityPrincipalBinding;
import com.example.appscomfirebase.helper.Base64Custom;
import com.example.appscomfirebase.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private Double despesaT = 0.0, receitaT = 0.0, resumo = 0.0;
    private FirebaseAuth auth = ConfiguracaoFirebase.getAutentificacao();
    private DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDataBase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recuperarResumo();
    }

    public void addDespesas(View v){

        startActivities(new Intent[]{new Intent(this, DespesasActivity.class)});
    }

    public void addReceita(View v){

        startActivities(new Intent[]{new Intent(this, ReceitaActivity.class)});
    }

    public void logout(View v){
        auth.signOut();
        startActivities(new Intent[]{new Intent(this, MainActivity.class)});
    }

    private void recuperarResumo(){

        String email = auth.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64(email);
        DatabaseReference usuario = reference.child("usuarios").child(id);

        usuario.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Usuario usuario = snapshot.getValue(Usuario.class);

                //Pegando os dados
                despesaT = usuario.getDespesaTotal();
                receitaT = usuario.getReceitaTotal();

                //Fazendo as contas necessárias
                resumo = receitaT - despesaT;

                //Formatando os dados
                DecimalFormat decimalFormat = new DecimalFormat("#.00");
                String resultadoFormato = decimalFormat.format(resumo);

                //Setando os dados
                binding.pValor.setText(getResources().getString(R.string.p_valor, resultadoFormato));
                binding.pTitulo.setText(getString(R.string.p_titulo,usuario.getNome()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}