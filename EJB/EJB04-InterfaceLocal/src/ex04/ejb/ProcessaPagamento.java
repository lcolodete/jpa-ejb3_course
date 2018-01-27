package ex04.ejb;

import java.sql.Date;

import ex04.dominio.Pagamento;
   
public interface ProcessaPagamento 
{	public Pagamento porCartao(Date dataPagamento, 
		                       String numCartao, 
		                       double precoTotal);
}
