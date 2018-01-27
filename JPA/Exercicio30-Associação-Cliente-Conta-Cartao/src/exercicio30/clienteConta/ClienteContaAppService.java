package exercicio30.clienteConta;

import java.util.List;

import exercicio30.controleDao.FabricaDeDao;
import exercicio30.controleTransacao.Transacional;
import exercicio30.util.AplicacaoException;
import exercicio30.util.ObjetoNaoEncontradoException;

public class ClienteContaAppService
{	
	private static ClienteContaDAO clienteContaDAO;
    
    @SuppressWarnings("unchecked")
    public ClienteContaAppService ()
    {
        try 
        {   clienteContaDAO = FabricaDeDao
        		.getDao(ClienteContaDAO.class, ClienteConta.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

    @Transacional
	public IdClienteConta inclui(ClienteConta umClienteConta) 
	{	return clienteContaDAO.inclui(umClienteConta).getId();
	}

    @Transacional
	public void altera(ClienteConta umClienteConta) 
	{	clienteContaDAO.altera(umClienteConta);
	}
	
    @Transacional
	public void exclui(ClienteConta umClienteConta) 
		throws AplicacaoException
	{	
		ClienteConta clienteConta = null;
		try
		{	clienteConta = clienteContaDAO.getPorId(umClienteConta.getId());
		} 
		catch (ObjetoNaoEncontradoException e1)
		{	throw new AplicacaoException ("ClienteConta não encontrado.");
		}

		clienteContaDAO.exclui(clienteConta);
	}
    
	public ClienteConta recuperaUmClienteConta(IdClienteConta id) 
		throws AplicacaoException
	{	try
		{	return clienteContaDAO.getPorId(id);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException('\n' + "ClienteConta não encontrado.");
		}
	}

	public ClienteConta recuperaUmClienteContaComCartoes(long idCliente, long idConta) 
		throws AplicacaoException
	{	try
		{	return clienteContaDAO.recuperaUmClienteContaComCartoes(idCliente, idConta);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException('\n' + "ClienteConta não encontrado.");
		}
	}

	public List<ClienteConta> recuperaListaDeClienteConta()
	{	return clienteContaDAO.recuperaListaDeClienteConta();
	}
}