package exercicio15.controleTransacao;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import exercicio15.util.InfraestruturaException;
import exercicio15.util.JPAUtil;

public class InterceptadorDeAppService implements MethodInterceptor 
{	
	/* Observe que o m�todo intercept() s� iniciar� uma transa��o se
	 * o  m�todo  original  contiver a  anota��o "@Transacional".  O
	 * m�todo  isTransacional  �  respons�vel   por  identificar  os 
	 * m�todos que s�o transacionais, isto �, os m�todos que  cont�m 
	 * a anota��o "@Transacional".
	 * 
	 * Observe tamb�m que a sess�o Hibernate � sempre fechada.
	 */
	
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
					// exce��o ocorrer, ela ser� propagada.
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
