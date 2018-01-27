package exercicio15.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import exercicio15.lance.Lance;
import exercicio15.util.InfraestruturaException;
import exercicio15.util.JPAUtil;
import exercicio15.util.ObjetoNaoEncontradoException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			em.persist(umProduto);
			
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void altera(Produto umProduto) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			em.getReference(Produto.class, new Long(umProduto.getId()));
		
			em.merge(umProduto);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void exclui(Produto umProduto) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();
		
			Produto c = em.getReference(Produto.class, new Long(umProduto.getId()));
	
			em.remove(c);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProduto(long numero) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = (Produto)em
				.find(Produto.class, new Long(numero));
			
			return umProduto;
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutos() 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "select p from Produto p order by id";

			return em.createQuery(busca).getResultList();
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "select p from Produto p " + 
			               "left outer join fetch p.lances " +
			               "where p.id = :id";

			Produto umProduto =	(Produto) em
				.createQuery(busca)
				.setParameter("id", new Long(numero))
				.getSingleResult();

			return umProduto;
		} 
		catch(NoResultException e)
		{	return null;
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			List<Produto> produtos = em
				.createQuery("from Produto p " + 
				     "left outer join fetch p.lances l " +
				     "order by p.id, l.id")
				.getResultList();

			return produtos;
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Lance recuperaUltimoLance(Produto produto) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "select l from Lance l where l.produto = :produto order by l.id desc";		
	
			List<Lance> lances = em
				.createQuery(busca)
				.setParameter("produto", produto)
				.getResultList();
	
			if (lances.isEmpty()) 
				return null;
			else 
				return lances.get(0);
		}
		catch(NoResultException e)
		{	return null;
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}
}