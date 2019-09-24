package br.com.fastfeira.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import br.com.fastfeira.R;
import br.com.fastfeira.helper.ConfiguracaoFirebase;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Button sair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        sair = findViewById(R.id.buttonSair);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autenticacao.signOut();
                startActivity(new Intent(getApplicationContext(), AutenticacaoActivity.class));
            }
        });
    }

    public void onBackPressed()
    {
        finish();
        System.exit(0);
    }
}
