package ex05.ejb;

import ex05.dataObject.PedidoDO;
import ex05.dominio.Pedido;
import ex05.excecao.AplicacaoException;
import ex05.excecao.EstadoConversacionalIncompleto;

public interface CarrinhoEJB
{	public void criarNovoPedido();
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