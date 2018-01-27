package ex01.dominio;

import java.io.Serializable;
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

import ex01.util.Util;


@Entity
@Table(name="PRODUTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PRODUTO",
		           allocationSize=1)

public class Produto implements Serializable
{	
	private static final long serialVersionUID = 1L;
	
	private Long id;
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

	@OneToMany (mappedBy = "produto")
	@OrderBy
	public Set<Lance> getLances()
	{	return lances;
	}
	
	public void setLances(Set<Lance> lances)
	{	this.lances = lances;	
	}
}

