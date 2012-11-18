package iff.tcc.obrafacil.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import iff.tcc.obrafacil.model.Lista;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class ListaDAO extends BasicoDAO {

	public ListaDAO(Context ctx) {
		super(ctx);
	}

	public static final String TABELA_LISTA = "LISTA";

	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_DATA = "DATA";
	public static final String COLUNA_VALOR = "VALOR";
	public static final String COLUNA_ID_OBRA = "ID_OBRA";

	public static final String LISTA_CREATE_TABLE = "CREATE TABLE "
			+ TABELA_LISTA + "  (" + COLUNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_DATA
			+ " INTEGER NOT NULL," + COLUNA_ID_OBRA
			+ " INTEGER NOT NULL," + COLUNA_VALOR
			+ " TEXT NOT NULL,"
			+ " FOREIGN KEY ( " + COLUNA_ID_OBRA + " )  REFERENCES "
			+ ObraDAO.TABELA_OBRA + " (" + ObraDAO.COLUNA_ID
			+ " ) ON DELETE RESTRICT ON UPDATE CASCADE);";

	public long criarLista(Lista lista) {

		ContentValues values = deListaParaContentValues(lista);
		return mDb.insert(TABELA_LISTA, null, values);
	}

	public ContentValues deListaParaContentValues(Lista lista) {

		ContentValues values = new ContentValues();

		values.put(COLUNA_DATA, lista.getData().getTime());
		values.put(COLUNA_VALOR, String.valueOf(lista.getValor()));
		values.put(COLUNA_ID_OBRA, lista.getObra().getId());

		return values;
	}

	public Lista deCursorParaLista(Cursor c) {

		if (c == null || c.getCount() < 1) {
			return null;
		}

		Lista lista = new Lista();
		lista.setId(c.getLong(c.getColumnIndex(COLUNA_ID)));
		lista.setValor(Double.valueOf(c.getString((c.getColumnIndex(COLUNA_VALOR)))));
		lista.setData(new Date(c.getLong(c.getColumnIndex(COLUNA_DATA))));		
		lista.getObra().setId(c.getLong(c.getColumnIndex(COLUNA_ID_OBRA)));

		return lista;
	}

	public boolean atualizarLista(Lista lista) {

		ContentValues values = deListaParaContentValues(lista);
		return mDb.update(TABELA_LISTA, values, COLUNA_ID + "=?",
				new String[] { String.valueOf(lista.getId()) }) > 0;
	}

	public boolean removerLista(long idLista) {

		return mDb.delete(TABELA_LISTA, COLUNA_ID + "=?",
				new String[] { String.valueOf(idLista) }) > 0;
	}

	public Lista consultarLista(Lista lista) {

		Cursor mCursor =
				mDb.query(true, TABELA_LISTA, null, COLUNA_ID + "=?",
						new String[] { String.valueOf(lista.getId()) }, null, null,
						null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return deCursorParaLista(mCursor);
	}

	public Cursor consultarListasPorObra(long idObra)
			throws SQLException {

		Cursor mCursor =
				mDb.query(true, TABELA_LISTA, null, COLUNA_ID_OBRA + "=?",
						new String[] { String.valueOf(idObra) }, null, null, null,
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor consultarListasPorData(Date data)
			throws SQLException {

		Cursor mCursor =
				mDb.query(true, TABELA_LISTA, null, COLUNA_DATA + "=?",
						new String[] { String.valueOf(data.getTime()) }, null, null, null,
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Vector<Lista> deCursorParaVectorLista(Cursor cursorLista) {

		Vector<Lista> vectorLista = new Vector<Lista>();

		if (cursorLista == null || cursorLista.getCount() < 1) {
			return null;
		}else{	
			do {
				Lista lista = new Lista();	
				lista.setId(cursorLista.getLong(cursorLista.getColumnIndex(COLUNA_ID)));	
				lista.setValor(Double.valueOf(cursorLista.getString(cursorLista.getColumnIndex(COLUNA_VALOR))));					
				lista.setData(new Date(cursorLista.getLong(cursorLista.getColumnIndex(COLUNA_DATA))));
				lista.getObra().setId(cursorLista.getLong(cursorLista.getColumnIndex(COLUNA_ID_OBRA)));
				vectorLista.add(lista);
			} while (cursorLista.moveToNext());
			return vectorLista;	
		}
	}

	public ArrayList<HashMap<String,String>> deCursorParaArrayList(Cursor cursorLista) {

		ArrayList<HashMap<String,String>> lista = new ArrayList<HashMap<String,String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

		if (cursorLista == null || cursorLista.getCount() < 1) {
			return null;
		}else{
			cursorLista.moveToFirst();
			do {
				HashMap<String,String> item = new HashMap<String,String>();
				item.put( "Data", String.valueOf(sdf.format(new Date(
						cursorLista.getLong(cursorLista.getColumnIndex(COLUNA_DATA))))));
				item.put( "Valor", cursorLista.getString(cursorLista.getColumnIndex(COLUNA_VALOR)));
				lista.add( item );
			} while (cursorLista.moveToNext());
			return lista;	
		}
	}
}