package com.example.appscomfirebase.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.databinding.ActivityDespesasBinding;
import com.example.appscomfirebase.databinding.ActivityReceitaBinding;

public class ReceitaActivity extends AppCompatActivity {

    private ActivityReceitaBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReceitaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}