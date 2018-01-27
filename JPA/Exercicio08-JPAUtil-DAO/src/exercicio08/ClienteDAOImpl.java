package exercicio08;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class ClienteDAOImpl implements ClienteDAO
{	
	public long inclui(Cliente umCliente) 
	{	try
		{	
//   		Inicia uma transação
			JPAUtil.beginTransaction();

//			Recupera ou cria uma sessão
			EntityManager em = JPAUtil.getEntityManager();

//          Salva o objeto cliente
			em.persist(umCliente);
			
// 			Comita a transação
			JPAUtil.commitTransaction();

//			retorna o número do cliente salvo			
			return umCliente.getNumero();
		} 
		catch(RuntimeException e)
		{	try
			{	/* O método rollbackTransaction() fecha o Entity
			     * Manager (sessão), mas  quando o EntityManager
			     * é fechado no finally não ocorre nenhum  erro.
			     * 
			     * Caso o método commitTransaction() acima  seja
			     * comentado (supondo que o programador esqueceu
			     * de digitá-lo) no  momento que o  finally  for
			     * executado, o método JPAUtil.closeEntityManager()
			     * irá  perceber  que  há  uma  transação ativa. 
			     * Neste momento será propagada uma exceção. Sem
			     * esse  controle,  a  transação ficaria  aberta
			     * (por  engano)  e   seria  comitada  no commit 
			     * da operação seguinte da aplicação. 
			     */
			
//				Efetua rollback da transação
				JPAUtil.rollbackTransaction();

			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {    
//				Fecha a sessão
				JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
			}
		}
	}

	public boolean altera(Cliente umCliente) 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();

			em.getReference(Cliente.class, new Long(umCliente.getNumero()));
			
			em.merge(umCliente);
			
			JPAUtil.commitTransaction();
			return true;
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

			return false;
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public boolean exclui(long numero) 
	{	try
		{	JPAUtil.beginTransaction();

			EntityManager em = JPAUtil.getEntityManager();
		
			Cliente umCliente = (Cliente)em
				.getReference(Cliente.class, new Long(numero));
			em.remove(umCliente);

			JPAUtil.commitTransaction();
			
			return true;
		}
		catch(EntityNotFoundException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

			return false;
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(RuntimeException he)
		    {	
		    	throw he;
		    }
		}
	}

	public Cliente recuperaUmCliente(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Cliente umCliente = em.find(Cliente.class, new Long(numero));

			if (umCliente == null)
			{	throw new ObjetoNaoEncontradoException();
			}
			
			return umCliente;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

			throw new AplicacaoException("Cliente não encontrado");
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	@SuppressWarnings("unchecked")
	public List<Cliente> recuperaClientes()
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			List<Cliente> clientes = em
				.createQuery("select c from Cliente c order by c.numero")
				.getResultList();

			return clientes;
		} 
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}
}