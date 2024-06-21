package com.example.appscomfirebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private Movimentacao movimenta;
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

        swipe();
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

    //Configura o calendário e instancia uma String
    // para uso na busca no DB de movimentação
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

    //Recupera os dados do usuário no DB
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

    //Atualizando o saldo total
    public void atualizaSaldoT(){

        String email = auth.getCurrentUser().getEmail();
        String id = Base64Custom.codificarBase64(email);
        usuario = reference.child("usuarios").child(id);

        if(movimenta.getTipo().equals("r")){
            receitaT = receitaT - movimenta.getValor();
            usuario.child("receitaTotal").setValue(receitaT);
        }else {
            despesaT = despesaT - movimenta.getValor();
            usuario.child("despesaTotal").setValue(despesaT);
        }
    }

    //Recupera os dados das movimentações do usuário no DB
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
                    movimentacao.setId(dados.getKey());
                    movimentacoes.add(movimentacao);
                }

                adapterMovimentacao.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Cria um swipe para exclusão de dados no DB da movimentação
    public void swipe(){
        ItemTouchHelper.Callback itemToch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

                int dragFlag = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipFlags = ItemTouchHelper.START| ItemTouchHelper.END;

                return makeMovementFlags(dragFlag,swipFlags);
            }
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                excluirMove(viewHolder);
            }
        };

        new ItemTouchHelper(itemToch).attachToRecyclerView(mRecycler);
    }

    public void excluirMove(RecyclerView.ViewHolder viewHolder){

        AlertDialog.Builder alerta = new AlertDialog.Builder(this);

        //Composição do alerta
        alerta.setTitle("Excuir movimentação");
        alerta.setMessage("Tem certeza de que deseja excluir essa movimentação?");
        alerta.setCancelable(false);

        alerta.setPositiveButton("Confirmar", (dialog, which) -> {

            int posicao = viewHolder.getAdapterPosition();
            movimenta = movimentacoes.get(posicao);

            //Remover do banco de dados
            String email = auth.getCurrentUser().getEmail();
            String id = Base64Custom.codificarBase64(email);
            moveRef = reference.child("movimentacao")
                    .child(id)
                    .child(mesAno);

            moveRef.child(movimenta.getId()).removeValue();
            adapterMovimentacao.notifyItemRemoved(posicao);

            atualizaSaldoT();
        });

        alerta.setNegativeButton("Cancelar", (dialog, which) -> {

            Toast.makeText(getApplicationContext(), "Operação cancelada", Toast.LENGTH_LONG).show();
            adapterMovimentacao.notifyDataSetChanged();

        });

        AlertDialog alertDialog = alerta.create();
        alertDialog.show();
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