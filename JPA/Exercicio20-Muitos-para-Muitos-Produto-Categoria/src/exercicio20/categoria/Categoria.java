package exercicio20.categoria;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exercicio20.produto.Produto;

@NamedQueries(
		{	@NamedQuery
			(	name = "Categoria.recuperaUmaCategoriaEProdutos",
				query = "select c from Categoria c left outer join fetch c.produtos where c.id = ?"
			),
			@NamedQuery
			(	name = "Categoria.recuperaListaDeCategorias",
				query = "select c from Categoria c order by id"
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
	private Set<Produto> produtos = new HashSet<Produto>();

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

	@ManyToMany(mappedBy="categorias")
	@OrderBy(value="id")
	// Em um mapeamento muitos para muitos não basta especificar @OrderBy
	// É preciso fornecer o  nome de uma  propriedade da classe  Produto,
	// caso contrário a cláusula SQL  Order By não será  acrescentada  no
	// comando SQL. No caso acima, utilizamos a propriedade "id".
	public Set<Produto> getProdutos()
	{	return produtos;
	}
	
	public void setProdutos(Set<Produto> produtos)
	{	this.produtos = produtos;	
	}
}
