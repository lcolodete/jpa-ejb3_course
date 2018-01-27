package ex05.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import ex05.dominio.Pedido;
import ex05.excecao.ObjetoNaoEncontradoException;

public class PedidoDAO
{	
    private EntityManager em;
    
    public PedidoDAO(EntityManager em)
    {	this.em = em;
    }

	public long inclui(Pedido umPedido) 
	{	em.persist(umPedido);
		return umPedido.getId();
	} 

	public boolean altera(Pedido umPedido) 
	{	try
		{	em.getReference(Pedido.class, new Long(umPedido.getId()));
			em.merge(umPedido);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public boolean exclui(Pedido umPedido) 
	{	try
		{	Pedido p = em.getReference(Pedido.class, new Long(umPedido.getId()));
			em.remove(p);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public Pedido recuperaUmPedido(long numero) 
		throws ObjetoNaoEncontradoException 
	{	Pedido umPedido = (Pedido)em
			.find(Pedido.class, new Long(numero));
			
		if (umPedido == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		return umPedido;
	} 

	public Pedido recuperaUmPedidoComLock(long numero) 
		throws ObjetoNaoEncontradoException 
	{			
		Pedido umPedido = (Pedido)em
			.find(Pedido.class, new Long(numero));

		if (umPedido == null)
		{	throw new ObjetoNaoEncontradoException();
		}
		
		em.lock(umPedido, LockModeType.READ);
		em.refresh(umPedido);
		
		return umPedido;
	} 

	public Pedido recuperaUmPedidoEItensPedidos(long numero) 
		throws ObjetoNaoEncontradoException 
	{	
		try
		{	String busca = "select distinct p from Pedido p " +
			               "left outer join fetch p.itensDePedidos it " +
			               "where p.id = :id";

			Pedido umPedido =
				(Pedido) em.createQuery(busca)
							    .setParameter("id", numero)
							    .getSingleResult();

			return umPedido;
		} 
		catch(NoResultException e)
		{	throw new ObjetoNaoEncontradoException();
		}
	}
}