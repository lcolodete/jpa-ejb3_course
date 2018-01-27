package exercicio21.produto;

import java.util.List;
import java.util.Set;

import exercicio21.categoria.Categoria;
import exercicio21.categoria.CategoriaDAO;
import exercicio21.controleDao.FabricaDeDao;
import exercicio21.controleTransacao.Transacional;
import exercicio21.lance.Lance;
import exercicio21.prodcat.IdProdutoCategoria;
import exercicio21.prodcat.ProdutoCategoria;
import exercicio21.prodcat.ProdutoCategoriaDAO;
import exercicio21.util.AplicacaoException;
import exercicio21.util.ObjetoNaoEncontradoException;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO;
	private static ProdutoCategoriaDAO produtoCategoriaDAO;
	private static CategoriaDAO categoriaDAO;
    
    public ProdutoAppService ()
    {	try 
        {   
        	produtoDAO = FabricaDeDao
        		.getDao(ProdutoDAO.class, Produto.class);

        	produtoCategoriaDAO = FabricaDeDao
        		.getDao(ProdutoCategoriaDAO.class, ProdutoCategoria.class);
        	
        	categoriaDAO = FabricaDeDao
    			.getDao(CategoriaDAO.class, Categoria.class);

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
	public long inclui(Produto umProduto, long idCategoria) 
		throws AplicacaoException
	{	
		try
		{	categoriaDAO.getPorId(idCategoria);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria nao encontrada.");
		}		

		umProduto = produtoDAO.inclui(umProduto);
		
/* ==> */ // Cria o objeto produtoCategoria	
		ProdutoCategoria produtoCategoria = 
				new ProdutoCategoria(umProduto.getId(), idCategoria);
		
/* ==> */ // Salva o objeto produtoCategoria
		produtoCategoriaDAO.inclui(produtoCategoria);

		return umProduto.getId();
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
	{	try
		{	return produtoDAO.recuperaUmProdutoELances(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");	
		}
	}
	
	public List<Produto> recuperaListaDeProdutos()
	{	return produtoDAO.recuperaListaDeProdutos();
	}
	
	public Set<Produto> recuperaConjuntoDeProdutosELances()
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

	@Transacional
	public void atribuiCategoriaAProduto(long idProduto, long idCategoria)
		throws AplicacaoException
	{	
		try
		{	produtoDAO.getPorId(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}		
		
		try
		{	categoriaDAO.getPorId(idCategoria);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}		
		
/* ==> */
		try
		{	produtoCategoriaDAO
				.getPorId(new IdProdutoCategoria(idProduto, idCategoria));	

			throw new AplicacaoException
					("Este produto já possui esta categoria.");
		}
		catch(ObjetoNaoEncontradoException e)
		{	// Se não encontrar significa que o produto 
		    // ainda não possui essa categoria.
		}		
							
		/* ==> */ // Cria o objeto produtoCategoria	
		ProdutoCategoria produtoCategoria = 
				new ProdutoCategoria(idProduto, idCategoria);

/* ==> */ // Salva o objeto produtoCategoria
		produtoCategoriaDAO.inclui(produtoCategoria);

		System.out.println(">>>>>>>>>>> Atribuiu categoria " + idCategoria + " a produto " + idProduto);
	}
	
	@Transacional
	public void removeCategoriaDeProduto(long idProduto, long idCategoria)
		throws AplicacaoException
	{		
		try
		{	produtoDAO.getPorId(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}		

/* ==> */	
		IdProdutoCategoria idProdutoCategoria = new IdProdutoCategoria(idProduto, idCategoria);
		
		ProdutoCategoria produtoCategoria;
/* ==> */
		try
		{	produtoCategoria = produtoCategoriaDAO.getPorId(idProdutoCategoria);	
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException
				("Este produto não possui esta categoria.");
		}		
		
/* ==> */	
		produtoCategoriaDAO.exclui(produtoCategoria);
	}
	
	public Produto recuperaUmProdutoECategorias(long numero) 
		throws AplicacaoException
	{		
		try 
		{	return produtoDAO.recuperaUmProdutoECategorias(numero);
		}
		catch (ObjetoNaoEncontradoException e) 
		{	throw new AplicacaoException("Produto não encontrado.");
		}
	}

	public List<Produto> recuperaListaDeProdutosCategorias() 
		throws AplicacaoException
	{	return produtoDAO.recuperaListaDeProdutosCategorias();
	}
}