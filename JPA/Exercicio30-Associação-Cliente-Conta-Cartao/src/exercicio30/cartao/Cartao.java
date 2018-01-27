package exercicio30.cartao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import exercicio30.clienteConta.ClienteConta;

@NamedQueries(
		{	@NamedQuery
			(	name = "Cartao.recuperaListaDeCartoes",
				query = "select c from Cartao c order by c.id"
			),
			@NamedQuery
			(	name = "Cartao.recuperaUmCartaoComClienteConta",
				query = "select c from Cartao c left outer join fetch c.clienteConta where c.id = ?"
			)
		})

@Entity
@Table(name="CARTAO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_CARTAO",
		           allocationSize=1)

public class Cartao
{	private Long id;
	private Date dataEmissao;
	private ClienteConta clienteConta;
	
	public Cartao()
	{
	}   

	public Cartao(Date dataEmissao)
	{	this.dataEmissao = dataEmissao;
	}

	@Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, 
    		        generator="SEQUENCIA")
    @Column(name="ID")
	public Long getId()
	{	return id;
	}
	
    @Column(name="DATA_EMISSAO")
	public Date getDataEmissao()
	{	return dataEmissao;
	}

    @SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}
	
	public void setDataEmissao(Date dataEmissao)
	{	this.dataEmissao = dataEmissao;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumns({@JoinColumn(name="ID_CLIENTE", referencedColumnName = "ID_CLIENTE"),
				  @JoinColumn(name="ID_CONTA", referencedColumnName = "ID_CONTA")})
				  
	// Quando a anotação @JoinColumns é utilizada os atributos name  e 
	// referencedColumnName devem ser especificados para cada anotação
	// @JoinColumn.
	public ClienteConta getClienteConta()
	{	return clienteConta;
	}
	
	public void setClienteConta(ClienteConta clienteConta)
	{	this.clienteConta = clienteConta;
	}
}

