package exercicio20.categoria;

import java.util.List;

import exercicio20.dao.DaoGenerico;
import exercicio20.util.ObjetoNaoEncontradoException;

public interface CategoriaDAO extends DaoGenerico<Categoria, Long>
{   List<Categoria> recuperaListaDeCategorias();

	Categoria recuperaUmaCategoriaEProdutos(long numero)
		throws ObjetoNaoEncontradoException;
}
