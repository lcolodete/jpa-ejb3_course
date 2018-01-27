package ex04.ejb;

import javax.ejb.Remote;

import ex04.dataObject.PedidoDO;
import ex04.dominio.Pedido;
import ex04.excecao.AplicacaoException;
import ex04.excecao.EstadoConversacionalIncompleto;

@Remote
public interface CarrinhoEJBRemote
{	
	public void designarCliente(long id) 		
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