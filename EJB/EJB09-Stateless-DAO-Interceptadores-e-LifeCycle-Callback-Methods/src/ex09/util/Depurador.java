package ex09.util;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.apache.log4j.Logger;

public class Depurador
{	
	Logger logger = Logger.getRootLogger();

	@AroundInvoke
	public Object efetuaLog(InvocationContext invocationContext) 
		throws Exception
	{	// O objeto InvocationContext, que deve constar como argumento do
		// método  anotado   com  @AroundInvoke,   provê  uma   série  de
		// características que tornam o mecanismo AOP muito flexível. Com
		// este objeto é possível, por exemplo, recuperar  os  parâmetros
		// passados  para  o  método  que  foi  interceptado e  até mesmo
		// modificar  seus  valores.  E  permite  ainda  que  dados sejam
		// anexados  a  este  objeto para que  possam ser recuperados por 
		// outro interceptador.
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>> Entrando no método " +
//			invocationContext.getMethod().getName());
		logger.info(">>>>>>>>>>>>>>>>>>>>>> Entrando no método " +
			invocationContext.getMethod().getName());
		
		// Neste  caso  sempre retornamos o objeto  retornado pelo método
		// proceed().  Isto diz ao container que ele deve prosseguir para 
		// o  próximo  interceptador  na  cadeia  de execução ou chamar o
		// método de negócio interceptado. 
		
		// Esta  característica   pode  ser  bastante  útil  no  caso  de
		// segurança   quando  um   método  só   pode  ser  executado  se
		// determinadas  condições  forem  verdadeiras.  Por  exemplo, um
		// determinado método não pode ser executado sábados e domingos. 
		
		// É  possível  ter  um  interceptador   para   um   método,   um
		// interceptador para uma classe e um interceptador default que é
		// aplicado  a  todos  os métodos de um  módulo EJB. Para definir
		// este último tipo de  interceptador é preciso especificá-lo  no
		// arquivo  ejb-jar.xml  (deploymente descriptor).  A  ordem   de
		// chamada  dos interceptadores  é a  seguinte, caso  todos  eles
		// sejam definidos:

		// 1. Interceptador default
		// 2. Interceptador de classe
		// 3. Interceptador de método
		// É possível mudar esta ordem no ejb-jar.xml
		
		return invocationContext.proceed();
	}
}