package exercicio05;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FabricaDeSessoes
{	private static FabricaDeSessoes fabrica = null;
	private EntityManagerFactory emf = null;
			
	private FabricaDeSessoes()
	{	emf = Persistence.createEntityManagerFactory("exercicio05");
	}

	public static EntityManager criarSessao()
	{	if (fabrica == null)
		{	fabrica = new FabricaDeSessoes();
		}	

		return fabrica.emf.createEntityManager();
	}

	public static void fecharFabricaDeSessoes()
	{	if (fabrica != null)
			if (fabrica.emf != null)
				fabrica.emf.close();
	}
}