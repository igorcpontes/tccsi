package iff.tcc.obrafacil.model;

public class Produto {	
	private Long id;
	private String nome;
	private String descricao;
	private int unidade;
	private Categoria categoria;

	public Produto(){
		this.id = null;
		this.nome = null;
		this.descricao = null;
		this.unidade = -1;
		this.categoria = new Categoria();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public int getUnidade() {
		return unidade;
	}

	public void setUnidade(int unidade) {
		this.unidade = unidade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
}