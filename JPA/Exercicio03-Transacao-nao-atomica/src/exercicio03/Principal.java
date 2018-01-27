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
					
						// Conceito de objeto transiente: existe em mem�ria, mas ainda
						// n�o foi persistido.
					
						Cliente umCliente = new Cliente(nome, salario);
			
						em = FabricaDeSessoes.criarSessao();
						tx = em.getTransaction();
						tx.begin();
			
						// O m�todo persist() agenda a emiss�o do comando SQL INSERT e
						// o objeto  umCliente se  torna  persistente.  Como o  objeto
						// umCliente passa a ser persistente ap�s a emiss�o do  m�todo
						// persist(), se  ap�s  sua  emiss�o  o  objeto  umCliente for
						// alterado,  esta  altera��o  ser�  persistida  ao  final  da
						// transa��o com um comando SQL UPDATE.
						
						em.persist(umCliente);
						// em.flush();
						
						System.out.println('\n' + "umCliente.getNumero() = " + 
						                   umCliente.getNumero());
						
					 	// Observe  que  a  altera��o  do  nome  abaixo  faz  com  que
					 	// seja emitido um comando update ap�s o insert.
					 	
						String outroNome = Console.readLine('\n' + "Informe outro nome: ");
						
						umCliente.setNome(outroNome);
						// em.flush();
						
					    tx.commit();

						// Como a tabela CLIENTE deste exerc�cio possui uma constraint
					    // UNIQUE para a coluna NOME, observe que se cadastrarmos dois
					    // clientes  com  o  mesmo  nome, a  exce��o s� ser� propagada 
					    // quando  o  m�todo  tx.commit()  for executado, pois  apenas
					    // neste momento os comandos SQL ser�o emitidos. 

					    // Ao ocorrer uma exce��o em  decorr�ncia  de  uma viola��o de 
					    // constraint  NO  MOMENTO  DO  COMMIT,  a  JPA  captura  esta
					    // exce��o, efetua um Rollback, e  regera a  exce��o na  forma
					    // de  um   RollbackException   (uma   exce��o   derivada   de
					    // RuntimeException).
					    
					    // Por  outro  lado,  utilizarmos o em.flush() a  JPA n�o  ir�
					    // capturar esta exce��o, logo o bloco catch poder� captur�-la
					    // e efetuar um commit ou um rollback.
					}
					catch(RuntimeException e)
					{	System.out.println(">>>>>>>>>>>>> Exce��o: " + e.getClass().getName());
						
						if (e.getCause() != null)
						{	System.out.println(">>>>>>>>>>>>> Causa: " + e.getCause().getClass().getName());
						}

						if(tx.isActive())
						{	System.out.println('\n' + ">>>>>>>>>>>>> Dentro do catch - tx est� ativa.");
						}
						else
						{	System.out.println('\n' + ">>>>>>>>>>>>> Dentro do catch - tx nao est� ativa pq j� sofreu rollback.");
						}

						if (tx != null)
						{	try
							{	System.out.println(">>>>>>>>>>>>> Vai efetuar rollback");
								tx.rollback();
								System.out.println(">>>>>>>>>>>>> Efetuaou rollback");

								// Se o insert funcionar e o update  falhar em  fun��o
								// do nome ser duplicado, n�o entrar� aqui uma vez que
								// a transa��o sofrer� um rollback autom�tico.
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


