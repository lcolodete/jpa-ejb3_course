package ex07.dao;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;

import ex07.dominio.Conta;
import ex07.excecao.ObjetoNaoEncontradoException;

public class ContaDAO
{	
    private EntityManager em;
    
    public ContaDAO(EntityManager em)
    {	this.em = em;
    }

	public long inclui(Conta umaConta) 
	{	em.persist(umaConta);
		return umaConta.getId();
	} 

	public boolean altera(Conta umaConta) 
	{	try
		{	em.getReference(Conta.class, new Long(umaConta.getId()));
			em.merge(umaConta);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public boolean exclui(Conta umaConta) 
	{	try
		{	Conta p = em.getReference(Conta.class, new Long(umaConta.getId()));
			em.remove(p);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public Conta recuperaUmaConta(long numero) 
		throws ObjetoNaoEncontradoException 
	{	Conta umaConta = (Conta)em
			.find(Conta.class, new Long(numero));
			
		if (umaConta == null)
		{	throw new ObjetoNaoEncontradoException();
		}

		return umaConta;
	} 

	public Conta recuperaUmaContaComLock(long numero) 
		throws ObjetoNaoEncontradoException 
	{			
		Conta umaConta = (Conta)em
			.find(Conta.class, new Long(numero));

		if (umaConta == null)
		{	throw new ObjetoNaoEncontradoException();
		}
		else
		{	em.lock(umaConta, LockModeType.READ);
			em.refresh(umaConta);
		}

		return umaConta;
	} 

	@SuppressWarnings("unchecked")
	public Set<Conta> recuperaContas()
	{	List contas = em
			.createQuery("from Conta c order by c.id asc")
			.getResultList();
	
		if(contas == null)
			System.out.println(">>>>>>>>>>>>>>>>>>>>>> Tabela vazia, conta == null");

		return new LinkedHashSet(contas);
	} 
}