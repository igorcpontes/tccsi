package iff.tcc.obrafacil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import iff.tcc.obrafacil.dao.ListaDAO;
import iff.tcc.obrafacil.dao.ListaProdutoDAO;
import iff.tcc.obrafacil.dao.ObraDAO;
import iff.tcc.obrafacil.dao.ProdutoDAO;
import iff.tcc.obrafacil.model.Lista;
import iff.tcc.obrafacil.model.ListaProduto;
import iff.tcc.obrafacil.model.Produto;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListaObraCadastroActivity extends ListActivity {
	private static final int REQUEST_ADD = 1;
	private static final int REQUEST_EDIT = 2;
	private int position=-1, idLista;
	private boolean primeiraEdicao = true, novoProduto = false;
	private AutoCompleteTextView autoComplete;
	private TextView txtTotal;
	private static String[] arrayAutoComplete=null, unidades=null;
	ArrayList<HashMap<String,String>> listaProdutos = new ArrayList<HashMap<String,String>>();
	private static Lista lista = new Lista();
	private Button btnAdicionarProdutoLista;
	private Cursor cursorProdutos;
	private Cursor cursorLista;
	private Cursor cursorListaProduto;
	private ProdutoDAO produtoDao;
	private ListaDAO listaDao;
	private ListaProdutoDAO listaProdutoDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.lista_lista);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		ListView lv = getListView();
		registerForContextMenu(lv);

		autoComplete = (AutoCompleteTextView) this.findViewById(R.lista.lista_autocomplete);
		txtTotal = (TextView) findViewById(R.lista.total);
		btnAdicionarProdutoLista = (Button) findViewById(R.lista.btnAdicionar);

		btnAdicionarProdutoLista.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				abrirCadastro();
			}
		});

		Resources res = getResources();
		unidades = res.getStringArray(R.array.unidades);
	}

	private void abrirCadastro() {

		int position=-1;
		Intent i = new Intent();
		i.setClass(this, ListaObraProdutoCadastroActivity.class);
		Cursor c = cursorProdutos;
		c.moveToFirst();

		do {
			if (c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_NOME)).equalsIgnoreCase(String.valueOf(autoComplete.getText()))){
				position = c.getPosition();
				ListaProduto listaProduto = new ListaProduto();
				listaProduto.getProduto().setId(c.getLong(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_ID)));
				listaProduto.getProduto().setNome(c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_NOME)));
				listaProduto.getProduto().setUnidade(c.getInt(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_UNIDADE)));
				lista.getListaProduto().add(listaProduto);
				break;
			}
		} while (c.moveToNext());

		if (position != -1){
			cursorProdutos.moveToPosition(position);
			i.putExtra(ProdutoDAO.COLUNA_NOME, c.getString(c.getColumnIndexOrThrow(ProdutoDAO.COLUNA_NOME)));
			startActivityForResult(i, REQUEST_ADD);
		}else{
			String mensagem = "Produto não encontrado. Favor tentar novamente.";
			Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
			.show();
			Log.w("ListaObraCadastroActivity", mensagem);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listamymenu, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {		
		super.onCreateContextMenu(menu, v, menuInfo);

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contextmenu, menu);
	}

	private void fillData() {

		listaDao = new ListaDAO(getApplicationContext());
		listaProdutoDao = new ListaProdutoDAO(getApplicationContext());
		stopCursor(cursorProdutos);		
		cursorProdutos = produtoDao.consultarTodosProdutos();
		arrayAutoComplete = produtoDao.deCursorParaArrayProduto(cursorProdutos, ProdutoDAO.COLUNA_NOME);
		startManagingCursor(cursorProdutos);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, arrayAutoComplete);
		autoComplete.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId())
		{
		case R.id.miAdd:
			if(lista.getListaProduto().size()>0){
				listaDao.open();
				if(lista.getId()==null){ //CASO SEJA UMA NOVA LISTA
					lista.setData(new Date());				
					listaDao.criarLista(lista);
					stopCursor(cursorLista);
					cursorLista = listaDao.consultarListasPorData(lista.getData());
					lista.setId(cursorLista.getLong(cursorLista.getColumnIndexOrThrow(ListaDAO.COLUNA_ID)));
				}else{ //CASO SEJA UMA LISTA JÁ CADASTRADA
					listaDao.atualizarLista(lista);
				}
				listaProdutoDao.open();
				for(int x=0; x<lista.getListaProduto().size(); x++){
					if(lista.getListaProduto().get(x).getLista().getId()==null){ //SE FOR UM PRODUTO NOVO
						lista.getListaProduto().get(x).setLista(lista);
						listaProdutoDao.criarListaProduto(lista.getListaProduto().get(x));

					}else{ //SE FOR UM PRODUTO JÁ CADASTRADO NA LISTA
						lista.getListaProduto().get(x).setLista(lista);
						listaProdutoDao.atualizarListaProduto(lista.getListaProduto().get(x));
					}					
				}
				listaProdutos.clear();
				atualizarLista();
				lista = new Lista();
				primeiraEdicao=true;
				finish();
			}else{
				String mensagem = "Favor adicionar um produto à lista e tentar novamente.";
				Toast.makeText(getApplicationContext(), mensagem, Toast.LENGTH_LONG)
				.show();
				Log.w("ListaObraCadastroActivity", mensagem);
			}

			return true;

		case R.id.miHome:
			primeiraEdicao=true;
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
		position = info.position;		

		switch (item.getItemId())
		{
		case R.id.miDelete:
			Double subTotal = lista.getListaProduto().get(position).getQuantidade() * lista.getListaProduto().get(position).getValor();
			lista.setValor(lista.getValor() - subTotal);
			txtTotal.setText("Total: R$" + String.valueOf(lista.getValor()));
			if(lista.getListaProduto().get(position).getLista().getId()!=null){ //SE FOR UM PRODUTO JÁ CADASTRADO NA LISTA
				listaProdutoDao.removerListaProduto(lista.getListaProduto().get(position).getId() - 1L);
			}
			listaProdutos.remove(position);
			lista.getListaProduto().remove(position);
			atualizarLista();
			return true;

		case R.id.miEdit:			
			Intent i = new Intent();
			i.setClass(this, ListaObraProdutoCadastroActivity.class);			
			i.putExtra(ProdutoDAO.COLUNA_ID, lista.getListaProduto().get(position).getProduto().getId());			
			i.putExtra(ProdutoDAO.COLUNA_NOME, lista.getListaProduto().get(position).getProduto().getNome());			
			i.putExtra(ListaProdutoDAO.COLUNA_QUANTIDADE, lista.getListaProduto().get(position).getQuantidade());			
			i.putExtra(ListaProdutoDAO.COLUNA_VALOR, lista.getListaProduto().get(position).getValor());			
			startActivityForResult(i, REQUEST_EDIT);				
			return true;

		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Double subTotal;
		switch(requestCode)
		{
		case REQUEST_ADD:
			try {
				lista.getListaProduto().lastElement().setQuantidade(Long.valueOf(data.getStringExtra(ListaProdutoDAO.COLUNA_QUANTIDADE)));
				lista.getListaProduto().lastElement().setValor(Double.valueOf(data.getStringExtra(ListaProdutoDAO.COLUNA_VALOR)));
				adicionarListaProdutos(String.valueOf(autoComplete.getText().toString()));
				subTotal = lista.getListaProduto().lastElement().getQuantidade() * lista.getListaProduto().lastElement().getValor();
				lista.setValor(lista.getValor() + subTotal);
				autoComplete.setText("");
				txtTotal.setText("Total: R$" + String.valueOf(lista.getValor()));
				atualizarLista();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ListaObraCadastroActivity - ADD");
			}
			break;

		case REQUEST_EDIT:
			try {
				subTotal = lista.getListaProduto().get(position).getQuantidade() * lista.getListaProduto().get(position).getValor();
				lista.setValor(lista.getValor() - subTotal);
				lista.getListaProduto().get(position).setQuantidade(Long.valueOf(data.getStringExtra(ListaProdutoDAO.COLUNA_QUANTIDADE)));
				lista.getListaProduto().get(position).setValor(Double.valueOf(data.getStringExtra(ListaProdutoDAO.COLUNA_VALOR)));
				subTotal = lista.getListaProduto().get(position).getQuantidade() * lista.getListaProduto().get(position).getValor();
				lista.setValor(lista.getValor() + subTotal);
				txtTotal.setText("Total: R$" + String.valueOf(lista.getValor()));
				atualizarLista();
			} catch(NullPointerException e) {
				System.out.println("NullPointerException - OnActivityResult - ListaObraCadastroActivity - EDIT");
			}				
			break;

		default:
			super.onActivityResult(requestCode, resultCode, data);
		}		
	}

	private void adicionarListaProdutos(String nome) {

		produtoDao.open();
		Produto produto = produtoDao.getProduto(nome);
		String dadosProduto = lista.getListaProduto().lastElement().getQuantidade().toString() + " " +
				unidades[produto.getUnidade()] + "  R$" + lista.getListaProduto().lastElement().getValor().toString();
		HashMap<String,String> item = new HashMap<String,String>();
		item.put( "Produto", nome);
		item.put( "Dados", dadosProduto);
		listaProdutos.add(item);

	}

	private void adicionarListaProdutos() {

		listaProdutos.clear();
		for(int x=0; x<lista.getListaProduto().size(); x++){
			Produto produto = produtoDao.getProduto(lista.getListaProduto().get(x).getProduto().getNome());
			String dadosProduto = lista.getListaProduto().get(x).getQuantidade().toString() + " " +
					unidades[produto.getUnidade()] + "  R$" + lista.getListaProduto().get(x).getValor().toString();
			HashMap<String,String> item = new HashMap<String,String>();
			item.put( "Produto", lista.getListaProduto().get(x).getProduto().getNome());
			item.put( "Dados", dadosProduto);
			listaProdutos.add(item);
		}

	}

	private void atualizarArrayProduto() {

		for(int x=0; x<lista.getListaProduto().size(); x++){
			Produto produto = new Produto();
			produto = produtoDao.getProduto(lista.getListaProduto().get(x).getProduto().getId());
			lista.getListaProduto().get(x).setProduto(produto);
			if (idLista != 0 || novoProduto == true){
				adicionarListaProdutos((lista.getListaProduto().get(x).getProduto().getNome()));
				if (novoProduto == true)
					novoProduto = false;
			}else{
				if (primeiraEdicao == false){
					adicionarListaProdutos();
				}
			}

		}
		if (idLista == 0 && primeiraEdicao==true) {
			adicionarListaProdutos();
			primeiraEdicao=false;
		}
	}

	private void atualizarLista() {

		SimpleAdapter adapter = new SimpleAdapter(
				getApplicationContext(), listaProdutos, R.layout.lista_linha,
				new String[] {"Produto", "Dados"}, new int[] {
					R.id.lista_produto_nome, R.id.lista_produto_dados});
		setListAdapter(adapter);
	}

	protected void onResume() {		
		super.onResume();

		produtoDao = new ProdutoDAO(getApplicationContext());
		produtoDao.open();
		listaDao = new ListaDAO(getApplicationContext());
		listaDao.open();
		listaProdutoDao = new ListaProdutoDAO(getApplicationContext());
		listaProdutoDao.open();

		Intent i = this.getIntent();
		idLista = i.getIntExtra("Cadastro", -1);

		if(idLista==0){
			listaProdutos.clear();
			atualizarLista();
			lista = new Lista();
			lista.setId(i.getLongExtra(ListaDAO.COLUNA_ID, 0L));
			lista = listaDao.consultarLista(lista);
			lista.setListaProduto(listaProdutoDao.consultarListaProduto(lista));
			txtTotal.setText("Total: R$" + String.valueOf(lista.getValor()));
			atualizarArrayProduto();
		}else{
			lista.getObra().setId(i.getLongExtra(ObraDAO.COLUNA_ID, 0L));
		}

		fillData();
		atualizarLista();
	}

	@Override
	protected void onPause() {		
		super.onPause();

		stopCursor(cursorProdutos);
		stopCursor(cursorLista);
		stopCursor(cursorListaProduto);
		produtoDao.close();
		listaDao.close();
		listaProdutoDao.close();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			lista = new Lista();
			primeiraEdicao=true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void stopCursor(Cursor c) {

		if (c != null && !c.isClosed()) {
			stopManagingCursor(c);
			c.close();
		}
	}
}