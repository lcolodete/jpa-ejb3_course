package exercicio16.lance;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import exercicio16.util.HibernateUtil;
import exercicio16.util.InfraestruturaException;
import exercicio16.util.ObjetoNaoEncontradoException;

public class LanceDAOImpl implements LanceDAO
{	
	public long inclui(Lance umLance) 
	{	try
		{	Session sessao = HibernateUtil.getSession();

			sessao.save(umLance);
			
			return umLance.getId();
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void altera(Lance umLance) 
	{	try
		{	Session sessao = HibernateUtil.getSession();

			sessao.update(umLance);
		}
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public void exclui(Lance umLance) 
	{	try
		{	Session sessao = HibernateUtil.getSession();
		
			sessao.delete(umLance);
		}
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	public Lance recuperaUmLance(long numero) 
    	throws ObjetoNaoEncontradoException
	{	try
		{	Session sessao = HibernateUtil.getSession();

			Lance umLance = (Lance)sessao
				.get(Lance.class, new Long(numero));
			
			if (umLance == null)
			{	throw new ObjetoNaoEncontradoException("Lance não encontrado");
			}

			return umLance;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List recuperaLances()
	{	try
		{	Session sessao = HibernateUtil.getSession();

			List lances = sessao
				.createQuery("from Lance order by id")
				.list();

			return lances;
		} 
		catch(HibernateException e)
		{	throw new InfraestruturaException(e);
		}
	}
}
