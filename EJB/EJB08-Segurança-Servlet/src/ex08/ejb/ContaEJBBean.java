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

// Os nomes de domínios de segurança devem ser configurados  no
// arquivo   conf/login-config.xml   do   JBoss.   A   anotação
// "SecurityDomain"  abaixo  é específica do JBoss. Ela  define
// o Security  Domain  que deve ser  utilizado  para autenticar
// os usuários. Mesmo que a autenticação  já  tenha  corrido no
// servidor  web,  se  desejamos segurança neste EJB, é preciso
// definir  um  security  domain,  caso  contrário  não teremos
// verificação  de  segurança  no  EJB.  Se  especificarmos  um
// security  domain  que  não existe (conforme vem abaixo) será 
// utilizada autenticação utilizada pelo servidor web.
@SecurityDomain("")

// Já a  anotação  abaixo especifica que todos os métodos deste
// EJB  poderão  ser  executados pelos  usuários que  tiverem o
// perfil ADMIM.
@RolesAllowed("admin")

@Stateless
@RemoteBinding(jndiBinding="exercicio08.ContaEJBBean/remote")
// 1 - O container deve gerenciar a transação para o bean (o default)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaEJBBean implements ContaEJBRemote
{	// A  anotação  abaixo  permite o acesso a vários  serviços
	// prestados  pelo  container  como:  segurança, transação,
	// etc. No  método transfereFundos() abaixo,  verificaremos
	// se o usuário logado possui o perfil admin nutilizando  o
	// objeto  SessionContext  que estende EJBContext. Há ainda 
	// MessageDrivenContext que estende EJBContext.

	// 2 - O contexto EJB é injetado no bean 
	@Resource SessionContext context;
	
	@PersistenceContext(unitName="EJB") private EntityManager em;
	
	private ContaDAO contaDAO; 

	// A  anotação  @PostConstruct  define  um  método que será
	// executado uma única vez logo após o objeto  LanceEJBBean
	// ser  instanciado.  Este  método  é  executado  após   as
	// injeções de dependências serem realizadas.
	@PostConstruct 
	public void init() 
	{	contaDAO = new ContaDAO(em);
	}

	@SuppressWarnings("unchecked")

// ************************************************************
	// 3 - Uma transação é requerida para este método
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
		{	throw new AplicacaoException("Conta número " + numero + 
				                         " não encontrada.");
		}
	}

	public Conta recuperaUmaContaComLock(long numero) 
		throws AplicacaoException
	{			
		Conta umaConta = (Conta)em
			.find(Conta.class, new Long(numero));

		if (umaConta == null)
		{	throw new AplicacaoException("Conta número " + numero + 
				                         " não encontrada.");
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
		System.out.println("Usuário: " + 
			context.getCallerPrincipal().getName());

		if(context.isCallerInRole("admin"))
		{	System.out.println("O usuário " + 
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