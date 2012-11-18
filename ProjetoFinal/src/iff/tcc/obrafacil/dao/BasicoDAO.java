package iff.tcc.obrafacil.dao;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class BasicoDAO {
	protected static SQLiteDatabase mDb;
	protected static Context context;
	private static DbAdapter dbAdapter;

	private static int contador;

	public BasicoDAO(Context ctx) {

		context = ctx;
	}

	private synchronized static int numeroConexoes(int i) {

		contador = contador + i;
		return contador;
	}

	/**
	 * Utiliza ctx para instânciar uma base de dados.
	 *            Abre o banco!
	 */
	public synchronized void open() {

		if (mDb == null || (mDb != null && !mDb.isOpen())) {
			dbAdapter = new DbAdapter(context);
			mDb = dbAdapter.open();
		}
		numeroConexoes(+1);
	}

	/**
	 * Fecha o acesso a uma base de dados.
	 */
	public synchronized void close() {

		if (mDb != null && mDb.isOpen() && (numeroConexoes(0) == 1)) {
			dbAdapter.close();
			mDb.close();
		}
		numeroConexoes(-1);
	}
}