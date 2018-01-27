package exercicio15.produto;

import java.util.List;

import exercicio15.controleTransacao.Transacional;
import exercicio15.util.AplicacaoException;
import exercicio15.util.ObjetoNaoEncontradoException;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO = new ProdutoDAOImpl();

	@Transacional
	@SuppressWarnings("unchecked")
	public long inclui(Produto umProduto) 
	{	return produtoDAO.inclui(umProduto);
	}	
	
	@Transacional
	public void altera(Produto umProduto) 
		throws AplicacaoException 
	{	try
		{	produtoDAO.altera(umProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}
	}	
	
	@Transacional
	public void exclui(Produto umProduto) 
		throws AplicacaoException
	{
		Produto produto = produtoDAO.recuperaUmProdutoELances(umProduto.getId());
		
		if (produto == null)
		{	throw new AplicacaoException("Produto não encontrado.");
		}

		if(produto.getLances().size() > 0)
		{	throw new AplicacaoException("Este produto possui lances e nao pode ser removido");
		}

		try
		{	produtoDAO.exclui(umProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}
	}	
	
	public Produto recuperaUmProduto(long numero) 
	{	return produtoDAO.recuperaUmProduto(numero);
	}
	
	public Produto recuperaUmProdutoELances(long numero) 
	{	return produtoDAO.recuperaUmProdutoELances(numero);
	}

	public List<Produto> recuperaProdutos()
	{	return produtoDAO.recuperaProdutos();
	}
	
	public List<Produto> recuperaProdutosELances()
	{	return produtoDAO.recuperaProdutosELances();
	}
}