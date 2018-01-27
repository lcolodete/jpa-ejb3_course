package exercicio03;

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
		{	// transiente - objeto novo: ainda n�o persistente
			// persistente - apos ser persistido 
			// destacado - objeto persistente n�o vinculado a um entity manager
		
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
		Cliente c = null;
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			c = em.getReference(Cliente.class, umCliente.getNumero());
	
			// O m�todo getReference() acrescenta ao contexto de persist�ncia
			// um  objeto   (proxy)   que   representa   o   cliente   n�mero  
			// umCliente.getNumero().
			
			// Ao  ser  executado  o  m�todo  em.merge(umCliente), como h� no 
			// contexto de  persist�ncia um  objeto (proxy n�o populado) para
			// este  mesmo  cliente, �  preciso  primeiramente  popular  este
			// proxy, e para que ele (proxy) possa ser populado, um acesso  a
			// banco de dados acontece. Caso a linha n�o  seja encontrada  no
			// banco ocorrer� um EntityNotFoundException.  Caso a linha  seja
			// encontrada o proxy ser� populado.  Em seguida ir� acontecer  o
			// merge entre o objeto umCliente e o  objeto que se encontra  no
			// contexto de persist�ncia.
			
			em.merge(umCliente);

			// Sem a execu��o de em.getReference(), no momento do merge() ir� 
			// ocorrer um select para que o objeto cliente possa ser colocado
			// no  contexto de  persist�ncia  para  que,  em  seguida,  possa
			// acontecer o merge. No entanto, se o cliente n�o for encontrado
			// no banco de dados, ele ser� inserido.
			
			if (umCliente == c)
				System.out.println(">>>>>>>>>>>>  SAO IGUAIS");
			else
				System.out.println(">>>>>>>>>>>>  NAO SAO IGUAIS");

			// Se n�o houver  uma inst�ncia  persistente igual no contexto de
			// persist�ncia, e  se uma  busca por  identificador  no banco de
			// dados  for  negativa, a  inst�ncia  umCliente ser� inserida no 
			// banco de dados.
			
			tx.commit();
			return true;
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        	System.out.println(">>>>>>>>>>>> Efetuou Rollback.");
		        }
		        catch(RuntimeException he)
		        {	throw he;
		        }
		    }

			return false;
		}
		catch(RuntimeException e)
		{   System.out.println(">>>>>>>>>>>> Ocorreu um RuntimeException.");
			if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        	System.out.println(">>>>>>>>>>>> Efetuou Rollback.");
		        }
		        catch(RuntimeException he)
		        { }
		    }
			else
			{	System.out.println(">>>>>>>>>>>> N�o Efetuou Rollback.");
			}
		    throw e;
		}
		finally
		{   try
		    {   
				em.close();
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
			
			Cliente c = em.getReference(Cliente.class, new Long(numero));
			em.remove(c);
			
			tx.commit();
			return true;
		} 
		catch(EntityNotFoundException e)
		{	if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
			
			return false;
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {   
		    		tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   try
		    {   
				em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}

	public Cliente recuperaUmCliente(long numero) 
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeSessoes.criarSessao();
			
			Cliente umCliente = em.find(Cliente.class, new Long(numero));
			
			// Caracter�sticas no m�todo find():
			// 1. � gen�rico: n�o requer um cast.
			// 2. Sempre efetua um acesso ao banco de dados, logo, a 
			//    inst�ncia � sempre inicializada (n�o retorna um proxy).
			// 3. Retorna null caso a linha n�o seja encontrada no banco.

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
				.createQuery("select c from Cliente c order by c.numero asc")
				.getResultList();
			
			// Retorna um List vazio caso a tabela correspondente esteja vazia.
			
			return clientes;
		} 
		finally
		{   try
		    {   
				em.close();
		    }
		    catch(RuntimeException he)
		    {	throw he;
		    }
		}
	}
}