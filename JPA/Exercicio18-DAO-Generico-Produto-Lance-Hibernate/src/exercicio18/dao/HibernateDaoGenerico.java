package exercicio18.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Query;
import org.hibernate.Session;

import exercicio18.util.HibernateUtil;
import exercicio18.util.InfraestruturaException;
import exercicio18.util.ObjetoNaoEncontradoException;


/**
 * A implementação de um DAO genérico para a JPA
 * Uma implementação "typesafe" dos métodos CRUD e dos métodos de busca.
 */
public class HibernateDaoGenerico<T, PK extends Serializable> 
	implements DaoGenerico<T, PK>, ExecutorDeBuscas<T>
{
    private Class<T> tipo;

    public HibernateDaoGenerico(Class<T> tipo)
    { 	// System.out.println("**************>>>>  Executou construtor de HibernateDaoGenerico");
    	this.tipo = tipo; 
    }

    @SuppressWarnings("unchecked")
    public T inclui(T o)
    {
    	Session sessao = HibernateUtil.getSession();
        try 
        {	sessao.save(o);
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }

        return o;
    }

    public void altera(T o)
    {
    	Session sessao = HibernateUtil.getSession();
        try 
        {	sessao.update(o);
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }

    public void exclui(T o)
    { 
    	Session sessao = HibernateUtil.getSession();
        try 
        {	sessao.delete(o);
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
	public T getPorId(PK id) throws ObjetoNaoEncontradoException
    {
    	Session sessao = HibernateUtil.getSession();
        T t = null;
        try 
        {	t = (T)sessao.get(tipo, id);
        
        	if (t == null)
        	{	throw new ObjetoNaoEncontradoException();
        	}
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
        return t;
    }

    @SuppressWarnings("unchecked")
	public T getPorIdComLock(PK id) throws ObjetoNaoEncontradoException
    {
    	Session sessao = HibernateUtil.getSession();
        T t = null;
        try 
        {	t = (T)sessao.load(tipo, id, LockMode.UPGRADE); 
        }
        catch(ObjectNotFoundException e)
        {   throw new ObjetoNaoEncontradoException();
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }

        return t;
    }

    @SuppressWarnings("unchecked")
    public T busca(Method metodo, Object[] argumentos)
    	throws ObjetoNaoEncontradoException
    {
        Session sessao = HibernateUtil.getSession ();
        T t = null;
        try 
        {
            //List lista;
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = sessao.getNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i, arg);
                }
            }

            t = (T) namedQuery.uniqueResult();
            
            if (t == null)
            {   throw new ObjetoNaoEncontradoException();
            }
            
            return t;
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public T buscaUltimoOuPrimeiro(Method metodo, Object[] argumentos)
    	throws ObjetoNaoEncontradoException
    {
        Session sessao = HibernateUtil.getSession ();
        T t = null;
        try 
        {
            List lista;
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = sessao.getNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i, arg);
                }
            }
            lista = namedQuery.list();
            
            t = (lista.size () == 0) ? null : (T)lista.get(0) ;
            
            if (t == null)
            {   throw new ObjetoNaoEncontradoException();
            }
            
            return t;
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }

    @SuppressWarnings("unchecked")
	public List<T> buscaLista(Method metodo, Object[] argumentos)    
    {
        Session sessao = HibernateUtil.getSession ();
        
        try 
        {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = sessao.getNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i, arg);
                }
            }
            return (List<T>)namedQuery.list();
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }
	
    @SuppressWarnings("unchecked")
    public Set<T> buscaConjunto(Method metodo, Object[] argumentos)
    {
        Session sessao = HibernateUtil.getSession ();
        
        try 
        {
            String nomeDaBusca = getNomeDaBuscaPeloMetodo(metodo);
            Query namedQuery = sessao.getNamedQuery(nomeDaBusca);
    
            if (argumentos != null)
            {
                for (int i = 0; i < argumentos.length; i++)
                {
                    Object arg = argumentos[i];
                    namedQuery.setParameter(i, arg);
                }
            }
            
            List<T> lista = namedQuery.list();
            
            return new LinkedHashSet(lista);
        }
        catch(HibernateException e)
        {   throw new InfraestruturaException(e);
        }
    }    

    public String getNomeDaBuscaPeloMetodo(Method metodo) 
    { 	return tipo.getSimpleName() + "." + metodo.getName(); 
    }    
}
