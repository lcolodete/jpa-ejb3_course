package exercicio21.categoria;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exercicio21.prodcat.ProdutoCategoria;

@NamedQueries(
		{	@NamedQuery
			(	name = "Categoria.recuperaUmaCategoriaEProdutos",
				query = "select c from Categoria c left outer join fetch c.produtoCategorias pc " +
				        "inner join fetch pc.produto where c.id = ?"
			),
			@NamedQuery
			(	name = "Categoria.recuperaListaDeCategoriasEProdutos",
				query = "select distinct c from Categoria c left join fetch c.produtoCategorias pc " +
				        "inner join fetch pc.produto order by c.id"
			)
		})

@Entity
@Table(name="CATEGORIA")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CATEGORIA",
		           allocationSize=1)

public class Categoria
{	private Long id;
	private String nome;
	private Set<ProdutoCategoria> produtoCategorias = new HashSet<ProdutoCategoria>();

	// ********* Construtores *********

	public Categoria()
	{
	}

	public Categoria(String nome)
	{	this.nome = nome;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}

	@Column(nullable = false)
	public String getNome()
	{	return nome;
	}

	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setNome(String nome)
	{	this.nome = nome;
	}

	// *********************************************

/* ==> */	
	@OneToMany (mappedBy = "categoria")
	@OrderBy("produto")
	// Cada Categoria possui um conjunto de  objetos do tipo  ProdutoCategoria
	// e desejamos  que estes  objetos  sejam exibidos  ordenados por Produto, 
	// daí o @OrderBy("produto").
	public Set<ProdutoCategoria> getProdutoCategorias()
	{	return produtoCategorias;
	}

/* ==> */	
	public void setProdutoCategorias(Set<ProdutoCategoria> produtoCategorias)
	{	this.produtoCategorias = produtoCategorias;
	}
}
