package ex04.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
	private double preco;

	// ********* Construtores *********

	public Produto()
	{
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}
	
	@Column(nullable = false)
	public String getNome()
	{	return nome;
	}
	
	@Column(nullable = false)
	public double getPreco()
	{	return preco;
	}

	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setNome(String nome)
	{	this.nome = nome;
	}
	
	public void setPreco(double preco)
	{	this.preco = preco;
	}
}

