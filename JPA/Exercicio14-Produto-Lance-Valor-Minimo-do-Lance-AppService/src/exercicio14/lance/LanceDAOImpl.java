package exercicio14.lance;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import exercicio14.util.InfraestruturaException;
import exercicio14.util.JPAUtil;
import exercicio14.util.ObjetoNaoEncontradoException;

public class LanceDAOImpl implements LanceDAO
{	
	public long inclui(Lance umLance) 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			em.persist(umLance);
			
			return umLance.getId();
		} 
		catch(RuntimeException e)
		{	throw new InfraestruturaException (e);
		}
	}

	public void exclui(Lance umLance) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Lance l = em.getReference(Lance.class, new Long(umLance.getId()));

			em.remove(l);
		}
		catch(EntityNotFoundException e)
		{	throw new ObjetoNaoEncontradoException();	
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Lance recuperaUmLance(long numero) 
		throws ObjetoNaoEncontradoException 
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Lance umLance = (Lance)em.find(Lance.class, new Long(numero));

			if(umLance == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umLance;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	// Para que não seja propagada uma exceção do tipo 
			// InfraestruturaException.
			throw e;
		}
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
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
		catch(RuntimeException e)
		{	throw new InfraestruturaException(e);
		}
	}
}
