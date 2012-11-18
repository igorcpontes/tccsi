package iff.tcc.obrafacil.test;

import iff.tcc.obrafacil.ObraCadastroActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;

public class ObraCadastroActivityTest extends
ActivityInstrumentationTestCase2<ObraCadastroActivity> {

	private ObraCadastroActivity obraCadastroActivity;
	private TextView textViewTitulo;
	private EditText editTextNome, editTextEndereco;
	private String titulo;

	public ObraCadastroActivityTest() {
		super("iff.tcc.obrafacil", ObraCadastroActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		obraCadastroActivity = getActivity();

		textViewTitulo = (TextView) obraCadastroActivity.findViewById(iff.tcc.obrafacil.R.obra_cadastro.titulo);
		titulo = obraCadastroActivity.getString(iff.tcc.obrafacil.R.string.Titulo);
		editTextNome = (EditText) obraCadastroActivity.findViewById(iff.tcc.obrafacil.R.obra_cadastro.nome);
		editTextEndereco = (EditText) obraCadastroActivity.findViewById(iff.tcc.obrafacil.R.obra_cadastro.endereco);

	}

	public void testPreConditions() {
		assertTrue(textViewTitulo != null);
		assertTrue(titulo != null);
		assertTrue(editTextNome.getText().toString().equals(""));
		assertTrue(editTextEndereco.getText().toString().equals(""));
	}

	public void testViewTextTitulo() {
		testPreConditions();
		assertEquals(titulo,textViewTitulo.getText());
	}

	public void testDePeenchimentoDeEditTexts() {

		testPreConditions();

		obraCadastroActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				editTextNome.requestFocus();
			}
		});

		this.sendKeys("N O M E");
		this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		this.sendKeys("E N D E R E C O");

		assertEquals("nome", editTextNome.getText().toString());
		assertEquals("endereco", editTextEndereco.getText().toString());
	}	
}