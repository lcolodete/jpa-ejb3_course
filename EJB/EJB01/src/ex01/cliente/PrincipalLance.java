package ex01.cliente;

import java.sql.Date;
import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex01.dominio.Lance;
import ex01.dominio.Produto;
import ex01.ejb.LanceEJBRemote;
import ex01.ejb.ProdutoEJBRemote;
import ex01.excecao.AplicacaoException;
import ex01.util.Util;

public class PrincipalLance
{	public static void main (String[] args) 
	{	
	    Context jndiContext;
	    LanceEJBRemote lanceDAO = null;
	    ProdutoEJBRemote produtoDAO = null;
		try 
		{	jndiContext = new javax.naming.InitialContext();
			Object ref1 = jndiContext.lookup("exercicio01.LanceEJBBean/remote");
			Object ref2 = jndiContext.lookup("exercicio01.ProdutoEJBBean/remote");
		    lanceDAO = (LanceEJBRemote)ref1;
		    produtoDAO = (ProdutoEJBRemote)ref2;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}
		
		double valor;
		Date dataCriacao;
		String umaData;

		Produto umProduto;
		Lance umLance;


		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
/* ==> */   System.out.println('\n' + "1. Cadastrar um lance de um produto");
			System.out.println("2. Remover um lance");
			System.out.println("3. Recuperar todos os lances");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 4:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.readInt('\n' + "Informe o numero do produto: ");
					
					try
					{	umProduto = produtoDAO.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					valor = Console.readDouble('\n' + "Informe o valor do lance: ");
					umaData = Console.readLine("Informe a data de emissao do lance: ");
					dataCriacao = Util.strToDate(umaData);

					// Cria o lance já informando qual é o produto
									
					umLance = new Lance(valor, dataCriacao, umProduto);
					
					// Salva o lance, evitando a recuperacao de todos os lances 
					// de um produto.
												
					lanceDAO.inclui(umLance);
						
					System.out.println('\n' + "Lance adicionado com sucesso");						

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do lance que voce deseja remover: ");
									
					try
					{	umLance = lanceDAO.recuperaUmLance(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umLance.getId() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emissao = " + umLance.getDataCriacao());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do lance?");

					if(resp.equals("s"))
					{	lanceDAO.exclui (umLance);
					}
					else
					{	System.out.println('\n' + 
							"Lance nao removido.");
					}
					
					break;
				}

				case 3:
				{	List<Lance> lances = lanceDAO.recuperaLances();
									
					if (lances.size() == 0)
					{	System.out.println('\n' + "Nao ha lances cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Lance lance : lances)
					{	System.out.println(	
							"Numero = " + lance.getId() + 
							"  Valor = " + lance.getValor() +
							"  Data de Emissao = " + lance.getDataCriacao());
					}
										
					break;
				}

				case 4:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");						
			}
		}		
	}
}
