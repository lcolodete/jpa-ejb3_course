package ex02.ejb;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex02.dao.LanceDAO;
import ex02.dao.ProdutoDAO;
import ex02.dominio.Lance;
import ex02.dominio.Produto;
import ex02.excecao.AplicacaoException;
import ex02.excecao.ObjetoNaoEncontradoException;
import ex02.util.Util;

@Stateless
@RemoteBinding(jndiBinding="exercicio02.LanceEJBBean/remote")

// @Remote(LanceEJBRemote.class) 

// Em vez do EJB implementar a interface Remota ou Local é  possível
// acrescentar a anotação acima que indica qual é a interface Remota
// e, neste caso, a interface remota não precisa  ser  implementada,
// isto é, o "implements LanceEJBRemote" pode ser removido.

public class LanceEJBBean implements LanceEJBRemote     
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;

	private LanceDAO lanceDAO; 	
	private ProdutoDAO produtoDAO; 

	// A anotação @PostConstruct define um método que será executado
	// uma   única  vez   logo  após  o   objeto  LanceEJBBean   ser 
	// instanciado.  Este  método  é  executado  após  as   injeções
	// de dependências serem realizadas.
	
	// As anotações que definem métodos callback são:

	// @PostConstruct   -   Statefull, Stateless, MDB
	// @PreDestroy      -   Statefull, Stateless, MDB
	// @PrePassivate    -   Statefull
	// @PostActivate    -   Statefull
	
	@PostConstruct 
	public void init()
	{	lanceDAO = new LanceDAO(em);
		produtoDAO = new ProdutoDAO(em);
	}
	
	@SuppressWarnings("unchecked")
	public long inclui(double valor, String dtCriacao, Produto umProduto) 
		throws AplicacaoException
	{	
		ArrayList mensagens = new ArrayList();

		try
		{	produtoDAO.recuperaUmProdutoComLock(umProduto.getId());
		}
		catch(ObjetoNaoEncontradoException e)
		{	mensagens.add("Produto não encontrado");
			throw new AplicacaoException(mensagens);
		}

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

		if(valor <= valorUltimoLance)
		{	deuErro = true;
			mensagens.add("O valor do lance tem que ser superior a " 
					      + valorUltimoLance);
		}
		
		Date dataCriacao = null;
		
		if (!Util.dataValida(dtCriacao))
		{	deuErro = true;
			mensagens.add("Data de cadastro invalida.");
		}
		else
		{	dataCriacao = Util.strToDate(dtCriacao);

			if(dataCriacao.before(dataUltimoLance))
			{	deuErro = true;
				mensagens.add("A data de emissão do lance não pode ser anterior a " 
					+ Util.dateToStr(dataUltimoLance));
			}

			Date hoje = new Date(System.currentTimeMillis());
		
			if(dataCriacao.after(hoje))
			{	deuErro = true;
				mensagens.add("A data de emissão do lance não pode ser posterior à data de hoje: " 
					+ Util.dateToStr(hoje));
			}
		}
			
		long numero;

		if (!deuErro)
		{	Lance umLance = new Lance(valor, dataCriacao, umProduto); 
			// Evita a recuperacao de todos os lances de um produto.
			numero = lanceDAO.inclui(umLance);
		}
		else
		{	throw new AplicacaoException(mensagens);
		}

		return numero;
	}

	public void exclui(Lance umLance) 
	{	lanceDAO.exclui(umLance);
	} 

	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException
	{	try
		{	Lance umLance = lanceDAO.recuperaUmLance(numero);
			return umLance;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado");
		}
	}

	public List<Lance> recuperaLances()
	{	return lanceDAO.recuperaLances();
	} 
}