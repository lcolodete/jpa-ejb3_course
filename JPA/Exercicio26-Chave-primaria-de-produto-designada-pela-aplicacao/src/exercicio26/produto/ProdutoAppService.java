package exercicio26.produto;

import java.util.List;
import java.util.Set;

import exercicio26.controleDao.FabricaDeDao;
import exercicio26.controleTransacao.Transacional;
import exercicio26.util.AplicacaoException;
import exercicio26.util.ObjetoNaoEncontradoException;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO;
    
    public ProdutoAppService ()
    {	// System.out.println("**************>>>>  Executou construtor de ProdutoAppService");
        try 
        {   produtoDAO = FabricaDeDao
        		.getDao(ProdutoDAO.class, Produto.class);

        	// O m�todo acima tb poderia ser chamado assim:
           	//produtoDAO = FabricaDeDao.<ProdutoDAO>getDao(ProdutoDAO.class, Produto.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

	@Transacional
	@SuppressWarnings("unchecked")
	public long inclui(Produto umProduto) throws AplicacaoException 
	{	try
		{	produtoDAO.getPorId(umProduto.getId());
	
			throw new AplicacaoException 
				("Produto j� cadastrado com este n�mero.");
		}
		catch(ObjetoNaoEncontradoException e)
		{	
		}
		
		return produtoDAO.inclui(umProduto).getId();
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
		{	throw new AplicacaoException("Produto n�o encontrado.");
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
		{	throw new AplicacaoException("Produto n�o encontrado");
		}
	}
	
	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	
		try
		{	return produtoDAO.recuperaUmProdutoELances(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto n�o encontrado");	
		}
	}
	
	public List<Produto> recuperaListaDeProdutos()
	{	return produtoDAO.recuperaListaDeProdutos();
	}
	
	public Set<Produto> recuperaConjuntoDeProdutosELances()
	{	return produtoDAO.recuperaConjuntoDeProdutosELances();
	}
}