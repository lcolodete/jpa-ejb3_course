package ex09.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import ex09.dominio.Lance;
import ex09.excecao.ObjetoNaoEncontradoException;

public class LanceDAO
{	
    private EntityManager em;
    
    public LanceDAO(EntityManager em)
    {	this.em = em;
    }

    public long inclui(Lance umLance) 
	{	em.persist(umLance);
		return umLance.getId();
	}

	public boolean exclui(Lance umLance) 
	{	try
		{	Lance l = em.getReference(Lance.class, new Long(umLance.getId()));
			em.remove(l);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;	
		}
	}

	public Lance recuperaUmLance(long numero) 
		throws ObjetoNaoEncontradoException 
	{	Lance umLance = (Lance)em.find(Lance.class, new Long(numero));

		if(umLance == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		return umLance;
	} 

	@SuppressWarnings("unchecked")
	public List<Lance> recuperaLances()
	{	List<Lance> lances = em
				.createQuery("from Lance l order by l.id")
				.getResultList();
			
		return lances;
	}
}
