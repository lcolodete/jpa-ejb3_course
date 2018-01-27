package exercicio;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import javax.ejb.EJBAccessException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ex08.dominio.Conta;
import ex08.ejb.ContaEJBRemote;
import ex08.excecao.AplicacaoException;

public class TransfereFundos extends HttpServlet 
{	
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, 
					   HttpServletResponse res)
    	throws ServletException, IOException 
	{	
    	res.setContentType("text/plain");		
    	PrintWriter out = res.getWriter(); 

    	Context jndiContext;
	    ContaEJBRemote contaEJBRemote = null;
	    
	    try 
	    {	jndiContext = new InitialContext();
	    	Object ref = jndiContext.lookup("exercicio08.ContaEJBBean/remote");
	    	contaEJBRemote = (ContaEJBRemote)ref;
	    } 
	    catch (NamingException ex) 
	    {	ex.printStackTrace();
	    }

	    int contaDebitada;			
	    int contaCreditada;			
	    double valor;

	    try
	    {	contaDebitada = Integer.parseInt(req.getParameter("contaDebitada"));			
	    	contaCreditada = Integer.parseInt(req.getParameter("contaCreditada"));			
	    	valor = Double.parseDouble(req.getParameter("valor"));
	    }
	    catch(NumberFormatException e)
	    {	out.println("Dados informados inválidos!");
	    	return;
	    }
 			
	    Set<Conta> contas;
	    try
	    {	contas = contaEJBRemote.recuperaContas();
	    }
	    catch(EJBAccessException e)
		{	out.println("Você não tem permissão para recuperar contas.");
			return;
		}

	    for (Iterator<Conta> it = contas.iterator(); 
		           			 it.hasNext(); )
	    {	Conta conta = (Conta) it.next();
	    	out.println( 
	    		"Conta numero = " + conta.getId() + 
	    		"  Saldo = " + conta.getSaldo());
	    } 
						
	    out.println("");

	    try
	    {	contaEJBRemote.transfereFundos(contaCreditada, contaDebitada, valor);
	    	out.println("Operação efetuada com sucesso!");
	    }
	    catch(AplicacaoException e)
	    {	out.println(e.getMessage());
	    	return;
	    }
	    catch(EJBAccessException e)
		{	out.println("Você não tem permissão para transferir fundos.");
			return;
		}

	    out.println("");
	    	
	    try
	    {	contas = contaEJBRemote.recuperaContas();
	    }
	    catch(EJBAccessException e)
		{	out.println("Você não tem permissão para recuperar contas.");
			return;
		}
			
	    for (Iterator<Conta> it = contas.iterator(); it.hasNext(); )
	    {	Conta conta = (Conta) it.next();
	    	out.println(
	    			"Conta numero = " + conta.getId() + 
	    			"  Saldo = " + conta.getSaldo());
	    } 
	}
}
