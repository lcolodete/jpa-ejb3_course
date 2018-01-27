package exercicio03;

import corejava.*;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class Principal
{	
	public static void main (String[] args)
	{	
		ClienteDAO clienteDAO = new ClienteDAOImpl();
	
		boolean continua = true;
		while (continua)
		{	System.out.println('\n' + "O que voce deseja fazer?");
			System.out.println('\n' + "1. Recuperar todos os clientes");
			System.out.println("2. Efetuar teste (Insere e altera nome na mesma transacao)");
			System.out.println("3. Sair");
						
			int opcao = Console.readInt('\n' + 
				"Digite um numero entre 1 e 3:");			
				
			switch (opcao)
			{	case 1:
				{	List<Cliente> arrayClientes = clienteDAO.recuperaClientes();
									
					if (arrayClientes.size() == 0)
					{	System.out.println('\n' + 
							"Nao ha clientes cadastrados.");
						break;
					}
										
					System.out.println("");

					for (int i = 0; i < arrayClientes.size(); i++)
					{	Cliente umCliente = (Cliente)arrayClientes.get(i);
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
						double salario = Console.readDouble('\n' + "Informe o salario: ");
					
						// Conceito de objeto transiente: existe em memória, mas ainda
						// não foi persistido.
					
						Cliente umCliente = new Cliente(nome, salario);
			
						em = FabricaDeSessoes.criarSessao();
						tx = em.getTransaction();
						tx.begin();
			
						// O método persist() agenda a emissão do comando SQL INSERT e
						// o objeto  umCliente se  torna  persistente.  Como o  objeto
						// umCliente passa a ser persistente após a emissão do  método
						// persist(), se  após  sua  emissão  o  objeto  umCliente for
						// alterado,  esta  alteração  será  persistida  ao  final  da
						// transação com um comando SQL UPDATE.
						
						em.persist(umCliente);
						// em.flush();
						
						System.out.println('\n' + "umCliente.getNumero() = " + 
						                   umCliente.getNumero());
						
					 	// Observe  que  a  alteração  do  nome  abaixo  faz  com  que
					 	// seja emitido um comando update após o insert.
					 	
						String outroNome = Console.readLine('\n' + "Informe outro nome: ");
						
						umCliente.setNome(outroNome);
						// em.flush();
						
					    tx.commit();

						// Como a tabela CLIENTE deste exercício possui uma constraint
					    // UNIQUE para a coluna NOME, observe que se cadastrarmos dois
					    // clientes  com  o  mesmo  nome, a  exceção só será propagada 
					    // quando  o  método  tx.commit()  for executado, pois  apenas
					    // neste momento os comandos SQL serão emitidos. 

					    // Ao ocorrer uma exceção em  decorrência  de  uma violação de 
					    // constraint  NO  MOMENTO  DO  COMMIT,  a  JPA  captura  esta
					    // exceção, efetua um Rollback, e  regera a  exceção na  forma
					    // de  um   RollbackException   (uma   exceção   derivada   de
					    // RuntimeException).
					    
					    // Por  outro  lado,  utilizarmos o em.flush() a  JPA não  irá
					    // capturar esta exceção, logo o bloco catch poderá capturá-la
					    // e efetuar um commit ou um rollback.
					}
					catch(RuntimeException e)
					{	System.out.println(">>>>>>>>>>>>> Exceção: " + e.getClass().getName());
						
						if (e.getCause() != null)
						{	System.out.println(">>>>>>>>>>>>> Causa: " + e.getCause().getClass().getName());
						}

						if(tx.isActive())
						{	System.out.println('\n' + ">>>>>>>>>>>>> Dentro do catch - tx está ativa.");
						}
						else
						{	System.out.println('\n' + ">>>>>>>>>>>>> Dentro do catch - tx nao está ativa pq já sofreu rollback.");
						}

						if (tx != null)
						{	try
							{	System.out.println(">>>>>>>>>>>>> Vai efetuar rollback");
								tx.rollback();
								System.out.println(">>>>>>>>>>>>> Efetuaou rollback");

								// Se o insert funcionar e o update  falhar em  função
								// do nome ser duplicado, não entrará aqui uma vez que
								// a transação sofrerá um rollback automático.
							}
							catch(RuntimeException he)
							{	System.out.println(">>>>>>>>>>>>> Nao conseguiu fazer rollback");
							}
						}
					} 
					finally
					{	try
						{	em.close();
						}
						catch(RuntimeException he)
						{	throw he;
						}
					}

					break;
				}

				case 3:
					FabricaDeSessoes.fecharFabricaDeSessoes();
					continua = false;
					break;

				default:
					System.out.println('\n' + "Opcao invalida!");						
			}
		}		
	}
}


