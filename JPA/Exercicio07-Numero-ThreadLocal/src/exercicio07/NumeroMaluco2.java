package exercicio07;

public class NumeroMaluco2 
{	
 	// As 4 linhas abaixo definem uma variável estática do tipo ThreadLocal
	// e efetua o override do método initialValue() da classe ThreadLocal.

	private static ThreadLocal<Double> numero = new ThreadLocal<Double>()
	{	protected synchronized Double initialValue() 
        {	return new Double(Math.random());
    	}
    };

    public static Double get() 
    {	return numero.get();
    }
}
