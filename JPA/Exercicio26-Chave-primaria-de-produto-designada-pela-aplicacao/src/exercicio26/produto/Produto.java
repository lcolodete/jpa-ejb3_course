package exercicio26.produto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import exercicio26.lance.Lance;
import exercicio26.util.Util;

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
			)
		})

@Entity
@Table(name="PRODUTO")

public class Produto
{	private Long id;
	private String nome;
	private String descricao;
	private double lanceMinimo;
	private Date dataCadastro;
	private Date dataVenda;

	//  Um produto possui lances

	private Set<Lance> lances = new HashSet<Lance>();
	
	// ********* Construtores *********

	public Produto()
	{
	}

	public Produto(Long id,
				   String nome, 
	               String descricao, 
	               double lanceMinimo, 
	               Date dataCadastro)
	{	this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.lanceMinimo = lanceMinimo;
		this.dataCadastro = dataCadastro;	
	}

	// ********* Métodos do Tipo Get *********

	
	@Id					// Para ter uma chave primária natural basta não
						// especificar uma estratégia de geração de chave. 
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

	// ********* Métodos do Tipo Set *********

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
	{	this.dataCadastro = dataCadastro;
	}
	
	public void setDataVenda(Date dataVenda)
	{	this.dataVenda = dataVenda;
	}
	
	// ********* Métodos para Associações *********

	@OneToMany(mappedBy = "produto")
	@OrderBy
	/*
	 * Sem as opções CascadeType.PERSIST e CascadeType.MERGE.
	 * 
	 * Com o atributo mappedBy. Sem ele a  JPA irá procurar  pela 
	 * tabela PRODUTO_LANCE. Com ele, ao se  tentar recuperar  um  
	 * produto  e  todos  os  seus  lances, o  join de PRODUTO  e 
	 * LANCE irá acontecer através da chave estrangeira existente
	 * em  LANCE.  
	 */
	public Set<Lance> getLances()
	{	return lances;
	}
	
	public void setLances(Set<Lance> lances)
	{	this.lances = lances;	
	}
}

