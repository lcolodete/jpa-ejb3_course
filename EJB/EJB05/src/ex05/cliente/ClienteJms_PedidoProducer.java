package ex05.cliente;

import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import corejava.Console;

public class ClienteJms_PedidoProducer {
	public static void main(String[] args) throws Exception {
		Context jndiContext = getInitialContext();

		// Recupera uma f�brica de conex�es com o servidor JMS.
		ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

		Queue pedidoQueue = (Queue) jndiContext.lookup("queue/processaPedidoQueue");
		Queue pagamentoQueue = (Queue) jndiContext.lookup("queue/processaPagamentoQueue");

		// Cria uma conex�o com o servidor JMS.
		Connection connect = factory.createConnection();
		// Objetos do tipo Connection s�o thread safe e s�o projetados para serem
		// compartilhados, uma vez que a abertura de uma conex�o n�o � uma tarefa leve.

		// J� a abertura de uma sess�o � uma tarefa leve e n�o � thread safe.
		// Uma nova sess�o deve ser aberta para se enniar uma nova mensagem.
		Session session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// O primeiro para�metro acima define se a sess�o � transacional. Isto
		// significa que a requisi��o para enviar uma mensagem n�o ser�
		// realizada at� que o m�todo commit() da sess�o seja executado ou at� que a
		// sess�o seja fechada. Quando uma sess�o n�o � transacional, a mesnagem �
		// enviada assim que o m�todo send() � executado.

		// Session.AUTO_ACKNOWLEDGE - indica que o consumidor (consumer) ou o
		// cliente ir� passar recibo da mensagem recebida.

		MessageProducer sender = session.createProducer(pedidoQueue);
		// Para enviar uma mensagem � preciso criar um objeto MessageProducer e
		// um objeto do tipo Message que deve ser populado com os dados que
		// devem ser enviados.

		MapMessage message = session.createMapMessage();
		message.setJMSReplyTo(pagamentoQueue);
		message.setStringProperty("MessageFormat", "Version 3.4");

		long idCliente = 0;

		do {
			idCliente = Console.readInt('\n' + "Informe o n�mero do cliente(1, 2 ou 3): ");
		} while (idCliente == 0);

		message.setLong("ClienteID", idCliente);

		String resposta = "s";
		int i = 1;
		while ("s".equalsIgnoreCase(resposta)) {
			int idProduto = Console.readInt('\n' + "Informe o n�mero do produto (1, 2 ou 3): ");
			int qtd = Console.readInt('\n' + "Informe a quantidade desejada: ");

			message.setLong("ProdutoID" + i, idProduto);
			message.setInt("Qtd" + i, qtd);

			do {
				resposta = Console.readLine('\n' + "Deseja cadastrar um novo item de pedido? (s/n): ");
			} while (!("s".equalsIgnoreCase(resposta)) && !("n".equalsIgnoreCase(resposta)));

			i++;
		}

		String numCartao = "";
		do {
			numCartao = Console.readLine('\n' + "Informe o n�mero de seu cart�o de cr�dito: ");
		} while ("".equals(numCartao));
		message.setString("NumCartao", numCartao);

		System.out.println('\n' + "Enviando pedido de compra");

		sender.send(message);

		connect.close();
	}

	private static InitialContext getInitialContext() throws NamingException {
		Properties env = new Properties();
		env.put(Context.SECURITY_PRINCIPAL, "guest");
		env.put(Context.SECURITY_CREDENTIALS, "guest");
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");
		env.put(Context.PROVIDER_URL, "jnp://localhost:1099");
		return new InitialContext(env);
	}
}
