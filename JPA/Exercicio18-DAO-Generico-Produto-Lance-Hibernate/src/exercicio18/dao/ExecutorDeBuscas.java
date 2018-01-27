package exercicio18.dao;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import exercicio18.util.ObjetoNaoEncontradoException;

public interface ExecutorDeBuscas<T>
{
    public T busca(Method method, Object[] queryArgs) 
		throws ObjetoNaoEncontradoException;

    public List<T> buscaLista(Method method, Object[] queryArgs);
        
    public Set<T> buscaConjunto(Method method, Object[] queryArgs);
    
    public T buscaUltimoOuPrimeiro (Method method, Object[] queryArgs) 
		throws ObjetoNaoEncontradoException;
}
