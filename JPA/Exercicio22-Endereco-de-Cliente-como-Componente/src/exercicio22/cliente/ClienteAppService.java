package exercicio22.cliente;

import java.util.List;

import exercicio22.controleDao.FabricaDeDao;
import exercicio22.controleTransacao.Transacional;
import exercicio22.util.AplicacaoException;
import exercicio22.util.ObjetoNaoEncontradoException;

public class ClienteAppService
{	
	private static ClienteDAO clienteDAO;
    
    @SuppressWarnings("unchecked")
    public ClienteAppService ()
    {
        try 
        {   clienteDAO = FabricaDeDao
        		.getDao(ClienteDAO.class, Cliente.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

    @Transacional
	public long inclui(Cliente umCliente) 
	{	return clienteDAO.inclui(umCliente).getNumero();
	}

    @Transacional
	public void altera(Cliente umCliente) 
	{	clienteDAO.altera(umCliente);
	}

    @Transacional
	public void exclui(long numero) throws AplicacaoException
	{	Cliente umCliente;
    	try
		{	umCliente = clienteDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("CLiente não encontrado.");
		}

		clienteDAO.exclui(umCliente);
	}

	public Cliente recuperaUmCliente(long numero) 
		throws AplicacaoException
	{	try
		{	return clienteDAO.getPorId(numero);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Cliente não encontrado.");
		}
	}

	public List<Cliente> recuperaListaDeClientes()
	{	return clienteDAO.recuperaListaDeClientes();
	}
}