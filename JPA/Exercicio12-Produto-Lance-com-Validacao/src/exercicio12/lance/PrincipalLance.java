package exercicio12.lance;

import java.util.List;

import corejava.Console;
import exercicio12.produto.Produto;
import exercicio12.produto.ProdutoDAO;
import exercicio12.produto.ProdutoDAOImpl;
import exercicio12.util.AplicacaoException;

public class PrincipalLance
{	public static void main (String[] args) 
	{	
		String valor;
		String dataCriacao;

		Produto umProduto;
		Lance umLance;

		ProdutoDAO produtoDAO = new ProdutoDAOImpl();
		LanceDAO lanceDAO = new LanceDAOImpl();

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um lance de um produto");
			System.out.println("2. Alterar um lance");
			System.out.println("3. Remover um lance");
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
					{	umProduto = produtoDAO.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					valor = Console.readLine('\n' + 
									"Informe o valor do lance: ");
					dataCriacao = Console.readLine(
									"Informe a data de emissao do lance: ");
									
					umLance = new Lance();	// Construtor com argumentos 
											// deixa de existir.
					
					boolean deuErro = false;
					
					try
					{	umLance.setValor(valor);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Valor inválido");
						deuErro = true;
					}
					
					try
					{	umLance.setDataCriacao(dataCriacao);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Data de emissão inválida");
						deuErro = true;
					}
					
					if (!deuErro)
					{	umLance.setProduto(umProduto); // Evita a recuperacao 
													   // de todos os lances 
													   // de um produto.
						lanceDAO.inclui(umLance);

						System.out.println('\n' + "Lance adicionado com sucesso");
					}

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
					  "Digite o numero do lance que voce deseja alterar: ");
										
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
						"    Data emissao = " + umLance.getDataCriacao());
																						
					System.out.println('\n' + "O que voce deseja alterar?");
					System.out.println('\n' + "1. Valor");
					System.out.println("2. Data de Emissao");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um numero de 1 a 2:");			
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoValor = Console
								.readLine('\n' + "Digite o novo valor: ");

							try
							{	umLance.setValor(novoValor);

								lanceDAO.altera(umLance);

								System.out.println('\n' + 
									"Alteracao de valor efetuada com sucesso!");
							}
							catch(AplicacaoException e)
							{	System.out.println('\n' + "Valor inválido");
							}

							break;

						case 2:
							String novaData = Console.
								readLine('\n' + "Digite a nova data de emissão: ");

							try
							{	umLance.setDataCriacao(novaData);

								lanceDAO.altera(umLance);

								System.out.println('\n' + 
									"Alteração de data efetuada com sucesso!");
							}
							catch(AplicacaoException e)
							{	System.out.println('\n' + "Data de emissão inválida");
							}
							
							break;

						default:
							System.out.println('\n' + "Opcao inválida!");						
					}

					break;
				}

				case 3:
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

				case 4:
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
