package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.CategoriaDAO;
import iff.tcc.obrafacil.dao.ProdutoDAO;
import iff.tcc.obrafacil.model.Produto;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

public class ProdutoListaActivity extends ListActivity {
	private static final int REQUEST_ADD = 1;
	private static final int REQUEST_EDIT = 2;
	private Spinner sp;
	private Cursor cursorCategoria;
	private Cursor cursorProdutos;
	private ProdutoDAO produtoDao;
	private CategoriaDAO categoriaDao;

	private OnItemSelectedListener spListener = new OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> adapter, View view,
				int position, long idCategoria) {

			String nome = null;

			Cursor aux = (Cursor) adapter.getItemAtPosition(position);

			if (aux != null) {
				nome = aux.getString(aux
						.getColumnIndex(CategoriaDAO.COLUNA_NOME));
			}

			stopCursor(cursorProdutos);

			if ((nome != null) && (nome.equalsIgnoreCase("Todas"))) {
				cursorProdutos = produtoDao.consultarTodosProdutos();				
			} else {
				cursorProdutos = produtoDao
						.consultarProdutosPorCategoria(idCategoria - 1L);
			}

			startManagingCursor(cursorProdutos);

			SimpleCursorAdapter lvAdapter = new SimpleCursorAdapter(
					getApplicationContext(), R.layout.produto_linha, cursorProdutos,
					new String[] { ProdutoDAO.COLUNA_NOME,
						ProdutoDAO.COLUNA_DESCRICAO }, new int[] {
						R.id.linha1, R.id.linha2 });

			setListAdapter(lvAdapter);

		}

		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}

	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.produto_lista);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		sp = (Spinner) findViewById(R.id.sp_categorias);
		sp.setOnItemSelectedListener(spListener);

		ListView lv = getListView();
		registerForContextMenu(lv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mymenu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId())
		{
		case R.id.miAdd:
			Intent i = new Intent();
			i.setClass(this, ProdutoCadastroActivity.class);
			i.putExtra(ProdutoDAO.COLUNA_ID, 0);			
			startActivityForResult(i, REQUEST_ADD);			
			return true;
		case R.id.miHome:
			finish();			
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = info.position;
		long id = info.id;


		switch (item.getItemId())
		{
		case R.id.miDelete:
			ProdutoDAO.removerProduto(id);			
			fillData();

			return true;
		case R.id.miEdit:

			Cursor c = cursorProdutos;
			c.moveToPosition(position);

			Intent i = new Intent();
			i.setClass(this, ProdutoCadastroActivity.class);

			i.putExtra(ProdutoDAO.COLUNA_ID, id);

			i.putExtra(ProdutoDAO.COLUNA_NOME, c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_NOME)));

			i.putExtra(ProdutoDAO.COLUNA_DESCRICAO, c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_DESCRICAO)));

			i.putExtra(ProdutoDAO.COLUNA_UNIDADE, c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_UNIDADE)));

			i.putExtra(ProdutoDAO.COLUNA_ID_CATEGORIA, c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_ID_CATEGORIA)));

			startActivityForResult(i, REQUEST_EDIT);		


			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Produto produto = new Produto();
		switch(requestCode)
		{
		case REQUEST_ADD:
			try {
				produto.setNome(data.getStringExtra(ProdutoDAO.COLUNA_NOME));
				produto.setDescricao(data.getStringExtra(ProdutoDAO.COLUNA_DESCRICAO));
				produto.setUnidade(data.getIntExtra(ProdutoDAO.COLUNA_UNIDADE,0));
				produto.getCategoria().setId(Long.valueOf(data.getStringExtra(ProdutoDAO.COLUNA_ID_CATEGORIA)));
				produtoDao.open();
				produtoDao.criarProduto(produto);
				produtoDao.close();
				fillData();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ProdutoListaActivity - ADD");
			}
			break;

		case REQUEST_EDIT:
			try {
				produto.setNome(data.getStringExtra(ProdutoDAO.COLUNA_NOME));
				produto.setDescricao(data.getStringExtra(ProdutoDAO.COLUNA_DESCRICAO));
				produto.setId(data.getLongExtra(ProdutoDAO.COLUNA_ID, 0));
				produto.setUnidade(data.getIntExtra(ProdutoDAO.COLUNA_UNIDADE,0));
				produto.getCategoria().setId(Long.valueOf(data.getStringExtra(ProdutoDAO.COLUNA_ID_CATEGORIA)));
				produtoDao.open();
				produtoDao.atualizarProduto(produto);
				produtoDao.close();
				fillData();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ProdutoListaActivity - EDIT");
			}				
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
		}		
	}

	private void fillData() {

		produtoDao = new ProdutoDAO(getApplicationContext());
		produtoDao.open();

		categoriaDao = new CategoriaDAO(getApplicationContext());
		categoriaDao.open();

		stopCursor(cursorCategoria);

		cursorCategoria = CategoriaDAO.consultarTodasCategorias();

		startManagingCursor(cursorCategoria);

		SimpleCursorAdapter spAdapter = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.produto_linha_spinner, cursorCategoria,
				new String[] { CategoriaDAO.COLUNA_NOME },
				new int[] { R.id.linha_spinner });

		if (sp != null) {
			sp.setAdapter(spAdapter);
		}

	}

	protected void onResume() {
		super.onResume();
		fillData();
	}

	@Override
	protected void onPause() {
		super.onPause();

		stopCursor(cursorCategoria);
		stopCursor(cursorProdutos);

		produtoDao.close();
		categoriaDao.close();

	}

	private void stopCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			stopManagingCursor(c);
			c.close();
		}
	}
}