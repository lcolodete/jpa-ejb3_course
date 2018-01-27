package exercicio28.cliente;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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

	private Set<String> telefones = new HashSet<String>();

	public Cliente ()
	{	
	}

	public Cliente (String nome, double salario)
	{	this.nome = nome;
		this.salario = salario;
	}

	/*******  Métodos do tipo get  *******/

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, 
    		        generator="SEQUENCIA")
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
		        joinColumns={@JoinColumn(name="ID", referencedColumnName="ID")})
	// O valor default para referencedColumnName somente é 
    // aplicado quando um único join está sendo utilizado. 
	// O valor default é o nome da  primary key da  tabela
	// referenciada.
	@Column(name = "TELEFONE")
	public Set<String> getTelefones()
	{	return telefones;
	}

	public void setTelefones(Set<String> telefones)
	{	this.telefones = telefones;
	}
	
	@SuppressWarnings("unchecked")
	public boolean adicionarTelefone(String telefone)
	{	return telefones.add(telefone);
	}
	
	public boolean removerTelefone(String telefone)
	{	return telefones.remove(telefone);
	}
}