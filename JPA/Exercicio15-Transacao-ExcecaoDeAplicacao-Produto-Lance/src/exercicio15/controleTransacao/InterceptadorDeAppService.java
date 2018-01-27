package exercicio15.controleTransacao;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import exercicio15.util.InfraestruturaException;
import exercicio15.util.JPAUtil;

public class InterceptadorDeAppService implements MethodInterceptor 
{	
	/* Observe que o método intercept() só iniciará uma transação se
	 * o  método  original  contiver a  anotação "@Transacional".  O
	 * método  isTransacional  é  responsável   por  identificar  os 
	 * métodos que são transacionais, isto é, os métodos que  contêm 
	 * a anotação "@Transacional".
	 * 
	 * Observe também que a sessão Hibernate é sempre fechada.
	 */
	
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
			                 MethodProxy metodoProxy) 
		throws Throwable 
    {
		try 
        {
			Object resultado = null;
			
			if (isTransacional(metodo)) 
	        {	JPAUtil.beginTransaction ();
	        
	        	resultado = metodoProxy.invokeSuper(objeto, args);
	        	//resultado = metodo.invoke(objeto, args);
	        	
	        	JPAUtil.commitTransaction ();
			}
			else
			{	resultado = metodoProxy.invokeSuper(objeto, args);
			}			
			return resultado;
		}
		catch(RuntimeException e)
		{	try
			{	JPAUtil.rollbackTransaction();
			}
			catch(InfraestruturaException ie)
			{	
			}
			
		    throw e;
		}
		catch(Exception e)
		{	
			if (isTransacional(metodo))
			{	if(efetuarRollback(e))
				{	JPAUtil.rollbackTransaction();
					// rollbackTransaction() e commitTransaction()
					// propagam  InfraestruturaException.  Se esta
					// exceção ocorrer, ela será propagada.
				}
				else
				{	JPAUtil.commitTransaction();
				}
			}
			
		    throw e;
		}
		finally
		{	JPAUtil.closeEntityManager();
		}
	}
	
	public boolean isTransacional(Method metodo) 
		throws Exception
	{	return metodo.isAnnotationPresent(Transacional.class);
	}

	public boolean efetuarRollback(Exception e) 
	{	if (e instanceof RuntimeException)
		{	return true;
		}
		else
		{	if(e.getClass().isAnnotationPresent(ExcecaoDeAplicacao.class))
			{	return e.getClass().getAnnotation(ExcecaoDeAplicacao.class).rollback();
			}
			else
			{	return true;
			}
		}
	}
}
