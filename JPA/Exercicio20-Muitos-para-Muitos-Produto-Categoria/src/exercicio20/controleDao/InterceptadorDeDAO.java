package exercicio20.controleDao;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import exercicio20.dao.ExecutorDeBuscas;

public class InterceptadorDeDAO implements MethodInterceptor 
{
	/* Parametros:
	 * 
	 * objeto - "this", o objeto "enhanced", isto �, o proxy.
	 * 
	 * metodo - o m�todo interceptado. 
	 * 
	 * args - um array de args; tipos primitivos s�o empacotados.
	 * 
	 * metodoProxy - utilizado para executar um m�todo super.
	 * 
	 * MethodProxy  -  Classes  geradas pela  classe Enhancer passam 
	 * este objeto para o objeto MethodInterceptor registrado quando
	 * um m�todo  interceptado �  executado.  Ele pode ser utilizado
	 * para  invocar o  m�todo  original,  ou  chamar o mesmo m�todo
	 * sobre um objeto diferente do mesmo tipo.
	 */
	
	public Object intercept (Object objeto, 
    		                 Method metodo, 
    		                 Object[] args, 
                             MethodProxy metodoDoProxy) 
    	throws Throwable 
    {
		// O s�mbolo ? representa um tipo desconhecido.
        ExecutorDeBuscas<?> daoGenerico = (ExecutorDeBuscas<?>)objeto;

        String nomeDoMetodo = metodo.getName();
        
        if (nomeDoMetodo.startsWith("recuperaLista"))
        {	// O m�todo recuperaLista() retorna um List
        	return daoGenerico.buscaLista(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recuperaConjunto"))
        {	// O m�todo recuperaConjunto() retorna um Set
        	return daoGenerico.buscaConjunto(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recuperaUltim") || 
        		 nomeDoMetodo.startsWith("recuperaPrimeir"))
        {	// O m�todo recuperaConjunto() retorna um Set
        	return daoGenerico.buscaUltimoOuPrimeiro(metodo, args);
        }
        else if (nomeDoMetodo.startsWith("recupera"))
        {	// O m�todo recupera() retorna uma Entidade
        	return daoGenerico.busca(metodo, args);
        }
        else 
        {  	return metodoDoProxy.invokeSuper(objeto, args);
        }
    }
}
