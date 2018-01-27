package ex02.ejb;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex02.dao.ProdutoDAO;
import ex02.dominio.Produto;
import ex02.excecao.AplicacaoException;
import ex02.excecao.ObjetoNaoEncontradoException;
import ex02.util.Util;

@Stateless
@RemoteBinding(jndiBinding="exercicio02.ProdutoEJBBean/remote")
@Remote(ProdutoEJBRemote.class)
public class ProdutoEJBBean //implements ProdutoEJBRemote
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;

	private ProdutoDAO produtoDAO; 

	// A anotação @PostConstruct define um método que será executado
	// uma   única  vez   logo  após  o  objeto  ProdutoEJBBean  ser 
	// instanciado.  Este  método  é  executado  após  as   injeções
	// de dependências serem realizadas.
	
	// As anotações que definem métodos callback são:

	// @PostConstruct   -   Statefull, Stateless, MDB
	// @PreDestroy      -   Statefull, Stateless, MDB
	// @PrePassivate    -   Statefull
	// @PostActivate    -   Statefull

	@PostConstruct
	public void init()
	{	produtoDAO = new ProdutoDAO(em);
	}

	@SuppressWarnings("unchecked")
	public long inclui(String nome, String descricao, 
	                   double lanceMinimo, String dtCadastro) 
		throws AplicacaoException
	{	
		boolean deuErro = false;
		ArrayList mensagens = new ArrayList();
		
		if(lanceMinimo <= 0)
		{	deuErro = true;
			mensagens.add("Lance minimo invalido.");
		}

		Date dataCadastro = null;
		
		if (!Util.dataValida(dtCadastro))
		{	deuErro = true;
			mensagens.add("Data de cadastro invalida.");
		}
		else
		{	dataCadastro = Util.strToDate(dtCadastro);
			Date hoje = new Date(System.currentTimeMillis());
		
			if (dataCadastro.after(hoje))
			{	deuErro = true;
				mensagens.add(
					"A data de cadastramento do produto nao pode ser " +
					 "posterior à data de hoje: " + Util.dateToStr(hoje));
			}
		}
		
		long numero;
		
		if (!deuErro)
		{	Produto umProduto = new Produto(nome, descricao, lanceMinimo, dataCadastro);
			numero = produtoDAO.inclui(umProduto);
		}
		else
		{	throw new AplicacaoException(mensagens);
		}

		return numero;
	}

	public boolean altera(Produto umProduto) 
	{	return produtoDAO.altera(umProduto);
	} 

	public boolean exclui(Produto umProduto) 
		throws AplicacaoException
	{	try
		{	Produto produto = produtoDAO.recuperaUmProdutoELances(umProduto.getId());

			if(produto.getLances().size() > 0)
			{	throw new AplicacaoException("Este produto possui lances e nao pode ser removido");
			}

			return produtoDAO.exclui(produto);
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException
	{	try
		{	Produto umProduto = produtoDAO.recuperaUmProduto(numero);
			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	Produto umProduto = produtoDAO.recuperaUmProdutoELances(numero);
			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
	}

	public List<Produto> recuperaProdutosELances()
	{	return produtoDAO.recuperaProdutosELances();
	}
}