package exercicio28.cliente;

import java.util.List;

import exercicio28.controleDao.FabricaDeDao;
import exercicio28.controleTransacao.Transacional;
import exercicio28.util.AplicacaoException;
import exercicio28.util.ObjetoNaoEncontradoException;

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
	{	return clienteDAO.inclui(umCliente).getId();
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
		{	throw new AplicacaoException("CLiente n�o encontrado.");
		}

		clienteDAO.exclui(umCliente);
	}

	public Cliente recuperaUmCliente(long numero) 
		throws AplicacaoException
	{	try
		{	return clienteDAO.getPorId(numero);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Cliente n�o encontrado.");
		}
	}

	public Cliente recuperaUmClienteETelefones(long id) 
		throws AplicacaoException
	{	try
		{	return clienteDAO.recuperaUmClienteETelefones(id);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Cliente n�o encontrado.");
		}
	}

	public List<Cliente> recuperaListaDeClientes()
	{	return clienteDAO.recuperaListaDeClientes();
	}

	public List<Cliente> recuperaListaDeClientesETelefones()
	{	return clienteDAO.recuperaListaDeClientesETelefones();
	}
}