package exercicio15.lance;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import exercicio15.util.InfraestruturaException;
import exercicio15.util.JPAUtil;
import exercicio15.util.ObjetoNaoEncontradoException;

public class LanceDAOImpl implements LanceDAO
{	
	public long inclui(Lance umLance) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			em.persist(umLance);
			
			return umLance.getId();
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void altera(Lance umLance) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			em.getReference(Lance.class, new Long(umLance.getId()));
		
			em.merge(umLance);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void exclui(Lance umLance) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();
		
			Lance c = em.getReference(Lance.class, new Long(umLance.getId()));
	
			em.remove(c);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Lance recuperaUmLance(long numero) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Lance umLance = (Lance)em
				.find(Lance.class, new Long(numero));
			
			return umLance;
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}


	@SuppressWarnings("unchecked")
	public List<Lance> recuperaLances() 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			String busca = "from Lance order by id";

			return em.createQuery(busca).getResultList();
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}
}
