package exercicio30.cliente;

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
			(	name = "Cliente.recuperaListaDeClientes",
				query = "select c from Cliente c order by c.id"
			)
		})

@Entity
@Table(name="CLIENTE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CLIENTE",
		           allocationSize=1)

public class Cliente
{	private Long id;
	private String nome;
	private Set<ClienteConta> ccs = new HashSet<ClienteConta>();
	
	public Cliente()
	{
	}

	public Cliente(String nome)
	{	this.nome = nome;
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
	
	public String getNome()
	{	return nome;
	}
	
	public void setNome(String nome)
	{	this.nome = nome;
	}
	
	@OneToMany (mappedBy = "cliente")   
	public Set<ClienteConta> getClienteContas()
	{	return ccs;
	}

	public void setClienteContas(Set<ClienteConta> ccs)
	{	this.ccs = ccs;
	}
}

