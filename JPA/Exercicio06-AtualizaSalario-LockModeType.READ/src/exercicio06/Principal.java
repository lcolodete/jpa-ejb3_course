package exercicio06;

import corejava.Console;
import java.util.List;

public class Principal
{	public static void main (String[] args)
	{	
		ClienteDAO clienteDAO = new ClienteDAOImpl();
	
		String nome;
		double salario;
		Cliente umCliente;
				
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um cliente");
			System.out.println("2. Alterar um cliente");

			System.out.println("3. Conceder aumento a um cliente");
			
			System.out.println("4. Remover um cliente");
			System.out.println("5. Listar todos os clientes");
			System.out.println("6. Sair");
						
			int opcao = Console.readInt('\n' + 
				"Digite um numero entre 1 e 6:");
				
			switch (opcao)
			{	case 1:
					nome = Console.readLine('\n' + 
										"Digite o nome do cliente: ");
					salario = Console.readDouble('\n' +
										"Digite o salario do cliente: ");

					umCliente = new Cliente(nome, salario);
					long numero = clienteDAO.inclui(umCliente);
					System.out.println('\n' + "Cliente numero " + 
						    numero + " incluido com sucesso!");	

					break;
				
				case 2:
				{	int resposta = Console.readInt('\n' + 
					  "Digite o numero do cliente que voce deseja alterar: ");
										
					umCliente = clienteDAO.recuperaUmCliente(resposta);
										
					if (umCliente != null)
					{	System.out.println('\n' + 
							"Número = " + umCliente.getNumero() + 
							"    Nome = " + umCliente.getNome() +
							"    Salario = " + umCliente.getSalario());
				
						System.out.println('\n' + 
							"O que voce deseja alterar?");
						System.out.println('\n' + "1. Nome");
						System.out.println("2. Salario");

						int opcaoAlteracao = Console.readInt('\n' + 
										"Digite um numero de 1 a 2:");
					
						switch (opcaoAlteracao)
						{	case 1:
								String novoNome = Console.
									readLine('\n' +"Digite o novo nome: ");

								umCliente.setNome(novoNome);

								if (clienteDAO.altera(umCliente))
								{	System.out.println('\n' + 
										"Alteracao de nome efetuada com " +
										"sucesso!");
								}
								else
								{	System.out.println('\n' + 
										"Cliente nao encontrado.");
								}
								
								break;
							case 2:
								double novoSalario = Console.
									readDouble('\n' +"Digite o novo salario: ");

								umCliente.setSalario(novoSalario);

								if (clienteDAO.altera(umCliente))
								{	System.out.println('\n' + 
									  "Alteracao de salario efetuada com " +
									  "sucesso!");						
								}
								else
								{	System.out.println('\n' + 
										"Cliente nao encontrado.");
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
						"Digite o numero do cliente: ");
										
					double percentualDeAumento = Console.readDouble('\n' +
						"Informe o percentual de aumento: ");

					if (clienteDAO.atualizaSalario(resposta, percentualDeAumento))
					{	System.out.println('\n' + 
							"Aumento de salario concedido com sucesso!");
					}
					else
					{	System.out.println('\n' + "Cliente nao encontrado!");
					}						

					break;
				}

				case 4:
				{	int resposta = Console.readInt('\n' + 
					  "Digite o numero do cliente que voce deseja remover: ");
									
					if (clienteDAO.exclui (resposta))
					{	System.out.println('\n' + 
							"Cliente removido com sucesso!");
					}
					else
					{	System.out.println('\n' + 
							"Cliente nao encontrado!");
					}
					
					break;
				}

				case 5:
				{	List<Cliente> arrayClientes = clienteDAO.recuperaClientes();
									
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
							"  Salario = " + umCliente.getSalario());
					}
										
					break;
				}

				case 6:
					continua = false;
					break;

				default:
					System.out.println('\n' + "Opcao invalida!");
			}
		}		
	}
}
