package exercicio15.lance;

import java.util.List;

import corejava.Console;
import exercicio15.controleTransacao.FabricaDeAppService;
import exercicio15.produto.Produto;
import exercicio15.produto.ProdutoAppService;
import exercicio15.util.AplicacaoException;
import exercicio15.util.Util;

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
			System.out.println("3. Recuperar todos os lances");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um numero entre 1 e 4:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.
						readInt('\n' + "Informe o numero do produto: ");
					
					umProduto = produtoAppService.recuperaUmProduto(idProduto);

					if(umProduto == null)
					{	System.out.println('\n' + "Produto não encontrado");
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

						for (String mensagem : e.getMensagens())
						{	System.out.println (mensagem);
						}
					}
						
					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do lance que voce deseja remover: ");
									
					umLance = lanceAppService.recuperaUmLance(resposta);

					if(umLance == null)
					{	System.out.println('\n' + "Lance não encontrado");
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
						catch(AplicacaoException e)
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
				{	List<Lance> arrayLances = lanceAppService.recuperaLances();
									
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
