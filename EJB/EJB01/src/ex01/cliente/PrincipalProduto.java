package ex01.cliente;

import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex01.dominio.Lance;
import ex01.dominio.Produto;
import ex01.ejb.ProdutoEJBRemote;
import ex01.excecao.AplicacaoException;
import ex01.util.Util;

public class PrincipalProduto
{	public static void main (String[] args) 
	{	
	    Context jndiContext;
	    ProdutoEJBRemote produtoDAO = null;
		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref = jndiContext.lookup("exercicio01.ProdutoEJBBean/remote");
		    produtoDAO = (ProdutoEJBRemote)ref;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}
	
		String nome;
		String descricao;
		double lanceMinimo;
		String dataCadastro;
		Produto umProduto;

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
					lanceMinimo = Console.readDouble(
						"Informe o valor do lance minimo: ");
					dataCadastro = Console.readLine(
						"Informe a data de cadastramento do produto: ");
						
					umProduto = new Produto(nome, 
					                        descricao, 
					                        lanceMinimo, 
					                        Util.strToDate(dataCadastro));
			
					long numero = produtoDAO.inclui(umProduto);

					System.out.println('\n' + "Produto numero " + 
						    numero + " incluido com sucesso!");	

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
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Salario = " + umProduto.getDescricao());
												
					System.out.println('\n' + 
									"O que voce deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println("2. Descricao");

					int opcaoAlteracao = Console.readInt('\n' + 
									"Digite um numero de 1 a 2:");	
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
									readLine("Digite o novo nome: ");

							umProduto.setNome(novoNome);

							produtoDAO.altera(umProduto);
							
							System.out.println('\n' + 
								  "Alteracao de nome efetuada com sucesso!");	
							
							break;

						case 2:
							String novaDescricao = Console.
									readLine("Digite a nova descricao: ");

							umProduto.setDescricao(novaDescricao);

							produtoDAO.altera(umProduto);
							
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
					{	umProduto = produtoDAO
							.recuperaUmProdutoELances(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					// Remover o teste abaixo e mostrar que se tentarmos
					// remover um produto com lances dará erro.
					
					if(umProduto.getLances().size() > 0)
					{	System.out.println('\n' + 
							"Este produto possui lances e nao pode ser removido.");
						break;
					}	

					System.out.println('\n' + 
						"Número = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Descricao = " + umProduto.getDescricao());
															
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do produto?");

					if(resp.equals("s"))
					{	produtoDAO.exclui (umProduto);
						
						System.out.println('\n' + 
								"Produto removido com sucesso!");
					}
					else
					{	System.out.println('\n' + 
							"Produto nao removido.");
					}
					
					break;
				}

				case 4:
				{	
					long numero = Console.readInt(
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
					    "  Data Cadastro = " + 
					    		umProduto.getDataCadastroMasc());
					
					Set<Lance> lances = umProduto.getLances();

					System.out.println('\n' + "=======>>> " + lances
																.getClass()
																.getName());
														
					// org.hibernate.collection.PersistentSet
					
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
						{	
							System.out.println('\n' + 
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
					System.out.println('\n' + "Opção inválida!");
			}
		}		
	}
}
