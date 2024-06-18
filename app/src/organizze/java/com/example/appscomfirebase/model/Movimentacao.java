package com.example.appscomfirebase.model;

import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.helper.Base64Custom;
import com.example.appscomfirebase.helper.DataCustom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class Movimentacao {

    private String data, categoria, descricao, tipo;
    private double valor;


    public Movimentacao() {
    }

    public void salvar(String data){

        FirebaseAuth auth = ConfiguracaoFirebase.getAutentificacao();
        DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDataBase();

        String idUsuario = Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
        //Add ao banco de dados do Firebase
        reference.child("movimentacao")
                .child(idUsuario)
                .child(DataCustom.dataEscolhida(data))
                .push()
                .setValue(this);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
