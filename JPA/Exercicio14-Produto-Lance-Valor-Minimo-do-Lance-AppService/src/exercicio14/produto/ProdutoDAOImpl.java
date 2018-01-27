package exercicio14.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import exercicio14.lance.Lance;
import exercicio14.util.InfraestruturaException;
import exercicio14.util.JPAUtil;
import exercicio14.util.ObjetoNaoEncontradoException;

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
		
			Produto p = em.getReference(Produto.class, new Long(umProduto.getId()));
	
			em.remove(p);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = (Produto)em
				.find(Produto.class, new Long(numero));
			
			if (umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw e;
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProdutoComLock(long numero) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = (Produto)em
				.find(Produto.class, new Long(numero));

			if (umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}
			else
			{	em.lock(umProduto, LockModeType.READ);
				em.refresh(umProduto);
			}

			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw e;
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "select p from Produto p " +
			               "left outer join fetch p.lances l " +
			               "where p.id = :id";

			Produto umProduto =
				(Produto) em.createQuery(busca)
							    .setParameter("id", numero)
							    .getSingleResult();

			return umProduto;
		} 
		catch(NoResultException e)
		{	throw new ObjetoNaoEncontradoException();
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
				.createQuery("select distinct p from Produto p " +
						     "left outer join fetch p.lances l " +
						     "order by p.id asc")
				.getResultList();

			return produtos;
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public Lance recuperaUltimoLance(Produto produto)
	{	 
		try
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
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}
}