package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


import com.example.appscomfirebase.config.ConfiguracaoFirebase;

import com.example.appscomfirebase.databinding.ActivityReceitaBinding;
import com.example.appscomfirebase.helper.Base64Custom;
import com.example.appscomfirebase.helper.DataCustom;
import com.example.appscomfirebase.model.Movimentacao;
import com.example.appscomfirebase.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitaActivity extends AppCompatActivity {
    private ActivityReceitaBinding binding;
    private Movimentacao movimentacao;
    private DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDataBase();
    private FirebaseAuth auth = ConfiguracaoFirebase.getAutentificacao();
    private double receitaTotal,receita,receitaAtual;

    private String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
    private DatabaseReference usuarioRef = reference.child("usuarios").child(idUsuario);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceitaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtData.setText(DataCustom.dataAtual());
        recuperaReceitatotal();
    }

    public void salvarReceita(View v){

        if(validarDados()){

            receita = Double.parseDouble(binding.editValor.getText().toString());
            //Pegando os dados da View
            movimentacao = new Movimentacao();
            movimentacao.setValor(receita);
            movimentacao.setCategoria(binding.txtCategoria.getText().toString());
            movimentacao.setData(binding.txtData.getText().toString());
            movimentacao.setDescricao(binding.txtDescricao.getText().toString());
            movimentacao.setTipo("r");

            receitaAtual = receita + receitaTotal;

            atualizaReceita();
            //Salvando dados em um nó especifico
            String data = binding.txtData.getText().toString();
            movimentacao.salvar(data);
        }
    }

    public Boolean validarDados(){

        if(!binding.editValor.getText().toString().isEmpty()){
            if (!binding.txtData.getText().toString().isEmpty()){
                if (!binding.txtCategoria.getText().toString().isEmpty()){
                    if (!binding.txtDescricao.getText().toString().isEmpty()){
                        return true;
                    }else{
                        Toast.makeText(getApplicationContext(), "Descrição da despesa não informada!", Toast.LENGTH_LONG).show();
                        return false;
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "Categoria da despesa não informada!", Toast.LENGTH_LONG).show();
                    return false;
                }

            }else {
                Toast.makeText(getApplicationContext(), "Data não informado!", Toast.LENGTH_LONG).show();
                return false;
            }

        }else {
            Toast.makeText(getApplicationContext(), "Valor não informado!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void recuperaReceitatotal(){

        //Recuperando a despesa total do bando de dados
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Usuario usuario = snapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void atualizaReceita(){

        usuarioRef.child("receitaTotal").setValue(receitaAtual);
    }
}