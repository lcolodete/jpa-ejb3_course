package exercicio14.produto;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import exercicio14.util.AplicacaoException;
import exercicio14.util.InfraestruturaException;
import exercicio14.util.JPAUtil;
import exercicio14.util.ObjetoNaoEncontradoException;
import exercicio14.util.Util;

public class ProdutoAppService
{	
	private static ProdutoDAO produtoDAO = new ProdutoDAOImpl();

	@SuppressWarnings("unchecked")
	public long inclui(String nome, String descricao, 
	                   double lanceMinimo, String dtCadastro) 
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();

			boolean deuErro = false;
			ArrayList mensagens = new ArrayList();
			
			if(lanceMinimo <= 0)
			{	deuErro = true;
				mensagens.add("Lance minimo invalido.");
			}

			Date dataCadastro = null;
			
			if (!Util.dataValida(dtCadastro))
			{	deuErro = true;
				mensagens.add("Data de cadastro invalida.");
			}
			else
			{	dataCadastro = Util.strToDate(dtCadastro);
				Date hoje = new Date(System.currentTimeMillis());
			
				if (dataCadastro.after(hoje))
				{	deuErro = true;
					mensagens.add(
						"A data de cadastramento do produto não pode ser " +
						 "posterior à data de hoje: " + Util.dateToStr(hoje));
				}
			}
			
			long numero;
			
			if (!deuErro)
			{	Produto umProduto = new Produto(nome, descricao, lanceMinimo, dataCadastro);
				numero = produtoDAO.inclui(umProduto);
			}
			else
			{	throw new AplicacaoException(mensagens);
			}

			JPAUtil.commitTransaction();
			
			return numero;
		} 
		catch(AplicacaoException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		catch(InfraestruturaException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public void altera(Produto umProduto) 
	{	try
		{	JPAUtil.beginTransaction();

			produtoDAO.altera(umProduto);

			JPAUtil.commitTransaction();
		} 
		catch(ObjetoNaoEncontradoException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }
		}
		catch(InfraestruturaException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public void exclui(Produto umProduto) 
		throws AplicacaoException
	{	try
		{	JPAUtil.beginTransaction();

			Produto produto = produtoDAO.recuperaUmProdutoELances(umProduto.getId());

			if(produto.getLances().size() > 0)
			{	throw new AplicacaoException("Este produto possui lances e nao pode ser removido");
			}

			produtoDAO.exclui(produto);

			JPAUtil.commitTransaction();
		} 
		catch(ObjetoNaoEncontradoException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw new AplicacaoException("Produto nao encontrado");
		}
		catch(AplicacaoException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		catch(InfraestruturaException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{ }

		    throw e;
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public Produto recuperaUmProduto(long numero) 
		throws AplicacaoException
	{	try
		{	Produto umProduto = produtoDAO.recuperaUmProduto(numero);
			
			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto nao encontrado");
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public Produto recuperaUmProdutoELances(long numero) 
		throws AplicacaoException
	{	try
		{	Produto umProduto = produtoDAO.recuperaUmProdutoELances(numero);

			return umProduto;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Produto não encontrado");
		}
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}

	public List<Produto> recuperaProdutosELances()
	{	try
		{	List<Produto> produtos = produtoDAO.recuperaProdutosELances();

			return produtos;
		} 
		finally
		{   try
		    {   JPAUtil.closeEntityManager();
		    }
		    catch(InfraestruturaException he)
		    {	throw he;
		    }
		}
	}
}