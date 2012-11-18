package iff.tcc.obrafacil;

import iff.tcc.obrafacil.dao.ObraDAO;
import iff.tcc.obrafacil.model.Obra;

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
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ObraListaActivity extends ListActivity {
	private static final int REQUEST_ADD = 1;
	private static final int REQUEST_EDIT = 2;
	private static final int REQUEST_LISTAR = 3;
	private Cursor cursorObra;
	private ObraDAO obraDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.obra_lista);
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
		inflater.inflate(R.menu.obracontextmenu, menu);
	}

	private void fillData() {

		obraDao = new ObraDAO(getApplicationContext());
		obraDao.open();

		stopCursor(cursorObra);
		cursorObra = obraDao.consultarTodasObras();

		startManagingCursor(cursorObra);

		SimpleCursorAdapter lvAdapter = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.obra_linha, cursorObra,
				new String[] {ObraDAO.COLUNA_NOME, ObraDAO.COLUNA_ENDERECO}, new int[] {
					R.id.linhaNomeObra, R.id.linhaEnderecoObra});

		setListAdapter(lvAdapter);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i = new Intent();
		switch (item.getItemId())
		{
		case R.id.miAdd:			
			i.setClass(this, ObraCadastroActivity.class);
			i.putExtra(ObraDAO.COLUNA_ID, 0);			
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
		Intent i = new Intent();

		switch (item.getItemId())
		{
		case R.id.miDelete:
			ObraDAO.removerObra(id);			
			fillData();			
			return true;
		case R.id.miEdit:			
			Cursor c = cursorObra;
			c.moveToPosition(position);			
			i.setClass(this, ObraCadastroActivity.class);			
			i.putExtra(ObraDAO.COLUNA_ID, id);			
			i.putExtra(ObraDAO.COLUNA_NOME, c.getString(c.getColumnIndexOrThrow(ObraDAO.COLUNA_NOME)));			
			i.putExtra(ObraDAO.COLUNA_ENDERECO, c.getString(c.getColumnIndexOrThrow(ObraDAO.COLUNA_ENDERECO)));			
			startActivityForResult(i, REQUEST_EDIT);
			return true;
		case R.id.miAbrirListas:
			i.setClass(this, ListasObraActivity.class);
			i.putExtra(ObraDAO.COLUNA_ID, id);			
			startActivityForResult(i, REQUEST_LISTAR);			
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == -2){
			finish();
		}

		Obra obra = new Obra();

		switch(requestCode)
		{
		case REQUEST_ADD:
			try {
				obra.setNome(data.getStringExtra(ObraDAO.COLUNA_NOME));
				obra.setEndereco(data.getStringExtra(ObraDAO.COLUNA_ENDERECO));
				obraDao.open();
				obraDao.criarObra(obra);
				obraDao.close();
				fillData();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ObraListaActivity - ADD");
			}
			break;

		case REQUEST_EDIT:
			try {
				obra.setNome(data.getStringExtra(ObraDAO.COLUNA_NOME));
				obra.setEndereco(data.getStringExtra(ObraDAO.COLUNA_ENDERECO));
				obra.setId(data.getLongExtra(ObraDAO.COLUNA_ID, 0));
				obraDao.open();
				obraDao.atualizarObra(obra);
				obraDao.close();
				fillData();	
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ObraListaActivity - EDIT");
			}			
			break;

		case REQUEST_LISTAR:
			fillData();				
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
		}		
	}

	protected void onResume() {
		super.onResume();
		fillData();
	}

	@Override
	protected void onPause() {
		super.onPause();

		stopCursor(cursorObra);
		obraDao.close();

	}

	private void stopCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			stopManagingCursor(c);
			c.close();
		}
	}
}