package exercicio26.lance;

import java.util.List;

import corejava.Console;
import exercicio26.controleTransacao.FabricaDeAppService;
import exercicio26.produto.Produto;
import exercicio26.produto.ProdutoAppService;
import exercicio26.util.AplicacaoException;
import exercicio26.util.Util;

public class PrincipalLance
{	public static void main (String[] args) 
	{	
		double valor;
		String dataCriacao;

		Produto umProduto;
		Lance umLance;

		ProdutoAppService produtoAppService = null;
		LanceAppService lanceAppService = null;
		
		try 
		{	produtoAppService = (ProdutoAppService) FabricaDeAppService
				.getAppService(ProdutoAppService.class);
			lanceAppService = (LanceAppService) FabricaDeAppService
				.getAppService(LanceAppService.class);
		} 
		catch (Exception e) 
		{	e.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um lance de um produto");
			System.out.println("2. Remover um lance");
			System.out.println("3. Recuperar um lance e seu respectivo produto - Exercício");
			System.out.println("4. Recuperar todos os lances");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um numero entre 1 e 5:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.
						readInt('\n' + "Informe o numero do produto: ");
					
					try
					{	umProduto = produtoAppService.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					valor = Console.readDouble('\n' + 
									"Informe o valor do lance: ");
					dataCriacao = Console.readLine(
									"Informe a data de emissao do lance: ");

					try
					{	lanceAppService.inclui(valor, Util.strToDate(dataCriacao), umProduto);	

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
						"Digite o numero do lance que voce deseja remover: ");
									
					try
					{	umLance = lanceAppService.getPorId(resposta);
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
					{	try
						{	lanceAppService.exclui (umLance);
							System.out.println('\n' + 
								"Lance removido com sucesso!");
						} 
						catch (AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + 
							"Lance nao removido.");
					}
					
					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do lance que voce deseja recueprar: ");

					try
					{	umLance = lanceAppService.recuperaUmLanceComProduto(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Lance não encontrado.");
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umLance.getId() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emissao = " + umLance.getDataCriacao() +
						"    Nome do Produto = " + umLance.getProduto().getNome());
					
					break;
				}

				case 4:
				{	List<Lance> arrayLances = lanceAppService.recuperaListaDeLances();
									
					if (arrayLances.size() == 0)
					{	System.out.println('\n' + 
							"Nao ha lances cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Lance lance : arrayLances)
					{	System.out.println(	
							"Numero = " + lance.getId() + 
							"  Valor = " + lance.getValor() +
							"  Data de Emissao = " + 
									lance.getDataCriacao());
					}
										
					break;
				}

				case 5:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");						
			}
		}		
	}
}
