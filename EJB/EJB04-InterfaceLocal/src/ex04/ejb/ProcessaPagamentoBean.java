package ex04.ejb;

import java.sql.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ex04.dao.PagamentoDAO;
import ex04.dominio.Pagamento;

@Stateless     
public class ProcessaPagamentoBean 
	implements ProcessaPagamentoRemote, ProcessaPagamentoLocal 
{	
	@PersistenceContext(unitName="EJB") private EntityManager em;

	private PagamentoDAO pagamentoDAO; 	

	@PostConstruct 
	public void init()
	{	pagamentoDAO = new PagamentoDAO(em);
	}
	
	public Pagamento porCartao(Date dataPagamento, 
		                      String numCartao, 
		                      double precoTotal)
   {	
	   Pagamento umPagamento = new Pagamento(dataPagamento, 
		                                     numCartao, 
		                                     precoTotal);
   		pagamentoDAO.inclui(umPagamento);
   		return umPagamento;
   }
}
