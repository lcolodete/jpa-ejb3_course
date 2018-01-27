package exercicio12.produto;

import java.util.List;
import java.util.Set;

import corejava.Console;
import exercicio12.lance.Lance;
import exercicio12.util.AplicacaoException;

public class PrincipalProduto
{	public static void main (String[] args) 
	{	
		String nome;
		String descricao;
		String lanceMinimo;
		String dataCadastro;
		Produto umProduto;

		ProdutoDAO produtoDAO = new ProdutoDAOImpl();

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um produto");
			System.out.println("2. Alterar um produto");
			System.out.println("3. Remover um produto");
			System.out.println("4. Listar um produto e seus lances");
			System.out.println("5. Listar todos os produtos e seus lances");
			System.out.println("6. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um numero entre 1 e 6:");
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome do produto: ");
					descricao = Console.readLine(
						"Informe a descricao do produto: ");
					lanceMinimo = Console.readLine(
						"Informe o valor do lance minimo: ");
					dataCadastro = Console.readLine(
						"Informe a data de cadastramento do produto: ");
						
					umProduto = new Produto();
					
					boolean deuErro = false;
					
					try
					{	umProduto.setNome(nome);			
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Nome invalido");
						deuErro = true;
					}
					
					try
					{	umProduto.setDescricao(descricao);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Descricao invalida");
						deuErro = true;
					}
					
					try
					{	umProduto.setLanceMinimo(lanceMinimo);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Valor de lance invalido");
						deuErro = true;
					}

					try
					{	umProduto.setDataCadastro(dataCadastro);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + "Data de cadastro invalida");
						deuErro = true;
					}

					if (!deuErro)
					{	long numero = produtoDAO.inclui(umProduto);

						System.out.println('\n' + "Produto numero " + 
						    numero + " incluido com sucesso!");	
					}

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do produto que voce deseja alterar: ");
										
					try
					{	umProduto = produtoDAO.recuperaUmProduto(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Descri��o = " + umProduto.getDescricao());
												
					System.out.println('\n' + "O que voce deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println("2. Descricao");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um numero de 1 a 2:");
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
										readLine("Digite o novo nome: ");

							try
							{	umProduto.setNome(novoNome);

								try
								{	produtoDAO.altera(umProduto);
									System.out.println('\n' + 
									  "Alteracao de nome efetuada com sucesso!");
								}
								catch(AplicacaoException e)
								{	System.out.println('\n' + e.getMessage());
								}
							}
							catch(AplicacaoException e)
							{	System.out.println('\n' + "Nome invalido");
							}
								
							break;
					
						case 2:
							String novaDescricao = Console.
									readLine("Digite a nova descricao: ");

							try
							{	umProduto.setDescricao(novaDescricao);

								try
								{	produtoDAO.altera(umProduto);
									System.out.println('\n' + 
									  "Alteracao de descri��o efetuada com sucesso!");
								}
								catch(AplicacaoException e)
								{	System.out.println('\n' + e.getMessage());
								}
							}
							catch(AplicacaoException e)
							{	System.out.println('\n' + "Descricao invalida");
							}

							break;

						default:
							System.out.println('\n' + "Opcao invalida!");
					}

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do produto que voce deseja remover: ");
									
					try
					{	umProduto = produtoDAO
							.recuperaUmProdutoELances(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					if(umProduto.getLances().size() > 0)
					{	System.out.println('\n' + 
							"Este produto possui lances e nao pode ser removido.");
						break;
					}	

					System.out.println('\n' + 
						"N�mero = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Descricao = " + umProduto.getDescricao());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do produto?");

					if(resp.equals("s"))
					{	try
						{	produtoDAO.exclui(umProduto);
							System.out.println('\n' + 
								"Produto removido com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + 
							"Produto nao removido.");
					}
					
					break;
				}

				case 4:
				{	
					long numero = Console.readInt('\n' + 
						"Informe o numero do produto: ");
				
					try
					{	umProduto = produtoDAO
							.recuperaUmProdutoELances(numero);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
									
					System.out.println('\n' + 
						"Id = " + umProduto.getId() +
					    "  Nome = " + umProduto.getNome() +
					    "  Descricao = " + umProduto.getDescricao() +
					    "  Lance minimo = " + umProduto.getLanceMinimo() +
					    "  Data Cadastro = " + umProduto.
					    						getDataCadastroMasc());
					
					Set<Lance> lances = umProduto.getLances();
					
					for (Lance lance : lances)
					{	System.out.println(	'\n' + 
							"      Lance numero = "  + lance.getId() + 
							"  valor = "  + lance.getValor() +
							"  Data = "  + lance.getDataCriacaoMasc());
					}	
										
					break;
				}

				case 5:
				{
					List<Produto> produtos = produtoDAO.recuperaProdutosELances();
						
					if (produtos.size() != 0)
					{	System.out.println("");

						for (Produto produto : produtos)
						{	System.out.println('\n' + 
								"Produto numero = "  + produto.getId() + 
								"  Nome = "  + produto.getNome() +
								"  Descricao = "  + produto.getDescricao() +
								"  Lance minimo = "  + 
										produto.getLanceMinimoMasc());

							Set<Lance> lances = produto.getLances();
							
							for (Lance lance : lances)
							{	System.out.println(	'\n' + 
								  "      Lance numero = "  + lance.getId() + 
								  "  valor = " + lance.getValor() +
								  "  Data = " + lance.getDataCriacaoMasc());
							}	
                    	} 
					}
					else
					{	System.out.println('\n' + 
						  "Nao ha produtos cadastrados com esta descricao.");
					}

					break;
				}

				case 6:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Op��o inv�lida!");
			}
		}		
	}
}
