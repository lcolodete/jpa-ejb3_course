package ex02.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;

import ex02.dominio.Lance;
import ex02.dominio.Produto;
import ex02.excecao.ObjetoNaoEncontradoException;

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

		if(umProduto == null)
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

	public Produto recuperaUmProdutoELances(long numero) 
		throws ObjetoNaoEncontradoException
	{	
		try
		{	String busca = "select distinct p from Produto p " +
			               "left outer join fetch p.lances " +
			               "where p.id = :id";

			Produto umProduto =
				(Produto) em.createQuery(busca)
							    .setParameter("id", numero)
							    .getSingleResult();

			return umProduto;
		} 
		catch(NoResultException e)
		{	throw new ObjetoNaoEncontradoException();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	if (em == null)
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>  NULLLLLLLLLLLLLLLLLLLLLLLL");
	
		return em
			.createQuery("select distinct p from Produto p " +
					     "left outer join fetch p.lances l " +
					     "order by p.id asc")
			.getResultList();
	} 

	public Lance recuperaUltimoLance(Produto produto)
	{	 
		try
		{	String busca = 
				"select distinct lance from Lance lance " +
			    "left outer join fetch lance.produto " +
	            "where lance.produto.id = :idProduto and " +
	                  "lance.valor = " +
	                  "(select max(l.valor) " +
	                   "from Lance l " +
	                   "where l.produto.id = :idProduto)";		

			Lance lance = (Lance)em
				.createQuery(busca)
				.setParameter("idProduto", produto.getId())
				.getSingleResult();

			return lance;
		} 
		catch(NoResultException e)
		{	return null;
		}
	}
}