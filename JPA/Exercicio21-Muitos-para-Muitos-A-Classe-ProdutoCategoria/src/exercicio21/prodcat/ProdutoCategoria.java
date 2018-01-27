package exercicio21.prodcat;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import exercicio21.categoria.Categoria;
import exercicio21.produto.Produto;

@Entity
@Table(name="PRODUTO_CATEGORIA") 

public class ProdutoCategoria
{	
	public IdProdutoCategoria id;
	private Produto produto;
	private Categoria categoria;

	// ********* Construtores *********

	public ProdutoCategoria()
	{	
	}

	public ProdutoCategoria(long idProduto, long idCategoria)
	{	this.id = new IdProdutoCategoria(idProduto, idCategoria);
	}

	// ********* Métodos do tipo get *********

	@EmbeddedId  // Para indicar que o atributo id não  deve
	             // ser mapeado para a coluna ID no banco de
	             // dados.
	public IdProdutoCategoria getId()
	{	return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_PRODUTO",
			    insertable=false,
			    updatable=false)
	public Produto getProduto()
	{	return produto;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CATEGORIA",
			    insertable=false,
			    updatable=false)
	public Categoria getCategoria()
	{	return categoria;
	}
	
	// ********* Métodos do tipo set *********

	@SuppressWarnings("unused")
	private void setId(IdProdutoCategoria id)
	{	this.id = id;
	}

	public void setProduto(Produto produto)
	{	this.produto = produto;
	}

	public void setCategoria(Categoria categoria)
	{	this.categoria = categoria;
	}
}

