package exercicio30.cliente;

import java.util.List;

import exercicio30.clienteConta.ClienteConta;
import exercicio30.clienteConta.ClienteContaDAO;
import exercicio30.controleDao.FabricaDeDao;
import exercicio30.controleTransacao.Transacional;
import exercicio30.util.AplicacaoException;
import exercicio30.util.ObjetoNaoEncontradoException;

public class ClienteAppService
{	
	private static ClienteDAO clienteDAO;
	private static ClienteContaDAO clienteContaDAO;
    
    @SuppressWarnings("unchecked")
    public ClienteAppService ()
    {
        try 
        {	clienteDAO = FabricaDeDao
        		.getDao(ClienteDAO.class, Cliente.class);

        	clienteContaDAO = FabricaDeDao
        		.getDao(ClienteContaDAO.class, ClienteConta.class);
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
	public void exclui(Cliente umCliente) 
		throws AplicacaoException
	{	
		Cliente cliente = null;
		try
		{	cliente = clienteDAO.getPorId(umCliente.getId());
		} 
		catch (ObjetoNaoEncontradoException e1)
		{	throw new AplicacaoException ("Cliente não encontrado.");
		}

		List<ClienteConta> lista = clienteContaDAO.recuperaListaDeClienteConta();

		if(lista != null)
		{	throw new AplicacaoException("Este Cliente possui " +
			                             "conta(s) " +
			                             "e nao pode ser removido");
		}

		clienteDAO.exclui(cliente);
	}
	
	public Cliente recuperaUmCliente(long numero) 
		throws AplicacaoException
	{	try
		{	return clienteDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException('\n' + "Cartão não encontrado.");
			
		}
	}

	public List<Cliente> recuperaListaDeClientes()
	{	return clienteDAO.recuperaListaDeClientes();
	}
}


