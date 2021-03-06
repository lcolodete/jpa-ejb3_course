package ex04.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import ex04.dominio.Cliente;
import ex04.excecao.ObjetoNaoEncontradoException;

public class ClienteDAO
{	
    private EntityManager em;
    
    public ClienteDAO(EntityManager em)
    {	this.em = em;
    }

    public long inclui(Cliente umCliente) 
	{	em.persist(umCliente);
		return umCliente.getId();
	}

	public boolean exclui(Cliente umCliente) 
	{	try
		{	Cliente c = em.getReference(Cliente.class, new Long(umCliente.getId()));
			em.remove(c);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;	
		}
	}

	public Cliente recuperaUmCliente(long numero) 
		throws ObjetoNaoEncontradoException 
	{	
		Cliente umCliente = em.find(Cliente.class, new Long(numero));
		
		if (umCliente == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		return umCliente;
	} 
}
