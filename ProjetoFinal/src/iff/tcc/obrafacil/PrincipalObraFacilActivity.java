package iff.tcc.obrafacil;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PrincipalObraFacilActivity extends Activity {

	private Button btnCategoria, btnProduto, btnObra, btnSair;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.principal);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		btnCategoria = (Button) findViewById(R.principal.btnCategoria);
		btnProduto = (Button) findViewById(R.principal.btnProduto);
		btnObra = (Button) findViewById(R.principal.btnObra);
		btnSair = (Button) findViewById(R.principal.btnSair);

		btnCategoria.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {
				Categoria();				
			}
		});

		btnProduto.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {

				Produto();
			}
		});

		btnObra.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {

				Obra();
			}
		});

		btnSair.setOnClickListener(new View.OnClickListener() {			
			public void onClick(View v) {

				finish();
			}
		});

	}

	private void Categoria(){
		Intent i = new Intent();
		i.setClass(this, CategoriaListaActivity.class);
		startActivityForResult(i, 0);
	}
	private void Produto(){
		Intent i = new Intent();
		i.setClass(this, ProdutoListaActivity.class);
		startActivityForResult(i, 1);
	}
	private void Obra(){
		Intent i = new Intent();
		i.setClass(this, ObraListaActivity.class);
		startActivityForResult(i, 2);
	}

	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

}
