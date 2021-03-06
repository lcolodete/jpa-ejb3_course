package exercicio21.lance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio21.controleDao.FabricaDeDao;
import exercicio21.controleTransacao.Transacional;
import exercicio21.produto.Produto;
import exercicio21.produto.ProdutoDAO;
import exercicio21.util.AplicacaoException;
import exercicio21.util.ObjetoNaoEncontradoException;
import exercicio21.util.Util;

public class LanceAppService
{   
	private static LanceDAO lanceDAO;
	private static ProdutoDAO produtoDAO;
    
    @SuppressWarnings("unchecked")
    public LanceAppService ()
    {
        try 
        {   lanceDAO = FabricaDeDao
        		.getDao(LanceDAO.class, Lance.class);

	        produtoDAO = FabricaDeDao
	        	.getDao(ProdutoDAO.class, Produto.class);

	        // O m�todo acima tb poderia ser chamado assim:
           	// lanceDAO = FabricaDeDao.<LanceDAO>getDao(LanceDAO.class, Lance.class);
        } 
        catch (Exception e) 
        {   e.printStackTrace();
            System.exit(1);
        }
    }

	@Transacional
	public long inclui(double valor, Date dataCriacao, Produto umProduto) 
		throws AplicacaoException
	{
		// A  implementa��o do  m�todo  getPorIdComLock(umProduto.getId()) 
		// impede que dois  lances  sejam  cadastrados em  paralelo, i. �, 
		// os lances  devem ser  cadastrados  obedecendo a uma fila.  Isto
		// impede que o valor do segundo lance seja  inferior  ao valor do
		// primeiro.
	
		try
		{	produtoDAO.getPorIdComLock(umProduto.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto n�o encontrado");
		}

		Lance umLance = new Lance();
	
		Lance ultimoLance; 
		try
		{	ultimoLance = produtoDAO.recuperaUltimoLance(umProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	ultimoLance = null;	
		}
	
		double valorUltimoLance;
		Date   dataUltimoLance;
		
		if (ultimoLance == null)
		{	valorUltimoLance = umProduto.getLanceMinimo() - 1;
			dataUltimoLance  = umProduto.getDataCadastro();
		}
		else
		{	valorUltimoLance = ultimoLance.getValor();
			dataUltimoLance  = ultimoLance.getDataCriacao();
		}
		
		boolean deuErro = false;
		ArrayList<String> mensagens = new ArrayList<String>();
	
		if(valor <= valorUltimoLance)
		{	mensagens.add("O valor do lance tem que ser superior a " + valorUltimoLance);
			deuErro = true;
		}
	
		if(dataCriacao.before(dataUltimoLance))
		{	mensagens.add("A data de emiss�o do lance n�o pode ser anterior a " 
				+ Util.dateToStr(dataUltimoLance));
			deuErro = true;
		}
	
		Date hoje = new Date(System.currentTimeMillis());
		
		if(dataCriacao.after(hoje))
		{	mensagens.add("A data de emiss�o do lance n�o pode ser posterior � data de hoje: " 
				+ Util.dateToStr(hoje));
			deuErro = true;
		}
	
		Lance lance;
		
		if (!deuErro)
		{	umLance.setValor(valor);
			umLance.setDataCriacao(dataCriacao);
			umLance.setProduto(umProduto); // Evita a recuperacao 
										   // de todos os lances 
										   // de um produto.
			lance = lanceDAO.inclui(umLance);
		}
		else
		{	throw new AplicacaoException(mensagens);
		}
		
		return lance.getId();
	}	
	
	@Transacional
	public void exclui(Lance umLance) 
	{	lanceDAO.exclui(umLance);		
	}
	
	public Lance getPorId(long numero)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance n�o encontrado.");
		}
	}
	
	public Lance recuperaUmLanceComProduto(long numero)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.recuperaUmLanceComProduto(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance n�o encontrado.");
		}
	}

	public List<Lance> recuperaListaDeLances()
	{	return lanceDAO.recuperaListaDeLances();
	}
}