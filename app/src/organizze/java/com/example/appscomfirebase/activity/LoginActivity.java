package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityCadastroBinding;
import com.example.appscomfirebase.databinding.ActivityLoginBinding;
import com.example.appscomfirebase.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private Usuario usuario;
    private FirebaseAuth autentica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {

            String txtE = binding.emailLogin.getText().toString();
            String txtS = binding.senhaLogin.getText().toString();
            if(!txtE.isEmpty() || !txtS.isEmpty()){

                usuario = new Usuario();
                usuario.setEmail(txtE);
                usuario.setSenha(txtS);
                validarLogin();
            }else {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos corretamente", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void validarLogin(){

        autentica = ConfiguracaoFirebase.getAutentificacao();
        autentica.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {

            if(task.isSuccessful()){
                abrirTelaPrincipal();
            }else {
                String execao = "";
                try {
                    throw task.getException();
                }catch (FirebaseAuthInvalidUserException e){
                    execao = "Usuário não está cadastrado";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    execao = "Email ou senha não correspondem";
                }catch (Exception e){
                    execao = "Erro ao fazer o login " + e.getMessage();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), execao, Toast.LENGTH_LONG).show();
            }

        });
    }

    public void abrirTelaPrincipal(){
        startActivities(new Intent[]{new Intent(this, PrincipalActivity.class)});
        finish();
    }
}