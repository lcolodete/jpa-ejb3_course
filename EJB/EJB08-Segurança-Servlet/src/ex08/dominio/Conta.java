package ex08.dominio;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import ex08.util.Util;

@Entity
@Table(name="CONTA")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CONTA",
		           allocationSize=1)

public class Conta implements Serializable
{	
	private static final long serialVersionUID = 1L;
	private Long id;
	private double saldo;

	// ********* Construtores *********

	public Conta()
	{
	}

	public Conta(double saldo)
	{	this.saldo = saldo;
	}

	// ********* Métodos do Tipo Get *********

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCIA")
	@Column(name="ID")
	public Long getId()
	{	return id;
	}
	
	@Column(nullable = false)
	public double getSaldo()
	{	return saldo;
	}
	
	@Transient
	public String getSaldoMasc()
	{	return Util.doubleToStr(saldo);
	}

	// ********* Métodos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}

	public void setSaldo(double saldo)
	{	this.saldo = saldo;
	}
}

