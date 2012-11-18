package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.CategoriaDAO;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CategoriaCadastroActivity extends Activity {

	private TextView txtTitulo;
	private EditText txtNome;
	private Button btnCadastrarCategoria;
	private boolean bUpdateMode = false;
	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.categoria_cadastro);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtTitulo = (TextView) findViewById(R.categoria_cadastro.titulo);
		txtNome = (EditText) findViewById(R.categoria_cadastro.nome);
		btnCadastrarCategoria = (Button) findViewById(R.categoria_cadastro.btnCadastrarCategoria);

		Intent i = this.getIntent();
		id = i.getLongExtra(CategoriaDAO.COLUNA_ID, Long.valueOf("0"));

		if (id==0){ // ADD
			bUpdateMode  = false;
		}
		else{ // EDIT
			bUpdateMode = true;
			btnCadastrarCategoria.setText("Atualizar");
			txtTitulo.setText("Atualização de Categoria");
			String nome = i.getStringExtra(CategoriaDAO.COLUNA_NOME);
			txtNome.setText(nome);			
		}

		btnCadastrarCategoria.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if(txtNome.getText().toString().equals("")){
					String mensagem = "Favor preencher todos os campos e tentar novamente.";
					Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
					.show();
					Log.w("CategoriaCadastroActivity", mensagem);
				}else{
					Intent i = new Intent();
					i.putExtra(CategoriaDAO.COLUNA_NOME, txtNome.getText().toString());
					if (bUpdateMode)
						i.putExtra(CategoriaDAO.COLUNA_ID, id);
					setResult(RESULT_OK, i);
					finish();
				}
			}
		});		
	}

	protected void onResume() {
		super.onResume();
	}

	protected void onPause() {
		super.onPause();
	}
}
