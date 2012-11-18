package iff.tcc.obrafacil.dao;

import iff.tcc.obrafacil.model.Produto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProdutoDAO extends BasicoDAO {

	public ProdutoDAO(Context ctx) {
		super(ctx);
	}

	public static final String TABELA_PRODUTOS = "PRODUTOS";

	public static final String COLUNA_ID = "_id";
	public static final String COLUNA_NOME = "NOME";
	public static final String COLUNA_DESCRICAO = "DESCRICAO";
	public static final String COLUNA_UNIDADE = "UNIDADE";
	public static final String COLUNA_ID_CATEGORIA = "ID_CATEGORIA";

	public static final String PRODUTOS_CREATE_TABLE = "CREATE TABLE "
			+ TABELA_PRODUTOS + "  (" + COLUNA_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUNA_NOME
			+ " TEXT NOT NULL," + COLUNA_DESCRICAO + " TEXT NOT NULL,"
			+ COLUNA_UNIDADE + " INTEGER NOT NULL," + COLUNA_ID_CATEGORIA
			+ " INTEGER NOT NULL,"
			+ " FOREIGN KEY ( " + COLUNA_ID_CATEGORIA + " )  REFERENCES "
			+ CategoriaDAO.TABELA_CATEGORIA + " (" + CategoriaDAO.COLUNA_ID
			+ " ) ON DELETE RESTRICT ON UPDATE CASCADE);";

	public static void createDummyData(SQLiteDatabase db) {

		ContentValues values = new ContentValues();

		values.put(COLUNA_NOME, "Fio 2.5MM Flexível");
		values.put(COLUNA_DESCRICAO, "Fio Elétrico");
		values.put(COLUNA_UNIDADE, 2L);
		values.put(COLUNA_ID_CATEGORIA, 1L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Lavatório");
		values.put(COLUNA_DESCRICAO, "Formacril");
		values.put(COLUNA_UNIDADE, 7L);
		values.put(COLUNA_ID_CATEGORIA, 2L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Forro PVC");
		values.put(COLUNA_DESCRICAO, "Maracanã");
		values.put(COLUNA_UNIDADE, 3L);
		values.put(COLUNA_ID_CATEGORIA, 3L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Tijolos 6 furos");
		values.put(COLUNA_DESCRICAO, "Tijolos");
		values.put(COLUNA_UNIDADE, 5L);
		values.put(COLUNA_ID_CATEGORIA, 4L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Areia");
		values.put(COLUNA_DESCRICAO, "Areia");
		values.put(COLUNA_UNIDADE, 4L);
		values.put(COLUNA_ID_CATEGORIA, 4L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Torneira");
		values.put(COLUNA_DESCRICAO, "Torneira");
		values.put(COLUNA_UNIDADE, 7L);
		values.put(COLUNA_ID_CATEGORIA, 5L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Pastilha");
		values.put(COLUNA_DESCRICAO, "Crystalcor");
		values.put(COLUNA_UNIDADE, 0L);
		values.put(COLUNA_ID_CATEGORIA, 6L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Porta");
		values.put(COLUNA_DESCRICAO, "Porta lisa");
		values.put(COLUNA_UNIDADE, 7L);
		values.put(COLUNA_ID_CATEGORIA, 7L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Tinta plástica");
		values.put(COLUNA_DESCRICAO, "Tinta azul");
		values.put(COLUNA_UNIDADE, 1L);
		values.put(COLUNA_ID_CATEGORIA, 8L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();

		values.put(COLUNA_NOME, "Bucha redução");
		values.put(COLUNA_DESCRICAO, "Conexão");
		values.put(COLUNA_UNIDADE, 7L);
		values.put(COLUNA_ID_CATEGORIA, 9L);
		db.insert(TABELA_PRODUTOS, null, values);
		values.clear();
	}

	public long criarProduto(Produto produto) {

		ContentValues values = deProdutoParaContentValues(produto);
		return mDb.insert(TABELA_PRODUTOS, null, values);
	}

	public static ContentValues deProdutoParaContentValues(Produto produto) {

		ContentValues values = new ContentValues();
		values.put(COLUNA_NOME, produto.getNome());
		values.put(COLUNA_DESCRICAO, produto.getDescricao());
		values.put(COLUNA_ID_CATEGORIA, produto.getCategoria().getId());
		values.put(COLUNA_UNIDADE, produto.getUnidade());
		return values;
	}

	public Produto getProduto(long idProduto) {

		Cursor c = consultarProduto(idProduto);
		Produto produto = deCursorParaProduto(c);
		c.close();
		return produto;
	}

	public Produto getProduto(String nome) {

		Cursor c = consultarProdutosPorNome(nome);
		Produto produto = deCursorParaProduto(c);
		c.close();
		return produto;
	}

	public Produto deCursorParaProduto(Cursor c) {

		if (c == null || c.getCount() < 1) {
			return null;
		}

		Produto produto = new Produto();
		produto.setDescricao(c.getString(c.getColumnIndex(COLUNA_DESCRICAO)));
		produto.setId(c.getLong(c.getColumnIndex(COLUNA_ID)));
		produto.getCategoria().setId(c.getLong(c.getColumnIndex(COLUNA_ID_CATEGORIA)));
		produto.setNome(c.getString(c.getColumnIndex(COLUNA_NOME)));
		produto.setUnidade(c.getInt(c.getColumnIndex(COLUNA_UNIDADE)));

		return produto;
	}

	public boolean atualizarProduto(Produto produto) {

		ContentValues values = deProdutoParaContentValues(produto);
		return mDb.update(TABELA_PRODUTOS, values, COLUNA_ID + "=?",
				new String[] { String.valueOf(produto.getId()) }) > 0;
	}

	public static boolean removerProduto(long idProduto) {

		return mDb.delete(TABELA_PRODUTOS, COLUNA_ID + "=?",
				new String[] { String.valueOf(idProduto) }) > 0;
	}

	public Cursor consultarTodosProdutos() {

		return mDb
				.query(TABELA_PRODUTOS, null, null, null, null, null, null);
	}

	public Cursor consultarProduto(long idProduto) throws SQLException {

		Cursor mCursor =
				mDb.query(true, TABELA_PRODUTOS, null, COLUNA_ID + "=?",
						new String[] { String.valueOf(idProduto) }, null, null,
						null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor consultarProdutosPorCategoria(long idCategoria)
			throws SQLException {

		Cursor mCursor =
				mDb.query(true, TABELA_PRODUTOS, null, COLUNA_ID_CATEGORIA + "=?",
						new String[] { String.valueOf(idCategoria) }, null, null, null,
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor consultarProdutosPorNome(String nome) {

		Cursor mCursor =
				mDb.query(true, TABELA_PRODUTOS, null, COLUNA_NOME + "=?",
						new String[] { String.valueOf(nome) }, null, null, null,
						null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public long getNumeroDeProdutos() {

		return DatabaseUtils.queryNumEntries(mDb, TABELA_PRODUTOS);
	}

	public String[] deCursorParaArrayProduto(Cursor cursorProdutos, String coluna) {

		String[] arrayProdutos;

		if (cursorProdutos != null) {
			int i=0;
			arrayProdutos = new String[cursorProdutos.getCount()];
			cursorProdutos.moveToFirst();
			do {
				arrayProdutos[i] = cursorProdutos.getString(cursorProdutos.getColumnIndex(coluna));
				i++;
			} while (cursorProdutos.moveToNext());
		}else{
			arrayProdutos = null;
		}

		return arrayProdutos;
	}
}