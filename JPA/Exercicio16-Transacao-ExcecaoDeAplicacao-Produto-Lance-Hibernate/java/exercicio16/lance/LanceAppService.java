package exercicio16.lance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio16.controleTransacao.Transacional;
import exercicio16.produto.Produto;
import exercicio16.produto.ProdutoDAO;
import exercicio16.produto.ProdutoDAOImpl;
import exercicio16.util.AplicacaoException;
import exercicio16.util.ObjetoNaoEncontradoException;
import exercicio16.util.Util;

public class LanceAppService
{	
	private static LanceDAO lanceDAO = new LanceDAOImpl();
	private static ProdutoDAO produtoDAO = new ProdutoDAOImpl();

	@Transacional
	@SuppressWarnings("unchecked")
	public long inclui(double valor, Date dataCriacao, Produto umProduto) 
		throws AplicacaoException
	{

		Lance umLance = new Lance();

		// A implementa��o do m�todo recuperaUltimoLance(umProduto) 
		// impede que dois lances sejam cadastrados em paralelo.
		// Como este m�todo p�e um lock em Produto, a inser��o de 
		// dois produtos acontece sempre em s�rie, i. �, obedecendo
		// a uma fila. Isto impede que o valor do segundo lance seja
		// inferior ao valor do primeiro.

		Lance ultimoLance = produtoDAO.recuperaUltimoLance(umProduto);

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

		long numero;
		
		if (!deuErro)
		{	umLance.setValor(valor);
			umLance.setDataCriacao(dataCriacao);
			umLance.setProduto(umProduto); // Evita a recuperacao 
										   // de todos os lances 
										   // de um produto.
			numero = lanceDAO.inclui(umLance);
		}
		else
		{	throw new AplicacaoException(mensagens);
		}
		
		return numero;
	}	
	
	@Transacional
	public void exclui(Lance umLance) 
		throws AplicacaoException 
	{	try
		{	lanceDAO.exclui(umLance);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance n�o encontrado.");
		}
	}
	
	@Transacional
	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException
	{	try
		{	return lanceDAO.recuperaUmLance(numero);
		}
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException ("Lance n�o encontrado");
		}
	}
	
	@Transacional
	public List<Lance> recuperaLances()
	{	return lanceDAO.recuperaLances();
	}
}