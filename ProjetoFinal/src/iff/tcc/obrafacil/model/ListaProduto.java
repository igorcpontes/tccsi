package iff.tcc.obrafacil.model;

public class ListaProduto {	
	private Long id;
	private Lista lista;
	private Produto produto;
	private Double valor;
	private Long quantidade;

	public ListaProduto(){
		this.id = null;
		this.lista = new Lista();
		this.produto = new Produto();
		this.valor = null;
		this.quantidade = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Lista getLista() {
		return lista;
	}

	public void setLista(Lista lista) {
		this.lista = lista;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}
}