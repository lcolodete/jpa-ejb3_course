package exercicio20.controleDao;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import exercicio20.dao.ExecutorDeBuscas;

public class InterceptadorDeDAO implements MethodInterceptor 
{
	/* Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto é, o proxy.
	 * 
	 * metodo - o método interceptado. 
	 * 
	 * args - um array de args; tipos primitivos são empacotados.
	 * 
	 * metodoProxy - utilizado para executar um método super.
	 * 
	 * MethodProxy  -  Classes  geradas pela  classe Enhancer passam 
	 * este objeto para o objeto MethodInterceptor registrado quando
	 * um método  interceptado é  executado.  Ele pode ser utilizado
	 * para  invocar o  método  original,  ou  chamar o mesmo método
	 * sobre um objeto diferente do mesmo tipo.
	 */
	
	public Object intercept (Object objeto, 
    		                 Method metodo, 
    		                 Object[] args, 
                             MethodProxy metodoDoProxy) 
    	throws Throwable 
    {
		// O símbolo ? representa um tipo desconhecido.
        ExecutorDeBuscas<?> daoGenerico = (ExecutorDeBuscas<?>)objeto;

        String nomeDoMetodo = metodo.getName();
        
        if (nomeDoMetodo.startsWith("recuperaLista"))
        {	// O método recuperaLista() retorna um List
        	return daoGenerico.buscaLista(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recuperaConjunto"))
        {	// O método recuperaConjunto() retorna um Set
        	return daoGenerico.buscaConjunto(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recuperaUltim") || 
        		 nomeDoMetodo.startsWith("recuperaPrimeir"))
        {	// O método recuperaConjunto() retorna um Set
        	return daoGenerico.buscaUltimoOuPrimeiro(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recupera"))
        {	// O método recupera() retorna uma Entidade
        	return daoGenerico.busca(metodo, args);
        }
        else 
        {  	return metodoDoProxy.invokeSuper(objeto, args);
        }
    }
}
