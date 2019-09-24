package br.com.fastfeira.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import br.com.fastfeira.R;
import br.com.fastfeira.helper.ConfiguracaoFirebase;
import br.com.fastfeira.helper.Usuario;

public class AutenticacaoActivity extends AppCompatActivity {

    private Button botaoAcessar,botaoCadastrar;
    private EditText campoEmail, campoSenha;

    private FirebaseAuth autenticacao;

    //Ailson

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacao);
        getSupportActionBar().hide();

        inicializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        verificaUsuarioLogado();

        botaoAcessar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                //verifica se o email esta vazio
                if(!email.isEmpty()){
                    //verifica se a senha esta vazio
                    if(!senha.isEmpty()){

                            //Fazer Login
                            autenticacao.signInWithEmailAndPassword(
                                    email, senha
                            ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Logado com sucesso",
                                                Toast.LENGTH_SHORT).show();
                                       abrirTelaPrincipal();

                                    }else{
                                        Toast.makeText(AutenticacaoActivity.this,
                                                "Erro ao fazer login : " + task.getException() ,
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                    }else{
                        Toast.makeText(AutenticacaoActivity.this,
                                "Preencha a Senha!",
                                Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AutenticacaoActivity.this,
                            "Preencha o E-mail!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastroUserActivity.class));
            }
        });


    }

    private void verificaUsuarioLogado(){
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }

    private void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    private void inicializaComponentes(){
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        botaoAcessar = findViewById(R.id.buttonAcesso);
        botaoCadastrar = findViewById(R.id.buttonTelaCadastro);
    }
}
