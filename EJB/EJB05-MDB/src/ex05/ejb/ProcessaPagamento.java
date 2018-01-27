package ex05.ejb;

import java.sql.Date;

import ex05.dominio.Pagamento;
   
public interface ProcessaPagamento 
{	public Pagamento porCartao(Date dataPagamento, String numCartao, double precoTotal);
}
