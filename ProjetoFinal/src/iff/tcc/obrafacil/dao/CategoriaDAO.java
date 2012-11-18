package iff.tcc.obrafacil.dao;

import java.util.ArrayList;

import iff.tcc.obrafacil.model.Categoria;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CategoriaDAO extends BasicoDAO {

	public CategoriaDAO(Context ctx) {
		super(ctx);
	}

	public static final String TABELA_CATEGORIA = "CATEGORIA";

	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "NOME";

	public static final String CATEGORIA_CREATE_TABLE = "CREATE TABLE "
			+ TABELA_CATEGORIA + "  (" + COLUNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_NOME
			+ " text not null);";

	public static void createDummyData(SQLiteDatabase db) {

		ContentValues values = new ContentValues();

		values.put(COLUNA_NOME, "Todas");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Elétrica e Iluminação");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Louças");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Madeiras e Forros");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Material Básico");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Metais e Acessórios");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Pisos e Azulejos");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Portas e Janelas");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Tintas e acessórios");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Tubos e Conexões");
		db.insert(TABELA_CATEGORIA, null, values);
		values.clear();
	}

	public long criarCategoria(Categoria categoria) {

		ContentValues values = deCategoriaParaContentValues(categoria);
		return mDb.insert(TABELA_CATEGORIA, null, values);
	}

	public static ContentValues deCategoriaParaContentValues(Categoria categoria) {

		ContentValues values = new ContentValues();
		values.put(COLUNA_NOME, categoria.getNome());
		return values;
	}

	public static Categoria deCursorParaCategoria(Cursor c) {

		if (c == null || c.getCount() < 1) {
			return null;
		}

		Categoria categoria = new Categoria();
		categoria.setId(c.getLong(c.getColumnIndex(COLUNA_ID)));
		categoria.setNome(c.getString(c.getColumnIndex(COLUNA_NOME)));

		return categoria;
	}

	public static boolean removerCategoria(long idCategoria) {

		return mDb.delete(TABELA_CATEGORIA, COLUNA_ID + "=?",
				new String[] { String.valueOf(idCategoria) }) > 0;
	}

	public boolean removerCategoria(Categoria categoria) {

		return mDb.delete(TABELA_CATEGORIA, COLUNA_ID + "=? AND "
				+ COLUNA_NOME + "=?",
				new String[] { String.valueOf(categoria.getId()), categoria.getNome() }) > 0;
	}

	public static Cursor consultarTodasCategorias() {

		return mDb.query(TABELA_CATEGORIA, new String[] { COLUNA_ID,
				COLUNA_NOME }, null, null, null, null, null);
	}

	public Cursor consultarCategoria(long idCategoria) throws SQLException {

		Cursor mCursor =
				mDb.query(true, TABELA_CATEGORIA, new String[] { COLUNA_ID,
						COLUNA_NOME }, COLUNA_ID + "=?",
						new String[] { String.valueOf(idCategoria) }, null, null, null,
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean atualizarCategoria(Categoria categoria) {

		ContentValues values = new ContentValues();
		values.put(COLUNA_NOME, categoria.getNome());
		return mDb.update(TABELA_CATEGORIA, values, COLUNA_ID + "=?",
				new String[] { String.valueOf(categoria.getId()) }) > 0;
	}

	public static ArrayList<String> consultarNomeDeTodasCategorias() {

		ArrayList<String> array = new ArrayList<String>();

		Cursor mCursor =
				mDb.query(true, TABELA_CATEGORIA, new String[] { COLUNA_NOME },
						null, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
			do {
				array.add(mCursor.getString(mCursor.getColumnIndex(COLUNA_NOME)));
			} while (mCursor.moveToNext());
		}else{
			return null;
		}
		return array;
	}
}