package exercicio05;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;
import javax.persistence.OptimisticLockException;

public class ClienteDAOImpl implements ClienteDAO
{	
	public long inclui(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	// transiente - objeto novo: ainda n�o persistente
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

	public void altera(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			em.getReference(Cliente.class, umCliente.getNumero());
			// O m�todo getReference() acrescenta ao contexto de persist�ncia
			// um  objeto   (proxy)   que   representa   o   cliente   n�mero  
			// umCliente.getNumero().
			
			// Ao  ser  executado  o  m�todo  em.merge(umCliente), como h� no 
			// contexto de  persist�ncia um  objeto (proxy n�o populado) para
			// este  mesmo  cliente, �  preciso  primeiramente  popular  este
			// proxy, e para que ele (proxy) possa ser populado, um acesso  a
			// banco de dados acontece. Caso a linha n�o  seja encontrada  no
			// banco ocorrer� um EntityNotFoundException.  E neste caso  ser�
			// propagada a exce��o ObjetoNaoEncontradoException. Caso a linha
			// seja   encontrada   o   proxy   ser�   populado.   Em  seguida
			// ir�  acontecer o  merge entre o  objeto  umCliente e o  objeto
			// que se encontra  no contexto de persist�ncia.
			
			em.merge(umCliente);

			// Sem a execu��o de em.getReference(), no momento do merge() ir� 
			// ocorrer um select para que o objeto cliente possa ser colocado
			// no  contexto de  persist�ncia  para  que,  em  seguida,  possa
			// acontecer o merge. No entanto, se o cliente n�o for encontrado
			// no banco de dados, ele ser� inserido.
			
			// No momento do merge, ao  perceber  que o  n�mero de  vers�o do 
			// objeto que se encontra no contexto de persist�ncia � diferente
			// do n�mero de vers�o do objeto umCliente, a JPA ir� propagar  a
			// exce��o OptimisticLockException.

			tx.commit();
		} 
		catch(OptimisticLockException e)  // sub-classe de RuntimeException
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        {	throw he;
		        }
		    }
			throw new EstadoDeObjetoObsoletoException();
		}
		catch(EntityNotFoundException e) // sub-classe de RuntimeException
		{	if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }

			throw new ObjetoNaoEncontradoException();
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
			
			Cliente c = em.getReference(Cliente.class, umCliente.getNumero());
			
			// COMO PARA REMOVER UM CLIENTE NA JPA � PRECISO PRIMEIRAMENTE
			// RECUPER�-LO, QUANDO O  CLIENTE �  RECUPERADO SEU N�MERO  DE
			// VERS�O VEM JUNTO. ISTO FAZ COM QUE O CONTROLE DE VERS�O N�O
			// FUNCIONE SE A REMO��O ACONTECER AP�S UMA ATUALIZA��O, OU SE
			// OCORREREM DUAS REMO��ES EM PARALELO DO MESMO CLIENTE.
			
			// LOGO, A  EXCE��O  OptimisticLockException NUNCA ACONTECER�.
			//                   =======================
			
			em.remove(c);

			tx.commit();
		} 
		catch(EntityNotFoundException e)
		{   if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        {	throw he; 
		        }
		    }
			throw new ObjetoNaoEncontradoException();
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