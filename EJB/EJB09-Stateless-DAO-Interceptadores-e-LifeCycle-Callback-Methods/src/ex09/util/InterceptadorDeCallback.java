package ex09.util;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.PostActivate;
import javax.ejb.PrePassivate;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

public class InterceptadorDeCallback
{	
	Logger logger = Logger.getRootLogger();
	
	// Life cycle callback methods não podem propagar exceções checked e
	// não retornam nada. Não faz sentido um método deste tipo  propagar
	// uma exceção checked uma vez que não há um cliente para receber  a 
	// exceção.
	
	// Um método do tipo callback pode ser definido na própria classe do
	// bean  ou  em  uma  classe  interceptadora. Ou  em  ambas. Métodos
	// callback definidos em classes interceptadoras são conhecidos como
	// "lifecycle callback interceptors".

	@PostConstruct 
	public void postconstruct(InvocationContext context)
	{	logger.info(">>>>>>>>>>>> Executando postConstruct()");
		try
		{	// A chamada ao método proceed() garante que o próximo método 
			// interceptador do ciclo de vida do bean será chamado.
			context.proceed();
		}
		catch(Exception e)
		{	throw new RuntimeException(e);
		}
	}

	@PrePassivate
	public void prePassivate(InvocationContext context) 
	{	logger.info(">>>>>>>>>>>> Executando prePassivate()");
		try
		{	context.proceed();
		}
		catch(Exception e)
		{	throw new RuntimeException(e);
		}
	}

	@PostActivate
	public void postActivate(InvocationContext context) 
	{	logger.info(">>>>>>>>>>>> Executando postActivate()");
		try
		{	context.proceed();
		}
		catch(Exception e)
		{	throw new RuntimeException(e);
		}
	}

	@PreDestroy
	public void preDestroy(InvocationContext context) 
	{	logger.info(">>>>>>>>>>>> Executando preDestroy()");
		try
		{	context.proceed();
		}
		catch(Exception e)
		{	throw new RuntimeException(e);
		}
	}
}