package exercicio20.produto;

import java.util.List;
import java.util.Set;

import exercicio20.categoria.Categoria;
import exercicio20.categoria.CategoriaDAO;
import exercicio20.controleDao.FabricaDeDao;
import exercicio20.controleTransacao.Transacional;
import exercicio20.lance.Lance;
import exercicio20.util.AplicacaoException;
import exercicio20.util.ObjetoNaoEncontradoException;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO;
	private static CategoriaDAO categoriaDAO;
    
    public ProdutoAppService ()
    {	try 
        {   
        	produtoDAO = FabricaDeDao
        		.getDao(ProdutoDAO.class, Produto.class);

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
		Categoria umaCategoria;			

		try
		{	umaCategoria = categoriaDAO.getPorId(idCategoria);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}		

		umProduto = produtoDAO.inclui(umProduto);

		// Neste caso, como a Categoria e o Produto já são persistentes
		// não  é  preciso  o  cascade="save-update"  na  definição  da 
		// propriedade  categorias  no  arquivo de mapeamento da classe 
		// Produto.  O  "save-update"  serviria  para  salvar o  objeto 
		// Categoria se ele ainda não fosse persistente.

		// Adiciona a categoria ao conjunto de categorias do produto
		umProduto.getCategorias().add(umaCategoria);
		
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
		Produto produto;
		Categoria categoria;
		
		try
		{	produto = produtoDAO.getPorId(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}		
		
		try
		{	categoria = categoriaDAO.getPorId(idCategoria);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}		

		// Neste caso, como a Categoria e o Produto já são persistentes
		// não é preciso o cascade="save-update" na definição da 
		// propriedade categorias no arquivo de mapeamento da classe 
		// Produto. O "save-update" serviria para salvar o objeto 
		// Categoria se ele ainda não fosse persistente.

		if(!produto.getCategorias().add(categoria))
		{	throw new AplicacaoException
				("Este produto já possui esta categoria.");
		}	
	}
	
	@Transacional
	public void removeCategoriaDeProduto(long idProduto, long idCategoria)
		throws AplicacaoException
	{		
		Produto produto;
		Categoria categoria;
		
		try
		{	produto = produtoDAO.getPorId(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado.");
		}		
		
		try
		{	categoria = categoriaDAO.getPorId(idCategoria);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}		

		// Remove a categoria do produto
		if(!produto.getCategorias().remove(categoria))
		{	throw new AplicacaoException
				("Este produto não possui esta categoria.");
		}	
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
}