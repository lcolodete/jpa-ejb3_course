package exercicio20.categoria;

import java.util.List;

import exercicio20.controleDao.FabricaDeDao;
import exercicio20.controleTransacao.Transacional;
import exercicio20.util.AplicacaoException;
import exercicio20.util.ObjetoNaoEncontradoException;

public class CategoriaAppService
{	
	private static CategoriaDAO categoriaDAO;
    
    @SuppressWarnings("unchecked")
    public CategoriaAppService ()
    {
        try 
        {   categoriaDAO = FabricaDeDao
        		.getDao(CategoriaDAO.class, Categoria.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }
    @Transacional
	public long inclui(Categoria umaCategoria) 
	{	return categoriaDAO.inclui(umaCategoria).getId();
	}

    @Transacional
	public void altera(Categoria umaCategoria) 
	{	categoriaDAO.altera(umaCategoria);
	}

    @Transacional
	public void exclui(long numero) throws AplicacaoException
	{	Categoria umaCategoria;
    	try
		{	umaCategoria = categoriaDAO.
    			recuperaUmaCategoriaEProdutos(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}

		if(umaCategoria.getProdutos().size() > 0)
		{	throw new AplicacaoException(
				"Produtos possuem esta categoria, logo ela não " + 
				"pode ser removida.");
		}
			
		categoriaDAO.exclui(umaCategoria);
	}

	public Categoria recuperaUmaCategoria(long numero) 
		throws AplicacaoException
	{	try
		{	return categoriaDAO.getPorId(numero);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Categoria não encontrada.");
		}
	}

	public List<Categoria> recuperaListaDeCategorias()
	{	return categoriaDAO.recuperaListaDeCategorias();
	}
}