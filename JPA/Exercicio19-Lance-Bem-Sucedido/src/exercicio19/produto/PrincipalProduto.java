package exercicio19.produto;

import java.util.Set;

import corejava.Console;
import exercicio19.controleTransacao.FabricaDeAppService;
import exercicio19.lance.Lance;
import exercicio19.util.AplicacaoException;
import exercicio19.util.Util;

public class PrincipalProduto
{	public static void main (String[] args) 
	{	
		String nome;
		String descricao;
		double lanceMinimo;
		String dataCadastro;
		Produto umProduto;
		
		ProdutoAppService produtoAppService = null;		
		
		try 
		{	produtoAppService = (ProdutoAppService) FabricaDeAppService
				.getAppService(ProdutoAppService.class);
		} 
		catch (Exception e) 
		{	e.printStackTrace();
			System.exit(1);
		}
		
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um produto");
			System.out.println("2. Alterar um produto");
			System.out.println("3. Remover um produto");
			System.out.println("4. Listar um produto e seus lances");
			System.out.println("5. Listar todos os produtos e seus lances");
			System.out.println("");
			System.out.println("6. Designar um lance como vencedor para um produto");
			System.out.println("7. Recuperar o lance vencedor para um produto");
			System.out.println("");
			System.out.println("8. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 8:");			
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome do produto: ");
					descricao = Console.readLine(
						"Informe a descricao do produto: ");
					lanceMinimo = Console.readDouble(
						"Informe o valor do lance minimo: ");
					dataCadastro = Console.readLine(
						"Informe a data de cadastramento do produto: ");
						
					umProduto = new Produto(nome, descricao, lanceMinimo, Util.strToDate(dataCadastro));
					
					long numero = produtoAppService.inclui(umProduto);
					
					System.out.println('\n' + "Produto numero " + 
					    numero + " incluido com sucesso!");	

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do produto que voce deseja alterar: ");
										
					try
					{	umProduto = produtoAppService
							.recuperaUmProduto(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Salario = " + umProduto.getDescricao());
												
					System.out.println('\n' + "O que voce deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println("2. Descricao");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um numero de 1 a 2:");
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
										readLine("Digite o novo nome: ");

							umProduto.setNome(novoNome);

							produtoAppService.altera(umProduto);

							System.out.println('\n' + 
								"Alteracao de nome efetuada com sucesso!");
								
							break;
					
						case 2:
							String novaDescricao = Console.
									readLine("Digite a nova descricao: ");

							umProduto.setDescricao(novaDescricao);

							produtoAppService.altera(umProduto);
								
							System.out.println('\n' + 
								"Alteracao de descricao efetuada " +
								"com sucesso!");						

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
					{	umProduto = produtoAppService
							.buscaUmProdutoELances(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Descricao = " + umProduto.getDescricao());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do produto?");

					if(resp.equals("s"))
					{	try
						{	produtoAppService.exclui (umProduto);
							System.out.println('\n' + 
								"Produto removido com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + 
							"Produto não removido.");
					}
					
					break;
				}

				case 4:
				{	
					long numero = Console.readInt('\n' + 
						"Informe o numero do produto: ");
				
					try
					{	umProduto = produtoAppService
							.buscaUmProdutoELances(numero);
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
					    "  Data Cadastro = " + umProduto.getDataCadastroMasc());
					
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
					Set<Produto> produtos = produtoAppService
						.recuperaProdutosELances();
						
					if (produtos.size() != 0)
					{	System.out.println("");

						for (Produto produto : produtos)
						{	System.out.println('\n' + 
								"Produto numero = " + produto.getId() + 
								"  Nome = " + produto.getNome() +
								"  Descricao = " + produto.getDescricao() +
								"  Lance minimo = " + produto.getLanceMinimoMasc() +
								"  Data Cadastro = " + produto.getDataCadastroMasc());

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

				// Designar um lance como vencedor para um produto
				case 6:
				{
					long idProduto = Console.readInt('\n' + "Informe o numero do produto: ");

					try
/* ==> */			{	Lance lanceVencedor = produtoAppService.
							atribuiLanceVencedorAProduto(idProduto);

						System.out.println('\n' + 
							"Lance vencedor - Id = " + lanceVencedor.getId() + 
						    "  Valor = " + lanceVencedor.getValor() +
						    "  Data de emissao = " + lanceVencedor.getDataCriacao());						
					}
					catch(AplicacaoException e)
					{	System.out.println ('\n' + e.getMessage());
					}
					
					break;
				}

				// Recuperar o lance vencedor para um produto
				case 7:
				{
					long idProduto = Console.readInt('\n' + "Informe o numero do produto: ");

					try
/* ==> */			{	Lance lanceVencedor = produtoAppService.
							recuperaLanceVencedorDeProduto(idProduto);
						
						System.out.println('\n' + 
							"Lance vencedor - Id = " + lanceVencedor.getId() + 
						    "  Valor = " + lanceVencedor.getValor() +
						    "  Data de emissao = " + lanceVencedor.getDataCriacao() +
						    "  Data da venda = " + lanceVencedor.getProduto().getDataVenda());						
					}
					catch(AplicacaoException e)
					{	System.out.println ('\n' + e.getMessage());
					}
					
					break;
				}

				case 8:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");
			}
		}		
	}
}
