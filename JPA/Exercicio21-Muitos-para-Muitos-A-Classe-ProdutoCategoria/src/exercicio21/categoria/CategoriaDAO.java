package exercicio21.categoria;

import java.util.List;

import exercicio21.dao.DaoGenerico;
import exercicio21.util.ObjetoNaoEncontradoException;

public interface CategoriaDAO extends DaoGenerico<Categoria, Long>
{   List<Categoria> recuperaListaDeCategoriasEProdutos();

	Categoria recuperaUmaCategoriaEProdutos(long numero)
		throws ObjetoNaoEncontradoException;
}
