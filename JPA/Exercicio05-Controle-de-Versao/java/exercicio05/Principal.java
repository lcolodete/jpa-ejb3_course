package exercicio05;

import corejava.Console;
import java.util.List;

public class Principal
{	public static void main (String[] args)
	{	
		ClienteDAOImpl clienteDAOImpl = new ClienteDAOImpl();
	
		String nome;
		double salario;
		Cliente umCliente;
				
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um cliente");
			System.out.println("2. Alterar um cliente");
			System.out.println("3. Remover um cliente");
			System.out.println("4. Listar todos os clientes");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 5:");			
				
			switch (opcao)
			{	case 1:
					nome = Console.readLine('\n' + 
										"Digite o nome do cliente: ");
					salario = Console.readDouble('\n' + 
										"Digite o salario do cliente: ");

					umCliente = new Cliente(nome, salario);

					long numero = clienteDAOImpl.inclui(umCliente);

					System.out.println('\n' + "Cliente numero " + 
						    numero + " incluido com sucesso!");						

					break;
				
				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente que voce deseja alterar: ");
										
					umCliente = clienteDAOImpl.recuperaUmCliente(resposta);								
										
					if (umCliente != null)
					{	System.out.println('\n' + 
							"Número = " + umCliente.getNumero() + 
							"  Nome = " + umCliente.getNome() +
							"  Salario = " + umCliente.getSalario() +
							"  Versao = " + umCliente.getVersao());
																						
						System.out.println('\n' + "O que voce deseja alterar?");
						System.out.println('\n' + "1. Nome");
						System.out.println("2. Salario");

						int opcaoAlteracao = Console.readInt('\n' + 
												"Digite um numero de 1 a 2:");			
					
						switch (opcaoAlteracao)
						{	case 1:
								String novoNome = Console.
											readLine('\n' + "Digite o novo nome: ");

								umCliente.setNome(novoNome);

								try
								{	clienteDAOImpl.altera(umCliente);
									System.out.println('\n' + 
										"Alteracao de nome efetuada " +
										"com sucesso!");						
								}
								catch(ObjetoNaoEncontradoException e)
								{	System.out.println('\n' + "Cliente não encontrado");
								}
								catch(EstadoDeObjetoObsoletoException e)
								{	System.out.println('\n' + "A operacao nao foi " +
								        "efetuada: os dados que voce " +
								    	"tentou salvar foram modificados " +
								    	"ou removidos por outro usuario.");
								}
								
								break;
							case 2:
								double novoSalario = Console.
										readDouble('\n' + "Digite o novo salario: ");

								umCliente.setSalario(novoSalario);

								try
								{	clienteDAOImpl.altera(umCliente);
									System.out.println('\n' + 
										"Alteracao de salario efetuada " +
										"com sucesso!");						
								}
								catch(ObjetoNaoEncontradoException e)
								{	System.out.println('\n' + "Cliente não encontrado");
								}
								catch(EstadoDeObjetoObsoletoException e)
								{	System.out.println('\n' + "A operacao nao foi " +
								        "efetuada: os dados que voce " +
								    	"tentou salvar foram modificados " +
								    	"ou removidos por outro usuario.");
								}
								
								break;

							default:
								System.out.println('\n' + "Opcao invalida!");						
								break;
						}
					}
					else
					{	System.out.println('\n' + "Cliente nao encontrado!");
					}						
					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente que voce deseja remover: ");
									
					umCliente = clienteDAOImpl.recuperaUmCliente(resposta);								
										
					if (umCliente != null)
					{	System.out.println('\n' + 
							"Número = " + umCliente.getNumero() + 
							"  Nome = " + umCliente.getNome() +
							"  Salario = " + umCliente.getSalario() +
							"  Versao = " + umCliente.getVersao());
																						
						String resp = Console.readLine('\n' + 
							"Confirma a remocao do cliente?");

						if(resp.equals("s"))
						{	try
							{	clienteDAOImpl.exclui (umCliente);
								System.out.println('\n' + 
									"Cliente removido com sucesso!");
							}
							catch(ObjetoNaoEncontradoException e)
							{	System.out.println('\n' + "Cliente não encontrado");
							}
							catch(EstadoDeObjetoObsoletoException e)
							{	System.out.println('\n' + "A operacao nao foi " +
							        "efetuada: os dados que voce " +
							    	"tentou salvar foram modificados " +
							    	"ou removidos por outro usuario.");
							}
						}
						else
						{	System.out.println('\n' + 
								"Cliente nao removido.");
						}
					}
					else
					{	System.out.println('\n' + "Cliente nao encontrado.");
					}
					
					break;
				}

				case 4:
				{	List<Cliente> arrayClientes = clienteDAOImpl.recuperaClientes();
									
					if (arrayClientes.size() == 0)
					{	System.out.println('\n' + 
							"Nao ha clientes cadastrados.");
						break;
					}
										
					System.out.println("");
					int i;
					for (i = 0; i < arrayClientes.size(); i++)
					{	umCliente = (Cliente)arrayClientes.get(i);
						System.out.println(	
						               "Numero = " + umCliente.getNumero() + 
									   "  Nome = " + umCliente.getNome() +
									   "  Salario = " + umCliente.getSalario() +
									   "  Versao = " + umCliente.getVersao());
					}
										
					break;
				}

				case 5:
					continua = false;
					break;

				default:
					System.out.println('\n' + "Opcao invalida!");						
			}
		}		
	}
}
