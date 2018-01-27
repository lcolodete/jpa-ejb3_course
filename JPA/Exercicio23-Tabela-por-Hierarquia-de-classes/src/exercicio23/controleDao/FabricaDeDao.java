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
        // método  intercept()  da  classe   interceptadora,  isto  é,   da
        // classe InterceptadorDeDAO (classe callback).
        
        enhancer
        	.setInterfaces(new Class[] {interfaceDoDao});// Interface implementada
                                                         // ProdutoDAO, por exemplo.

        enhancer.setSuperclass(JPADaoGenerico.class);    // Superclasse do DAO  
        enhancer.setCallback(new InterceptadorDeDAO());  // Interceptador do DAO

        //                     I M P O R T A N T E
        
        // Como  a  classe  Proxy  que  será  criada estende JPADaoGenerico
        // é  preciso  que  o  proxy  possua  um  construtor  semelhante ao 
        // construtor de JPADaoGenerico(Class class). 
        
        // O  método  create()  abaixo  recebe  como  parâmetro  o tipo  do 
        // argumento   do  construtor  de   JPADaoGenerico  e  o  valor  do
        // argumento que deve ser passado para ele.
        
        // Documentação do método create() da classe Enhancer:
        // create(java.lang.Class[] argumentTypes, 
        //        java.lang.Object[] arguments) 
        
        // O  método  create()  abaixo  cria o  objeto  proxy  executando o 
        // construtor que recebe como parâmetro um objeto do tipo Class. 

        return (T) enhancer.create(new Class[] { Class.class }, 
        		                   new Object[] { tipoDoDao });
    }
}
