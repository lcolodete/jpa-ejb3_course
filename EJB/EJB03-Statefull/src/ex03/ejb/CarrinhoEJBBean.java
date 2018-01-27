package ex03.ejb;

import java.sql.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex03.dao.ClienteDAO;
import ex03.dao.PedidoDAO;
import ex03.dao.ProdutoDAO;
import ex03.dataObject.PedidoDO;
import ex03.dominio.Cliente;
import ex03.dominio.Pedido;
import ex03.dominio.Produto;
import ex03.excecao.AplicacaoException;
import ex03.excecao.EstadoConversacionalIncompleto;
import ex03.excecao.ObjetoNaoEncontradoException;

@Stateful
@RemoteBinding(jndiBinding="exercicio03.CarrinhoEJBBean/remote")
public class CarrinhoEJBBean implements CarrinhoEJBRemote 
{
    @PersistenceContext(unitName="EJB") private EntityManager em;

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
    {	
		Cliente umCliente;
		try
    	{	umCliente = clienteDAO.recuperaUmCliente(id);
    	}
    	catch(ObjetoNaoEncontradoException e)
    	{	throw new AplicacaoException("Cliente n�o encontrado."); 
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
    	{	throw new AplicacaoException("Produto n�o encontrado."); 
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

    // Estamos dizendo ao Container EJB que n�o h� mais necessidade
    // de manter uma sess�o aberta com o cliente ap�s a execu��o do
    // m�todo  fecharCompraMesmo(). Se  n�o dissermos ao  Container 
    // que este  m�todo  encerra o  fluxo de trabalho (workflow), o
    // Container poder� esperar por um longo per�odo para remover a
    // sess�o apenas ap�s o seu time-out.
    
    @Remove
    public PedidoDO fecharCompraMesmo(String numCartao)
	{	
		pedidoDAO.inclui(umPedido);
		return new PedidoDO(umPedido.getId(), 
					    umPedido.getCliente().getNome(),
					    umPedido.getPrecoTotal());
	}
}