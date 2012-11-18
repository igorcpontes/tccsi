package iff.tcc.obrafacil;

import java.util.ArrayList;

import iff.tcc.obrafacil.dao.CategoriaDAO;
import iff.tcc.obrafacil.dao.ProdutoDAO;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ProdutoCadastroActivity extends Activity {

	private TextView txtTitulo;
	private EditText txtNome, txtDescricao;
	private Button btnCadastrarProduto;
	private Spinner spinner_unidades, spinner_categoria;
	private boolean bUpdateMode = false;
	private long id;
	private Cursor cursorCategoria;
	private CategoriaDAO categoriaDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.produto_cadastro);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtTitulo = (TextView) findViewById(R.produto_cadastro.titulo);
		txtNome = (EditText) findViewById(R.produto_cadastro.nome);
		txtDescricao = (EditText) findViewById(R.produto_cadastro.descricao);
		spinner_unidades = (Spinner) findViewById(R.produto_cadastro.unidade);
		spinner_categoria = (Spinner) findViewById(R.produto_cadastro.categoria);
		btnCadastrarProduto = (Button) findViewById(R.produto_cadastro.btnCadastrarProduto);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.unidades, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_unidades.setAdapter(adapter);
		filldata();

		Intent i = this.getIntent();
		id = i.getLongExtra(ProdutoDAO.COLUNA_ID, Long.valueOf("0"));

		if (id==0){ //ADD
			bUpdateMode  = false;
		}else{ //EDIT
			bUpdateMode = true;
			btnCadastrarProduto.setText("Atualizar");
			txtTitulo.setText("Atualização de Produto");
			txtNome.setText(i.getStringExtra(ProdutoDAO.COLUNA_NOME));
			txtDescricao.setText(i.getStringExtra(ProdutoDAO.COLUNA_DESCRICAO));
			spinner_unidades.setSelection(Integer.parseInt(i.getStringExtra(ProdutoDAO.COLUNA_UNIDADE)));
			spinner_categoria.setSelection(Integer.parseInt(i.getStringExtra(ProdutoDAO.COLUNA_ID_CATEGORIA))-1);
		}

		btnCadastrarProduto.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if(txtNome.getText().toString().equals("") || txtDescricao.getText().toString().equals("")){
					String mensagem = "Favor preencher todos os campos e tentar novamente.";
					Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
					.show();
					Log.w("ProdutoCadastroActivity", mensagem);
				}else{
					Intent i = new Intent();
					i.putExtra(ProdutoDAO.COLUNA_NOME, txtNome.getText().toString());
					i.putExtra(ProdutoDAO.COLUNA_DESCRICAO, txtDescricao.getText().toString());
					i.putExtra(ProdutoDAO.COLUNA_UNIDADE, spinner_unidades.getSelectedItemPosition());
					i.putExtra(ProdutoDAO.COLUNA_ID_CATEGORIA, String.valueOf(spinner_categoria.getSelectedItemPosition()+1));
					if (bUpdateMode)
						i.putExtra(ProdutoDAO.COLUNA_ID, id);
					setResult(RESULT_OK, i);					
					finish();
				}
			}
		});		
	}

	private void filldata() {
		categoriaDao = new CategoriaDAO(getApplicationContext());
		categoriaDao.open();
		stopCursor(cursorCategoria);

		ArrayList<String> nomesCategorias = CategoriaDAO.consultarNomeDeTodasCategorias();
		nomesCategorias.remove(0);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nomesCategorias);
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (spinner_categoria != null) {
			spinner_categoria.setAdapter(arrayAdapter);
			if (id != 0){
				Intent i = this.getIntent();
				spinner_categoria.setSelection(Integer.parseInt(i.getStringExtra(ProdutoDAO.COLUNA_ID_CATEGORIA))-1);
			}
		}
	}

	protected void onResume() {
		super.onResume();
		filldata();
	}

	protected void onPause() {
		super.onPause();

		stopCursor(cursorCategoria);
		categoriaDao.close();
	}

	private void stopCursor(Cursor c) {

		if (c != null && !c.isClosed()) {
			stopManagingCursor(c);
			c.close();
		}
	}
}
