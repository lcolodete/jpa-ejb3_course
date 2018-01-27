package exercicio21.prodcat;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class IdProdutoCategoria implements Serializable
{	
	private final static long serialVersionUID = 1;	

	private long idProduto;
	private long idCategoria;

	public IdProdutoCategoria()
	{
	}

	public IdProdutoCategoria(long idProduto, long idCategoria)
	{	this.idProduto = idProduto;
		this.idCategoria = idCategoria;
	}
	
	@SuppressWarnings("unused")
	@Column(name="ID_PRODUTO")
	public long getIdProduto()
	{	return idProduto;
	}

	@SuppressWarnings("unused")
	@Column(name="ID_CATEGORIA")
	public long getIdCategoria()
	{	return idCategoria;
	}

	public void setIdProduto(long idProduto)
	{	this.idProduto = idProduto;
	}

	public void setIdCategoria(long idCategoria)
	{	this.idCategoria = idCategoria;
	}
}
