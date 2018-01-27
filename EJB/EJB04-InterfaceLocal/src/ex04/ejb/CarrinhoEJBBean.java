package ex04.ejb;

import java.sql.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex04.dao.ClienteDAO;
import ex04.dao.PedidoDAO;
import ex04.dao.ProdutoDAO;
import ex04.dataObject.PedidoDO;
import ex04.dominio.Cliente;
import ex04.dominio.Pagamento;
import ex04.dominio.Pedido;
import ex04.dominio.Produto;
import ex04.excecao.AplicacaoException;
import ex04.excecao.EstadoConversacionalIncompleto;
import ex04.excecao.ObjetoNaoEncontradoException;

@Stateful
@RemoteBinding(jndiBinding="exercicio04.CarrinhoEJBBean/remote")
public class CarrinhoEJBBean implements CarrinhoEJBRemote 
{
    @PersistenceContext(unitName="EJB") private EntityManager em;

    // Injeta uma referência para um bean de sessão sem estado e local.
    @EJB private ProcessaPagamentoLocal processaPagamento;
    
    private Pedido umPedido =  new Pedido(new Date(System.currentTimeMillis()));
    
	private ClienteDAO clienteDAO; 	
	private ProdutoDAO produtoDAO; 	
	private PedidoDAO pedidoDAO; 	

	@PostConstruct 
	public void init()
	{	clienteDAO = new ClienteDAO(em);
		produtoDAO = new ProdutoDAO(em);
		pedidoDAO = new PedidoDAO(em);
	}

	public void designarCliente(long id) 
		throws AplicacaoException
    {	Cliente umCliente;
    
    	try
    	{	umCliente = clienteDAO.recuperaUmCliente(id);
    	}
    	catch(ObjetoNaoEncontradoException e)
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
    	
    	Pagamento umPagamento = processaPagamento
			.porCartao(new Date(System.currentTimeMillis()),
				       numCartao, precoTotal);

		umPedido.setPagamento(umPagamento);
		
		pedidoDAO.inclui(umPedido);

		return new PedidoDO(umPedido.getId(), 
					    umPedido.getCliente().getNome(),
					    umPedido.getPrecoTotal(),
					    umPagamento.getNumCartao());
	}
}