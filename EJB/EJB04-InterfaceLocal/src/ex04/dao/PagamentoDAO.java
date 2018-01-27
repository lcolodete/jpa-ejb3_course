package ex04.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;

import ex04.dominio.Pagamento;
import ex04.excecao.ObjetoNaoEncontradoException;

public class PagamentoDAO
{	
    private EntityManager em;
    
    public PagamentoDAO(EntityManager em)
    {	this.em = em;
    }

	public long inclui(Pagamento umPagamento) 
	{	em.persist(umPagamento);
		return umPagamento.getId();
	} 

	public boolean altera(Pagamento umPagamento) 
	{	try
		{	em.getReference(Pagamento.class, new Long(umPagamento.getId()));
			em.merge(umPagamento);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public boolean exclui(Pagamento umPagamento) 
	{	try
		{	Pagamento p = em.getReference(Pagamento.class, new Long(umPagamento.getId()));
			em.remove(p);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public Pagamento recuperaUmPagamento(long numero) 
		throws ObjetoNaoEncontradoException 
	{	Pagamento umPagamento = em
			.find(Pagamento.class, new Long(numero));
		
		if(umPagamento == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		return umPagamento;
	} 

	public Pagamento recuperaUmPagamentoComLock(long numero)
		throws ObjetoNaoEncontradoException
	{			
		Pagamento umPagamento = em
			.find(Pagamento.class, new Long(numero));
		
		if(umPagamento == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		em.lock(umPagamento, LockModeType.READ);
		em.refresh(umPagamento);
		
		return umPagamento;
	} 
}