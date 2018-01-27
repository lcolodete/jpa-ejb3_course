package exercicio26.lance;

import java.util.List;

import exercicio26.dao.DaoGenerico;
import exercicio26.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
    Lance recuperaUmLanceComProduto(long numero) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
