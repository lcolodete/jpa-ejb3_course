package exercicio30.clienteConta;

import java.util.Date;
import java.util.List;

import corejava.Console;
import exercicio30.cartao.Cartao;
import exercicio30.cartao.CartaoAppService;
import exercicio30.cliente.Cliente;
import exercicio30.cliente.ClienteAppService;
import exercicio30.conta.Conta;
import exercicio30.conta.ContaAppService;
import exercicio30.controleTransacao.FabricaDeAppService;
import exercicio30.util.AplicacaoException;
import exercicio30.util.Util;

public class Principal
{	public static void main (String[] args) 
	{	
		ClienteAppService clienteAppService = null;
		ContaAppService contaAppService = null;
		CartaoAppService cartaoAppService = null;
		ClienteContaAppService clienteContaAppService = null;
		
		try 
		{	
			clienteAppService = (ClienteAppService) FabricaDeAppService
				.getAppService(ClienteAppService.class);

			contaAppService = (ContaAppService) FabricaDeAppService
				.getAppService(ContaAppService.class);
			
			cartaoAppService = (CartaoAppService) FabricaDeAppService
				.getAppService(CartaoAppService.class);
			
			clienteContaAppService = (ClienteContaAppService) FabricaDeAppService
				.getAppService(ClienteContaAppService.class);
		} 
		catch (Exception e) 
		{	e.printStackTrace();
			System.exit(1);
		}
		

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que você deseja fazer?");
			System.out.println('\n' + "1. Cadastrar um cliente, conta e cartao novos");
			System.out.println("2. Cadastrar um novo cartao para um cliente e uma conta");
			System.out.println("3. Cadastrar uma nova conta e cartao para um determinado cliente");
			System.out.println("4. Cadastrar um novo cliente com um novo cartao em determinada conta");
			System.out.println("5. Remover um cliente");
			System.out.println("6. Listar todos os Clientes");
			System.out.println("7. Listar todas as Contas");
			System.out.println("8. Listar todos os Cartoes");
			System.out.println("9. Listar todos os Cartões de um determinado Cliente e Conta");
			System.out.println("10. Sair");
						
			int opcao = Console.readInt('\n' + "Digite um numero entre 1 e 10:");			
					
			switch (opcao)
			{	
				case 1:
				{
					String nome  = Console
						.readLine('\n' + "Informe o nome do cliente: ");
					Cliente umCliente = new Cliente(nome);
					
					Date dataAbertura = Util.strToDate(Console
							.readLine("Informe a data de abertura da conta: "));
					Conta umaConta = new Conta(dataAbertura);
							
					Date dataEmissao = Util.strToDate(Console
							.readLine("Informe a data de emissao do cartao: "));
					Cartao umCartao = new Cartao(dataEmissao);

					clienteAppService.inclui(umCliente);
					contaAppService.inclui(umaConta);
						
					ClienteConta cc = new ClienteConta(umCliente.getId(), umaConta.getId());
					
/* ==> */			clienteContaAppService.inclui(cc);

					umCartao.setClienteConta(cc);
					
					cartaoAppService.inclui(umCartao);


					System.out.println('\n' + "Cliente " + umCliente.getId() +
					                          " Conta " + umaConta.getId() + 
					                          " Cartao " + umCartao.getId() +
					                          " cadastrados com sucesso.");
					
					break;
				}

				case 2:
				{	// Cadastrar um novo cartao para um cliente e uma conta	
				
					int idCliente = Console.readInt('\n' + 
						"Digite o numero do cliente: ");
					int idConta = Console.readInt('\n' + 
						"Digite o numero da conta: ");
										
/* ==> */			ClienteConta cc = null;

					try
					{	cc = clienteContaAppService.
							recuperaUmClienteConta(new IdClienteConta(idCliente, idConta));
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}

					Date dataEmissao = Util.strToDate(Console
							.readLine("Informe a data de emissao do cartao: "));
/* ==> */			Cartao umCartao = new Cartao(dataEmissao);

					umCartao.setClienteConta(cc);

/* ==> */			cartaoAppService.inclui(umCartao);

					System.out.println('\n' + "Cartao " + umCartao.getId() +
					                          " cadastrado com sucesso.");
								
					break;
				}
				case 3: // Cadastrar uma nova conta e cartao para um determinado cliente
				{	int idCliente = Console.readInt('\n' + 
						"Digite o numero do cliente: ");
										
					Cliente umCliente;
					
					try
/* ==> */			{	umCliente = clienteAppService.
							recuperaUmCliente(idCliente);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					Date dataAbertura = Util.strToDate(Console.readLine("Informe a data de abertura da conta: "));
/* ==> */			Conta umaConta = new Conta(dataAbertura);
							
					Date dataEmissao = Util.strToDate(Console.readLine("Informe a data de emissao do cartao: "));
/* ==> */			Cartao umCartao = new Cartao(dataEmissao);

/* ==> */			contaAppService.inclui(umaConta);
						
/* ==> */			ClienteConta cc = 
						new ClienteConta(umCliente.getId(), umaConta.getId());

/* ==> */			clienteContaAppService.inclui(cc);
					
/* ==> */			umCartao.setClienteConta(cc);
					
/* ==> */			cartaoAppService.inclui(umCartao);

					System.out.println('\n' + "Conta " + umaConta.getId() + 
					                          " e Cartao " + umCartao.getId() +
					                          " cadastrados com sucesso " +
					                          "para o Cliente " + umCliente.getId());
								
					break;
				}

				case 4: // Cadastrar um novo cliente com um novo cartao em determinada conta
				{	int idConta = Console.readInt('\n' + 
						"Digite o numero da conta: ");
										
					Conta umaConta;
					
					try
/* ==> */			{	umaConta = contaAppService.
							recuperaUmaConta(idConta);								
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					String nome  = Console.readLine('\n' + "Informe o nome do cliente: ");
/* ==> */			Cliente umCliente = new Cliente(nome);
					
					Date dataEmissao = Util.strToDate(Console.readLine("Informe a data de emissao do cartao: "));
/* ==> */			Cartao umCartao = new Cartao(dataEmissao);

/* ==> */			clienteAppService.inclui(umCliente);
						
/* ==> */			ClienteConta cc = new ClienteConta(umCliente.getId(), umaConta.getId());

/* ==> */			clienteContaAppService.inclui(cc);

/* ==> */			umCartao.setClienteConta(cc);

/* ==> */			cartaoAppService.inclui(umCartao);

					System.out.println('\n' + "Cliente " + umCliente.getId() +
					                          " e Cartao " + umCartao.getId() +
					                          " cadastrados com sucesso " +
					                          "para a Conta " + umaConta.getId());
					
					break;
				}

				case 5: // Remover um cliente
				{	int idCliente = Console.readInt('\n' + 
						"Digite o numero do cliente: ");

					Cliente umCliente;
										
					try
/* ==> */			{	umCliente = clienteAppService.
										recuperaUmCliente(idCliente);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"Número = " + umCliente.getId() + 
						"    Nome = " + umCliente.getNome());
																						
					String resp = Console.readLine('\n' + 
						"Confirma a remocao do cliente?");

					if(resp.equals("s"))
					{	try
/* ==> */				{	clienteAppService.exclui(umCliente);

							System.out.println('\n' + 
								"Cliente removido com sucesso!");
						}
						catch(AplicacaoException e)
						{	System.out.println('\n' + e.getMessage());
							break;
						}
					}
					else
					{	System.out.println('\n' + "Cliente nao removido.");
					}
					
					break;
				}

				case 6:
				{
					List<Cliente> clientes = clienteAppService.recuperaListaDeClientes();
						
					if (clientes.size() != 0)
					{	System.out.println("");

						for (Cliente cliente : clientes)
						{	System.out.println('\n' + 
								"Cliente numero = "  + cliente.getId() + 
								"  nome = "  + cliente.getNome());
                    	} 
					}
					else
					{	System.out.println('\n' + 
							"Nao ha Cientes cadastrados.");
					}

					break;
				}

				case 7:
				{
					List<Conta> contas = contaAppService.recuperaListaDeContas();
						
					if (contas.size() != 0)
					{	System.out.println("");

						for (Conta conta : contas)
						{	System.out.println('\n' + 
								"Conta numero = "  + conta.getId() + 
								"  data emissao = "  + conta.getDataAbertura());
                    	} 
					}
					else
					{	System.out.println('\n' + 
							"Nao ha Contas cadastradas.");
					}

					break;
				}

				case 8:
				{
					List<Cartao> cartoes = cartaoAppService.recuperaListaDeCartoes();
						
					if (cartoes.size() != 0)
					{	System.out.println("");

						for (Cartao cartao : cartoes)
						{	System.out.println('\n' + 
								"Cartao numero = "  + cartao.getId() + 
								"  data emissao = "  + cartao.getDataEmissao());
                    	} 
					}
					else
					{	System.out.println('\n' + 
							"Nao ha Cartoes cadastrados.");
					}

					break;
				}

				case 9:
				{
					int idCliente = Console.readInt('\n' + 
						"Digite o numero do cliente: ");
					int idConta = Console.readInt('\n' + 
						"Digite o numero da conta: ");
										
					ClienteConta cc;
					try
					{	cc = clienteContaAppService
							.recuperaUmClienteContaComCartoes(idCliente, idConta);
					} 
					catch (AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
						
					System.out.println("");

					for (Cartao cartao : cc.getCartoes())
					{	System.out.println('\n' + 
							"Cartao numero = "  + cartao.getId());
                    } 

					break;
				}

				case 10:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Opção inválida!");						
			}
		}		
	}
}
