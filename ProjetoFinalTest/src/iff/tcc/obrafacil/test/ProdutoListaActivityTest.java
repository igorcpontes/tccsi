package iff.tcc.obrafacil.test;

import iff.tcc.obrafacil.ProdutoListaActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ProdutoListaActivityTest extends
ActivityInstrumentationTestCase2<ProdutoListaActivity> {

	private ProdutoListaActivity produtoListaActivity;
	private Spinner spinnerCategoria;
	private ListView mList;
	private SimpleCursorAdapter mAdapter;

	public ProdutoListaActivityTest() {
		super("iff.tcc.obrafacil", ProdutoListaActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		produtoListaActivity = getActivity();

		spinnerCategoria = (Spinner) produtoListaActivity.findViewById(iff.tcc.obrafacil.R.id.sp_categorias);
		mList = (ListView) produtoListaActivity.findViewById(android.R.id.list);  
	    mAdapter = (SimpleCursorAdapter) mList.getAdapter();

	}

	public void testPreConditions() {
		assertTrue(spinnerCategoria != null);
		assertTrue(mList != null);
		assertTrue(mAdapter != null);
	}

	public void testTrocaDeCategoriaSpinner() {

		testPreConditions();
		
		LinearLayout item = (LinearLayout)(mList.getChildAt(0));
	    TextView view = (TextView) (item.findViewById(iff.tcc.obrafacil.R.id.linha1));
	    String nomeDoProduto = view.getText().toString();
	    view = (TextView) (item.findViewById(iff.tcc.obrafacil.R.id.linha2));
	    String descricaoDoProduto = view.getText().toString();
		
	    assertEquals("Fio 2.5MM Flexível", nomeDoProduto);
	    assertEquals("Fio Elétrico", descricaoDoProduto);

		produtoListaActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				spinnerCategoria.requestFocus();
			}
		});

		this.sendKeys("DPAD_CENTER DPAD_DOWN DPAD_DOWN DPAD_CENTER");
		
		item = (LinearLayout)(mList.getChildAt(0));
	    view = (TextView) (item.findViewById(iff.tcc.obrafacil.R.id.linha1));
	    nomeDoProduto = view.getText().toString();
	    view = (TextView) (item.findViewById(iff.tcc.obrafacil.R.id.linha2));
	    descricaoDoProduto = view.getText().toString();
	    
	    assertEquals("Lavatório", nomeDoProduto);
	    assertEquals("Formacril", descricaoDoProduto);
	}	
}