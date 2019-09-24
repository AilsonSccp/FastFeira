package br.com.fastfeira.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.fastfeira.R;
import br.com.fastfeira.helper.ConfiguracaoFirebase;
import br.com.fastfeira.helper.Usuario;

public class CadastroUserActivity extends AppCompatActivity {

    private Button cadastrousuario;
    private EditText campoNome, campoSobrenome, campoEmail, campoSenha, campoDataNascimento, campoTelefone, campoCPF;
    private RadioButton sexoMasculino, sexoFeminino;

    private FirebaseAuth autenticacao;
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_user);
        getSupportActionBar().hide();

        inicializaComponentes();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();





        cadastrousuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = campoEmail.getText().toString();
                String senha = campoSenha.getText().toString();

                if(!email.isEmpty() && !senha.isEmpty()){
                    //campos não vazios
                    autenticacao.createUserWithEmailAndPassword(
                            email, senha
                    ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(CadastroUserActivity.this,
                                        "Cadastro realizado com sucesso!",
                                        Toast.LENGTH_SHORT).show();
                                abrirTelaPrincipal();
                            }else{
                                String erroExcecao = "";

                                try{
                                    throw task.getException();
                                }catch (FirebaseAuthWeakPasswordException e){
                                    erroExcecao = "Digite uma senha mais forte!";
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    erroExcecao = "Por favor, digite um e-mail válido";
                                }catch (FirebaseAuthUserCollisionException e){
                                    erroExcecao = "Este conta já foi cadastrada";
                                } catch (Exception e) {
                                    erroExcecao = "ao cadastrar usuário: "  + e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(CadastroUserActivity.this,
                                        "Erro: " + erroExcecao ,
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                    Usuario usuario = new Usuario();
                    usuario.setNome(campoNome.getText().toString());
                    usuario.setSobreNome(campoSobrenome.getText().toString());
                    usuario.setDataNascimento(campoDataNascimento.getText().toString());
                    usuario.setTelefone(campoTelefone.getText().toString());
                    usuario.setCPF(campoCPF.getText().toString());
                    RadioGroup radioGroup = (RadioGroup)findViewById(R.id.Sexo);
                    String radiovalue =((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
                    usuario.setSexo(radiovalue);
                    DatabaseReference usuarios = referencia.child("Usuarios");
                    usuarios.push().setValue(usuario);


                }else{
                    Toast.makeText(CadastroUserActivity.this,
                            "Preencha todos os campos",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    private void abrirTelaPrincipal(){
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
    }

    public void onBackPressed(){

        startActivity(new Intent(getApplicationContext(), AutenticacaoActivity.class));
        super.onBackPressed();
    }

    private void inicializaComponentes(){
        campoSobrenome = findViewById(R.id.textSobrenome);
        campoNome = findViewById(R.id.textNome);
        campoTelefone = findViewById(R.id.editTelefone);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        cadastrousuario = findViewById(R.id.buttonCadastrarUsuario);
        campoDataNascimento = findViewById(R.id.dataNascimento);
        sexoMasculino = findViewById(R.id.radioButtonSexoMasculino);
        sexoFeminino = findViewById(R.id.radioButtonSexoFeminino);
        campoCPF = findViewById(R.id.editCPF);

    }

    /*private void dadosUsuarios(){
        Usuario usuario = new Usuario();

        usuario.setNome(campoNome.getText().toString());
        usuario.setSobreNome(campoSobrenome.getText().toString());
        usuario.setDataNascimento(campoDataNascimento.getText().toString());
        usuario.setTelefone(campoTelefone.getText().toString());
        usuario.setCPF(campoCPF.getText().toString());
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.Sexo);
        String radiovalue =((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();
        usuario.setSexo(radiovalue);
    }*/
}
