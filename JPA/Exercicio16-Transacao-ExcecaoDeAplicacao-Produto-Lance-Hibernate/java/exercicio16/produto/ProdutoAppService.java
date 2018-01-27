package exercicio16.produto;

import java.util.List;

import exercicio16.controleTransacao.Transacional;
import exercicio16.util.AplicacaoException;
import exercicio16.util.ObjetoNaoEncontradoException;

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
		Produto produto;
		try
		{	produto = produtoDAO.recuperaUmProdutoELances(umProduto.getId());
		} 
		catch (ObjetoNaoEncontradoException e1)
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

	@Transacional	
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException 
	{	try
		{	return produtoDAO.recuperaUmProduto(numero);
		} 
		catch (ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}
	}
	
	@Transacional	
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException 
	{	try
		{	return produtoDAO.recuperaUmProdutoELances(numero);
		} 
		catch (ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}
	}

	@Transacional
	public List<Produto> recuperaProdutos()
	{	return produtoDAO.recuperaProdutos();
	}
	
	@Transacional
	public List<Produto> recuperaProdutosELances()
	{	return produtoDAO.recuperaProdutosELances();
	}
}