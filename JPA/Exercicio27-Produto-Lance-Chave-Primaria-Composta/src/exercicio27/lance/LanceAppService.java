package exercicio27.lance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio27.controleDao.FabricaDeDao;
import exercicio27.controleTransacao.Transacional;
import exercicio27.produto.Produto;
import exercicio27.produto.ProdutoDAO;
import exercicio27.util.AplicacaoException;
import exercicio27.util.ObjetoNaoEncontradoException;
import exercicio27.util.Util;

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
	public IdLance inclui(long idProduto, long numeroLance, 
	           double valor,   Date dataCriacao, 
	           Produto umProduto) 
	throws AplicacaoException
	{
		Lance umLance = new Lance();
		
		// A   execução    do   método   getPorIdComLock(id)   abaixo
		// impede  que  dois lances  sejam  cadastrados em  paralelo.
		// Como este  método põe  um lock em  Produto, a inserção  de 
		// dois  lances  acontece  sempre em  série, i. é, obedecendo
		// a uma fila. Isto impede que o valor do  segundo lance seja
		// inferior ao valor do primeiro ou que se tente cadastrar um      
		// lance para um produto que tenha sido removido.
		
		ArrayList<String> mensagens = new ArrayList<String>();
		
		try
		{	produtoDAO.getPorIdComLock(idProduto);
		}
		catch(ObjetoNaoEncontradoException e)
		{	mensagens.add("Produto não encontrado");
			throw new AplicacaoException(mensagens);
		}
		
		// Verifica se o lance já foi cadastrado.
		try
		{	// Para recuperar o ultimoLance de um Produto, é  preciso         
			// recuperar  todos os lances emitidos para esse produto.
			// Por  esse  motivo,  ao  se  tentar  recuperar  o lance 
			// abaixo,  se  ele  já   existir  ele  não   poderá  ser 
			// cadastrado novamente.  
			lanceDAO.getPorId(new IdLance(idProduto, numeroLance));
			mensagens.add("Lance numero " + numeroLance + " já cadastrado.");
			throw new AplicacaoException(mensagens);
		}
		catch(ObjetoNaoEncontradoException e)
		{	// O  lance  cujo  numero  é  "numeroLance" ainda não foi 
			// cadastrado.
		}
		
		boolean deuErro = false;
		
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
		{	umLance.setId(new IdLance(idProduto, numeroLance));
			umLance.setValor(valor);
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
	
	public Lance recuperaUmLancePorIdProdutoNumeroLance(long idProduto, long numeroLance)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.getPorId(new IdLance(idProduto, numeroLance));
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado.");
		}
	}

	public Lance recuperaUmLanceComProduto(long idProduto, long numeroLance)
		throws AplicacaoException	
	{	try
		{	return lanceDAO.recuperaUmLanceComProduto(idProduto, numeroLance);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado.");
		}
	}

	public List<Lance> recuperaListaDeLances()
	{	return lanceDAO.recuperaListaDeLances();
	}
}