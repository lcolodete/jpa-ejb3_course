package ex02.cliente;

import java.util.List;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex02.dominio.Lance;
import ex02.dominio.Produto;
import ex02.ejb.LanceEJBRemote;
import ex02.ejb.ProdutoEJBRemote;
import ex02.excecao.AplicacaoException;

public class PrincipalLance
{	public static void main (String[] args) 
	{	
		double valor;
		String dataCriacao;

		Produto umProduto;
		Lance umLance;

	    Context jndiContext;

	    ProdutoEJBRemote produtoEJBRemote = null;
		LanceEJBRemote lanceEJBRemote = null;

		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref1 = jndiContext.lookup("exercicio02.LanceEJBBean/remote");
		    Object ref2 = jndiContext.lookup("exercicio02.ProdutoEJBBean/remote");
		    lanceEJBRemote = (LanceEJBRemote)ref1;
		    produtoEJBRemote = (ProdutoEJBRemote)ref2;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um lance de um produto");
			System.out.println("2. Remover um lance");
			System.out.println("3. Recuperar todos os lances");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um número entre 1 e 4:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.
						readInt('\n' + "Informe o número do produto: ");
					
					try
					{	umProduto = produtoEJBRemote.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					valor = Console.readDouble('\n' + 
									"Informe o valor do lance: ");
					dataCriacao = Console.readLine(
									"Informe a data de emissão do lance: ");
					
					try
					{	lanceEJBRemote.inclui(valor, dataCriacao, umProduto);	

						System.out.println('\n' + "Lance adicionado com sucesso");						
					}
					catch(AplicacaoException e)
					{	System.out.println("");

						for (int i=0; i<e.getMensagens().size(); i++)
						{	System.out.println (e.getMensagens().get(i));
						}
					}
						
					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o número do lance que você deseja remover: ");
									
					try
					{	umLance = lanceEJBRemote.recuperaUmLance(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umLance.getId() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emissão = " + umLance.getDataCriacao());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remoção do lance?");

					if(resp.equals("s"))
					{	lanceEJBRemote.exclui (umLance);
						System.out.println('\n' + 
								"Lance removido com sucesso!");
					}
					else
					{	System.out.println('\n' + 
							"Lance não removido.");
					}
					
					break;
				}

				case 3:
				{	List<Lance> lances = lanceEJBRemote.recuperaLances();
									
					if (lances.size() == 0)
					{	System.out.println('\n' + 
							"Nao há lances cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Lance lance : lances)
					{	System.out.println(	
							"Número = " + lance.getId() + 
							"  Valor = " + lance.getValor() +
							"  Data de Emissão = " + 
									lance.getDataCriacao());
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
