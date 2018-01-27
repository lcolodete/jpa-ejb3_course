package ex08.ejb;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import ex08.dao.ContaDAO;
import ex08.dominio.Conta;
import ex08.excecao.AplicacaoException;
import ex08.excecao.ObjetoNaoEncontradoException;

// Os nomes de dom�nios de seguran�a devem ser configurados  no
// arquivo   conf/login-config.xml   do   JBoss.   A   anota��o
// "SecurityDomain"  abaixo  � espec�fica do JBoss. Ela  define
// o Security  Domain  que deve ser  utilizado  para autenticar
// os usu�rios. Mesmo que a autentica��o  j�  tenha  corrido no
// servidor  web,  se  desejamos seguran�a neste EJB, � preciso
// definir  um  security  domain,  caso  contr�rio  n�o teremos
// verifica��o  de  seguran�a  no  EJB.  Se  especificarmos  um
// security  domain  que  n�o existe (conforme vem abaixo) ser� 
// utilizada autentica��o utilizada pelo servidor web.
@SecurityDomain("")

// J� a  anota��o  abaixo especifica que todos os m�todos deste
// EJB  poder�o  ser  executados pelos  usu�rios que  tiverem o
// perfil ADMIM.
@RolesAllowed("admin")

@Stateless
@RemoteBinding(jndiBinding="exercicio08.ContaEJBBean/remote")
// 1 - O container deve gerenciar a transa��o para o bean (o default)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaEJBBean implements ContaEJBRemote
{	// A  anota��o  abaixo  permite o acesso a v�rios  servi�os
	// prestados  pelo  container  como:  seguran�a, transa��o,
	// etc. No  m�todo transfereFundos() abaixo,  verificaremos
	// se o usu�rio logado possui o perfil admin nutilizando  o
	// objeto  SessionContext  que estende EJBContext. H� ainda 
	// MessageDrivenContext que estende EJBContext.

	// 2 - O contexto EJB � injetado no bean 
	@Resource SessionContext context;
	
	@PersistenceContext(unitName="EJB") private EntityManager em;
	
	private ContaDAO contaDAO; 

	// A  anota��o  @PostConstruct  define  um  m�todo que ser�
	// executado uma �nica vez logo ap�s o objeto  LanceEJBBean
	// ser  instanciado.  Este  m�todo  �  executado  ap�s   as
	// inje��es de depend�ncias serem realizadas.
	@PostConstruct 
	public void init() 
	{	contaDAO = new ContaDAO(em);
	}

	@SuppressWarnings("unchecked")

// ************************************************************
	// 3 - Uma transa��o � requerida para este m�todo
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public long inclui(double saldo)
	{	Conta umaConta = new Conta(saldo);
		return contaDAO.inclui(umaConta);
	}

	public boolean altera(Conta umaConta) 
	{	return contaDAO.altera(umaConta);
	} 

	public boolean exclui(Conta umaConta)
	{	return contaDAO.exclui(umaConta);
	}

	public Conta recuperaUmaConta(long numero) 
		throws AplicacaoException
	{	try
		{	Conta umaConta = contaDAO.recuperaUmaConta(numero);
			return umaConta;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Conta n�mero " + numero + 
				                         " n�o encontrada.");
		}
	}

	public Conta recuperaUmaContaComLock(long numero) 
		throws AplicacaoException
	{			
		Conta umaConta = (Conta)em
			.find(Conta.class, new Long(numero));

		if (umaConta == null)
		{	throw new AplicacaoException("Conta n�mero " + numero + 
				                         " n�o encontrada.");
		}
		else
		{	em.lock(umaConta, LockModeType.READ);
			em.refresh(umaConta);
		}

		return umaConta;
	} 
	
	public Set<Conta> recuperaContas()
	{	return contaDAO.recuperaContas();
	}
	
	public void transfereFundos(long contaCreditada,
            					long contaDebitada,
            					double valor)
		throws AplicacaoException
	{	
		System.out.println("Usu�rio: " + 
			context.getCallerPrincipal().getName());

		if(context.isCallerInRole("admin"))
		{	System.out.println("O usu�rio " + 
				context.getCallerPrincipal().getName() +
				" possui o perfil 'admin'.");
		}
	
		if(contaDebitada < contaCreditada)
		{	Conta umaConta = recuperaUmaContaComLock(contaDebitada);
			umaConta.setSaldo(umaConta.getSaldo() - valor);
			Conta outraConta = recuperaUmaContaComLock(contaCreditada);
			outraConta.setSaldo(outraConta.getSaldo() + valor);
		}
		else
		{	Conta outraConta = recuperaUmaContaComLock(contaCreditada);
			outraConta.setSaldo(outraConta.getSaldo() + valor);
			Conta umaConta = recuperaUmaContaComLock(contaDebitada);
			umaConta.setSaldo(umaConta.getSaldo() - valor);
		}
	}
}