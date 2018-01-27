package ex01.ejb;

import java.util.List;

import javax.ejb.Remote;

import ex01.dominio.Produto;
import ex01.excecao.AplicacaoException;

@Remote
public interface ProdutoEJBRemote
{	
	public long inclui(Produto umProduto);
	public boolean altera(Produto umProduto);
	public boolean exclui(Produto umProduto);
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException;
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException;
	public List<Produto> recuperaProdutosELances();
}