package exercicio15.lance;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio15.controleTransacao.Transacional;
import exercicio15.produto.Produto;
import exercicio15.produto.ProdutoDAO;
import exercicio15.produto.ProdutoDAOImpl;
import exercicio15.util.AplicacaoException;
import exercicio15.util.ObjetoNaoEncontradoException;
import exercicio15.util.Util;

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

		// A implementação do método recuperaUltimoLance(umProduto) 
		// impede que dois lances sejam cadastrados em paralelo.
		// Como este método põe um lock em Produto, a inserção de 
		// dois produtos acontece sempre em série, i. é, obedecendo
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
		{	throw new AplicacaoException("Lance não encontrado.");
		}
	}
	
	public Lance recuperaUmLance(long numero)
	{	return lanceDAO.recuperaUmLance(numero);
	}
	
	public List<Lance> recuperaLances()
	{	return lanceDAO.recuperaLances();
	}
}