package exercicio19.lance;

import java.util.List;

import exercicio19.dao.DaoGenerico;
import exercicio19.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, Long>
{
    Lance recuperaUmLanceComProduto(long numero) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
