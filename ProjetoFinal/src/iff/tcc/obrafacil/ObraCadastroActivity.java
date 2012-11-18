package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.ObraDAO;
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

public class ObraCadastroActivity extends Activity {

	private TextView txtTitulo;
	private EditText txtNome, txtEndereco;
	private Button btnCadastrarObra;
	private boolean bUpdateMode = false;
	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.obra_cadastro);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtTitulo = (TextView) findViewById(R.obra_cadastro.titulo);
		txtNome = (EditText) findViewById(R.obra_cadastro.nome);
		txtEndereco = (EditText) findViewById(R.obra_cadastro.endereco);
		btnCadastrarObra = (Button) findViewById(R.obra_cadastro.btnCadastrarObra);

		Intent i = this.getIntent();
		id = i.getLongExtra(ObraDAO.COLUNA_ID, Long.valueOf("0"));

		if (id==0){ //ADD
			bUpdateMode  = false;
		}else{ // EDIT
			bUpdateMode = true;
			btnCadastrarObra.setText("Atualizar");
			txtTitulo.setText("Atualização de Obra");
			txtNome.setText(i.getStringExtra(ObraDAO.COLUNA_NOME));
			txtEndereco.setText(i.getStringExtra(ObraDAO.COLUNA_ENDERECO));
		}

		btnCadastrarObra.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if(txtNome.getText().toString().equals("") || txtEndereco.getText().toString().equals("")){
					String mensagem = "Favor preencher todos os campos e tentar novamente.";
					Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
					.show();
					Log.w("ObraCadastroActivity", mensagem);
				}else{
					Intent i = new Intent();
					i.putExtra(ObraDAO.COLUNA_NOME, txtNome.getText().toString());
					i.putExtra(ObraDAO.COLUNA_ENDERECO, txtEndereco.getText().toString());
					if (bUpdateMode)
						i.putExtra(ObraDAO.COLUNA_ID, id);
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
