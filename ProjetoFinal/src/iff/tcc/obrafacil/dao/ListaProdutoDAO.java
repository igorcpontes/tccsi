package iff.tcc.obrafacil.dao;

import java.util.Vector;

import iff.tcc.obrafacil.model.Lista;
import iff.tcc.obrafacil.model.ListaProduto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ListaProdutoDAO extends BasicoDAO {

	public ListaProdutoDAO(Context ctx) {
		super(ctx);
	}

	public static final String TABELA_LISTA_PRODUTO = "LISTAPRODUTO";

	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_ID_LISTA = "ID_LISTA";
	public static final String COLUNA_ID_PRODUTO = "ID_PRODUTO";
	public static final String COLUNA_VALOR = "VALOR";
	public static final String COLUNA_QUANTIDADE = "QUANTIDADE";

	public static final String LISTA_PRODUTO_CREATE_TABLE = "CREATE TABLE "
			+ TABELA_LISTA_PRODUTO + "  (" + COLUNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_ID_LISTA
			+ " INTEGER NOT NULL, " + COLUNA_ID_PRODUTO
			+ " INTEGER NOT NULL, " + COLUNA_VALOR
			+ " TEXT NOT NULL, " + COLUNA_QUANTIDADE
			+ " INTEGER NOT NULL, "
			+ " FOREIGN KEY ( " + COLUNA_ID_LISTA + " )  REFERENCES "
			+ ListaDAO.TABELA_LISTA + " (" + ListaDAO.COLUNA_ID
			+ " ) ON DELETE RESTRICT ON UPDATE CASCADE,"
			+ " FOREIGN KEY ( " + COLUNA_ID_PRODUTO + " )  REFERENCES "
			+ ProdutoDAO.TABELA_PRODUTOS + " (" + ProdutoDAO.COLUNA_ID
			+ " ) ON DELETE RESTRICT ON UPDATE CASCADE);";

	public long criarListaProduto(ListaProduto listaProduto) {

		ContentValues values = deListaProdutoParaContentValues(listaProduto);
		return mDb.insert(TABELA_LISTA_PRODUTO, null, values);
	}

	public static ContentValues deListaProdutoParaContentValues(ListaProduto listaProduto) {

		ContentValues values = new ContentValues();

		values.put(COLUNA_ID_LISTA, listaProduto.getLista().getId());
		values.put(COLUNA_ID_PRODUTO, listaProduto.getProduto().getId());
		values.put(COLUNA_VALOR, String.valueOf(listaProduto.getValor()));
		values.put(COLUNA_QUANTIDADE, listaProduto.getQuantidade());

		return values;
	}

	public static ListaProduto deCursorParaListaProduto(Cursor c) {

		if (c == null || c.getCount() < 1) {
			return null;
		}

		ListaProduto listaProduto = new ListaProduto();
		listaProduto.setId(c.getLong(c.getColumnIndex(COLUNA_ID)));
		listaProduto.getLista().setId(c.getLong(c.getColumnIndex(COLUNA_ID_LISTA)));
		listaProduto.getProduto().setId(c.getLong(c.getColumnIndex(COLUNA_ID_PRODUTO)));
		listaProduto.setValor((c.getDouble(c.getColumnIndex(COLUNA_VALOR))));
		listaProduto.setQuantidade(c.getLong(c.getColumnIndex(COLUNA_QUANTIDADE)));

		return listaProduto;
	}

	public boolean atualizarListaProduto(ListaProduto listaProduto) {

		ContentValues values = deListaProdutoParaContentValues(listaProduto);
		return mDb.update(TABELA_LISTA_PRODUTO, values, COLUNA_ID + "=?",
				new String[] { String.valueOf(listaProduto.getId()) }) > 0;
	}

	public boolean removerListaProduto(long idListaProduto) {

		return mDb.delete(TABELA_LISTA_PRODUTO, COLUNA_ID + "=?",
				new String[] { String.valueOf(idListaProduto) }) > 0;
	}

	public Vector<ListaProduto> consultarListaProduto(Lista lista) {

		Vector<ListaProduto> VectorListaProduto = new Vector<ListaProduto>();

		Cursor mCursor =
				mDb.query(true, TABELA_LISTA_PRODUTO, null, COLUNA_ID_LISTA + "=?",
						new String[] { String.valueOf(lista.getId()) }, null, null,
						null, null);

		if (mCursor != null) {
			System.out.println(VectorListaProduto.size());
			mCursor.moveToFirst();
			do {
				ListaProduto listaProduto = new ListaProduto();
				listaProduto.setId(mCursor.getLong(mCursor.getColumnIndex(COLUNA_ID)));
				listaProduto.getLista().setId(mCursor.getLong(mCursor.getColumnIndex(COLUNA_ID_LISTA)));
				listaProduto.getProduto().setId(mCursor.getLong(mCursor.getColumnIndex(COLUNA_ID_PRODUTO)));
				listaProduto.setValor((mCursor.getDouble(mCursor.getColumnIndex(COLUNA_VALOR))));
				listaProduto.setQuantidade(mCursor.getLong(mCursor.getColumnIndex(COLUNA_QUANTIDADE)));
				VectorListaProduto.add(listaProduto);
				System.out.println(VectorListaProduto.size());
			} while (mCursor.moveToNext());
		}

		return VectorListaProduto;
	}
}