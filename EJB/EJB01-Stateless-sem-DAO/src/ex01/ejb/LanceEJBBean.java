package ex01.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex01.dominio.Lance;
import ex01.excecao.AplicacaoException;

@Stateless
@RemoteBinding(jndiBinding="exercicio01.LanceEJBBean/remote")
public class LanceEJBBean implements LanceEJBRemote
{	
	@PersistenceContext(unitName="EJB") private EntityManager em; 

	public LanceEJBBean() 
	{	System.out.println(">>>>>>>>>>> Criou o objeto LanceEJBBean.");
	} 

	public long inclui(Lance umLance) 
	{	em.persist(umLance);

		return umLance.getId();
	} 

	public boolean altera(Lance umLance) 
	{	try
		{	em.getReference(Lance.class, new Long(umLance.getId()));
			em.merge(umLance);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public boolean exclui(Lance umLance) 
	{	
		try
		{	Lance l = em.getReference(Lance.class, new Long(umLance.getId()));
			em.remove(l);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException
	{	Lance umLance = (Lance)em.find(Lance.class, new Long(numero));

		if(umLance == null)
		{	throw new AplicacaoException("Lance nao encontrado");
		}
		
		return umLance;
	}
		
	@SuppressWarnings("unchecked")
	public List<Lance> recuperaLances()
	{	List<Lance> lances = em
			.createQuery("select l from Lance l order by l.id")
			.getResultList();
			
		//	System.out.println(">>>>>>>>>>>> em.getClass().getName() = " + em.getClass().getName());
		//  retorna org.jboss.ejb3.entity.TransactionScopedEntityManager
		
		System.out.println(">>>>>>>>>>>> hashCode() de TransactionScopedEntityManager = " + em.hashCode());
		// retorna org.jboss.ejb3.entity.TransactionScopedEntityManager
		System.out.println(">>>>>>>>>>>> hashCode() de HibernateSession = " + 
			((org.jboss.ejb3.entity.TransactionScopedEntityManager)em).getHibernateSession().hashCode());
	
		// O objeto EntityManager que � injetado no EJB � sempre o mesmo. Ap�s  ser
		// injetado este objeto n�o muda, mesmo  entre execu��es de m�todos do EJB.
		// No entanto, ele cont�m uma referencia para a sess�o do Hibernate, e esta
		// sess�o muda a cada execu��o de m�todo do EJB. 

		return lances;
	} 
}
