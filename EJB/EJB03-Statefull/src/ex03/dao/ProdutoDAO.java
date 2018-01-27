package ex03.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;

import ex03.dominio.Produto;
import ex03.excecao.ObjetoNaoEncontradoException;

public class ProdutoDAO
{	
    private EntityManager em;
    
    public ProdutoDAO(EntityManager em)
    {	this.em = em;
    }

	public long inclui(Produto umProduto) 
	{	em.persist(umProduto);
		return umProduto.getId();
	} 

	public boolean altera(Produto umProduto) 
	{	try
		{	em.getReference(Produto.class, new Long(umProduto.getId()));
			em.merge(umProduto);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public boolean exclui(Produto umProduto) 
	{	try
		{	Produto p = em.getReference(Produto.class, new Long(umProduto.getId()));
			em.remove(p);
			return true;
		}
		catch(EntityNotFoundException e)
		{	return false;
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws ObjetoNaoEncontradoException 
	{	Produto umProduto = em
			.find(Produto.class, new Long(numero));
	
		if (umProduto == null)
		{	throw new ObjetoNaoEncontradoException();
		}
			
		return umProduto;
	} 

	public Produto recuperaUmProdutoComLock(long numero) 
		throws ObjetoNaoEncontradoException 
	{			
		Produto umProduto = (Produto)em
			.find(Produto.class, new Long(numero));

		if(umProduto == null)
		{	throw new ObjetoNaoEncontradoException();
		}
		
		em.lock(umProduto, LockModeType.READ);
		em.refresh(umProduto);

		return umProduto;
	} 
}