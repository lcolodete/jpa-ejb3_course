package exercicio17.produto;

import java.util.List;

import exercicio17.controleDao.FabricaDeDao;
import exercicio17.controleTransacao.Transacional;
import exercicio17.util.AplicacaoException;
import exercicio17.util.ObjetoNaoEncontradoException;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO;
    
    public ProdutoAppService ()
    {	// System.out.println("**************>>>>  Executou construtor de ProdutoAppService");
        try 
        {   produtoDAO = FabricaDeDao
        		.getDao(ProdutoDAO.class, Produto.class);

        	// O método acima tb poderia ser chamado assim:
           	//produtoDAO = FabricaDeDao.<ProdutoDAO>getDao(ProdutoDAO.class, Produto.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

	@Transacional
	@SuppressWarnings("unchecked")
	public long inclui(Produto umProduto) 
	{	return produtoDAO.inclui(umProduto).getId();
	}	
	
	@Transacional
	public void altera(Produto umProduto) 
	{	produtoDAO.altera(umProduto);
	}	
	
	@Transacional
	public void exclui(Produto umProduto) 
		throws AplicacaoException
	{
		Produto produto;
		try
		{	produto = produtoDAO.recuperaUmProdutoELances(umProduto.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}

		if(produto.getLances().size() > 0)
		{	throw new AplicacaoException("Este produto possui lances e nao pode ser removido");
		}

		produtoDAO.exclui(produto);
	}	
	
	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException
	{	try 
		{	return produtoDAO.getPorId(numero);
		}
		catch (ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");
		}
	}
	
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	
		try
		{	return produtoDAO.recuperaUmProdutoELances(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");	
		}
	}
	
	public List<Produto> recuperaListaDeProdutos()
	{	return produtoDAO.recuperaListaDeProdutos();
	}
	
	public List<Produto> recuperaListaDeProdutosELances()
	{	return produtoDAO.recuperaListaDeProdutosELances();
	}
}