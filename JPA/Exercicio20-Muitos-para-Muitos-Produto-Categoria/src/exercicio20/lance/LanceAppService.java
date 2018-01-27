package exercicio20.lance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio20.controleDao.FabricaDeDao;
import exercicio20.controleTransacao.Transacional;
import exercicio20.produto.Produto;
import exercicio20.produto.ProdutoDAO;
import exercicio20.util.AplicacaoException;
import exercicio20.util.ObjetoNaoEncontradoException;
import exercicio20.util.Util;

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

	        // O método acima tb poderia ser chamado assim:
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
		// A  implementação do  método  getPorIdComLock(umProduto.getId()) 
		// impede que dois  lances  sejam  cadastrados em  paralelo, i. é, 
		// os lances  devem ser  cadastrados  obedecendo a uma fila.  Isto
		// impede que o valor do segundo lance seja  inferior  ao valor do
		// primeiro.
	
		try
		{	produtoDAO.getPorIdComLock(umProduto.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");
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
		{	mensagens.add("A data de emissão do lance não pode ser anterior a " 
				+ Util.dateToStr(dataUltimoLance));
			deuErro = true;
		}
	
		Date hoje = new Date(System.currentTimeMillis());
		
		if(dataCriacao.after(hoje))
		{	mensagens.add("A data de emissão do lance não pode ser posterior à data de hoje: " 
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
		throws AplicacaoException 
	{
		Lance lance;
		try
		{	lance = lanceDAO.getPorId(umLance.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado.");
		}

		Produto produto = null;
		try
		{	produto = produtoDAO.recuperaUmProdutoQuePossuiUmDeterminadoLanceVencedor(umLance.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	
		}

		if (produto != null)
		{	produto.setLanceVencedor(null);
		}

		lanceDAO.exclui(lance);
	}	
	
	public Lance getPorId(long numero)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.getPorId(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado.");
		}
	}
	
	public Lance recuperaUmLanceComProduto(long numero)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.recuperaUmLanceComProduto(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado.");
		}
	}

	public List<Lance> recuperaListaDeLances()
	{	return lanceDAO.recuperaListaDeLances();
	}
}