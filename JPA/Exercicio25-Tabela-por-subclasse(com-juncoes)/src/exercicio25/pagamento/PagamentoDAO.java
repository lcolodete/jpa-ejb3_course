package exercicio25.pagamento;

import java.util.List;

import exercicio25.dao.DaoGenerico;
import exercicio25.util.ObjetoNaoEncontradoException;

public interface PagamentoDAO extends DaoGenerico<Pagamento, Long>
{   Cartao recuperaUmPagamentoEmCartao(long numero) 
		throws ObjetoNaoEncontradoException;

	List<Pagamento> recuperaListaDePagamentos();
}
