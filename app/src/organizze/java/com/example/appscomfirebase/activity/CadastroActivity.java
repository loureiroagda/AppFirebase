package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityCadastroBinding;
import com.example.appscomfirebase.databinding.ActivityMainBinding;
import com.example.appscomfirebase.helper.Base64Custom;
import com.example.appscomfirebase.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {
    ActivityCadastroBinding binding;
    private FirebaseAuth autentifica;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnCadastro.setOnClickListener(v -> {

            String txtN = binding.nomeCadastro.getText().toString();
            String txtE = binding.emailCadastro.getText().toString();
            String txtS = binding.senhaCadastro.getText().toString();

            if(txtN.isEmpty() || txtE.isEmpty() || txtS.isEmpty())
            {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
            }else {

                usuario =new Usuario();
                usuario.setNome(txtN);
                usuario.setEmail(txtE);
                usuario.setSenha(txtS);
                cadastrarUsuario();
            }
        });
    }

    public void cadastrarUsuario(){

        autentifica = ConfiguracaoFirebase.getAutentificacao();

        autentifica.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(this, task -> {

            if(task.isSuccessful())
            {
                String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                usuario.setIdUsuario(idUsuario);
                usuario.salvar();
                finish();
            }else {

                String execao = "";
                try {
                    throw task.getException();
                }catch (FirebaseAuthWeakPasswordException e){
                    execao = "Digite uma senha mais forte!";
                }catch (FirebaseAuthInvalidCredentialsException e){
                    execao = "Digite um email válido";
                }catch (FirebaseAuthUserCollisionException e){
                    execao = "Usuário já existe";
                }catch (Exception e){
                    execao = "Erro ao cadastrar o usuário" + e.getMessage();
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), execao, Toast.LENGTH_LONG).show();
            }
        });

    }
}