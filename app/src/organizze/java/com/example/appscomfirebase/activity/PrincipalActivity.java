package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.appscomfirebase.R;
import com.example.appscomfirebase.adapter.AdapterMovimentacao;
import com.example.appscomfirebase.config.ConfiguracaoFirebase;
import com.example.appscomfirebase.databinding.ActivityPrincipalBinding;
import com.example.appscomfirebase.helper.Base64Custom;
import com.example.appscomfirebase.model.Movimentacao;
import com.example.appscomfirebase.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private ActivityPrincipalBinding binding;
    private Double despesaT = 0.0, receitaT = 0.0, resumo = 0.0;
    private FirebaseAuth auth = ConfiguracaoFirebase.getAutentificacao();
    private DatabaseReference reference = ConfiguracaoFirebase.getFirebaseDataBase();
    private DatabaseReference usuario;
    private ValueEventListener eventListenerUsuario, eventListenerMes;
    private TextView saldo, pessoa;
    private RecyclerView mRecycler;
    private AdapterMovimentacao adapterMovimentacao;
    private List<Movimentacao> movimentacoes = new ArrayList<>();
    private DatabaseReference moveRef;
    private String mesAno;
    private MaterialCalendarView calendario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        saldo = findViewById(R.id.p_valor);
        pessoa = findViewById(R.id.p_titulo);

        calendario = findViewById(R.id.calendarView);
        configCalendario();

        mRecycler = findViewById(R.id.movimentos_recycler);

        //Configurar adapter
        adapterMovimentacao = new AdapterMovimentacao(movimentacoes, getApplicationContext());

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(adapterMovimentacao);
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

    public void configCalendario(){

        CharSequence meses[] = {"Janeiro", "Fevereiro", "Março", "Abril", "Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"};
        calendario.setTitleMonths(meses);

        CalendarDay dataAtual = calendario.getCurrentDate();
        String mesAtual = String.format("%02d", (dataAtual.getMonth() +1));

        mesAno = String.valueOf( mesAtual + "" + dataAtual.getYear());
        calendario.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {

                String mesAtual = String.format("%02d", (date.getMonth() +1));
                mesAno =  String.valueOf(mesAtual + "" + date.getYear());
                moveRef.removeEventListener(eventListenerMes);
                recuperarMovimentacao();
            }
        });
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

    private void recuperarMovimentacao(){
        String email = auth.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64(email);
        moveRef = reference.child("movimentacao")
                            .child(id)
                            .child(mesAno);

        eventListenerMes = moveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                movimentacoes.clear();

                for(DataSnapshot dados: snapshot.getChildren()){

                    Movimentacao movimentacao = dados.getValue(Movimentacao.class);
                    movimentacoes.add(movimentacao);
                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        recuperarResumo();
        recuperarMovimentacao();
    }

    @Override
    protected void onStop() {
        super.onStop();

        usuario.removeEventListener(eventListenerUsuario);
        moveRef.removeEventListener(eventListenerMes);
    }
}