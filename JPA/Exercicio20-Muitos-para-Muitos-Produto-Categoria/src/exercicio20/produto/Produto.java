package exercicio20.produto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import exercicio20.categoria.Categoria;
import exercicio20.lance.Lance;
import exercicio20.util.AplicacaoException;
import exercicio20.util.Util;

@NamedQueries(
		{	@NamedQuery
			(	name = "Produto.recuperaUmProdutoELances",
				query = "select p from Produto p left outer join fetch p.lances where p.id = ?"
			),
			@NamedQuery
			(	name = "Produto.recuperaListaDeProdutos",
				query = "select p from Produto p order by id"
			),
			@NamedQuery
			(	name = "Produto.recuperaConjuntoDeProdutosELances",
				query = "select p from Produto p left outer join fetch p.lances order by p.id asc"
			),
			@NamedQuery
			(	name = "Produto.recuperaUltimoLance",
				query = "select l from Lance l where l.produto = ? order by l.id desc"
			),
			@NamedQuery
			(	name = "Produto.recuperaUmProdutoELanceVencedor",
				query = "select p from Produto p left outer join fetch p.lanceVencedor where p.id = ?"
			),
			@NamedQuery
			(	name = "Produto.recuperaUmProdutoECategorias",
				query = "select p from Produto p left outer join fetch p.categorias where p.id = ?"
			),
			@NamedQuery
			(	name = "Produto.recuperaUmProdutoQuePossuiUmDeterminadoLanceVencedor",
				query = "select p from Produto p where p.lanceVencedor.id = ?"
			)
		})
			
@Entity
@Table(name="PRODUTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PRODUTO",
		           allocationSize=1)

public class Produto
{	
	private Long id;
	private String nome;
	private String descricao;
	private double lanceMinimo;
	private Date dataCadastro;
	private Date dataVenda;
    private Lance lanceVencedor;
	private Set<Lance> lances = new HashSet<Lance>();
	private Set<Categoria> categorias = new HashSet<Categoria>();

	// ********* Construtores *********

	public Produto()
	{
	}

	public Produto(String nome, 
	               String descricao, 
	               double lanceMinimo, 
	               Date dataCadastro)
	{	this.nome = nome;
		this.descricao = descricao;
		this.lanceMinimo = lanceMinimo;
		this.dataCadastro = dataCadastro;	
	}

	// ********* M�todos do Tipo Get *********

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
	
	@Column(nullable = false)
	public String getDescricao()
	{	return descricao;
	}
	
	@Column(name="LANCE_MINIMO", nullable = false)
	public double getLanceMinimo()
	{	return lanceMinimo;
	}
	
	@Transient
	public String getLanceMinimoMasc()
	{	return Util.doubleToStr(lanceMinimo);
	}

	@Column(name="DATA_CADASTRO", nullable = false)
	public Date getDataCadastro()
	{	return dataCadastro;
	}
	
	@Transient
	public String getDataCadastroMasc()
	{	return Util.dateToStr(dataCadastro);
	}

	@Column(name="DATA_VENDA")
	public Date getDataVenda()
	{	return dataVenda;
	}
	
	@Transient
	public String getDataVendaMasc()
	{	return Util.dateToStr(dataVenda);
	}

	// ********* M�todos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setNome(String nome)
	{	this.nome = nome;
	}
	
	public void setDescricao(String descricao)
	{	this.descricao = descricao;
	}
	
	public void setLanceMinimo(double lanceMinimo)
	{	this.lanceMinimo = lanceMinimo;
	}
	
	public void setDataCadastro(Date dataCadastro)
		throws AplicacaoException
	{	this.dataCadastro = dataCadastro;	
	}
	
	public void setDataVenda(Date dataVenda)
	{	this.dataVenda = dataVenda;
	}
	
	// ********* M�todos para Associa��es *********

	@OneToMany (mappedBy = "produto")
	@OrderBy
	public Set<Lance> getLances()
	{	return lances;
	}
	
	public void setLances(Set<Lance> lances)
	{	this.lances = lances;	
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LANCE_VENCEDOR_ID")
	public Lance getLanceVencedor()
	{	return lanceVencedor;
	}
	
	public void setLanceVencedor(Lance lanceVencedor)
	{	this.lanceVencedor = lanceVencedor;	
	}

	@ManyToMany
	@JoinTable(name="PRODUTO_CATEGORIA",
			   joinColumns = {@JoinColumn(name="ID_PRODUTO")},
			   inverseJoinColumns = {@JoinColumn(name = "ID_CATEGORIA")})
	@OrderBy(value="id")
	// Em um mapeamento muitos para muitos n�o basta especificar @OrderBy
	// � preciso fornecer o nome de uma propriedade da classe  Categoria,
	// caso contr�rio a cl�usula SQL  Order By n�o ser�  acrescentada  no
	// comando SQL. No caso acima, utilizamos a propriedade "id".
	public Set<Categoria> getCategorias()
	{	return categorias;
	}
	
	public void setCategorias(Set<Categoria> categorias)
	{	this.categorias = categorias;	
	}
}

