package exercicio28.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


public class JPAUtil 
{	private static EntityManagerFactory emf = null;
	private static final ThreadLocal<EntityManager> threadEntityManager = new ThreadLocal<EntityManager>();
	private static final ThreadLocal<EntityTransaction> threadTransaction = new ThreadLocal<EntityTransaction>();

	static 
	{	try
		{	emf = Persistence.createEntityManagerFactory("exercicio28");
		}
		catch(Throwable e)
		{	System.out.println(">>>>>>>>>>>>>>>> " + e.getMessage());
		}
	}

	public static EntityManager getEntityManager() 
	{	// System.out.println("Abriu ou recuperou sess�o");
	
		EntityManager s = threadEntityManager.get();
		// Abre uma nova Sess�o, se a thread ainda n�o possui uma.
		try 
		{	if (s == null) 
			{	s = emf.createEntityManager();
				threadEntityManager.set(s);
				//System.out.println("criou sessao");
			}
		} 
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
		return s;
	}

	public static void closeEntityManager() 
	{	//System.out.println("Vai fechar sess�o");

		try 
		{	EntityManager s = threadEntityManager.get();
			threadEntityManager.set(null);
			if (s != null && s.isOpen())
			{	s.close();
				//System.out.println("Fechou a sess�o");
			}

			EntityTransaction tx = threadTransaction.get();
			if ( tx != null && tx.isActive())
			{	rollbackTransaction();
				throw new RuntimeException("EntityManager sendo fechado " +
						                   "com transa��o ativa.");
			}
		} 	
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
	}

	public static void beginTransaction() 
	{	//System.out.println("Vai criar transacao");

		EntityTransaction tx = threadTransaction.get();
		try 
		{	if (tx == null) 
			{	tx = getEntityManager().getTransaction();
				tx.begin();
				threadTransaction.set(tx);
				//System.out.println("Criou transacao");
			}
			else
			{	//System.out.println("Nao criou transacao");
			}
		} 
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		}
	}

	public static void commitTransaction() 
	{	EntityTransaction tx = threadTransaction.get();
		try 
		{	if ( tx != null && tx.isActive())
			{	tx.commit();
				//System.out.println("Comitou transacao");
			}
			threadTransaction.set(null);
		} 
		catch (RuntimeException ex) 
		{	try
			{	rollbackTransaction();
			}
			catch(RuntimeException e)
			{}
			
			throw new InfraestruturaException(ex);
		}
	}

	public static void rollbackTransaction() 
	{	//System.out.println("Vai efetuar rollback de transacao");
	
		EntityTransaction tx = threadTransaction.get();
		try 
		{	threadTransaction.set(null);
			if ( tx != null && tx.isActive()) 
			{	tx.rollback();
			}
		} 
		catch (RuntimeException ex) 
		{	throw new InfraestruturaException(ex);
		} 
		finally 
		{	closeEntityManager();
		}
	}
}