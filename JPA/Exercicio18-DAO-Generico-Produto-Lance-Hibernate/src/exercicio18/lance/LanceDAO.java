package exercicio18.lance;

import java.util.List;

import exercicio18.dao.DaoGenerico;
import exercicio18.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
    Lance recuperaUmLanceComProduto(long numero) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
