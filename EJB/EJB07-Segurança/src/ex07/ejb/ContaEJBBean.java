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

// O nome  deste  domínio   de  segurança foi configurado no  arquivo 
// conf/login-config.xml do JBoss. A anotação "SecurityDomain" abaixo
// é específica do JBoss. Ela define o Security Domain que deve   ser
// utilizado para autenticar os usuários.
@SecurityDomain("security-domain-exercicio07")

// Declara os perfis de segurança utilizados por este EJB
// O JBoss está ignorando esta anotação.
@DeclareRoles("admin")

// Já  a  anotação  abaixo  especifica  que  todos  os  métodos deste   
// EJB  poderão  ser  executados  pelos usuários que tiverem o perfil 
// ADMIM.
@RolesAllowed("admin")

@Stateless
@RemoteBinding(jndiBinding="exercicio07.ContaEJBBean/remote")
// 1 - O container deve gerenciar a transação para o bean (o default)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaEJBBean implements ContaEJBRemote
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;
	
	// 2 - O contexto EJB é injetado no bean 
	//@Resource
	//private SessionContext contexto;
	
	private ContaDAO contaDAO; 

	// A anotação @PostConstruct define um método que será  executado           
	// uma única vez logo após o objeto LanceEJBBean ser instanciado.
	// Este método é executado após as injeções de dependências serem
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
				("Conta número " + numero + " não encontrada.");
		}
	}

	public Conta recuperaUmaContaComLock(long numero) 
		throws AplicacaoException
	{	try
		{	Conta umaConta = contaDAO.recuperaUmaContaComLock(numero);
			return umaConta;
		} 
		catch(ObjetoNaoEncontradoException e)
		{	throw new AplicacaoException("Conta número " + numero + " não encontrada.");
		}
	}
	
	public Set<Conta> recuperaContas()
	{	return contaDAO.recuperaContas();
	}
	
	// 3 - Uma transação é requerida para este método
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