package exercicio01;

import corejava.Console;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;


import org.apache.log4j.Logger;


public class Principal
{	
	@SuppressWarnings("unchecked")
	public static void main (String[] args)
	{	
		// Criando um logger
//==>
	    Logger logger = Logger.getLogger(new Principal().getClass().getName());
	    
	    // Nome do logger que será criado:  "exercicio01.Principal"

	    /*
		 * Persistence - O método createEntityManagerFactory da classe Persistence
		 * lê e efetua o parse do arquivo  persistence.xml e das  classes do  tipo
		 * Entidade.   O  método   createEntityManagerFactory   gera   um   objeto
		 * EntityManagerFactory. 
		 *
		 * EntityManagerFactory - o método createEntityManager() constrói o objeto 
		 * EntityManager.   Geralmente    uma    aplicação    possui    um   único                
		 * EntityManagerFactory. As Threads que atendem a requisições de  clientes 
		 * obtêm sessões a partir do objeto EntityManagerFactory. Objetos do  tipo 
		 * EntityManagerFactory    são    imutáveis.   O   comportamento   de   um               
		 * EntityManagerFactory  é  controlado  pelas  propriedades  fornecidas em 
		 * tempo de configuração. Durante a construção do  EntityManagerFactory  é
		 * criado  um  pool  de  conexões  e  aberta  uma  conexão  para este pool
		 * utilizando os dados fornecidos no arquivo persistence.xml.  Em  seguida
		 * são criados os  Strings relativos  aos comandos  SQL  necessários  para 
		 * lidar  com as  tabelas  mencionadas  nas classes do  tipo Entity. Neste 
		 * momento não são criados os PreparedStatements,  mas apenas os  Strings. 
		 * O objeto EntityManagerFactory permite ainda a  criação de um objeto  do
		 * tipo EntityManager.
		 *
		 * EntityManager - Representa  um  conjunto de  objetos  persistentes  que
		 * estão instanciados em memória, e mantém o seu estado em sincronia com o 
		 * banco de dados no final de uma transação. Possibilita também a  criação
		 * de objetos do tipo Query.
		 *
		 * Query - Possibilita a execução de consultas em HQL.
		 *
		 * EntityTransaction  -  Possibilita  que  o  commit ou o  rollback de uma 
		 * transação seja realizado  independentemente do  ambiente de execução da
		 * aplicação  e  de  forma   coerente  com  a   sincronização  de  objetos 
		 * persistentes gerenciados pelo objeto EntityManager.
		 */ 
	     	
	    EntityManagerFactory emf = null;
	    EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			long inicio, fim;
		
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");
		
			inicio = System.currentTimeMillis();

//==>
			emf = Persistence.createEntityManagerFactory("exercicio01");

			fim = System.currentTimeMillis();
			
			System.out.println ('\n' + "Milissegundos decorridos para criar o EMF = " + (fim - inicio));
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");
			
			inicio = System.currentTimeMillis();

//==>
			em = emf.createEntityManager();
													
			fim = System.currentTimeMillis();
			
			System.out.println ('\n' + "Milissegundos decorridos para criar o EM = " + (fim - inicio));
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");

			inicio = System.currentTimeMillis();

//==>
			tx = em.getTransaction();
			
			fim = System.currentTimeMillis();

			System.out.println ('\n' + "Milissegundos decorridos criar um ET = " + (fim - inicio));
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");

			// Iniciando uma Transação. Não é preciso uma transação para 
			// executar uma query. Se a transação não existir as entidades 
			// recuperadas se tornam objetos destacados. 
			tx.begin();

			inicio = System.currentTimeMillis();
			
			// Recuperando um List de objetos do tipo Cliente

//==>
			List<Cliente> clientes = em
					.createQuery("select c from Cliente c order by c.numero asc")
					.getResultList();
		
			// Se nenhum exercicio01 for encontrado será retornado um List vazio.
			// Neste caso nenhuma exceção será propagada.
			
			fim = System.currentTimeMillis();

			System.out.println ('\n' + "Milissegundos decorridos recuperar clientes com HQL = " + (fim - inicio));
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");

			System.out.println('\n' + "======> " + clientes.getClass().getName());

			// Comitando a transação
			
//==>
			tx.commit();

			if (clientes.isEmpty())
			{	System.out.println('\n' + "Nao ha clientes cadastrados.");
			}
			else
			{	System.out.println("");

				// Exibindo os Clientes que foram recuperados
				
				for (Cliente c : clientes)
				{	System.out.println(	
				    	"Numero = " + c.getNumero() + 
						"  Nome = " + c.getNome() +
						"  Salario = " + c.getSalario());
				}
			} 

			// Efetuando um log só para testar se está funcionando

			logger.error("Efetuando um log só para testar se funciona.");

			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {	// Efetuando o rollback da transação
//==>
		    		tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }

		    // efetuar log com o log4j

			logger.error(e.getMessage());

		    throw e;
		}
		finally
		{   try
		    {	
//==>
				em.close();
//==>			
				emf.close();
		    }
		    catch(RuntimeException he)
		    {	
		     	// efetuar log com o log4j

				logger.error("Ocorreu um erro ao fechar a Session ou o SessionFactory");

				throw he;
			}
		}
	}
}
