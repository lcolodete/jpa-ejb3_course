package exercicio23.controleDao;

import net.sf.cglib.proxy.Enhancer;
import exercicio23.dao.JPADaoGenerico;

public class FabricaDeDao
{
    //produtoDAO = FabricaDeDao.<ProdutoDAO>getDao(ProdutoDAO.class, Produto.class);
    
	@SuppressWarnings("unchecked")  
    public static <T> T getDao(Class<T> interfaceDoDao, Class tipoDoDao) 
        throws Exception 
    {
        Enhancer enhancer = new Enhancer();

        // O proxy deve implementar uma interface (ProdutoDAO por exemplo),
        // deve   estender   a  classe   JPADaoGenerico  e  deve  chamar  o
        // m�todo  intercept()  da  classe   interceptadora,  isto  �,   da
        // classe InterceptadorDeDAO (classe callback).
        
        enhancer
        	.setInterfaces(new Class[] {interfaceDoDao});// Interface implementada
                                                         // ProdutoDAO, por exemplo.

        enhancer.setSuperclass(JPADaoGenerico.class);    // Superclasse do DAO  
        enhancer.setCallback(new InterceptadorDeDAO());  // Interceptador do DAO

        //                     I M P O R T A N T E
        
        // Como  a  classe  Proxy  que  ser�  criada estende JPADaoGenerico
        // �  preciso  que  o  proxy  possua  um  construtor  semelhante ao 
        // construtor de JPADaoGenerico(Class class). 
        
        // O  m�todo  create()  abaixo  recebe  como  par�metro  o tipo  do 
        // argumento   do  construtor  de   JPADaoGenerico  e  o  valor  do
        // argumento que deve ser passado para ele.
        
        // Documenta��o do m�todo create() da classe Enhancer:
        // create(java.lang.Class[] argumentTypes, 
        //        java.lang.Object[] arguments) 
        
        // O  m�todo  create()  abaixo  cria o  objeto  proxy  executando o 
        // construtor que recebe como par�metro um objeto do tipo Class. 

        return (T) enhancer.create(new Class[] { Class.class }, 
        		                   new Object[] { tipoDoDao });
    }
}
