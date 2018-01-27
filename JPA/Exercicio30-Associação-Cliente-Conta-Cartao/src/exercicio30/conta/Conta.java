package exercicio30.conta;

import java.util.Date;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exercicio30.clienteConta.ClienteConta;

@NamedQueries(
		{	@NamedQuery
			(	name = "Conta.recuperaListaDeContas",
				query = "select c from Conta c order by c.id"
			)
		})

@Entity
@Table(name="CONTA")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CONTA",
		           allocationSize=1)
public class Conta
{	private Long id;
	private Date dataAbertura;
	private Set<ClienteConta> ccs = new HashSet<ClienteConta>();

	public Conta()
	{	
	}

	public Conta(Date dataAbertura)
	{	this.dataAbertura = dataAbertura;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, 
    		        generator="SEQUENCIA")
    @Column(name="ID")
	public Long getId()
	{	return id;
	}
	
	public void setId(Long id)
	{	this.id = id;
	}

	@Column(name="DATA_ABERTURA")
	public Date getDataAbertura()
	{	return dataAbertura;
	}
	
	public void setDataAbertura(Date dataAbertura)
	{	this.dataAbertura = dataAbertura;
	}

	@OneToMany (mappedBy = "conta")  
	public Set<ClienteConta> getClienteContas()
	{	return ccs;
	}

	public void setClienteContas(Set<ClienteConta> ccs)
	{	this.ccs = ccs;
	}

}

