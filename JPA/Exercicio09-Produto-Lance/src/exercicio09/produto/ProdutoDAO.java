package exercicio09.produto;

import java.util.List;

import exercicio09.lance.Lance;
import exercicio09.util.AplicacaoException;

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
	public void adicionarLance(long idProduto, Lance umLance); 
}