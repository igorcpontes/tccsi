package iff.tcc.obrafacil.dao;

import iff.tcc.obrafacil.model.Obra;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class ObraDAO extends BasicoDAO {

	public ObraDAO(Context ctx) {
		super(ctx);
	}

	public static final String TABELA_OBRA = "OBRA";

	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "NOME";
	public static final String COLUNA_ENDERECO = "ENDERECO";

	public static final String OBRA_CREATE_TABLE = "CREATE TABLE "
			+ TABELA_OBRA + "  (" + COLUNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_NOME
			+ " TEXT NOT NULL, " + COLUNA_ENDERECO + " TEXT NOT NULL);";

	public long criarObra(Obra obra) {

		ContentValues values = deObraParaContentValues(obra);
		return mDb.insert(TABELA_OBRA, null, values);
	}

	public static ContentValues deObraParaContentValues(Obra obra) {

		ContentValues values = new ContentValues();
		values.put(COLUNA_ID, obra.getId());
		values.put(COLUNA_NOME, obra.getNome());
		values.put(COLUNA_ENDERECO, obra.getEndereco());
		return values;
	}

	public static boolean removerObra(long idObra) {

		return mDb.delete(TABELA_OBRA, COLUNA_ID + "=?",
				new String[] { String.valueOf(idObra) }) > 0;
	}

	public Cursor consultarTodasObras() {

		return mDb.query(TABELA_OBRA, new String[] { COLUNA_ID, COLUNA_NOME,
				COLUNA_ENDERECO }, null, null, null, null, null);
	}

	public boolean atualizarObra(Obra obra) {

		ContentValues values = new ContentValues();
		values.put(COLUNA_NOME, obra.getNome());
		values.put(COLUNA_ENDERECO, obra.getEndereco());

		return mDb.update(TABELA_OBRA, values, COLUNA_ID + "=?",
				new String[] { String.valueOf(obra.getId()) }) > 0;
	}

	public static String consultarNomeDaObra(Long idObra) {

		Cursor mCursor =
				mDb.query(true, TABELA_OBRA, new String[] {	COLUNA_NOME }, COLUNA_ID + "=?",
						new String[] { String.valueOf(idObra) }, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
			return mCursor.getString(mCursor.getColumnIndex(COLUNA_NOME));
		}else{
			return "Sem Nome";
		}
	}
}