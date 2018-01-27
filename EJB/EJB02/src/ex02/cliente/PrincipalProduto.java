package ex02.cliente;

import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex02.dominio.Lance;
import ex02.dominio.Produto;
import ex02.ejb.ProdutoEJBRemote;
import ex02.excecao.AplicacaoException;

public class PrincipalProduto
{	public static void main (String[] args) 
	{	
		String nome;
		String descricao;
		double lanceMinimo;
		String dataCadastro;
		Produto umProduto;

	    Context jndiContext;
	    ProdutoEJBRemote produtoEJBRemote = null;
		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref = jndiContext.lookup("exercicio02.ProdutoEJBBean/remote");
		    produtoEJBRemote = (ProdutoEJBRemote)ref;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voc� deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um produto");
			System.out.println("2. Alterar um produto");
			System.out.println("3. Remover um produto");
			System.out.println("4. Listar um produto e seus lances");
			System.out.println("5. Listar todos os produtos e seus lances");
			System.out.println("6. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um n�mero entre 1 e 6:");
					
			switch (opcao)
			{	case 1:
				{
					nome = Console.readLine('\n' + 
						"Informe o nome do produto: ");
					descricao = Console.readLine(
						"Informe a descri��o do produto: ");
					lanceMinimo = Console.readDouble(
						"Informe o valor do lance m�nimo: ");
					dataCadastro = Console.readLine(
						"Informe a data de cadastramento do produto: ");
						
					try
					{	long numero = produtoEJBRemote.inclui(nome, descricao, lanceMinimo, dataCadastro);
					
						System.out.println('\n' + "Produto n�mero " + 
						    numero + " inclu�do com sucesso!");	
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
						"Digite o n�mero do produtoo que voc� deseja alterar: ");
										
					try
					{	umProduto = produtoEJBRemote.recuperaUmProduto(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Sal�rio = " + umProduto.getDescricao());
												
					System.out.println('\n' + "O que voc� deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println("2. Descri��o");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um n�mero de 1 a 2:");
					
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.
										readLine("Digite o novo nome: ");
							umProduto.setNome(novoNome);
							produtoEJBRemote.altera(umProduto);

							System.out.println('\n' + 
								"Altera��o de nome efetuada com sucesso!");
								
							break;
					
						case 2:
							String novaDescricao = Console.
									readLine("Digite a nova descri��o: ");
							umProduto.setDescricao(novaDescricao);
							produtoEJBRemote.altera(umProduto);
								
							System.out.println('\n' + 
								"Altera��o de descri��o efetuada " +
								"com sucesso!");						

							break;

						default:
							System.out.println('\n' + "Op��o inv�lida!");
					}

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o n�mero do produto que voc� deseja remover: ");
									
					try
					{	umProduto = produtoEJBRemote.
										recuperaUmProduto(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umProduto.getId() + 
						"    Nome = " + umProduto.getNome() +
						"    Descri��o = " + umProduto.getDescricao());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remo��o do produto?");

					if(resp.equals("s"))
					{	try
						{	produtoEJBRemote.exclui (umProduto);
							System.out.println('\n' + 
								"Produto removido com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
						}
					}
					else
					{	System.out.println('\n' + 
							"Produto n�o removido.");
					}
					
					break;
				}

				case 4:
				{	
					long numero = Console.readInt('\n' + 
						"Informe o n�mero do produto: ");
				
					try
					{	umProduto = produtoEJBRemote.
										recuperaUmProdutoELances(numero);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
									
					System.out.println('\n' + 
						"Id = " + umProduto.getId() +
					    "  Nome = " + umProduto.getNome() +
					    "  Descri��o = " + umProduto.getDescricao() +
					    "  Lance m�nimo = " + umProduto.getLanceMinimo() +
					    "  Data Cadastro = " + umProduto.getDataCadastroMasc());
					
					Set<Lance> lances = umProduto.getLances();
					
					for (Lance lance : lances)
					{	
						System.out.println(	'\n' + 
							"      Lance n�mero = "  + lance.getId() + 
							"  Valor = "  + lance.getValor() +
							"  Data = "  + lance.getDataCriacaoMasc());
					}	
										
					break;
				}

				case 5:
				{
					List<Produto> produtos = produtoEJBRemote.recuperaProdutosELances();
						
					if (produtos.size() != 0)
					{	System.out.println("");

						for (Produto produto : produtos)
						{	
							System.out.println('\n' + 
								"Produto numero = " + produto.getId() + 
								"  Nome = " + produto.getNome() +
								"  Descri��o = " + produto.getDescricao() +
								"  Lance m�nimo = " + produto.getLanceMinimoMasc() +
								"  Data Cadastro = " + produto.getDataCadastroMasc());

							Set<Lance> lances = produto.getLances();
							
							for (Lance lance : lances)
							{	
								System.out.println(	'\n' + 
								  "      Lance n�mero = "  + lance.getId() + 
								  "  Valor = " + lance.getValor() +
								  "  Data = " + lance.getDataCriacaoMasc());
							}	
                    	} 
					}
					else
					{	System.out.println('\n' + 
						  "N�o h� produtos cadastrados com esta descri��o.");
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
