package exercicio11.lance;

import java.sql.Date;
import java.util.List;

import corejava.Console;
import exercicio11.produto.Produto;
import exercicio11.produto.ProdutoDAO;
import exercicio11.produto.ProdutoDAOImpl;
import exercicio11.util.AplicacaoException;
import exercicio11.util.Util;

public class PrincipalLance
{	public static void main (String[] args) 
	{	
		double valor;
		Date dataCriacao;
		String umaData;

		Produto umProduto;
		Lance umLance;

		ProdutoDAO produtoDAO = new ProdutoDAOImpl();
		LanceDAO lanceDAO = new LanceDAOImpl();

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
					long idProduto = Console.readInt('\n' + 
										"Informe o numero do produto: ");
					
					try
					{	umProduto = produtoDAO.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					valor = Console.readDouble('\n' + 
									"Informe o valor do lance: ");
					umaData = Console.readLine(
									"Informe a data de emissao do lance: ");
					dataCriacao = Util.strToDate(umaData);
									
					umLance = new Lance (valor, dataCriacao, umProduto);
					
					// Evita a recuperacao de todos os lances 
					// de um produto.
												
					lanceDAO.inclui(umLance);
						
					System.out.println('\n' + 
								"Lance adicionado com sucesso");						

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
					{	try
						{	lanceDAO.exclui(umLance);
							System.out.println('\n' + "Lance removido com sucesso!");
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
				{	List<Lance> arrayLances = lanceDAO.recuperaLances();
									
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
