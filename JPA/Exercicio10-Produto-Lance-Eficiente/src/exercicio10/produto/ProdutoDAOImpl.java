package exercicio10.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import exercicio10.util.AplicacaoException;
import exercicio10.util.InfraestruturaException;
import exercicio10.util.JPAUtil;
import exercicio10.util.ObjetoNaoEncontradoException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();

			em.persist(umProduto);
			
			JPAUtil.commitTransaction();
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public void altera(Produto umProduto)
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();
	
			em.getReference(Produto.class, new Long(umProduto.getId()));
			
			em.merge(umProduto);
			
			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
				throw new AplicacaoException("Produto não encontrado.");
			}
			catch(InfraestruturaException ie)
			{ }
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }
	
		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}
	
	public void exclui(Produto umProduto) 
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();
		
			Produto p = em.getReference(Produto.class, new Long(umProduto.getId()));
	
			em.remove(p);
	
			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
				throw new AplicacaoException("Produto não encontrado.");
			}
			catch(InfraestruturaException ie)
			{ }
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }
	
		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = em.find(Produto.class, new Long(numero));

			if(umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "select p from Produto p " + 
			               "left outer join fetch p.lances l " +
                           "where p.id = :id"; 

			Produto umProduto =	(Produto) em
				.createQuery(busca)
				.setParameter("id", new Long(numero))
				.getSingleResult();

	/* 		Em função do método getSingleResult() será propagada
	 *      a exceção NoResultException caso nenhum produto seja
	 *      encontrado.
	 */

			return umProduto;
		} 
		catch(NoResultException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
		finally
		{   try
		    {	JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();
		
			// EXECUTAR SEM O DISTINCT PARA VER O QUE ACONTECE
		
			List<Produto> produtos = em
				.createQuery("select distinct p from Produto p " + 
						     "left outer join fetch p.lances l " +
						     "order by p.id")
				.getResultList();
   	
			return produtos;
		} 
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}
}