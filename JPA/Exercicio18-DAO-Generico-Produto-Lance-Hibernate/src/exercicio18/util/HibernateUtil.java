package exercicio18.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class HibernateUtil 
{	private static final SessionFactory sessionFactory;

	static 
	{	sessionFactory = new AnnotationConfiguration()
				.configure("hibernate.cfg.xml")
				.buildSessionFactory();
	}

	public static Session getSession() 
	{	
		try
		{	return sessionFactory.getCurrentSession();
			
			// Recupera ou cria uma nova sess�o. Caso a 
			// transa��o tenha sofrido um commit ou rollback, 
			// a sess�o ter� sido fechada. Neste caso uma 
			// nova sess�o ser� criada. Caso contr�rio, a 
			// sess�o corrente ser� recuperada.
		}
		catch (HibernateException e) 
		{	throw new InfraestruturaException(e);
		}
	}

	public static void beginTransaction() 
	{	
		try
		{	getSession().beginTransaction();

			// Cria ou recupera a transa��o corrente,
			// caso ela n�o tenha sofrido commit nem
			// rollback.
		}
		catch (HibernateException e) 
		{	throw new InfraestruturaException(e);
		}
	}

	public static void commitTransaction() 
	{	
		try
		{	getSession().getTransaction().commit();
			// Efetua o commit da transacao e fecha a sess�o.
		}
		catch (HibernateException ex) 
		{	rollbackTransaction();
			throw new InfraestruturaException(ex);
		}
	}

	public static void rollbackTransaction() 
	{	try
		{	getSession().getTransaction().rollback();
			// Efetua o rollback da transacao e fecha a sess�o.
		}
		catch (HibernateException ex) 
		{	throw new InfraestruturaException(ex);
		}
	}
}