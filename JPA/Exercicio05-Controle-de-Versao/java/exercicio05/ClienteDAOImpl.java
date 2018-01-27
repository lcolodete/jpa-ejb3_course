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

	public void altera(Cliente umCliente) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeSessoes.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			em.getReference(Cliente.class, umCliente.getNumero());
			// O método getReference() acrescenta ao contexto de persistência
			// um  objeto   (proxy)   que   representa   o   cliente   número  
			// umCliente.getNumero().
			
			// Ao  ser  executado  o  método  em.merge(umCliente), como há no 
			// contexto de  persistência um  objeto (proxy não populado) para
			// este  mesmo  cliente, é  preciso  primeiramente  popular  este
			// proxy, e para que ele (proxy) possa ser populado, um acesso  a
			// banco de dados acontece. Caso a linha não  seja encontrada  no
			// banco ocorrerá um EntityNotFoundException.  E neste caso  será
			// propagada a exceção ObjetoNaoEncontradoException. Caso a linha
			// seja   encontrada   o   proxy   será   populado.   Em  seguida
			// irá  acontecer o  merge entre o  objeto  umCliente e o  objeto
			// que se encontra  no contexto de persistência.
			
			em.merge(umCliente);

			// Sem a execução de em.getReference(), no momento do merge() irá 
			// ocorrer um select para que o objeto cliente possa ser colocado
			// no  contexto de  persistência  para  que,  em  seguida,  possa
			// acontecer o merge. No entanto, se o cliente não for encontrado
			// no banco de dados, ele será inserido.
			
			// No momento do merge, ao  perceber  que o  número de  versão do 
			// objeto que se encontra no contexto de persistência é diferente
			// do número de versão do objeto umCliente, a JPA irá propagar  a
			// exceção OptimisticLockException.

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
			
			// COMO PARA REMOVER UM CLIENTE NA JPA É PRECISO PRIMEIRAMENTE
			// RECUPERÁ-LO, QUANDO O  CLIENTE É  RECUPERADO SEU NÚMERO  DE
			// VERSÃO VEM JUNTO. ISTO FAZ COM QUE O CONTROLE DE VERSÃO NÃO
			// FUNCIONE SE A REMOÇÃO ACONTECER APÓS UMA ATUALIZAÇÃO, OU SE
			// OCORREREM DUAS REMOÇÕES EM PARALELO DO MESMO CLIENTE.
			
			// LOGO, A  EXCEÇÃO  OptimisticLockException NUNCA ACONTECERÁ.
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