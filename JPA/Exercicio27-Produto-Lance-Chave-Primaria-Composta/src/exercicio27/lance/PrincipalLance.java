package exercicio27.lance;

import java.util.List;

import corejava.Console;
import exercicio27.controleTransacao.FabricaDeAppService;
import exercicio27.produto.Produto;
import exercicio27.produto.ProdutoAppService;
import exercicio27.util.AplicacaoException;
import exercicio27.util.Util;

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
/* ==> */	System.out.println('\n' + "1. Cadastrar um lance de um produto");
/* ==> */	System.out.println("2. Remover um lance");
/* ==> */	System.out.println("3. Recuperar um lance e seu respectivo produto");
			System.out.println("4. Recuperar todos os lances");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um numero entre 1 e 5:");			
					
			switch (opcao)
			{	case 1:
				{
					long idProduto = Console.
						readInt('\n' + "Informe o n�mero do produto: ");
					
					try
					{	umProduto = produtoAppService.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					long numeroLance = Console.readInt('\n' + 
									"Informe o n�mero do lance: ");
					valor = Console.readDouble("Informe o valor do lance: ");
					dataCriacao = Console.readLine(
									"Informe a data de emiss�o do lance: ");

					try
					{	lanceAppService.inclui(idProduto, numeroLance,
							                   valor,     Util.strToDate(dataCriacao), 
							                   umProduto);	

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
				{	int idProduto = Console.readInt('\n' + 
						"Digite o n�mero do produto: ");
					int numeroLance = Console.readInt('\n' + 
						"Digite o n�mero do lance: ");
									
					try
					{	umLance = lanceAppService.recuperaUmLancePorIdProdutoNumeroLance(idProduto, numeroLance);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero do produto = " + umLance.getId().getIdProduto() + 
						"    N�mero do lance = " + umLance.getId().getNumeroLance() + 
						"    Valor = " + umLance.getValor() +
						"    Data de Emissao = " + umLance.getDataCriacao());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do lance?");
	
					if(resp.equals("s"))
					{	lanceAppService.exclui (umLance);
						System.out.println('\n' + 
								"Lance removido com sucesso!");
					}
					else
					{	System.out.println('\n' + 
							"Lance nao removido.");
					}
					
					break;
				}

				case 3:
				{	int idProduto = Console.readInt('\n' + 
						"Digite o n�mero do produto: ");
					int numeroLance = Console.readInt('\n' + 
						"Digite o n�mero do lance: ");

					try
					{	umLance = lanceAppService.recuperaUmLanceComProduto(idProduto, numeroLance);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Lance n�o encontrado.");
						break;
					}
										
					System.out.println('\n' + 
						"N�mero do produto = " + umLance.getId().getIdProduto() + 
						"    N�mero do lance = " + umLance.getId().getNumeroLance() + 
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
							"N�mero do produto = " + lance.getId().getIdProduto() + 
							"  N�mero do lance = " + lance.getId().getNumeroLance() + 
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
					System.out.println('\n' + "Op��o inv�lida!");						
			}
		}		
	}
}
