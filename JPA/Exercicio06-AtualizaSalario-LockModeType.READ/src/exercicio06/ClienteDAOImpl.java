package exercicio06;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;

import corejava.Console;

public class ClienteDAOImpl implements ClienteDAO
{	
	public long inclui(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	// transiente - objeto novo: ainda não persistente
			// persistente - apos salvar 
			// destacado - obj persistente fora de uma sessao
		
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			em.persist(umCliente);
			
			tx.commit();
			return umCliente.getNumero();
		} 
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
				{	tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
			{	em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}

	public boolean altera(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			em.getReference(Cliente.class, umCliente.getNumero());
			em.merge(umCliente);
			
			tx.commit();
			return true;
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        {	throw re;
		        }
		    }
			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}

	public boolean exclui(long numero) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			Cliente umCliente = (Cliente)em.getReference(Cliente.class, new Long(numero));
			em.remove(umCliente);

			tx.commit();
			return true;
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}

	public boolean atualizaSalario(long numero, double percentualDeAumento)
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			Cliente umCliente = em.getReference(Cliente.class, numero);
			
			System.out.println('\n' + "Nome da classe = " + umCliente.getClass().getName()); 

			Console.readLine("Após executar getReference() - Aperte uma tecla para prosseguir...");

			em.lock(umCliente, LockModeType.READ);

			// Ao utilizarmos o LockModeType do tipo READ é efetuado um select for update
			// no banco de dados. O  ideal é  que  o  comando  select  for  update  fosse
			// o comando responsável por popular o objeto. MAS NÃO É ISSO QUE ACONTECE!!!

			// Se utilizarmos o  LockModeType do tipo  WRITE será  efetuado um select for 
			// update com a cláusula NOWAIT (Única diferença).

			Console.readLine("Após executar lock() - Aperte uma tecla para prosseguir...");

			em.refresh(umCliente);
			
			System.out.println('\n' + "Nome da classe = " + umCliente.getClass().getName()); 

			Console.readLine("Após executar refresh() - Aperte uma tecla para prosseguir...");
//==>
			umCliente.setSalario(umCliente.getSalario() * 
				 	(1 + percentualDeAumento / 100));

			tx.commit();

			return true;
		} 
		catch(EntityNotFoundException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
			
			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException re)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {	em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}
	
	public Cliente recuperaUmCliente(long numero) 
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeSessoes.criarSessao();
			Cliente umCliente = em.find(Cliente.class, new Long(numero));
			
			// Características no método find():
			// 1. É genérico: não requer um cast.
			// 2. Sempre efetua um acesso ao banco de dados, logo, a 
			//    instância é sempre inicializada (não retorna um proxy).
			// 3. Retorna null caso a linha não seja encontrada no banco.

			return umCliente;
		} 
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> recuperaClientes()
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeSessoes.criarSessao();
			
			List<Cliente> clientes = em
					.createQuery("select c from Cliente c order by c.numero")
					.getResultList();

			return clientes;
		} 
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException re)
		    {	throw re;
		    }
		}
	}
}