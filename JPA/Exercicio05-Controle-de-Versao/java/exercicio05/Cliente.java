package exercicio05;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

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
	
	private int versao;

//  M�todos construtores

	public Cliente ()
	{	
	}

	public Cliente (String nome, double salario)
	{	this.nome = nome;
		this.salario = salario;
	}

//  M�todos do tipo get

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

	@Version
	@Column(name = "VERSAO")
	public int getVersao()  
	{	return versao;
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

	public void setVersao(int versao)
	{	this.versao = versao;
	}
}