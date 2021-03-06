package exercicio06;

import java.util.List;

public interface ClienteDAO
{	public long inclui(Cliente umCliente);
	public boolean altera(Cliente umCliente);
	public boolean exclui(long numero);
	public Cliente recuperaUmCliente(long numero);
	public List<Cliente> recuperaClientes();
	public boolean atualizaSalario(long numero, double percentualDeAumento);
}