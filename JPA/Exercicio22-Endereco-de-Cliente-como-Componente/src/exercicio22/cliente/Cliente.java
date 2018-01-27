package exercicio22.cliente;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries(
		{	@NamedQuery
			(	name = "Cliente.recuperaListaDeClientes",
				query = "select c from Cliente c order by id"
			)
		})

@Entity
@Table(name="CLIENTE")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CLIENTE",
		           allocationSize=1)

public class Cliente
{	
	private Long numero;
	private String nome;
	private double salario;
	private Endereco enderecoResidencial;
	private Endereco enderecoComercial;
	
	/*****  Métodos Construtores *****/
	
	public Cliente ()
	{	
	}

	public Cliente (String nome, 
	                double salario, 
	                Endereco enderecoResidencial, 
	                Endereco enderecoComercial)
	{	this.nome = nome;
		this.salario = salario;
		this.enderecoResidencial = enderecoResidencial;
		this.enderecoComercial = enderecoComercial;
	}

	/*****  Métodos do tipo get *****/

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, 
			        generator="SEQUENCIA")
	@Column(name="NUMERO")

	public Long getNumero()
	{	return numero;
	}

	@Column(nullable = false)
	public String getNome()
	{	return nome;
	}
		
	@Column(nullable = false)
	public double getSalario()
	{	return salario;
	}

	/* A JPA chama os componentes de classes embutidas (embedded 
	 * classes). A  JPA  verifica  se  existe  dentro  da classe
	 * associada    (Endereco,    neste   caso)   uma   anotação
	 * denominada  @Embeddable.   Se  ela  estiver  presente   a
	 * propriedade é mapeada como um componente dependente.
	 */ 
	@Embedded
	@AttributeOverrides
	({
		@AttributeOverride(name = "rua",
				           column = @Column(name="RUA_RES")),
		@AttributeOverride(name = "numero",
				           column = @Column(name="NUMERO_RES")),				           
   		@AttributeOverride(name = "complemento",
				           column = @Column(name="COMPLEMENTO_RES")) 
	})
	
	public Endereco getEnderecoResidencial()
	{	return enderecoResidencial;
	}

	@Embedded
	@AttributeOverrides
	({
		@AttributeOverride(name = "rua",
				           column = @Column(name="RUA_COM")),
		@AttributeOverride(name = "numero",
				           column = @Column(name="NUMERO_COM")),				           
   		@AttributeOverride(name = "complemento",
				           column = @Column(name="COMPLEMENTO_COM")) 
	})
	public Endereco getEnderecoComercial()
	{	return enderecoComercial;
	}

	/*****  Métodos do tipo set *****/

	@SuppressWarnings("unused")
	private void setNumero(Long numero)
	{	this.numero = numero;
	}

	public void setNome(String nome)
	{	this.nome = nome;
	}
		
	public void setSalario(double salario)
	{	this.salario = salario;
	}

	public void setEnderecoResidencial(Endereco enderecoResidencial)
	{	this.enderecoResidencial = enderecoResidencial;
	}

	public void setEnderecoComercial(Endereco enderecoComercial)
	{	this.enderecoComercial = enderecoComercial;
	}
}