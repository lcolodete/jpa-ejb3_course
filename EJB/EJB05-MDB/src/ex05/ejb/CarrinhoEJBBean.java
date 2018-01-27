package ex05.ejb;

import java.sql.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex05.dao.ClienteDAO;
import ex05.dao.PedidoDAO;
import ex05.dao.ProdutoDAO;
import ex05.dataObject.PedidoDO;
import ex05.dominio.Cliente;
import ex05.dominio.Pagamento;
import ex05.dominio.Pedido;
import ex05.dominio.Produto;
import ex05.excecao.AplicacaoException;
import ex05.excecao.EstadoConversacionalIncompleto;
import ex05.excecao.ObjetoNaoEncontradoException;

@Stateful
@RemoteBinding(jndiBinding="exercicio05.CarrinhoEJBBean/remote")
// Esta anotação @RemoteBinding é proprietária do JBoss.

// Observe que como este EJB é acessado remotamente e localmente, ele 
// necessita implementar ambas as interfaces, remota e local.
public class CarrinhoEJBBean implements CarrinhoEJBRemote, CarrinhoEJBLocal 
{
    @PersistenceContext(unitName="EJB")
    private EntityManager em;

    @EJB private ProcessaPagamentoLocal processaPagamento;
    
    private Pedido umPedido = null;
    
	private ClienteDAO clienteDAO; 	
	private ProdutoDAO produtoDAO; 	
	private PedidoDAO pedidoDAO; 	

	@PostConstruct 
	public void init()
	{	clienteDAO = new ClienteDAO(em);
		produtoDAO = new ProdutoDAO(em);
		pedidoDAO = new PedidoDAO(em);
	}

	public void criarNovoPedido() 
	{	umPedido =  new Pedido(new Date(System.currentTimeMillis()));
	}

	public void designarCliente(long id) 
		throws AplicacaoException
    {	Cliente umCliente = clienteDAO.recuperaUmCliente(id);	
    	
    	if (umCliente == null)
    	{	throw new AplicacaoException("Cliente não encontrado."); 
    	}
    	
    	umPedido.setCliente(umCliente);
    }

    public void adicionarProduto(long idProduto, int qtd) 
    	throws AplicacaoException
    {	Produto umProduto;
    
    	try
    	{	umProduto = produtoDAO.recuperaUmProduto(idProduto);
    	}
    	catch(ObjetoNaoEncontradoException e)
    	{	throw new AplicacaoException("Produto não encontrado."); 
		}

		umPedido.adicionarProduto(umProduto, qtd);
    }

    public double calcularPrecoTotal()
    {	return umPedido.getPrecoTotal();    	
    }

    public Pedido recuperarCarrinho() 
		throws EstadoConversacionalIncompleto
	{	if (umPedido.getCliente() == null ||
    		umPedido.getPrecoTotal() == 0)
		{	throw new EstadoConversacionalIncompleto("Pedido incompleto.");
		}

		return umPedido;
	}
    
    public PedidoDO fecharCompra(String numCartao) 
    	throws EstadoConversacionalIncompleto
    {	if (umPedido.getCliente() == null ||
    		umPedido.getPrecoTotal() == 0)
    	{	throw new EstadoConversacionalIncompleto("Pedido incompleto.");
    	}
    	
    	return fecharCompraMesmo(numCartao);
    }

    @Remove
    public PedidoDO fecharCompraMesmo(String numCartao)
	{	
    	double precoTotal = umPedido.getPrecoTotal();
    	System.out.println(">>>>>>>>>>>>>>>>>> Passou 1");
		Pagamento umPagamento = processaPagamento
			.porCartao(new Date(System.currentTimeMillis()),
				       numCartao, precoTotal);
    	System.out.println(">>>>>>>>>>>>>>>>>> Passou 2");

		umPedido.setPagamento(umPagamento);
    	System.out.println(">>>>>>>>>>>>>>>>>> Passou 3");
		
		pedidoDAO.inclui(umPedido);
	   	System.out.println(">>>>>>>>>>>>>>>>>> Passou 4");

		return new PedidoDO(umPedido.getId(), 
					    umPedido.getCliente().getNome(),
					    umPedido.getPrecoTotal(),
					    umPagamento.getNumCartao());
	}
}