package exercicio24.pagamento;

import java.util.List;

import exercicio24.controleDao.FabricaDeDao;
import exercicio24.controleTransacao.Transacional;
import exercicio24.util.AplicacaoException;
import exercicio24.util.ObjetoNaoEncontradoException;

public class PagamentoAppService
{	
	private static PagamentoDAO pagamentoDAO;
    
    @SuppressWarnings("unchecked")
    public PagamentoAppService ()
    {
        try 
        {   pagamentoDAO = FabricaDeDao
        		.getDao(PagamentoDAO.class, Pagamento.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

    @Transacional
	public long inclui(Pagamento umPagamento) 
	{	return pagamentoDAO.inclui(umPagamento).getId();
	}

    @Transacional
	public void altera(Pagamento umPagamento) 
	{	pagamentoDAO.altera(umPagamento);
	}

    @Transacional
	public void exclui(long numero) throws AplicacaoException
	{	Pagamento umPagamento;
    	try
		{	umPagamento = pagamentoDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("CLiente n�o encontrado.");
		}

		pagamentoDAO.exclui(umPagamento);
	}

	public Pagamento recuperaUmPagamento(long numero) 
		throws AplicacaoException
	{	try
		{	return pagamentoDAO.getPorId(numero);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Pagamento n�o encontrado.");
		}
	}

	public Cartao recuperaUmPagamentoEmCartao(long numero) 
		throws AplicacaoException
	{	try
		{	return pagamentoDAO.recuperaUmPagamentoEmCartao(numero);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Pagamento n�o encontrado.");
		}
	}

	public List<Pagamento> recuperaListaDePagamentos()
	{	return pagamentoDAO.recuperaListaDePagamentos();
	}
}