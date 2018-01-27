package exercicio24.pagamento;

import java.util.List;

import exercicio24.dao.DaoGenerico;
import exercicio24.util.ObjetoNaoEncontradoException;

public interface PagamentoDAO extends DaoGenerico<Pagamento, Long>
{   Cartao recuperaUmPagamentoEmCartao(long numero) 
		throws ObjetoNaoEncontradoException;
	
	List<Pagamento> recuperaListaDePagamentos();
}
