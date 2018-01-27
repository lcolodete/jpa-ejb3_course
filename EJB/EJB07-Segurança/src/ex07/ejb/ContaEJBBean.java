package ex07.ejb;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;
import org.jboss.annotation.security.SecurityDomain;

import ex07.dao.ContaDAO;
import ex07.dominio.Conta;
import ex07.excecao.AplicacaoException;
import ex07.excecao.ObjetoNaoEncontradoException;

// O nome  deste  dom�nio   de  seguran�a foi configurado no  arquivo 
// conf/login-config.xml do JBoss. A anota��o "SecurityDomain" abaixo
// � espec�fica do JBoss. Ela define o Security Domain que deve   ser
// utilizado para autenticar os usu�rios.
@SecurityDomain("security-domain-exercicio07")

// Declara os perfis de seguran�a utilizados por este EJB
// O JBoss est� ignorando esta anota��o.
@DeclareRoles("admin")

// J�  a  anota��o  abaixo  especifica  que  todos  os  m�todos deste   
// EJB  poder�o  ser  executados  pelos usu�rios que tiverem o perfil 
// ADMIM.
@RolesAllowed("admin")

@Stateless
@RemoteBinding(jndiBinding="exercicio07.ContaEJBBean/remote")
// 1 - O container deve gerenciar a transa��o para o bean (o default)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaEJBBean implements ContaEJBRemote
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;
	
	// 2 - O contexto EJB � injetado no bean 
	//@Resource
	//private SessionContext contexto;
	
	private ContaDAO contaDAO; 

	// A anota��o @PostConstruct define um m�todo que ser�  executado           
	// uma �nica vez logo ap�s o objeto LanceEJBBean ser instanciado.
	// Este m�todo � executado ap�s as inje��es de depend�ncias serem
	// realizadas.
	@PostConstruct 
	public void init() 
	{	contaDAO = new ContaDAO(em);
	}

	@SuppressWarnings("unchecked")

// ************************************************************

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
		{	throw new AplicacaoException
				("Conta n�mero " + numero + " n�o encontrada.");
		}
	}

	public Conta recuperaUmaContaComLock(long numero) 
		throws AplicacaoException
	{	try
		{	Conta umaConta = contaDAO.recuperaUmaContaComLock(numero);
			return umaConta;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Conta n�mero " + numero + " n�o encontrada.");
		}
	}
	
	public Set<Conta> recuperaContas()
	{	return contaDAO.recuperaContas();
	}
	
	// 3 - Uma transa��o � requerida para este m�todo
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void transfereFundos(long contaCreditada,
            					long contaDebitada,
            					double valor)
		throws AplicacaoException
	{	if(contaDebitada < contaCreditada)
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