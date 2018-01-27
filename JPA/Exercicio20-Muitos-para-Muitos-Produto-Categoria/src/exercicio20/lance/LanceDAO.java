package exercicio20.lance;

import java.util.List;

import exercicio20.dao.DaoGenerico;
import exercicio20.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
    Lance recuperaUmLanceComProduto(long numero) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
