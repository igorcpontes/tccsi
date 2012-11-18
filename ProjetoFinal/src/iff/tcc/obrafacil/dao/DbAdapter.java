package iff.tcc.obrafacil.dao;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	private static final String TAG = "DbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DB_NAME = "ObraFacil";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			if (!db.isReadOnly()) {
				db.execSQL("PRAGMA foreign_keys=ON;");
			}
		}

		DatabaseHelper(Context context) {

			super(context, DB_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {

			db.execSQL(CategoriaDAO.CATEGORIA_CREATE_TABLE);
			db.execSQL(ProdutoDAO.PRODUTOS_CREATE_TABLE);
			db.execSQL(ObraDAO.OBRA_CREATE_TABLE);
			db.execSQL(ListaDAO.LISTA_CREATE_TABLE);
			db.execSQL(ListaProdutoDAO.LISTA_PRODUTO_CREATE_TABLE);

			CategoriaDAO.createDummyData(db);
			ProdutoDAO.createDummyData(db);
			Log.w("DbAdapter", "DB criado com sucesso!");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

			Log.w(TAG, "Atualizando o banco de dados da versão " + oldVersion
					+ " para " + newVersion
					+ ", todos os dados serão perdidos!");
			db.execSQL("DROP TABLE IF EXISTS " + CategoriaDAO.TABELA_CATEGORIA);
			db.execSQL("DROP TABLE IF EXISTS " + ProdutoDAO.TABELA_PRODUTOS);
			db.execSQL("DROP TABLE IF EXISTS " + ObraDAO.TABELA_OBRA);
			db.execSQL("DROP TABLE IF EXISTS " + ListaDAO.TABELA_LISTA);
			db.execSQL("DROP TABLE IF EXISTS " + ListaProdutoDAO.TABELA_LISTA_PRODUTO);
			onCreate(db);
		}
	}

	public DbAdapter(Context ctx) {

		this.mCtx = ctx;
	}

	public  SQLiteDatabase open() throws SQLException {

		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return mDb;
	}

	public void close() {

		mDbHelper.close();
		mDb.close();
	}
}