package exercicio23.pagamento;

import java.util.List;

import exercicio23.dao.DaoGenerico;
import exercicio23.util.ObjetoNaoEncontradoException;

public interface PagamentoDAO extends DaoGenerico<Pagamento, Long>
{   Cartao recuperaUmPagamentoEmCartao(long numero) 
		throws ObjetoNaoEncontradoException;
	
	List<Pagamento> recuperaListaDePagamentos();
}
