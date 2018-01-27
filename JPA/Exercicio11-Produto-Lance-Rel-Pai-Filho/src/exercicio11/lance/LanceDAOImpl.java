package exercicio11.lance;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import exercicio11.util.AplicacaoException;
import exercicio11.util.InfraestruturaException;
import exercicio11.util.JPAUtil;
import exercicio11.util.ObjetoNaoEncontradoException;

public class LanceDAOImpl implements LanceDAO
{	
	public long inclui(Lance umLance) 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();

			em.persist(umLance);
			
			JPAUtil.commitTransaction();
			return umLance.getId();
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

	public void altera(Lance umLance) 
		throws AplicacaoException 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();
	
			em.getReference(Lance.class, new Long(umLance.getId()));
			
			em.merge(umLance);
			
			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
				throw new AplicacaoException("Lance não encontrado.");
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
	
	public void exclui(Lance umLance) 
		throws AplicacaoException 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();
		
			Lance l = em.getReference(Lance.class, new Long(umLance.getId()));
	
			em.remove(l);
	
			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
				throw new AplicacaoException("Lance não encontrado.");
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

	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Lance umLance = (Lance)em.find(Lance.class, new Long(numero));

			if(umLance == null)
			{	throw new ObjetoNaoEncontradoException();
			}
		
			return umLance;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado");
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

	@SuppressWarnings("unchecked")
	public List<Lance> recuperaLances()
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			List<Lance> lances = em
					.createQuery("select l from Lance l order by l.id")
					.getResultList();
			
			return lances;
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
