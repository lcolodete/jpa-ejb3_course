package exercicio04;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.EntityTransaction;

import corejava.Console;

public class Principal
{	
	public static void main (String[] args)
	{	
		ClienteDAO clienteDAO = new ClienteDAOImpl();
		Cliente umCliente = null;
	
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Recuperar todos os clientes");
			System.out.println("2. Efetuar o teste 1 - dynamic update em uma unica sessao");
			System.out.println("3. Efetuar o teste 2 - dynamic update em sessoes diferentes");
			System.out.println("4. Sair");
						
			int opcao = Console.readInt('\n' + 
				"Digite um numero entre 1 e 4:");			
				
			switch (opcao)
			{	case 1:
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

				case 2:
				{	EntityManager em = null;
					EntityTransaction tx = null;
					
					try
					{	String nome = Console.readLine('\n' + "Informe o nome: ");
						double salario = 
							Console.readDouble('\n' + "Informe o salario: ");
					
						// Conceito de objeto transiente: existe em memória, 
						// mas ainda não foi persistido.
					
						umCliente = new Cliente(nome, salario);
			
						em = FabricaDeSessoes.criarSessao();
						
						System.out.println('\n' + "====> Abriu uma sessao no Entity Manager.");
						
						tx = em.getTransaction();
						tx.begin();
			
						em.persist(umCliente);
			
						String novoNome;
						double novoSalario;
						
						String resposta = "";
						
						while(!resposta.equals("s") && !resposta.equals("n"))
						{	resposta = Console.readLine('\n' + "Deseja alterar o nome? (s/n) ");
						}
						
						if (resposta.equals("s"))
						{	novoNome = Console.readLine('\n' + "Informe o novo nome: ");
							umCliente.setNome(novoNome);
						}
						
						resposta = "";
						
						while(!resposta.equals("s") && !resposta.equals("n"))
						{	resposta = Console.readLine('\n' + "Deseja alterar o salario? (s/n) ");
						}
						
						if (resposta.equals("s"))
						{	novoSalario = Console.readDouble('\n' + "Informe o novo salario: ");
							umCliente.setSalario(novoSalario);
						}
					
					    tx.commit();
					}
					catch(RuntimeException e)
					{	if (tx != null)
						{	try
							{	tx.rollback();
							}
							catch(RuntimeException he)
							{ }
						}
					    
						throw e;
					}
					finally
					{	try
						{	em.close();
							System.out.println('\n' + "====> Fechou sessao no Entity Manager.");
						}
						catch(RuntimeException he)
						{	throw he;
						}
					}

					break;
				}

				case 3:
				{	EntityManager em = null;
					EntityTransaction tx = null;
					
					try
					{	String nome = Console.readLine('\n' + "Informe o nome: ");
						double salario = Console.readDouble('\n' + "Informe o salario: ");
					
						umCliente = new Cliente(nome, salario);
			
						em = FabricaDeSessoes.criarSessao();

						System.out.println('\n' + "====> Abriu uma sessao Entity Manager.");

						tx = em.getTransaction();
						tx.begin();
			
						em.persist(umCliente);
			
					    tx.commit();
					}
					catch(RuntimeException e)
					{	if (tx != null)
						{	try
							{	tx.rollback();
							}
							catch(RuntimeException he)
							{ }
						}
						
						throw e;
					}
					finally
					{	try
						{	em.close();
							System.out.println('\n' + "====> Fechou sessao Entity Manager.");
						}
						catch(RuntimeException he)
						{	throw he;
						}
					}


					String novoNome;
					double novoSalario;
					
					String resposta = "";
					
					while(!resposta.equals("s") && !resposta.equals("n"))
					{	resposta = Console.readLine('\n' + "Deseja alterar o nome?");
					}
					
					if (resposta.equals("s"))
					{	novoNome = Console.readLine('\n' + "Informe o novo nome: ");
						umCliente.setNome(novoNome);
					}
					
					resposta = "";
					
					while(!resposta.equals("s") && !resposta.equals("n"))
					{	resposta = Console.readLine('\n' + "Deseja alterar o salario?");
					}
					
					if (resposta.equals("s"))
					{	novoSalario = Console.readDouble('\n' + "Informe o novo salario: ");
						umCliente.setSalario(novoSalario);
					}

					try
					{	em = FabricaDeSessoes.criarSessao();
						System.out.println('\n' + "====> Abriu sessao no Entity Manager.");
						
						tx = em.getTransaction();
						tx.begin();

						em.getReference(Cliente.class, umCliente.getNumero());

						em.merge(umCliente);
						// Como o objeto umCliente não se encontra no contexto de persistência, 
						// é efetuado um acesso a  banco de dados e é  verificado o que  mudou:
						// por este motivo,  mesmo em sessões  diferentes apenas o que mudou  é
						// atualizado.
						
						// O perigo desta  implementação (sem o em.getReference()) é que se  os
						// dados do objeto cliente não  tivessem sido  encontrados  no banco de
						// dados o merge teria provocado a inclusão do cliente. 
			
					    tx.commit();
					}
					catch(EntityNotFoundException e)
					{	if (tx != null)
					    {   try
					        {   tx.rollback();
					        }
					        catch(RuntimeException he)
					        { }
					    }

						System.out.println("Cliente não encontrado.");
					}
					catch(RuntimeException e)
					{	if (tx != null)
						{	try
							{	tx.rollback();
							}
							catch(RuntimeException he)
							{ }
						}
						
						throw e;
					}
					finally
					{	try
						{	em.close();
							System.out.println('\n' + "====> Fechou sessao no Entity Manager.");
						}
						catch(RuntimeException he)
						{	throw he;
						}
					}
					break;
				}

				case 4:
					continua = false;
					break;

				default:
					System.out.println('\n' + "Opcao invalida!");
			}
		}		
	}
}
