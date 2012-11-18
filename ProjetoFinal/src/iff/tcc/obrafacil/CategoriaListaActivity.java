package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.CategoriaDAO;
import iff.tcc.obrafacil.model.Categoria;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CategoriaListaActivity extends ListActivity {
	private static final int REQUEST_ADD = 1;
	private static final int REQUEST_EDIT = 2;
	private Cursor cursorCategoria;
	private CategoriaDAO categoriaDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categoria_lista);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

		Intent i = new Intent();
		switch (item.getItemId())
		{
		case R.id.miAdd:
			i.setClass(this, CategoriaCadastroActivity.class);
			i.putExtra(CategoriaDAO.COLUNA_ID, 0);			
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

		if (id != 1){
			switch (item.getItemId())
			{
			case R.id.miDelete:
				CategoriaDAO.removerCategoria(id);			
				fillData();

				return true;
			case R.id.miEdit:
				Cursor c = cursorCategoria;
				c.moveToPosition(position);

				Intent i = new Intent();
				i.setClass(this, CategoriaCadastroActivity.class);

				i.putExtra(CategoriaDAO.COLUNA_ID, id);

				i.putExtra(CategoriaDAO.COLUNA_NOME, c.getString(c.getColumnIndexOrThrow(CategoriaDAO.COLUNA_NOME)));

				startActivityForResult(i, REQUEST_EDIT);					

				return true;
			default:
				return super.onContextItemSelected(item);
			}
		}else{
			String mensagem = "Esta categoria não pode ser editada ou removida.";
			Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
			.show();
			Log.w("CategoriaListaActivity", mensagem);
			return true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Categoria categoria = new Categoria();
		switch(requestCode)
		{
		case REQUEST_ADD:
			try {
				categoria.setNome(data.getStringExtra(CategoriaDAO.COLUNA_NOME));
				categoriaDao.open();
				categoriaDao.criarCategoria(categoria);
				categoriaDao.close();
				fillData();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - CategoriaListaActivity - ADD");
			}
			break;

		case REQUEST_EDIT:
			try {
				categoria.setNome(data.getStringExtra(CategoriaDAO.COLUNA_NOME));
				categoria.setId(data.getLongExtra(CategoriaDAO.COLUNA_ID, 0));
				categoriaDao.open();
				categoriaDao.atualizarCategoria(categoria);
				categoriaDao.close();
				fillData();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - CategoriaListaActivity - EDIT");
			}
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
		}		
	}

	private void fillData() {

		categoriaDao = new CategoriaDAO(getApplicationContext());
		categoriaDao.open();

		stopCursor(cursorCategoria);
		cursorCategoria = CategoriaDAO.consultarTodasCategorias();

		startManagingCursor(cursorCategoria);

		SimpleCursorAdapter lvAdapter = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.categoria_linha, cursorCategoria,
				new String[] {CategoriaDAO.COLUNA_NOME}, new int[] {
					R.id.linhaNomeCategoria});

		setListAdapter(lvAdapter);

	}

	protected void onResume() {
		super.onResume();
		fillData();
	}

	@Override
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