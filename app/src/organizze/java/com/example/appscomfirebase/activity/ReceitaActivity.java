package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.databinding.ActivityDespesasBinding;
import com.example.appscomfirebase.databinding.ActivityReceitaBinding;
import com.example.appscomfirebase.model.Movimentacao;

public class ReceitaActivity extends AppCompatActivity {
    private ActivityReceitaBinding binding;
    private Movimentacao movimentacao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceitaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
}