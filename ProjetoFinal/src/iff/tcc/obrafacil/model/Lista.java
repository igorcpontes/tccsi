package iff.tcc.obrafacil.model;

import java.util.Date;
import java.util.Vector;

public class Lista {	
	private Long id;
	private Obra obra;
	private Date data;
	private Double valor;
	private Vector<ListaProduto> listaProduto;

	public Lista(){
		this.id = null;
		this.obra = new Obra();
		this.data = null;
		this.valor = 0.0;
		this.listaProduto = new Vector<ListaProduto>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public Vector<ListaProduto> getListaProduto() {
		return listaProduto;
	}

	public void setListaProduto(Vector<ListaProduto> listaProduto) {
		this.listaProduto = listaProduto;
	}
}