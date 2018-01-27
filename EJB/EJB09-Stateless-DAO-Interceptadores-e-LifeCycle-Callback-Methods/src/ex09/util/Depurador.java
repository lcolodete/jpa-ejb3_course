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
		// m�todo  anotado   com  @AroundInvoke,   prov�  uma   s�rie  de
		// caracter�sticas que tornam o mecanismo AOP muito flex�vel. Com
		// este objeto � poss�vel, por exemplo, recuperar  os  par�metros
		// passados  para  o  m�todo  que  foi  interceptado e  at� mesmo
		// modificar  seus  valores.  E  permite  ainda  que  dados sejam
		// anexados  a  este  objeto para que  possam ser recuperados por 
		// outro interceptador.
		
//		System.out.println(">>>>>>>>>>>>>>>>>>>>>> Entrando no m�todo " +
//			invocationContext.getMethod().getName());
		logger.info(">>>>>>>>>>>>>>>>>>>>>> Entrando no m�todo " +
			invocationContext.getMethod().getName());
		
		// Neste  caso  sempre retornamos o objeto  retornado pelo m�todo
		// proceed().  Isto diz ao container que ele deve prosseguir para 
		// o  pr�ximo  interceptador  na  cadeia  de execu��o ou chamar o
		// m�todo de neg�cio interceptado. 
		
		// Esta  caracter�stica   pode  ser  bastante  �til  no  caso  de
		// seguran�a   quando  um   m�todo  s�   pode  ser  executado  se
		// determinadas  condi��es  forem  verdadeiras.  Por  exemplo, um
		// determinado m�todo n�o pode ser executado s�bados e domingos. 
		
		// �  poss�vel  ter  um  interceptador   para   um   m�todo,   um
		// interceptador para uma classe e um interceptador default que �
		// aplicado  a  todos  os m�todos de um  m�dulo EJB. Para definir
		// este �ltimo tipo de  interceptador � preciso especific�-lo  no
		// arquivo  ejb-jar.xml  (deploymente descriptor).  A  ordem   de
		// chamada  dos interceptadores  � a  seguinte, caso  todos  eles
		// sejam definidos:

		// 1. Interceptador default
		// 2. Interceptador de classe
		// 3. Interceptador de m�todo
		// � poss�vel mudar esta ordem no ejb-jar.xml
		
		return invocationContext.proceed();
	}
}