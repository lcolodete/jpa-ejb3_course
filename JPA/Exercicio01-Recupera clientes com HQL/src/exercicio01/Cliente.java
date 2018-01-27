package exercicio01;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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

	public Cliente ()
	{	
	}

//  M�todos do tipo get

	// H� 3 formas populares de gerar valores de chave prim�ria: identity,
	// sequence e table.
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
    @Column(name="NUMERO")
	public Long getNumero()
	{	return numero;
	}

	public String getNome()
	{	return nome;
	}
		
	public double getSalario()
	{	return salario;
	}

//  M�todos do tipo set
	
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
}