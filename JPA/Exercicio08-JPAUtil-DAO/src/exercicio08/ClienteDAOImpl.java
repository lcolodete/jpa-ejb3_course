package exercicio08;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class ClienteDAOImpl implements ClienteDAO
{	
	public long inclui(Cliente umCliente) 
	{	try
		{	
//   		Inicia uma transa��o
			JPAUtil.beginTransaction();

//			Recupera ou cria uma sess�o
			EntityManager em = JPAUtil.getEntityManager();

//          Salva o objeto cliente
			em.persist(umCliente);
			
// 			Comita a transa��o
			JPAUtil.commitTransaction();

//			retorna o n�mero do cliente salvo			
			return umCliente.getNumero();
		} 
		catch(RuntimeException e)
		{	try
			{	/* O m�todo rollbackTransaction() fecha o Entity
			     * Manager (sess�o), mas  quando o EntityManager
			     * � fechado no finally n�o ocorre nenhum  erro.
			     * 
			     * Caso o m�todo commitTransaction() acima  seja
			     * comentado (supondo que o programador esqueceu
			     * de digit�-lo) no  momento que o  finally  for
			     * executado, o m�todo JPAUtil.closeEntityManager()
			     * ir�  perceber  que  h�  uma  transa��o ativa. 
			     * Neste momento ser� propagada uma exce��o. Sem
			     * esse  controle,  a  transa��o ficaria  aberta
			     * (por  engano)  e   seria  comitada  no commit 
			     * da opera��o seguinte da aplica��o. 
			     */
			
//				Efetua rollback da transa��o
				JPAUtil.rollbackTransaction();

			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {    
//				Fecha a sess�o
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

			throw new AplicacaoException("Cliente n�o encontrado");
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