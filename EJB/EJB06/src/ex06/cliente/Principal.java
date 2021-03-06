package ex06.cliente;

import java.util.Set;

import javax.naming.Context;
import javax.naming.NamingException;

import corejava.Console;
import ex06.dominio.Conta;
import ex06.ejb.ContaEJBRemote;
import ex06.excecao.AplicacaoException;

public class Principal
{	public static void main (String[] args) 
	{	
		long numero;
		double saldo;
		Conta umaConta = null;

	    Context jndiContext;
	    ContaEJBRemote contaEJBRemote = null;
		try 
		{	jndiContext = new javax.naming.InitialContext();
		    Object ref = jndiContext.lookup("exercicio06.ContaEJBBean/remote");
		    contaEJBRemote = (ContaEJBRemote)ref;
		} 
		catch (NamingException ex) 
		{	ex.printStackTrace();
			System.exit(1);
		}

		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voc� deseja fazer?");
			System.out.println('\n' + "1. Cadastrar uma conta");
			System.out.println("2. Alterar uma conta");
			System.out.println("3. Remover uma conta");
			System.out.println("4. Exibir uma conta");
			System.out.println("5. Listar todas as contas");
			System.out.println("6. Efetuar uma transfer�ncia");
			System.out.println("7. Sair");
						
			int opcao = Console.readInt('\n' + 
							"Digite um n�mero entre 1 e 7:");
					
			switch (opcao)
			{	case 1:
				{
					saldo = Console.readDouble('\n' + 
						"Informe o saldo da conta: ");
						
					numero = contaEJBRemote.inclui(saldo);
					
					System.out.println('\n' + "Conta n�mero " + 
					    numero + " inclu�da com sucesso!");	

					break;
				}

				case 2:
				{	int resposta = Console.readInt('\n' + 
						"Digite o n�mero da conta que voc� deseja alterar: ");
										
					try
					{	umaConta = contaEJBRemote.recuperaUmaConta(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umaConta.getId() + 
						"    Nome = " + umaConta.getSaldo());
												
					double novoSaldo = Console.
						readDouble("Digite o novo saldo: ");

					umaConta.setSaldo(novoSaldo);
					
					contaEJBRemote.altera(umaConta);

					System.out.println('\n' + 
						"Altera��o de saldo efetuada com sucesso!");

					break;
				}

				case 3:
				{	int resposta = Console.readInt('\n' + 
						"Digite o n�mero do conta que voc� deseja remover: ");
									
					try
					{	umaConta = contaEJBRemote.
							recuperaUmaConta(resposta);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
										
					System.out.println('\n' + 
						"N�mero = " + umaConta.getId() + 
						"    Saldo = " + umaConta.getSaldo());
														
					String resp = Console.readLine('\n' + 
						"Confirma a remo��o da conta?");

					if(resp.equals("s"))
					{	contaEJBRemote.exclui (umaConta);
						System.out.println('\n' + 
							"Conta removida com sucesso!");
					}
					else
					{	System.out.println('\n' + 
							"Conta n�o removida.");
					}
					
					break;
				}

				case 4:
				{	
					numero = Console.readInt('\n' + 
						"Informe o n�mero do conta: ");
				
					try
					{	umaConta = contaEJBRemote.
							recuperaUmaConta(numero);
					}
					catch(AplicacaoException e)
					{	System.out.println('\n' + e.getMessage());
						break;
					}
									
					System.out.println('\n' + 
						"Id = " + umaConta.getId() +
					    "  Saldo = " + umaConta.getSaldo());
										
					break;
				}

				case 5:
				{
					Set<Conta> contas = contaEJBRemote.recuperaContas();
						
					if (contas.size() != 0)
					{	System.out.println("");

						for (Conta conta : contas)
						{	System.out.println('\n' + 
								"Conta numero = " + conta.getId() + 
								"  Saldo = " + conta.getSaldo());
                    	} 
					}
					else
					{	System.out.println('\n' + 
						  "N�o h� contas cadastrados.");
					}

					break;
				}

				case 6:
				{
					long contaDebitada = Console
						.readInt("Informe o n�mero da conta a ser debitada: ");
						
					long contaCreditada = Console
						.readInt("Informe o n�mero da conta a ser creditada: ");

					double valor = Console
						.readDouble("Informe o valor: ");

					try
					{	contaEJBRemote.transfereFundos(contaCreditada, contaDebitada, valor);
					}
					catch(AplicacaoException e)
					{	System.out.println(e.getMessage());
					}
					
					break;
				}

				case 7:
				{	continua = false;
					break;
				}

				default:
					System.out.println('\n' + "Op��o inv�lida!");
			}
		}		
	}
}
