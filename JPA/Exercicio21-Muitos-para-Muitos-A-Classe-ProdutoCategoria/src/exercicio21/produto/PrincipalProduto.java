package exercicio21.produto;

import static java.lang.System.out;

import java.util.List;
import java.util.Set;

import corejava.Console;
import exercicio21.categoria.Categoria;
import exercicio21.controleTransacao.FabricaDeAppService;
import exercicio21.lance.Lance;
import exercicio21.prodcat.ProdutoCategoria;
import exercicio21.util.AplicacaoException;
import exercicio21.util.Util;

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
		{	out.println('\n' + "O que voce deseja fazer?");
/* ==> */	out.println('\n' + "1. Cadastrar um produto informando sua categoria");
			out.println("2. Alterar um produto");
/* ==> */	out.println("3. Remover um produto (necessário cascade=CascadeType.REMOVE " +
		              "\n                       para remoção em cascata)");
			out.println("4. Listar um produto e seus lances");
			out.println("5. Listar todos os produtos e seus lances");
			out.println("");
			out.println("6. Designar um lance como vencedor para um produto");
			out.println("7. Recuperar o lance vencedor para um produto");
			out.println("");
/* ==> */	out.println("8. Informar nova categoria de um produto");
/* ==> */	out.println("9. Remover uma categoria de um produto");
/* ==> */	out.println("10. Recuperar as categorias de um produto");
/* ==> */	out.println("11. Recuperar todos os produtos e categorias");
			out.println("");
			out.println("12. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 12:");			
					
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
						
					long idCategoria = Console.
					readInt('\n' + "Informe a categoria do produto: ");
			
					umProduto = new Produto(nome,
					                        descricao,
					                        lanceMinimo,
					                        Util.strToDate(dataCadastro));
						
					try
/* ==> */			{	long numero = produtoAppService
							.inclui(umProduto, idCategoria);
		
						out.println('\n' + "Produto numero " + 
							numero + " incluido com sucesso!");
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
					}

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
							.recuperaUmProdutoELances(resposta);
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
						.recuperaConjuntoDeProdutosELances();
						
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
					{	Lance lanceVencedor = produtoAppService.
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
					{	Lance lanceVencedor = produtoAppService.
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

				// Informar nova categoria de um produto
/* ==> */		case 8: 
				{	int idProduto = Console.readInt('\n' + 
						"Digite o numero do produto ao qual voce deseja " +
						"atribuir uma categoria: ");
										
					try
					{	umProduto = produtoAppService
							.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Salario = " + umProduto.getDescricao());
																						
					long idCategoria = Console.
						readInt('\n' + "Informe o numero da categoria: ");

					try
/* ==> */			{	produtoAppService.
							atribuiCategoriaAProduto(idProduto, idCategoria);
						out.println('\n' + 
							"Categoria atribuida ao produto com sucesso!");						
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
					}

					break;
				}

				// Remover uma categoria de um produto
/* ==> */		case 9: 
				{	int idProduto = Console.readInt('\n' + 
						"Digite o numero do produto do qual voce deseja " +
						"remover uma categoria: ");
										
					try
					{	umProduto = produtoAppService
							.recuperaUmProduto(idProduto);
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
						break;
					}
										
					out.println('\n' + 
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Salario = " + umProduto.getDescricao());
																						
					long idCategoria = Console.
						readInt('\n' + "Informe o numero da categoria que " +
						        "voce deseja remover: ");

					try
					{	// A razão de ter sido criado um método que remove
						// uma Categoria de um Produto é o fato da remoção
						// ter  que  acontecer  dentro  de  uma  sessao da 
						// JPA.
						
/* ==> */				produtoAppService.
							removeCategoriaDeProduto(idProduto, idCategoria);

						out.println('\n' + 
							"Categoria removida do produto com sucesso!");						
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
					}

					break;
				}

				// Recuperar as categorias de um produto
/* ==> */		case 10: 
				{	
					long numero = Console.readInt('\n' + 
						"Informe o numero do produto: ");
				
					try
/* ==> */			{	umProduto = produtoAppService
							.recuperaUmProdutoECategorias(numero);
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
						break;
					}
										
					out.println('\n' +
						"Produto numero = " + umProduto.getId() +
					    "  Nome = " + umProduto.getNome() +
					    "  Descricao = " + umProduto.getDescricao() +
					    "  Lance minimo = " + umProduto.getLanceMinimo() +
					    "  Data Cadastro = " + umProduto.getDataCadastroMasc());
					
/*==>*/				Set<ProdutoCategoria> produtoCategorias = umProduto.getProdutoCategorias();
					
					for (ProdutoCategoria produtoCategoria : produtoCategorias)
					{	
						Categoria categoria = produtoCategoria.getCategoria();

						out.println(	
							'\n' + "      Categoria numero = "  + categoria.getId() + 
								   "  Nome = "  + categoria.getNome());
					}	
										
					break;
				}

/* ==> */		case 11: 
				{	
					List<Produto> produtos = null;
					try
/* ==> */			{	produtos = produtoAppService
							.recuperaListaDeProdutosCategorias();
					}
					catch(AplicacaoException e)
					{	out.println('\n' + e.getMessage());
						break;
					}
										
					for(Produto produto : produtos)
					{	out.println('\n' +
							"Produto numero = " + produto.getId() +
							"  Nome = " + produto.getNome() +
							"  Descricao = " + produto.getDescricao() +
							"  Lance minimo = " + produto.getLanceMinimo() +
							"  Data Cadastro = " + produto.getDataCadastroMasc());
/*==>*/				
							Set<ProdutoCategoria> produtoCategorias = produto.getProdutoCategorias();
							
							for (ProdutoCategoria produtoCategoria : produtoCategorias)
							{	
								out.println(	
									'\n' + "      Categoria numero = "  + produtoCategoria.getCategoria().getId() + 
										   "  Nome = "  + produtoCategoria.getCategoria().getNome());
							}	
					}
					
										
					break;
				}

				case 12:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");
			}
		}		
	}
}
