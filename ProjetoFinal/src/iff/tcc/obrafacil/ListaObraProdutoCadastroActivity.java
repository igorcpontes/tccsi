package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.ListaProdutoDAO;
import iff.tcc.obrafacil.dao.ProdutoDAO;
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

public class ListaObraProdutoCadastroActivity extends Activity {

	private TextView txtTitulo;
	private TextView txtProduto;
	private EditText txtQuantidade, txtValor;
	private Button btnAdicionarProduto;
	private long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lista_produto_cadastro);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtTitulo = (TextView) findViewById(R.lista_cadastro.titulo);
		txtProduto = (TextView) findViewById(R.lista_cadastro.produto);
		txtQuantidade = (EditText) findViewById(R.lista_cadastro.quantidade);
		txtValor = (EditText) findViewById(R.lista_cadastro.preco);				
		btnAdicionarProduto = (Button) findViewById(R.lista_cadastro.btnAdicionarProduto);		

		Intent i = this.getIntent();
		id = i.getLongExtra(ProdutoDAO.COLUNA_ID, Long.valueOf("0"));

		if (id==0){ // ADD
			txtProduto.setText(i.getStringExtra(ProdutoDAO.COLUNA_NOME));
		}else{ //EDIT
			btnAdicionarProduto.setText("Atualizar");
			txtTitulo.setText("Atualização de Produto");
			txtProduto.setText(i.getStringExtra(ProdutoDAO.COLUNA_NOME));
			txtQuantidade.setText(String.valueOf(i.getLongExtra(ListaProdutoDAO.COLUNA_QUANTIDADE, 0L)));
			txtValor.setText(String.valueOf(i.getDoubleExtra(ListaProdutoDAO.COLUNA_VALOR, 0)));
		}

		btnAdicionarProduto.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if(txtQuantidade.getText().toString().equals("") || txtValor.getText().toString().equals("")){
					String mensagem = "Favor preencher todos os campos e tentar novamente.";
					Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
					.show();
					Log.w("ListaObraProdutoCadastroActivity", mensagem);
				}else{
					Intent i = new Intent();
					i.putExtra(ListaProdutoDAO.COLUNA_QUANTIDADE, txtQuantidade.getText().toString());
					i.putExtra(ListaProdutoDAO.COLUNA_VALOR, txtValor.getText().toString());
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
