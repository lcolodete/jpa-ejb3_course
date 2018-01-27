package exercicio04;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

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
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
			{	em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public boolean altera(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
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
		        catch(RuntimeException he)
		        { }
		    }

			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public boolean exclui(long numero) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			Cliente umCliente = em.getReference(Cliente.class, numero);

			em.remove(umCliente);

			tx.commit();
			return true;
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
			
			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public void exclui(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			Cliente c = (Cliente)em.find(Cliente.class, new Long(umCliente.getNumero()));
			
			if(c != null)
			{	em.remove(c);
			}
			
			tx.commit();
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public Cliente recuperaUmCliente(long numero) 
	{	EntityManager em = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			
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
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> recuperaClientes()
	{	EntityManager em = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			
			List<Cliente> clientes = em
					.createQuery("select c from Cliente c order by c.numero")
					.getResultList();

			return clientes;
		} 
		finally
		{   try
		    {   em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}
}