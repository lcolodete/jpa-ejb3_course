package ex01.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex01.dominio.Produto;
import ex01.excecao.AplicacaoException;

@Stateless
@RemoteBinding(jndiBinding="exercicio01.ProdutoEJBBean/remote")
public class ProdutoEJBBean implements ProdutoEJBRemote
{	
@PersistenceContext(unitName="EJB") private EntityManager em;

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
		throws AplicacaoException
	{	Produto umProduto = em.find(Produto.class, new Long(numero));

		if(umProduto == null)
		{	throw new AplicacaoException("Produto não encontrado");
		}

		return umProduto;
	} 

	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	// FOI PRECISO ACRESCENTAR A CLÁUSULA DISTINCT NO SELECT PQ
			// O MÉTODO  getSingleResult()  ESTAVA  DANDO  ERRO.  SE UM 
			// PRODUTO  POSSUI 4  LANCES É  CRIADO UM  List COM O MESMO 
			// PRODUTO NAS 4 PRIMEIRAS POSIÇÕES. E CADA PRODUTO  APONTA
			// PARA SEUS LANCES.
		
			String busca = "select distinct p from Produto p " + 
			               "left outer join fetch p.lances l " +
	                       "where p.id = :id order by p.id"; 
	
			// A busca retorna um único produto (uniqueResult()).
			
			return (Produto) em
				.createQuery(busca)
				.setParameter("id", new Long(numero))
				.getSingleResult();
		} 
		catch(NoResultException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	List produtos = em
			.createQuery("select distinct p from Produto p " + 
					     "left outer join fetch p.lances " +
					     "order by p.id")
			.getResultList();
		
		return produtos;
	} 
}