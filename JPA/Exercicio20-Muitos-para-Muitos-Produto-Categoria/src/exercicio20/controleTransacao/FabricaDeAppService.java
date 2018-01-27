package exercicio20.controleTransacao;

import java.util.HashMap;

import net.sf.cglib.proxy.Enhancer;

public class FabricaDeAppService 
{	
	@SuppressWarnings("unchecked")
	private static HashMap<Class, Object> map = new HashMap<Class, Object>();
	
	@SuppressWarnings("unchecked")
	public static Object getAppService(Class classeDoBean) 
		throws Exception 
    {
		// classeDoBean = ContaAppService.class ou 
		//                LanceAppService.class, por exemplo.
		Object appService = map.get(classeDoBean);
		
		if(appService == null)
		{	InterceptadorDeAppService interceptadorDeAppService = new InterceptadorDeAppService();
			appService = Enhancer.create (classeDoBean, interceptadorDeAppService);
			map.put(classeDoBean, appService);
		}

		return appService;

		/*
			A  classe  Enhancer  gera  subclasses  din�micas para habilitar a 
			intercepta��o de m�todos.  
			  
			A subclasse  gerada dinamicamente  faz o override dos m�todos n�o 
			"finais"   da  superclasse  (ContaAppService,  por exemplo)   e
			insere  chamadas que  executam  implementa��es de interceptadores 
			definidos pelo usu�rio. 

			O original e mais geral  tipo de callback � o  MethodInterceptor. 
			MethodInterceptor   �   uma  interface   que   possui  um  m�todo 
			denominado 
                        
                 intercept(java.lang.Object obj,
                           java.lang.reflect.Method method,
                           java.lang.Object[] args,
                           MethodProxy proxy)

            durante   a   execu��o   do  m�todo   intercept()  acima  pode-se
            invocar  c�digo  customizado  antes  e  ap�s  a chamada do m�todo 
            "super".  Al�m  disso,  �  poss�vel  modificar  os  valores   dos 
            argumentos  antes  de  chamar o m�todo  "super", ou at� mesmo n�o 
            cham�-lo.
            
            Embora a  inteface  MethodInterceptor  seja gen�rica o suficiente 
            para   fazer  o   que  �  necess�rio   em  qualquer  situa��o  de 
            intercepta��o, ela  frequentemente � exagerada.  Por simplicidade
            e  desempenho,  outros  tipos de callback  especializados (como o 
            LazyLoader)  tamb�m est�o  dispon�veis.  Frequentemente  um �nico
            callback ser�  utilizado  por classe  "enhanced", mas �  poss�vel 
            controlar qual  callback  est�  sendo  utilizado  (utilizando  um 
            CallbackFilter) a cada execu��o de m�todo.  
 */    
    
    }
}
