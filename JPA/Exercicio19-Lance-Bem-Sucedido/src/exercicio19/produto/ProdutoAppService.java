package exercicio19.produto;

import java.util.List;
import java.util.Set;

import exercicio19.controleDao.FabricaDeDao;
import exercicio19.controleTransacao.Transacional;
import exercicio19.lance.Lance;
import exercicio19.util.AplicacaoException;
import exercicio19.util.ObjetoNaoEncontradoException;

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
	
	public Produto buscaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	return produtoDAO.recuperaUmProdutoELances(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");	
		}
	}
	
	public List<Produto> recuperaProdutos()
	{	return produtoDAO.recuperaListaDeProdutos();
	}
	
	public Set<Produto> recuperaProdutosELances()
	{	return produtoDAO.recuperaConjuntoDeProdutosELances();
	}
	
	@Transacional
	public Lance atribuiLanceVencedorAProduto(long idProduto)
		throws AplicacaoException
	{	
		Produto produto;
		Lance maiorLance;

		try
		{	produto = produtoDAO.getPorId(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado.");
		}		
		
		try
		{	maiorLance = produtoDAO.recuperaUltimoLance(produto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException
				("Este produto não possui nenhum lance.");
		}		
		
		// Designa lance vencedor a produto
		produto.setLanceVencedor(maiorLance);

		// Designa a produto a data de sua venda
		produto.setDataVenda(maiorLance.getDataCriacao());

		return maiorLance;
	}
	
	public Lance recuperaLanceVencedorDeProduto(long idProduto)
		throws AplicacaoException
	{	
		Produto produto;
		
		try
		{	produto = produtoDAO.recuperaUmProdutoELanceVencedor(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}
		
		Lance lanceVencedor = produto.getLanceVencedor();

		if (lanceVencedor == null)
		{	throw new AplicacaoException
				("Este produto não possui um lance vencedor.");
		}

		return lanceVencedor;
	}
}