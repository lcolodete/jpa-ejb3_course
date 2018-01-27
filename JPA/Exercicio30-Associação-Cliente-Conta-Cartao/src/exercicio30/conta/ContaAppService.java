package exercicio30.conta;

import java.util.List;

import exercicio30.clienteConta.ClienteConta;
import exercicio30.clienteConta.ClienteContaDAO;
import exercicio30.controleDao.FabricaDeDao;
import exercicio30.controleTransacao.Transacional;
import exercicio30.util.AplicacaoException;
import exercicio30.util.ObjetoNaoEncontradoException;

public class ContaAppService
{	
	private static ContaDAO contaDAO;

	private static ClienteContaDAO clienteContaDAO;
	
    @SuppressWarnings("unchecked")
    public ContaAppService ()
    {
        try 
        {   contaDAO = FabricaDeDao
        		.getDao(ContaDAO.class, Conta.class);
	    	clienteContaDAO = FabricaDeDao
	    		.getDao(ClienteContaDAO.class, ClienteConta.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

    @Transacional
	public long inclui(Conta umaConta) 
	{	return contaDAO.inclui(umaConta).getId();
	}

    @Transacional
	public void altera(Conta umaConta) 
	{	contaDAO.altera(umaConta);
	}
	
    @Transacional
	public void exclui(Conta umaConta) 
		throws AplicacaoException
	{	
		Conta conta = null;
		try
		{	conta = contaDAO.getPorId(umaConta.getId());
		} 
		catch (ObjetoNaoEncontradoException e1)
		{	throw new AplicacaoException ("Conta não encontrada.");
		}

		List<ClienteConta> lista = clienteContaDAO.recuperaListaDeClienteConta();

		if(lista != null)
		{	throw new AplicacaoException("Esta Conta possui " +
			                             "cliente(s) " +
			                             "e nao pode ser removida");
		}

		contaDAO.exclui(conta);
	}
	
	public Conta recuperaUmaConta(long numero) 
		throws AplicacaoException
	{	try
		{	return contaDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException('\n' + "Conta não encontrada.");
			
		}
	}

	public List<Conta> recuperaListaDeContas()
	{	return contaDAO.recuperaListaDeContas();
	}
}

