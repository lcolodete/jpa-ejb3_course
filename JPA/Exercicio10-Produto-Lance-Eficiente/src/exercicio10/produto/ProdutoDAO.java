package exercicio10.produto;

import java.util.List;

import exercicio10.util.AplicacaoException;

public interface ProdutoDAO
{	
	public long inclui(Produto umProduto); 
	public void altera(Produto umProduto)
		throws AplicacaoException;
	public void exclui(Produto umProduto) 
		throws AplicacaoException;
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException;
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException;
	public List<Produto> recuperaProdutosELances();
}