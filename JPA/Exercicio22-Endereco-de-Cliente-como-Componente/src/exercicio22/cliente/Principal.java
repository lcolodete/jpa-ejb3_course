package exercicio22.cliente;

import java.util.List;

import corejava.Console;
import exercicio22.controleTransacao.FabricaDeAppService;
import exercicio22.util.AplicacaoException;

public class Principal
{	public static void main (String[] args)
	{	
		Cliente umCliente;
	
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
				
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
/* ==> */	System.out.println('\n' + "1. Cadastrar um cliente");
/* ==> */	System.out.println("2. Alterar um cliente");
			System.out.println("3. Remover um cliente");
/* ==> */	System.out.println("4. Listar todos os clientes");
			System.out.println("5. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 5:");			
				
			switch (opcao)
			{	case 1:
					nome = Console.readLine('\n' + "Digite o nome do cliente: ");
					salario = Console.readDouble("Digite o salario do cliente: ");

					System.out.println('\n' + "Informe o endereço residencial" + '\n');

/* ==> */			String rua = Console.readLine("Informe a rua: ");
					String numero = Console.readLine("Informe o numero: ");
					String complemento = Console.readLine("Informe o complemento: ");

					// Cria o objeto enderecoRes
					Endereco enderecoRes = new Endereco(rua, numero, complemento);

					System.out.println('\n' + "Informe o endereço comercial" + '\n');

/* ==> */			rua = Console.readLine("Informe a rua: "); // Retorna String vazio com enter.
															   // Isto é, não retorna null.
					numero = Console.readLine("Informe o numero: ");
					complemento = Console.readLine("Informe o complemento: ");

/* ==> */			rua = "".equals(rua) ? null : rua;		   // Para atribuir null
					numero = "".equals(numero) ? null : numero;		
					complemento = "".equals(complemento) ? null : complemento;	
					
					// Cria o objeto enderecoCom
					Endereco enderecoCom = new Endereco(rua, numero, complemento);
					
					// Cria o objeto cliente
					umCliente = new Cliente(nome, salario, enderecoRes, enderecoCom);

					long id = clienteAppService.inclui(umCliente);

					System.out.println('\n' + "Cliente numero " + 
						    id + " incluido com sucesso!");						

					break;
				
				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente que voce deseja alterar: ");
										
					try
					{	umCliente = clienteAppService
							.recuperaUmCliente(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					Principal.exibeCliente(umCliente);										
																						
					System.out.println('\n' + "O que voce deseja alterar?");
					System.out.println('\n' + "1. Nome");
					System.out.println("2. Salario");
/* ==> */			System.out.println("3. Rua (res)");

					int opcaoAlteracao = Console.readInt('\n' + 
											"Digite um numero de 1 a 3:");			
				
					switch (opcaoAlteracao)
					{	case 1:
							String novoNome = Console.readLine("Digite o novo nome: ");
							umCliente.setNome(novoNome);
							clienteAppService.altera(umCliente);
							System.out.println('\n' + "Alteracao de nome efetuada com sucesso!");						
							
							break;

						case 2:
							double novoSalario = Console.readDouble("Digite o novo salario: ");
							umCliente.setSalario(novoSalario);
							clienteAppService.altera(umCliente);
							System.out.println('\n' + "Alteracao de salario efetuada com sucesso!");						
							
							break;

						case 3:
/* ==> */					String novaRua = Console.readLine("Digite a nova rua do endereco residencial: ");
							Endereco endRes = umCliente.getEnderecoResidencial();
							if (endRes == null)
							{	endRes = new Endereco();
								// Atribui o endereco residencial ao cliente
                                umCliente.setEnderecoResidencial(endRes);								
							}

							// Altera a rua
							endRes.setRua(novaRua);
							clienteAppService.altera(umCliente);
							System.out.println('\n' + 
								"Alteracao de rua (res) efetuada com sucesso!");						
							
							break;
		
						default:
							System.out.println('\n' + "Opcao invalida!");						
							break;
					}

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o numero do cliente que voce deseja remover: ");
									
					try
					{	umCliente = clienteAppService.recuperaUmCliente(resposta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}

					Principal.exibeCliente(umCliente);										
																						
					String resp = Console.readLine('\n' + 
							"Confirma a remocao do cliente?");

					if(resp.equals("s"))
					{	try 
						{	clienteAppService.exclui (resposta);
							System.out.println('\n' + 
								"Cliente removido com sucesso!");
						}
						catch (AplicacaoException e) 
						{	System.out.println('\n' + 
								"Cliente não encontrado.");
						}
					}
					else
					{	System.out.println('\n' + 
							"Cliente não removido.");
					}
					
					break;
				}

				case 4:
				{	List<Cliente> arrayClientes = clienteAppService.recuperaListaDeClientes();
									
					if (arrayClientes.size() == 0)
					{	System.out.println('\n' + "Nao ha clientes cadastrados.");
						break;
					}
										
					System.out.println("");
					for (Cliente cliente : arrayClientes)
					{	exibeCliente(cliente);										
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

	private static void exibeCliente(Cliente umCliente)
	{
		String ruaRes;
		String numeroRes;
		String complementoRes;
		
		String ruaCom;
		String numeroCom;
		String complementoCom;
					
		Endereco endRes = umCliente.getEnderecoResidencial();
		
		if (endRes == null)
		{	ruaRes = "";
			numeroRes = "";
			complementoRes = "";
		}
		else
		{	ruaRes = endRes.getRua() == null ? "" : endRes.getRua();
			numeroRes = endRes.getNumero() == null ? "" : endRes.getNumero();
			complementoRes = endRes.getComplemento() == null ? "" : endRes.getComplemento();
		}	
		
		Endereco endCom = umCliente.getEnderecoComercial();
		
		if (endCom == null) 		// Se no banco de dados a rua, o numero e o 
		{	ruaCom = "";			// complemento do endereco comercial forem 
			numeroCom = ""; 		// nulos, ao ser recuperado o objeto cliente, 
			complementoCom = "";	// o método getEnderecoComercial() retornará
		}							// null.
		else
		{	ruaCom = endCom.getRua() == null ? "" : endCom.getRua();
			numeroCom = endCom.getNumero() == null ? "" : endCom.getNumero();
			complementoCom = endCom.getComplemento() == null ? "" : endCom.getComplemento();
		}
		
		System.out.println('\n' +
		  "Numero = " + umCliente.getNumero() + '\n' +
		  "  Nome = " + umCliente.getNome() + '\n' +
		  "  Salario = " + umCliente.getSalario() + '\n' +
		  "  Rua (res) = "  + ruaRes + '\n' +
		  "    numero = "  + numeroRes + '\n' +
		  "    complemento = " + complementoRes + '\n' +
		  "  Rua (com) = " + ruaCom + '\n' +
		  "    numero = " + numeroCom + '\n' +
		  "    complemento = " + complementoCom);
	}
}
