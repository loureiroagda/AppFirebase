package com.example.appscomfirebase.config;

import com.example.appscomfirebase.helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autentifica;
    private static DatabaseReference reference;

    private static FirebaseDatabase database;

    //Retorna a intancia FirebaseAuth
    public static FirebaseAuth getAutentificacao(){

        if(autentifica == null)
        {
            autentifica = FirebaseAuth.getInstance();
        }

        return  autentifica;
    }

//    Retorna dados dos bancos de dados
    public static DatabaseReference getFirebaseDataBase(){

        if(reference == null){
            database = database.getInstance();
            reference = database.getReference();
        }
        return reference;
    }

    public static DatabaseReference getUsuario(){

        String email = autentifica.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64(email);
        DatabaseReference usuario = reference.child("usuarios").child(id);
        return usuario;
    }
}
