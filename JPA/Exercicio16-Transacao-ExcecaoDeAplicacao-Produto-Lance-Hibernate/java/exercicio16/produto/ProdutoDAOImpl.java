package exercicio16.produto;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import exercicio16.lance.Lance;
import exercicio16.util.HibernateUtil;
import exercicio16.util.InfraestruturaException;
import exercicio16.util.ObjetoNaoEncontradoException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	try
		{	Session sessao = HibernateUtil.getSession();

			sessao.save(umProduto);
			
			return umProduto.getId();
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void altera(Produto umProduto) 
	{	try
		{	Session sessao = HibernateUtil.getSession();

			sessao.update(umProduto);
		}
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void exclui(Produto umProduto) 
	{	try
		{	Session sessao = HibernateUtil.getSession();
		
			sessao.delete(umProduto);
		}
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProduto(long numero) 
        throws ObjetoNaoEncontradoException
	{	try
		{	Session sessao = HibernateUtil.getSession();

			Produto umProduto = (Produto)sessao
				.get(Produto.class, new Long(numero));
			
			if (umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umProduto;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
        throws ObjetoNaoEncontradoException
	{	try
		{	Session sessao = HibernateUtil.getSession();

			String busca = "from Produto p " + 
			               "left outer join fetch p.lances " +
			               "where p.id = :id";

			Produto umProduto =
				(Produto) sessao.createQuery(busca)
							    .setLong("id", numero)
							    .uniqueResult();

			if (umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umProduto;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutos()
	{	try
		{	Session sessao = HibernateUtil.getSession();

			List<Produto> produtos = sessao
				.createQuery("from Produto order by id")
				.list();

			return produtos;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	try
		{	Session sessao = HibernateUtil.getSession();

			List<Produto> produtos = sessao
				.createQuery("select distinct p from Produto p " + 
						     "left outer join fetch p.lances " +
						     "order by p.id asc")
				.list();

			return produtos;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Lance recuperaUltimoLance(Produto produto)
	{	try
		{	Session sessao = HibernateUtil.getSession();

			String busca = "select lance " +
				           "from Lance lance " +
	                       "left outer join lance.produto produto " +
	                       "where produto.id = :id " +
	                       "and lance.valor = (select max(valor) " +
	                                          "from Lance l " +
	                                          "where l.produto.id = :id)"; 
	
			Lance umLance = (Lance)sessao
				.createQuery(busca)
				.setLong("id", produto.getId())
				.uniqueResult();

			return umLance;
		}
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}
}