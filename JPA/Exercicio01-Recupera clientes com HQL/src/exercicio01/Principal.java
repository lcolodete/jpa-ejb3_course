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
	    
	    // Nome do logger que ser� criado:  "exercicio01.Principal"

	    /*
		 * Persistence - O m�todo createEntityManagerFactory da classe Persistence
		 * l� e efetua o parse do arquivo  persistence.xml e das  classes do  tipo
		 * Entidade.   O  m�todo   createEntityManagerFactory   gera   um   objeto
		 * EntityManagerFactory. 
		 *
		 * EntityManagerFactory - o m�todo createEntityManager() constr�i o objeto 
		 * EntityManager.   Geralmente    uma    aplica��o    possui    um   �nico                
		 * EntityManagerFactory. As Threads que atendem a requisi��es de  clientes 
		 * obt�m sess�es a partir do objeto EntityManagerFactory. Objetos do  tipo 
		 * EntityManagerFactory    s�o    imut�veis.   O   comportamento   de   um               
		 * EntityManagerFactory  �  controlado  pelas  propriedades  fornecidas em 
		 * tempo de configura��o. Durante a constru��o do  EntityManagerFactory  �
		 * criado  um  pool  de  conex�es  e  aberta  uma  conex�o  para este pool
		 * utilizando os dados fornecidos no arquivo persistence.xml.  Em  seguida
		 * s�o criados os  Strings relativos  aos comandos  SQL  necess�rios  para 
		 * lidar  com as  tabelas  mencionadas  nas classes do  tipo Entity. Neste 
		 * momento n�o s�o criados os PreparedStatements,  mas apenas os  Strings. 
		 * O objeto EntityManagerFactory permite ainda a  cria��o de um objeto  do
		 * tipo EntityManager.
		 *
		 * EntityManager - Representa  um  conjunto de  objetos  persistentes  que
		 * est�o instanciados em mem�ria, e mant�m o seu estado em sincronia com o 
		 * banco de dados no final de uma transa��o. Possibilita tamb�m a  cria��o
		 * de objetos do tipo Query.
		 *
		 * Query - Possibilita a execu��o de consultas em HQL.
		 *
		 * EntityTransaction  -  Possibilita  que  o  commit ou o  rollback de uma 
		 * transa��o seja realizado  independentemente do  ambiente de execu��o da
		 * aplica��o  e  de  forma   coerente  com  a   sincroniza��o  de  objetos 
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

			// Iniciando uma Transa��o. N�o � preciso uma transa��o para 
			// executar uma query. Se a transa��o n�o existir as entidades 
			// recuperadas se tornam objetos destacados. 
			tx.begin();

			inicio = System.currentTimeMillis();
			
			// Recuperando um List de objetos do tipo Cliente

//==>
			List<Cliente> clientes = em
					.createQuery("select c from Cliente c order by c.numero asc")
					.getResultList();
		
			// Se nenhum exercicio01 for encontrado ser� retornado um List vazio.
			// Neste caso nenhuma exce��o ser� propagada.
			
			fim = System.currentTimeMillis();

			System.out.println ('\n' + "Milissegundos decorridos recuperar clientes com HQL = " + (fim - inicio));
			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");

			System.out.println('\n' + "======> " + clientes.getClass().getName());

			// Comitando a transa��o
			
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

			// Efetuando um log s� para testar se est� funcionando

			logger.error("Efetuando um log s� para testar se funciona.");

			Console.readLine('\n' + "Aperte uma tecla para prosseguir...");
		}
		catch(RuntimeException e)
		{   if (tx != null)
		    {   try
		        {	// Efetuando o rollback da transa��o
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
