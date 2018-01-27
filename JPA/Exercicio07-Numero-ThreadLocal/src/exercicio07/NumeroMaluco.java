package exercicio07;

public class NumeroMaluco 
{	
	private static ThreadLocal<Double> numero = 
										new ThreadLocal<Double>();

    public static double get() 
    {	Double n = numero.get();
    	if(n == null)
    	{	n = new Double(Math.random());
    		numero.set(n);
    	}
    	return n; 
    }

	/* A classe ThreadLocal provê uma  forma  de  criar   variáveis 
	 * com escopo de Thread.  A classe ThreadLocal  provê uma opção 
	 * entre   as   variáveis   estáticas   (cujos   valores    são 
	 * compartilhados  por  todos os  objetos  de uma  classe) e as
	 * variáveis não estáticas (que possuem valores diferentes para
	 * cada objeto). 
	 *
	 * Ao se  declarar um campo  estático para  armazenar um objeto
	 * ThreadLocal,  este  objeto  ThreadLocal  armazena  um  valor 
	 * diferente para cada thread.
	 * 
	 * O método  set() designa o  valor  que será  armazenado  pela 
	 * objeto ThreadLocal para a thread que estiver em execução  no 
	 * momento.
	 *
	 * O  método  get() recupera o  valor para a  thread  corrente, 
	 * armazenado no objeto ThreadLocal.
	 *
	 * Ao  se  chamar  o  método  get()  pela  primeira vez sem ter 
	 * chamado o  método set()  para  designar  um  valor, o método 
	 * get() irá  chamar o  método  protected  initialValue()  para 
	 * obter   o   valor  inicial.    A  implementação  default  de 
	 * initialValue() retorna null.
	 */
}
