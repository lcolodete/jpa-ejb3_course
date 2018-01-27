package exercicio27.lance;

import java.util.List;

import exercicio27.dao.DaoGenerico;
import exercicio27.util.ObjetoNaoEncontradoException;

public interface LanceDAO extends DaoGenerico<Lance, IdLance>
{	Lance recuperaUmLanceComProduto(long idProduto, long numeroLance) 
    	throws ObjetoNaoEncontradoException;

    List<Lance> recuperaListaDeLances();
}
