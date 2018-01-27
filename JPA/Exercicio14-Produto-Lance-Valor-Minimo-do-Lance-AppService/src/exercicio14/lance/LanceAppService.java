package exercicio14.lance;

import java.util.List;
import java.sql.Date;
import java.util.ArrayList;

import exercicio14.produto.*;
import exercicio14.util.*;

public class LanceAppService
{	
	private static LanceDAO lanceDAO = new LanceDAOImpl();
	private static ProdutoDAO produtoDAO = new ProdutoDAOImpl();

	@SuppressWarnings("unchecked")
	public long inclui(double valor, String dtCriacao, Produto umProduto) 
		throws AplicacaoException
	{	
		try
		{	JPAUtil.beginTransaction();
		
			// A execução do método  recuperaUmProdutoComLock(id)  abaixo
			// impede  que  dois lances  sejam  cadastrados em  paralelo.
			// Como este  método põe  um lock em  Produto, a inserção  de 
			// dois  lances  acontece  sempre em  série, i. é, obedecendo
			// a uma fila. Isto impede que o valor do  segundo lance seja
			// inferior ao valor do primeiro ou que se tente cadastrar um
			// lance para um produto que tenha sido removido.
			
			ArrayList mensagens = new ArrayList();
			
			try
			{	produtoDAO.recuperaUmProdutoComLock(umProduto.getId());
			}
			catch(ObjetoNaoEncontradoException e)
			{	mensagens.add("Produto não encontrado");
				throw new AplicacaoException(mensagens);
			}

			Lance ultimoLance = produtoDAO.recuperaUltimoLance(umProduto);

			double valorUltimoLance;
			Date   dataUltimoLance;  
			
			if (ultimoLance == null)
			{	valorUltimoLance = umProduto.getLanceMinimo() - 1;
				dataUltimoLance  = umProduto.getDataCadastro();
			}
			else
			{	valorUltimoLance = ultimoLance.getValor();
				dataUltimoLance  = ultimoLance.getDataCriacao();
			}
			
			boolean deuErro = false;

			if(valor <= valorUltimoLance)
			{	deuErro = true;
				mensagens.add("O valor do lance tem que ser superior a " + valorUltimoLance);
			}
			
			Date dataCriacao = null;
			
			if (!Util.dataValida(dtCriacao))
			{	deuErro = true;
				mensagens.add("Data de cadastro invalida.");
			}
			else
			{	dataCriacao = Util.strToDate(dtCriacao);

				if(dataCriacao.before(dataUltimoLance))
				{	deuErro = true;
					mensagens.add("A data de emissão do lance não pode ser anterior a " 
						+ Util.dateToStr(dataUltimoLance));
				}

				Date hoje = new Date(System.currentTimeMillis());
			
				if(dataCriacao.after(hoje))
				{	deuErro = true;
					mensagens.add("A data de emissão do lance não pode ser posterior à data de hoje: " 
						+ Util.dateToStr(hoje));
				}
			}
				
			long numero;

			if (!deuErro)
			{	Lance umLance = new Lance(valor, dataCriacao, umProduto); 
				// Evita a recuperacao de todos os lances de um produto.
				numero = lanceDAO.inclui(umLance);
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

	public void exclui(Lance umLance) 
	{	try
		{	JPAUtil.beginTransaction();

			lanceDAO.exclui(umLance);

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

	public Lance recuperaUmLance(long numero) 
		throws AplicacaoException
	{	try
		{	Lance umLance = lanceDAO.recuperaUmLance(numero);

			return umLance;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Lance não encontrado");
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

	public List<Lance> recuperaLances()
	{	try
		{	List<Lance> lances = lanceDAO.recuperaLances();

			return lances;
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