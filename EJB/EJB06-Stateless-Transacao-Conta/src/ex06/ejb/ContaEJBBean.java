package ex06.ejb;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.jboss.annotation.ejb.RemoteBinding;

import ex06.dao.ContaDAO;
import ex06.dominio.Conta;
import ex06.excecao.AplicacaoException;
import ex06.excecao.ObjetoNaoEncontradoException;

@Stateless
@RemoteBinding(jndiBinding="exercicio06.ContaEJBBean/remote")
// 1 - O container deve gerenciar a transação para o bean (o default)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ContaEJBBean implements ContaEJBRemote
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;
	
	// 2 - O contexto EJB é injetado no bean 
	//@Resource private SessionContext contexto;
	
	private ContaDAO contaDAO; 

	// A anotação @PostConstruct define  um método que será executado
	// uma única vez logo após o objeto LanceEJBBean ser instanciado.
	// Este método é executado após as injeções de dependências serem
	// realizadas.
	@PostConstruct 
	public void init() 
	{	contaDAO = new ContaDAO(em);
	}

	@SuppressWarnings("unchecked")

// ************************************************************
	// 3 - Uma transação é requerida para este método
	
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
		{	throw new AplicacaoException("Conta número " + numero + " não encontrada.");
		}
	}

	public Conta recuperaUmaContaComLock(long numero) 
		throws AplicacaoException
	{			
		Conta umaConta = (Conta)em
			.find(Conta.class, new Long(numero));

		if (umaConta == null)
		{	throw new AplicacaoException("Conta número " + numero + " não encontrada.");
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