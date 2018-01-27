package ex06.ejb;

import java.util.Set;

import javax.ejb.Remote;

import ex06.dominio.Conta;
import ex06.excecao.AplicacaoException;

@Remote
public interface ContaEJBRemote
{	
	public long inclui(double saldo);
	
	public boolean altera(Conta umaConta); 
	
	public boolean exclui(Conta umaConta);
	
	public Conta recuperaUmaConta(long numero)
		throws AplicacaoException;

	public Set<Conta> recuperaContas();
	
	public void transfereFundos(long contaCreditada,
			                    long contaDebitada,
			                    double valor)
		throws AplicacaoException;
}