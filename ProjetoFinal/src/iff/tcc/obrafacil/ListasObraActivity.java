package iff.tcc.obrafacil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import iff.tcc.obrafacil.dao.ListaDAO;
import iff.tcc.obrafacil.dao.ObraDAO;
import iff.tcc.obrafacil.model.Lista;

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
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ListasObraActivity extends ListActivity {

	private TextView txtNomeObra;
	private static final int REQUEST_ADD = 1;
	private static final int REQUEST_EDIT = 2;
	private Vector<Lista> vectorLista = new Vector<Lista>();
	private Cursor cursorLista;
	private ListaDAO listaDao;
	private Long idObra;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_obra_lista);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		txtNomeObra = (TextView) findViewById(R.lista_compras.nome_obra);

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

	private void fillData() {

		ArrayList<HashMap<String,String>> listaObras = new ArrayList<HashMap<String,String>>();
		Intent i = this.getIntent();
		idObra = i.getLongExtra(ObraDAO.COLUNA_ID, Long.valueOf("0"));

		listaDao = new ListaDAO(getApplicationContext());
		listaDao.open();

		txtNomeObra.setText(ObraDAO.consultarNomeDaObra(idObra));

		stopCursor(cursorLista);
		cursorLista = listaDao.consultarListasPorObra(idObra);
		startManagingCursor(cursorLista);
		vectorLista = listaDao.deCursorParaVectorLista(cursorLista);
		listaObras = listaDao.deCursorParaArrayList(cursorLista);

		if (listaObras==null){
			listaObras = new ArrayList<HashMap<String,String>>();
		}

		SimpleAdapter lvAdapter = new SimpleAdapter(
				getApplicationContext(), listaObras, R.layout.lista_obra_linha,
				new String[] {"Data", "Valor"}, new int[] {
					R.id.linhaData, R.id.linhaValor});

		setListAdapter(lvAdapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId())
		{
		case R.id.miAdd:
			Intent i = new Intent();
			i.setClass(this, ListaObraCadastroActivity.class);
			i.putExtra("Cadastro", 1);
			i.putExtra(ObraDAO.COLUNA_ID, idObra);			
			startActivityForResult(i, REQUEST_ADD);			
			return true;
		case R.id.miHome:
			setResult(-2, getIntent());
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


		switch (item.getItemId())
		{
		case R.id.miDelete:
			listaDao.removerLista(vectorLista.get(position).getId());			
			fillData();

			return true;
		case R.id.miEdit:

			Cursor c = cursorLista;
			c.moveToPosition(position);

			Intent i = new Intent();
			i.setClass(this, ListaObraCadastroActivity.class);

			i.putExtra("Cadastro", 0);
			i.putExtra(ListaDAO.COLUNA_ID, vectorLista.get(position).getId());

			startActivityForResult(i, REQUEST_EDIT);		


			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == -2){
			setResult(-2, getIntent());
			finish();
		}

		switch(requestCode)
		{
		case REQUEST_ADD:
			fillData();
			break;

		case REQUEST_EDIT:
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

		stopCursor(cursorLista);

		listaDao.close();

	}

	private void stopCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			stopManagingCursor(c);
			c.close();
		}
	}
}