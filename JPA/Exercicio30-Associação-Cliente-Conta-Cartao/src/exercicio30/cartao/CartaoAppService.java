package exercicio30.cartao;

import java.util.List;

import exercicio30.controleDao.FabricaDeDao;
import exercicio30.controleTransacao.Transacional;
import exercicio30.util.AplicacaoException;
import exercicio30.util.ObjetoNaoEncontradoException;

public class CartaoAppService
{	
	private static CartaoDAO cartaoDAO;
    
    @SuppressWarnings("unchecked")
    public CartaoAppService ()
    {
        try 
        {   cartaoDAO = FabricaDeDao
        		.getDao(CartaoDAO.class, Cartao.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

    @Transacional
	public long inclui(Cartao umCartao) 
	{	return cartaoDAO.inclui(umCartao).getId();
	}

    @Transacional
	public void altera(Cartao umCartao) 
	{	cartaoDAO.altera(umCartao);
	}
	
    @Transacional
	public void exclui(Cartao umCartao) 
		throws AplicacaoException
	{	
		Cartao cartao = null;
		try
		{	cartao = cartaoDAO.getPorId(umCartao.getId());
		} 
		catch (ObjetoNaoEncontradoException e1)
		{	throw new AplicacaoException ("Cartão não encontrado.");
		}
	
		cartao.getClienteConta().setCartoes(null);
			
		cartaoDAO.exclui(cartao);
	}
	
	public Cartao recuperaUmCartao(long numero) 
		throws AplicacaoException
	{	try
		{	return cartaoDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException('\n' + "Cartão não encontrado.");
			
		}
	}

	public List<Cartao> recuperaListaDeCartoes()
	{	return cartaoDAO.recuperaListaDeCartoes();
	}
}