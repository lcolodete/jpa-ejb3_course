package exercicio23.dao;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import exercicio23.util.ObjetoNaoEncontradoException;

public interface ExecutorDeBuscas<T>
{
    public T busca(Method method, Object[] queryArgs) 
		throws ObjetoNaoEncontradoException;

    public List<T> buscaLista(Method method, Object[] queryArgs);
        
    public Set<T> buscaConjunto(Method method, Object[] queryArgs);
}
