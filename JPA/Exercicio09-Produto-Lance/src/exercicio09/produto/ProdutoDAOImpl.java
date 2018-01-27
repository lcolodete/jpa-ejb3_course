package exercicio09.produto;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import exercicio09.lance.Lance;
import exercicio09.util.AplicacaoException;
import exercicio09.util.InfraestruturaException;
import exercicio09.util.JPAUtil;
import exercicio09.util.ObjetoNaoEncontradoException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();

			em.persist(umProduto);
			
			JPAUtil.commitTransaction();
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	public void altera(Produto umProduto)
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();

			em.getReference(Produto.class, new Long(umProduto.getId()));
			
			em.merge(umProduto);
			
			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	JPAUtil.rollbackTransaction();
			throw new AplicacaoException("Produto n�o encontrado.");
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	public void exclui(Produto umProduto) 
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();
			EntityManager em = JPAUtil.getEntityManager();
		
			Produto p = em.getReference(Produto.class, new Long(umProduto.getId()));

			em.remove(p);

			JPAUtil.commitTransaction();
		}
		catch(EntityNotFoundException e)
		{	JPAUtil.rollbackTransaction();
			throw new AplicacaoException("Produto n�o encontrado.");
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = em.find(Produto.class, new Long(numero));

			if(umProduto == null)
			{	throw new ObjetoNaoEncontradoException();
			}

			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();
	
			/* O  que a  maioria das  pessoas pensam  quando escutam a 
			 * palavra  join no  contexto  de  bancos  de dados SQL  � 
			 * um inner join.  Um  inner  join � o  tipo de join  mais 
			 * simples.
			 *
			 * Por exemplo, para se  recuperar  todos os  produtos que 
			 * possuem lances, � preciso utilizar um inner join. Neste 
			 * caso apenas produtos que possuem lances s�o recuperados. 
			 * Mas se desejarmos recuperar os produtos e valores nulos
			 * para os  dados dos  lances  quando o  produto n�o tiver 
			 * lances,  neste caso  utilizaremos um  left  outer join. 
			 * (estilo ANSI).
			 *
			 * Se fizermos a  jun��o de  duas tabelas PRODUTO e LANCE, 
			 * utilizando um inner join obteremos  todos os produtos e 
			 * seus lances na tabela resultante.  No caso de um  "left 
			 * outer join", cada  linha da  tabela a  esquerda (left - 
			 * tabela PRODUTO) que nunca satisfaz a condi��o de jun��o
			 * tamb�m  �  inclu�da  no  resultado  com  valores  nulos 
			 * retornados para todas as colunas da tabela LANCE.
			 * 
			 * Um "right outer join" recuperaria  todos os lances  com 
			 * um valor nulo para o produto se o lance n�o tem rela��o
			 * com nenhum produto.
			 * 
			 * A condi��o de  join deve ser  especificada na  cl�usula 
			 * "on" para  uma  jun��o no  estilo "ANSI" ou na cl�usula 
			 * "where" para uma jun��o no estilo "theta". 
			 * 
			 * Exemplo: P.PRODUTO_ID = L.PRODUTO_ID.
			 *
			 * Left Outer Join no Oracle:
			 *
			 * SELECT P.ID, P.NOME, L.ID, L.VALOR
			 * FROM PRODUTO P, LANCE L
			 * WHERE P.ID = L.PRODUTO_ID(+)
			 * ORDER BY P.ID, L.VALOR;
			 */
	
			String busca = "select p from Produto p left outer join fetch p.lances l where p.id = :id order by p.id";  
	
			// A busca retorna um �nico produto (uniqueResult()).
			
			Produto umProduto =	(Produto) em
				.createQuery(busca)
				.setParameter("id", numero)
				.getSingleResult();

			/* Em fun��o do m�todo getSingleResult() ser� propagada
			 * a exce��o NoResultException caso nenhum produto seja
			 * encontrado.
			 */
			
			return umProduto;
		} 
		catch(NoResultException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutosELances()
	{	try
		{	EntityManager em = JPAUtil.getEntityManager();

			return em
				.createQuery("select p from Produto p left outer join fetch p.lances order by p.id")
				.getResultList();
		} 
		finally
		{   JPAUtil.closeEntityManager();
		}
	}

	@SuppressWarnings("unchecked")
	public void adicionarLance(long idProduto, Lance umLance) 
	{	try
		{	JPAUtil.beginTransaction();

			EntityManager em = JPAUtil.getEntityManager();

			Produto umProduto = em.find(Produto.class, new Long(idProduto));

			// Designa produto a lance
			umLance.setProduto(umProduto);

			// Adiciona o lance � cole��o de lances do produto
			umProduto.getLances().add(umLance);

  			// O m�todo add() retorna true se o elemento �  adicionado
  			// ao Set, e  retorna  false se o  elemento j�  existe  no 
  			// Set.
  		 	
  		      
			/* Como em  nenhum  momento estamos  persistindo  o  lance 
			 * atrav�s do m�todo persist(), a �nica forma de persist�-
			 * lo � adicionando-o � cole��o de lances de Produto,  uma 
			 * vez  que  CascadeType.PERSIST e  CascadeType.MERGE  foi 
			 * especificado  para esta cole��o.  No entanto, � preciso
			 * executar  o m�todo umLance.setProduto(umProduto),  caso
			 * contr�rio quando o lance  for  inserido  no  banco   de
			 * dados o valor especificado para  produto  ser� null,  o 
			 * que provocar� um erro.
		 	 * 
		 	 * umProduto.getLances().add(umLance) ==> provoca o insert
		 	 *                       no  banco de  dados em  fun��o do 
		 	 *                       CascadeType.PERSIST.
		 	 *
		 	 * O  m�todo  add(umLance) acima provoca a recupera��o  de 
		 	 * todos  os  lances de  um  produto (conforme vem abaixo) 
		 	 * para poder  adicionar o lance  corrente ao conjunto  de 
		 	 * lances:
		 	 * 
		 	 * select lances0_.PRODUTO_ID as PRODUTO4_1_, 
		 	 *        lances0_.ID as ID1_, lances0_.ID as ID1_0_, 
		 	 *        lances0_.VALOR as VALOR1_0_, 
		 	 *        lances0_.DATA_CRIACAO as DATA3_1_0_, 
		 	 *        lances0_.PRODUTO_ID as PRODUTO4_1_0_ 
		 	 * from LANCE lances0_ 
		 	 * where lances0_.PRODUTO_ID=?
		 	 */

			JPAUtil.commitTransaction();
		} 
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   JPAUtil.closeEntityManager();
		}
	}
}