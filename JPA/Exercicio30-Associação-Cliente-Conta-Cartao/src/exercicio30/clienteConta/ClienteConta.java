package exercicio30.clienteConta;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import exercicio30.cartao.Cartao;
import exercicio30.cliente.Cliente;
import exercicio30.conta.Conta;

@NamedQueries(
	{	@NamedQuery
		(	name = "ClienteConta.recuperaUmClienteContaComCartoes",
			query = "select c from ClienteConta c left outer join fetch c.cartoes where c.cliente.id = ? and c.conta.id = ?"
		),
		@NamedQuery
		(	name = "ClienteConta.recuperaListaDeClienteConta",
			query = "select c from ClienteConta c order by c.cliente.id, c.conta.id"
		)
	})

@Entity
@Table(name="CLIENTE_CONTA")

public class ClienteConta                 
{	
	public IdClienteConta id;
	private Cliente cliente;
	private Conta conta;
	private Set<Cartao> cartoes = new HashSet<Cartao>();

	// ********* Construtores *********

	public ClienteConta()
	{	
	}

	public ClienteConta(long idCliente, long idConta)
	{	this.id = new IdClienteConta(idCliente, idConta);
	}

	// ********* Métodos do tipo get *********

	@EmbeddedId  // Para indicar que o atributo id não  deve
	             // ser mapeado para a coluna ID no banco de
	             // dados.
	public IdClienteConta getId()
	{	return id;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CLIENTE",
			    insertable=false,
			    updatable=false)
	public Cliente getCliente()
	{	return cliente;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ID_CONTA",
			    insertable=false,
			    updatable=false)
	public Conta getConta()
	{	return conta;
	}
	
	// ********* Métodos do tipo set *********

	@SuppressWarnings("unused")
	private void setId(IdClienteConta id)
	{	this.id = id;
	}

	public void setCliente(Cliente cliente)
	{	this.cliente = cliente;
	}

	public void setConta(Conta conta)
	{	this.conta = conta;
	}

	@OneToMany (mappedBy = "clienteConta")
	@OrderBy
	public Set<Cartao> getCartoes()
	{	return cartoes;
	}

	public void setCartoes(Set<Cartao> cartoes)
	{	this.cartoes = cartoes;
	}
}

