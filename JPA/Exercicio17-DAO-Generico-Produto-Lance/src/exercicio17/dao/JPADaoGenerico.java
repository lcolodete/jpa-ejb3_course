package exercicio17.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import exercicio17.util.InfraestruturaException;
import exercicio17.util.JPAUtil;
import exercicio17.util.ObjetoNaoEncontradoException;


/**
 * A implementação de um DAO genérico para a JPA
 * Uma implementação "typesafe" dos métodos CRUD e dos métodos de busca.
 */
public class JPADaoGenerico<T, PK extends Serializable> 
	implements DaoGenerico<T, PK>, ExecutorDeBuscas<T>
{
    private Class<T> tipo;

    public JPADaoGenerico(Class<T> tipo)
    { 	// System.out.println("**************>>>>  Executou construtor de JPADaoGenerico");
    	this.tipo = tipo; 
    }

    @SuppressWarnings("unchecked")
    public T inclui(T o)
    {
		EntityManager em = JPAUtil.getEntityManager();
        try 
        {	em.persist(o);
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }

        return o;
    }

    public void altera(T o)
    {
    	EntityManager em = JPAUtil.getEntityManager();
        try 
        {	em.merge(o);
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }

    public void exclui(T o)
    { 
    	EntityManager em = JPAUtil.getEntityManager();
        try 
        {	em.remove(o);
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }

    public T getPorId(PK id) throws ObjetoNaoEncontradoException
    {
    	EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try 
        {	t = (T)em.find(tipo, id);
        
        	if (t == null)
        	{	throw new ObjetoNaoEncontradoException();
        	}
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
        return t;
    }

    public T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException
    {
    	EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try 
        {
            t = (T)em.find(tipo, id);
            
            if (t != null)
            {   em.lock(t, LockModeType.READ);
				em.refresh(t);
            }
            else
        	{	throw new ObjetoNaoEncontradoException();
        	}
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }

        return t;
    }

    @SuppressWarnings("unchecked")
	public T busca(Method metodo, Object[] argumentos) 
    	throws ObjetoNaoEncontradoException
    {	
    	EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try 
        {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {	for (int i = 0; i < argumentos.length; i++)
                {	Object arg = argumentos[i];
                    namedQuery.setParameter(i+1, arg);  // Parâmetros de buscas são 1-based.
                }
            }
            t = (T)namedQuery.getSingleResult();
            
            return t;
        }
        catch(NoResultException e)
        {   throw new ObjetoNaoEncontradoException();
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public T buscaUltimoOuPrimeiro(Method metodo, 
    		                       Object[] argumentos)
    	throws ObjetoNaoEncontradoException
    {
    	EntityManager em = JPAUtil.getEntityManager();
        T t = null;
        try 
        {
            List lista;
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i+1, arg);
                }
            }
            lista = namedQuery.getResultList();
            
            t = (lista.size () == 0) ? null : (T)lista.get(0) ;
            
            if (t == null)
            {   throw new ObjetoNaoEncontradoException();
            }
            
            return t;
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
	public List<T> buscaLista(Method metodo, 
    		                     Object[] argumentos)    
    {
    	EntityManager em = JPAUtil.getEntityManager();
        
        try 
        {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i+1, arg); // Parâmetros de buscas são 1-based.
                }
            }
            return (List<T>)namedQuery.getResultList();
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }
	
    @SuppressWarnings("unchecked")
    public Set<T> buscaConjunto(Method metodo, 
    		                       Object[] argumentos)
    {
    	EntityManager em = JPAUtil.getEntityManager();
        
        try 
        {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = em.createNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {	for (int i = 0; i < argumentos.length; i++)
                {	Object arg = argumentos[i];
                    namedQuery.setParameter(i+1, arg); // Parâmetros de buscas são 1-based.
                }
            }
            
            List<T> lista = namedQuery.getResultList();
            
            return new LinkedHashSet(lista);
        }
        catch(RuntimeException e)
        {   throw new InfraestruturaException(e);
        }
    }    

    public String getNomeDaBuscaPeloMetodo(Method metodo) 
    { 	return tipo.getSimpleName() + "." + metodo.getName(); 
    }    
}
