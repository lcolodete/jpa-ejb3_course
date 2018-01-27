package exercicio09.produto;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import exercicio09.lance.Lance;
import exercicio09.util.Util;

@Entity
@Table(name="PRODUTO")
@SequenceGenerator(name="SEQUENCIA", 
		           sequenceName="SEQ_PRODUTO",
		           allocationSize=1)

public class Produto
{	private Long id;
	private String nome;
	private String descricao;
	private double lanceMinimo;
	private Date dataCadastro;
	private Date dataVenda;

	//  Um produto possui lances

	private Set<Lance> lances = new HashSet<Lance>();
	
	/* A   interface  Set   representa  uma   cole��o  sem  elementos 
	 * duplicados.  Para que um elemento  seja armazenado em um Set �
	 * utilizado  o  m�todo  HashCode()  que  ir� retornar um  n�mero 
	 * inteiro que ser�  convertido na posi��o do elemento dentro  do
	 * Set. Um  Set  n�o  garante a ordem de  itera��o dos  elementos 
	 * contidos no Set; em particular, n�o garante  que a  ordem  ir�
	 * permanecer  constante  ao  longo  do  tempo.  Um Set permite a 
	 * exist�ncia delementos null.
	 */
	
	// ********* Construtores *********

	public Produto()
	{
	}

	public Produto(String nome, 
	               String descricao, 
	               double lanceMinimo, 
	               Date dataCadastro)
	{	this.nome = nome;
		this.descricao = descricao;
		this.lanceMinimo = lanceMinimo;
		this.dataCadastro = dataCadastro;	
	}

	// ********* M�todos do Tipo Get *********

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
	public String getDescricao()
	{	return descricao;
	}
	
	@Column(name="LANCE_MINIMO", nullable = false)
	public double getLanceMinimo()
	{	return lanceMinimo;
	}
	
	@Transient
	public String getLanceMinimoMasc()
	{	return Util.doubleToStr(lanceMinimo);
	}

	@Column(name="DATA_CADASTRO", nullable = false)
	public Date getDataCadastro()
	{	return dataCadastro;
	}
	
	@Transient
	public String getDataCadastroMasc()
	{	return Util.dateToStr(dataCadastro);
	}

	@Column(name="DATA_VENDA")
	public Date getDataVenda()
	{	return dataVenda;
	}
	
	@Transient
	public String getDataVendaMasc()
	{	return Util.dateToStr(dataVenda);
	}

	// ********* M�todos do Tipo Set *********

	@SuppressWarnings("unused")
	private void setId(Long id)
	{	this.id = id;
	}
	
	public void setNome(String nome)
	{	this.nome = nome;
	}
	
	public void setDescricao(String descricao)
	{	this.descricao = descricao;
	}
	
	public void setLanceMinimo(double lanceMinimo)
	{	this.lanceMinimo = lanceMinimo;
	}
	
	public void setDataCadastro(Date dataCadastro)
	{	this.dataCadastro = dataCadastro;
	}
	
	public void setDataVenda(Date dataVenda)
	{	this.dataVenda = dataVenda;
	}
	
	// ********* M�todos para Associa��es *********

/* 
      	A  anota��o  @OrderBy("valor")  iria acrescentar ao  final  da      
      	cl�usula  order  by  da  inscru��o  JPQL  esta   ordena��o.  O
      	resultado seria o seguinte: order by produto0_.ID,lances1_.ID,
      	lances1_.valor.
      	
      	Se  o  que  se  deseja � conseguir a ordena��o especificada na 
      	cl�usula order by da  instru��o  JPQL, ent�o  basta incluir  a
      	anota��o  @OrderBy ap�s a  anota��o  @OneToMany. Isto far� com 
      	que a JPA, ao recuperar os lances de um produto (da forma como
      	foi especificado na instru��o JPQL) utilize  um  LinkedHashSet
      	para armazenar os  lances  de  um  produto.  Caso  a  anota��o
      	@OrderBy n�o seja  utilizada, a  JPA ir�  utilizar  um HashSet
      	para armazenar os lances, o que far�  com que a ordena��o  n�o
      	seja mantida.
      	 
      	O atributo  CascadeType.PERSIST  determina que um  novo  Lance 
      	deve ser persistido (i.�, salvo no banco de dados) se ele  for 
      	referenciado por um Produto persistente. O atributo cascade  �
      	direcional: ele se aplica a apenas um lado da associa��o.  Ele
      	tamb�m  poderia  ser  adicionado �  associa��o  muitos-para-um 
      	declarada no  mapeamento de Lance, no  entanto isto n�o  faria 
      	sentido uma vez que os Lances s�o criados ap�s os Produtos.
      
      	O  atributo  mappedby   indica   o   campo   que  implementa o
      	relacionamento,  isto �,  indica  que na  tabela LANCE o campo 
      	chave  estrangeira  ser�  o  campo  mapeado  para  o  atributo 
      	"produto"  da classe  Lance.  Sem ele a  JPA ir� procurar pela
      	tabela  PRODUTO_LANCE.  Note  que  a  chave  estrangeira   foi
      	definida no mapeamento da associa��o muitos-para-um na  classe
      	Lance. 

      	Agora existem duas associa��es unidirecionais mapeadas para  a
      	mesma chave estrangeira.  O atributo mappedby indica ainda que
      	a  cole��o  (Set<Lance>) �  um   espelhamento  da   associa��o 
      	muitos-para-um do outro lado, logo  apenas um INSERT na tabela
      	LANCE ser� executado quando a associa��o entre duas inst�ncias
      	de Produto e Lance forem  manipuladas  pela  implementa��o  do
      	m�todo  adicionarLance(lance) da classe  ProdutoDAO. Isto �, o
      	INSERT  em  LANCE  ser�  executado  em   fun��o  do   atributo
      	cascade = { CascadeType.PERSIST, CascadeType.MERGE } ter  sido
      	especificado para a propriedade lances  de  Produto. Quando um
      	objeto   transiente  "lance"  �   referenciado  por um  objeto 
      	persistente "produto", o lance � persistido no banco de dados.
      	E o UPDATE em  LANCE que  seria executado  pelo  fato  de  uma
      	propriedade  da  classe  Lance ter  sido  modificada  n�o ser�
      	executado.

        � poss�vel mapear associa��es com os seguintes atributos:
        ========================================================

        Exemplo: 	

        @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			       mappedBy = "produto")

        � cascade=�none�       O default.  Diz � JPA  para  ignorar  a 
                               associa��o.

        � CascadeType.MERGE    Apenas as opera��es EntityManager.merge       
                               s�o     propagadas     �s     entidades 
                               relacionadas.
                                     
        � CascadeType.PERSIST  Apenas   as   opera��es   EntityManager
                               .persist  s�o  propagadas �s  entidades 
                               relacionadas.

        � CascadeType.REFRESH  Apenas   as   opera��es   EntityManager
                               .refresh  s�o  propagadas �s  entidades
                               relacionadas.

        � CascadeType.REMOVE   Apenas   as   opera��es   EntityManager
                               .remove  s�o  propagadas  �s  entidades
                               relacionadas.

        � CascadeType.ALL      Todas as opera��es do EntityManager s�o 
                               propagadas �s entidades relacionadas.
*/     
	
	
	@OneToMany(cascade={CascadeType.MERGE, CascadeType.PERSIST}, mappedBy="produto")
	@OrderBy
	public Set<Lance> getLances()
	{	return lances;
	}
	
	public void setLances(Set<Lance> lances)
	{	this.lances = lances;	
	}
}
