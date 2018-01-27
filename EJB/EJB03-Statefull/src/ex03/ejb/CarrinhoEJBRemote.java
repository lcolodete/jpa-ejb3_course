package ex03.ejb;

import javax.ejb.Remote;

import ex03.dataObject.PedidoDO;
import ex03.dominio.Pedido;
import ex03.excecao.AplicacaoException;
import ex03.excecao.EstadoConversacionalIncompleto;

@Remote
public interface CarrinhoEJBRemote
{	public void designarCliente(long id) 		
		throws AplicacaoException;
    public void adicionarProduto(long idProduto, int qtd)
		throws AplicacaoException;
    public double calcularPrecoTotal();
    public PedidoDO fecharCompra(String numCartao) 
    	throws EstadoConversacionalIncompleto;
    public PedidoDO fecharCompraMesmo(String numCartao);
    public Pedido recuperarCarrinho() 
		throws EstadoConversacionalIncompleto;
}