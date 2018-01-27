package exercicio12.produto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import exercicio12.lance.Lance;
import exercicio12.util.AplicacaoException;
import exercicio12.util.Util;

@Entity
@Table(name="PRODUTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PRODUTO",
		           allocationSize=1)

public class Produto
{	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	private Long id;

	@Column(nullable = false)
	private String nome;
	
	@Column(nullable = false)
	private String descricao;
	
	@Column(name="LANCE_MINIMO", nullable = false)
	private double lanceMinimo;
	
	@Column(name="DATA_CADASTRO", nullable = false)
	private Date dataCadastro;
	
	@Column(name="DATA_VENDA")
	private Date dataVenda;


	/* 
	 * Sem as opções CascadeType.PERSIST e CascadeType.MERGE.
	 * 
	 * Com o atributo mappedBy. Sem ele a  JPA irá procurar  pela 
	 * tabela PRODUTO_LANCE. Com ele, ao se  tentar recuperar  um  
	 * produto  e  todos  os  seus  lances, o  join de PRODUTO  e 
	 * LANCE irá acontecer através da chave estrangeira existente
	 * em  LANCE.  
	 */

	@OneToMany (mappedBy = "produto")
	@OrderBy
	private Set<Lance> lances = new HashSet<Lance>();
	
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

	// ********* Métodos do Tipo Get *********

	public Long getId()
	{	return id;
	}
	
	public String getNome()
	{	return nome;
	}
	
	public String getDescricao()
	{	return descricao;
	}
	
	public double getLanceMinimo()
	{	return lanceMinimo;
	}
	
	@Transient
	public String getLanceMinimoMasc()
	{	return Util.doubleToStr(lanceMinimo);
	}

	public Date getDataCadastro()
	{	return dataCadastro;
	}
	
	@Transient
	public String getDataCadastroMasc()
	{	return Util.dateToStr(dataCadastro);
	}

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
		throws AplicacaoException
	{	if (nome != null && nome.trim().length() > 0)
		{	this.nome = nome;
		}
		else
		{	throw new AplicacaoException("Nome inválido.");	
		}
	}
	
	public void setDescricao(String descricao)
		throws AplicacaoException
	{	if (descricao != null && descricao.trim().length() > 0)
		{	this.descricao = descricao;
		}
		else
		{	throw new AplicacaoException("Descrição inválida.");
		}
	}
	
	public void setLanceMinimo(String lanceMinimo)
		throws AplicacaoException
	{	try
		{	if (lanceMinimo == null || lanceMinimo.trim().length() == 0)
			{	throw new AplicacaoException("Lance mínimo inválido.");
			}
			else
			{	this.lanceMinimo = Util.strToDouble(lanceMinimo);
			}
		}
		catch(NumberFormatException e)
		{	throw new AplicacaoException("Lance mínimo inválido.");
		}
	}
	
	public void setDataCadastro(String dataCadastro)
		throws AplicacaoException
	{	if (!Util.dataValida(dataCadastro))
		{	throw new AplicacaoException("Data de cadastro inválida.");
		}
		else
		{	this.dataCadastro = Util.strToDate(dataCadastro);
		}
	}
	
	public void setDataVenda(Date dataVenda)
	{	this.dataVenda = dataVenda;
	}
	
	// ********* Métodos para Associações *********

	public Set<Lance> getLances()
	{	return lances;
	}
	
	public void setLances(Set<Lance> lances)
	{	this.lances = lances;	
	}
}

