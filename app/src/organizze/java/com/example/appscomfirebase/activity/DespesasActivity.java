package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.databinding.ActivityDespesasBinding;
import com.example.appscomfirebase.helper.DataCustom;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

public class DespesasActivity extends AppCompatActivity {
    private ActivityDespesasBinding binding;

    private TextInputEditText cData, cCategoria, cDescricao, cValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        binding.txtData.setText(DataCustom.dataAtual());
    }
}