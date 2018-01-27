package exercicio29.cliente;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(
		{	@NamedQuery
			(	name = "Cliente.recuperaUmClienteETelefones",
				query = "select c from Cliente c left outer join fetch c.telefones f where c.id = ?"
			),
			@NamedQuery
			(	name = "Cliente.recuperaListaDeClientesETelefones",
				query = "select distinct c from Cliente c left outer join fetch c.telefones order by c.id asc"
			),
			@NamedQuery
			(	name = "Cliente.recuperaListaDeClientes",
				query = "select distinct c from Cliente c order by c.id asc"
			)
		})

@Entity
@Table(name="CLIENTE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CLIENTE",
		           allocationSize=1)

public class Cliente
{	
	private Long id;
	private String nome;
	private double salario;

	private Set<Telefone> telefones = new HashSet<Telefone>();

	public Cliente ()
	{	
	}

	public Cliente (String nome, double salario)
	{	this.nome = nome;
		this.salario = salario;
	}

	/*******  Métodos do tipo get  *******/

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
    @Column(name="ID")
	public Long getId()
	{	return id;
	}

	public String getNome()
	{	return nome;
	}
		
	public double getSalario()
	{	return salario;
	}

	/*******  Métodos do tipo set  *******/

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}	
	
	public void setNome(String nome)
	{	this.nome = nome;
	}
		
	public void setSalario(double salario)
	{	this.salario = salario;
	}

	/*******  Métodos que representam associações  *******/

	@org.hibernate.annotations.CollectionOfElements
	// O package  Hibernate Annotations  suporta anotações 
	// não padrão para o mapeamento de coleções que contêm 
	// elementos  do tipo  valor.  A principal  anotação é
	// org.hibernate.annotations.CollectionOfElements. 
	
	@JoinTable( name="CLIENTE_TELEFONE", 
		        joinColumns=@JoinColumn(name="ID", referencedColumnName="ID"))

	// Poderia fazer o  override de quantos campos  fossem 
	// necessários. Neste caso  o override não faz sentido
	// uma vez que na  classe Telefone a  propriedade  ddd
	// já foi  mapeada para a  coluna DDD.  Acrescentei  o
	// override apenas para mostrar que é possível.
	
	@AttributeOverride
		( name="ddd",
		  column=@Column(name="DDD",
				         nullable=false)
		)
	@OrderBy("ddd, numero")
	public Set<Telefone> getTelefones()
	{	return telefones;
	}

	public void setTelefones(Set<Telefone> telefones)
	{	this.telefones = telefones;
	}
	
	@SuppressWarnings("unchecked")
	public boolean adicionarTelefone(Telefone telefone)
	{	return telefones.add(telefone);
	}
	
	public boolean removerTelefone(Telefone telefone)
	{	return telefones.remove(telefone);
	}
}