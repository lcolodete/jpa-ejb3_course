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
			A  classe  Enhancer  gera  subclasses  dinâmicas para habilitar a 
			interceptação de métodos.  
			  
			A subclasse  gerada dinamicamente  faz o override dos métodos não 
			"finais"   da  superclasse  (ContaAppService,  por exemplo)   e
			insere  chamadas que  executam  implementações de interceptadores 
			definidos pelo usuário. 

			O original e mais geral  tipo de callback é o  MethodInterceptor. 
			MethodInterceptor   é   uma  interface   que   possui  um  método 
			denominado 
                        
                 intercept(java.lang.Object obj,
                           java.lang.reflect.Method method,
                           java.lang.Object[] args,
                           MethodProxy proxy)

            durante   a   execução   do  método   intercept()  acima  pode-se
            invocar  código  customizado  antes  e  após  a chamada do método 
            "super".  Além  disso,  é  possível  modificar  os  valores   dos 
            argumentos  antes  de  chamar o método  "super", ou até mesmo não 
            chamá-lo.
            
            Embora a  inteface  MethodInterceptor  seja genérica o suficiente 
            para   fazer  o   que  é  necessário   em  qualquer  situação  de 
            interceptação, ela  frequentemente é exagerada.  Por simplicidade
            e  desempenho,  outros  tipos de callback  especializados (como o 
            LazyLoader)  também estão  disponíveis.  Frequentemente  um único
            callback será  utilizado  por classe  "enhanced", mas é  possível 
            controlar qual  callback  está  sendo  utilizado  (utilizando  um 
            CallbackFilter) a cada execução de método.  
 */    
    
    }
}
