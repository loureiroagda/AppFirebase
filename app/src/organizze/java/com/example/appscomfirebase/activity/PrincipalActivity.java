package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
    private DatabaseReference usuario;
    private ValueEventListener eventListenerUsuario;

    TextView saldo, pessoa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
        usuario = reference.child("usuarios").child(id);

        eventListenerUsuario = usuario.addValueEventListener(new ValueEventListener() {
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
                saldo.setText(getResources().getString(R.string.p_valor, resultadoFormato));
                pessoa.setText(getString(R.string.p_titulo,usuario.getNome()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        saldo = findViewById(R.id.p_valor);
        pessoa = findViewById(R.id.p_titulo);

        recuperarResumo();
    }

    @Override
    protected void onStop() {
        super.onStop();

        usuario.removeEventListener(eventListenerUsuario);
    }
}