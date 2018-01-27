package exercicio28.cliente;

import java.util.List;
import java.util.Set;

import corejava.Console;
import exercicio28.controleTransacao.FabricaDeAppService;
import exercicio28.util.AplicacaoException;

public class Principal
{	public static void main (String[] args)
	{	
		ClienteAppService clienteAppService = null;
		
		try 
		{	clienteAppService = (ClienteAppService) FabricaDeAppService
				.getAppService(ClienteAppService.class);
		} 
		catch (Exception e) 
		{	e.printStackTrace();
			System.exit(1);
		}
	
		String nome;
		double salario;
		Cliente umCliente;
				
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
/* ==> */	System.out.println('\n' + "1. Cadastrar um cliente e seus telefones");
/* ==> */	System.out.println("2. Remover um cliente");
/* ==> */	System.out.println("3. Cadastrar um novo telefone de um cliente");
/* ==> */	System.out.println("4. Remover um telefone de um cliente");
/* ==> */	System.out.println("5. Remover todos os telefones de um cliente");
			System.out.println("6. Listar todos os clientes e seus telefones");
			System.out.println("7. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 7:");
				
			switch (opcao)
			{	case 1:
					nome = Console.readLine('\n' + "Digite o nome do cliente: ");
					salario = Console.readDouble("Digite o salario do cliente: ");

					umCliente = new Cliente(nome, salario);

					String resp = Console.readLine('\n' + "Deseja cadastrar um telefone? ");

					if(resp.equals("s"))
					{	do
						{	String telefone = Console.readLine("Informe o telefone: ");

							if(!umCliente.adicionarTelefone(telefone))
							{	System.out.println("Telefone ja cadastrado");
							}
							
							resp = Console.readLine('\n' + 
								"Deseja cadastrar outro telefone? ");
						}
						while(resp.equals("s"));
					}

					long id = clienteAppService.inclui(umCliente);

					System.out.println('\n' + "Cliente numero " + 
						    id + " incluido com sucesso!");						

					break;
				
				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente: ");
										
					try
					{	umCliente = clienteAppService.recuperaUmCliente(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Numero = " + umCliente.getId() + 
						"    Nome = " + umCliente.getNome() +
						"    Salario = " + umCliente.getSalario());
																						
					resp = Console.readLine('\n' +"Confirma a remocao do cliente? (s/n): ");


					if(resp.equals("s"))
					{	
						// Remove o cliente independentemente dele possuir
						// ou nao telefones. Se existirem telefones eles
						// também serão automaticamente removidos.
						
						try 
						{clienteAppService.exclui(resposta);
							System.out.println('\n' + "Cliente excluido com sucesso");
						} 
						catch (AplicacaoException e) 
						{	System.out.println('\n' + e.getMessage());
						}
					}
								
					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente: ");
										
					try
/* ==> */			{	umCliente = clienteAppService.recuperaUmClienteETelefones(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Numero = " + umCliente.getId() + 
						"    Nome = " + umCliente.getNome() +
						"    Salario = " + umCliente.getSalario());
																						
					resp = "s";

					if(resp.equals("s"))
					{	do
						{	String telefone = Console.readLine('\n' +"Informe o numero do telefone: ");

							if( !umCliente.removerTelefone(telefone))
							{	System.out.println('\n' +"Telefone ja cadastrado");
							}
							
							resp = Console.readLine('\n' + "Deseja cadastrar outro telefone? ");
						}
						while(resp.equals("s"));
					}
					
					clienteAppService.altera(umCliente);
					
					System.out.println('\n' + "Telefone(s) cadastrado(s) com sucesso!");						
								
					break;
				}

				case 4:
				{	int resposta = Console.readInt('\n' + 
						"Informe o numero do cliente: ");
									
					try
/* ==> */			{	umCliente = clienteAppService.recuperaUmClienteETelefones(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					String telefone = Console.readLine('\n' + 
						"Informe o numero do telefone a ser removido: ");
																												
					if(!umCliente.removerTelefone(telefone))
					{	System.out.println('\n' + "Telefone inexistente!");
						break;
					}

					clienteAppService.altera(umCliente);
					
					System.out.println('\n' + "Telefone removido com sucesso!");						
								
					break;
				}

				case 5:
				{	int resposta = Console.readInt('\n' + "Informe o numero do cliente: ");
									
					try
/* ==> */			{	umCliente = clienteAppService.recuperaUmClienteETelefones(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
					
					// Remove todos os telefones de um cliente.

					umCliente.setTelefones(null);			
					clienteAppService.altera(umCliente);
					
					System.out.println('\n' + "Telefones removidos com sucesso!");						
								
					break;
				}

				case 6:
				{
					List<Cliente> clientes = clienteAppService.recuperaListaDeClientesETelefones();
						
					if (clientes.size() != 0)
					{	System.out.println("");

						for (Cliente cliente : clientes)
						{	System.out.println('\n' + 
								"Cliente numero = "  + cliente.getId() + 
								"  Nome = "  + cliente.getNome());

/* ==> */					Set<String> telefones = cliente.getTelefones();
							
							for (String telefone : telefones)
							{	System.out.println(	'\n' + "      Telefone numero = " + telefone);
							}	
                    	} 
					}
					else
					{	System.out.println('\n' + "Nao ha clientes cadastrados.");
					}

					break;
				}

				case 7:
					continua = false;
					break;

				default:
					System.out.println('\n' + "Opcao invalida!");						
			}
		}		
	}
}
