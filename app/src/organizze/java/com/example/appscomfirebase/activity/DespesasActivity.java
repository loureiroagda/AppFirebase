package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.databinding.ActivityDespesasBinding;
import com.example.appscomfirebase.databinding.ActivityMainBinding;
import com.example.appscomfirebase.helper.DataCustom;
import com.example.appscomfirebase.model.Movimentacao;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class DespesasActivity extends AppCompatActivity {
    private ActivityDespesasBinding binding;

    private Movimentacao movimentacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDespesasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.txtData.setText(DataCustom.dataAtual());
    }

    public void salvarDespesa(View v){

        if(validarDados()){
            //Pegando os dados da View
            movimentacao = new Movimentacao();
            movimentacao.setValor(Double.parseDouble(binding.editValor.getText().toString()));
            movimentacao.setCategoria(binding.txtCategoria.getText().toString());
            movimentacao.setData(binding.txtData.getText().toString());
            movimentacao.setDescricao(binding.txtDescricao.getText().toString());
            movimentacao.setTipo("d");

            //Salvando dados em um nó especifico
            String data = binding.txtData.getText().toString();
            movimentacao.salvar(data);


        }else {

        }
    }

    public Boolean validarDados(){



        return true;
    }
}