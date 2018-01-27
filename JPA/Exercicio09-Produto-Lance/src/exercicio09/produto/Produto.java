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
	
	/* A   interface  Set   representa  uma   coleção  sem  elementos 
	 * duplicados.  Para que um elemento  seja armazenado em um Set é
	 * utilizado  o  método  HashCode()  que  irá retornar um  número 
	 * inteiro que será  convertido na posição do elemento dentro  do
	 * Set. Um  Set  não  garante a ordem de  iteração dos  elementos 
	 * contidos no Set; em particular, não garante  que a  ordem  irá
	 * permanecer  constante  ao  longo  do  tempo.  Um Set permite a 
	 * existência delementos null.
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

	// ********* Métodos do Tipo Set *********

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
	
	// ********* Métodos para Associações *********

/* 
      	A  anotação  @OrderBy("valor")  iria acrescentar ao  final  da      
      	cláusula  order  by  da  inscrução  JPQL  esta   ordenação.  O
      	resultado seria o seguinte: order by produto0_.ID,lances1_.ID,
      	lances1_.valor.
      	
      	Se  o  que  se  deseja é conseguir a ordenação especificada na 
      	cláusula order by da  instrução  JPQL, então  basta incluir  a
      	anotação  @OrderBy após a  anotação  @OneToMany. Isto fará com 
      	que a JPA, ao recuperar os lances de um produto (da forma como
      	foi especificado na instrução JPQL) utilize  um  LinkedHashSet
      	para armazenar os  lances  de  um  produto.  Caso  a  anotação
      	@OrderBy não seja  utilizada, a  JPA irá  utilizar  um HashSet
      	para armazenar os lances, o que fará  com que a ordenação  não
      	seja mantida.
      	 
      	O atributo  CascadeType.PERSIST  determina que um  novo  Lance 
      	deve ser persistido (i.é, salvo no banco de dados) se ele  for 
      	referenciado por um Produto persistente. O atributo cascade  é
      	direcional: ele se aplica a apenas um lado da associação.  Ele
      	também  poderia  ser  adicionado à  associação  muitos-para-um 
      	declarada no  mapeamento de Lance, no  entanto isto não  faria 
      	sentido uma vez que os Lances são criados após os Produtos.
      
      	O  atributo  mappedby   indica   o   campo   que  implementa o
      	relacionamento,  isto é,  indica  que na  tabela LANCE o campo 
      	chave  estrangeira  será  o  campo  mapeado  para  o  atributo 
      	"produto"  da classe  Lance.  Sem ele a  JPA irá procurar pela
      	tabela  PRODUTO_LANCE.  Note  que  a  chave  estrangeira   foi
      	definida no mapeamento da associação muitos-para-um na  classe
      	Lance. 

      	Agora existem duas associações unidirecionais mapeadas para  a
      	mesma chave estrangeira.  O atributo mappedby indica ainda que
      	a  coleção  (Set<Lance>) é  um   espelhamento  da   associação 
      	muitos-para-um do outro lado, logo  apenas um INSERT na tabela
      	LANCE será executado quando a associação entre duas instâncias
      	de Produto e Lance forem  manipuladas  pela  implementação  do
      	método  adicionarLance(lance) da classe  ProdutoDAO. Isto é, o
      	INSERT  em  LANCE  será  executado  em   função  do   atributo
      	cascade = { CascadeType.PERSIST, CascadeType.MERGE } ter  sido
      	especificado para a propriedade lances  de  Produto. Quando um
      	objeto   transiente  "lance"  é   referenciado  por um  objeto 
      	persistente "produto", o lance é persistido no banco de dados.
      	E o UPDATE em  LANCE que  seria executado  pelo  fato  de  uma
      	propriedade  da  classe  Lance ter  sido  modificada  não será
      	executado.

        É possível mapear associações com os seguintes atributos:
        ========================================================

        Exemplo: 	

        @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
			       mappedBy = "produto")

        • cascade=”none”       O default.  Diz à JPA  para  ignorar  a 
                               associação.

        • CascadeType.MERGE    Apenas as operações EntityManager.merge       
                               são     propagadas     às     entidades 
                               relacionadas.
                                     
        • CascadeType.PERSIST  Apenas   as   operações   EntityManager
                               .persist  são  propagadas às  entidades 
                               relacionadas.

        • CascadeType.REFRESH  Apenas   as   operações   EntityManager
                               .refresh  são  propagadas às  entidades
                               relacionadas.

        • CascadeType.REMOVE   Apenas   as   operações   EntityManager
                               .remove  são  propagadas  às  entidades
                               relacionadas.

        • CascadeType.ALL      Todas as operações do EntityManager são 
                               propagadas às entidades relacionadas.
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
