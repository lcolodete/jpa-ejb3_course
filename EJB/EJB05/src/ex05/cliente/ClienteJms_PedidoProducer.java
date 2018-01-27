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

		// Recupera uma fábrica de conexões com o servidor JMS.
		ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");

		Queue pedidoQueue = (Queue) jndiContext.lookup("queue/processaPedidoQueue");
		Queue pagamentoQueue = (Queue) jndiContext.lookup("queue/processaPagamentoQueue");

		// Cria uma conexão com o servidor JMS.
		Connection connect = factory.createConnection();
		// Objetos do tipo Connection são thread safe e são projetados para serem
		// compartilhados, uma vez que a abertura de uma conexão não é uma tarefa leve.

		// Já a abertura de uma sessão é uma tarefa leve e não é thread safe.
		// Uma nova sessão deve ser aberta para se enniar uma nova mensagem.
		Session session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// O primeiro paraâmetro acima define se a sessão é transacional. Isto
		// significa que a requisição para enviar uma mensagem não será
		// realizada até que o método commit() da sessão seja executado ou até que a
		// sessão seja fechada. Quando uma sessão não é transacional, a mesnagem é
		// enviada assim que o método send() é executado.

		// Session.AUTO_ACKNOWLEDGE - indica que o consumidor (consumer) ou o
		// cliente irá passar recibo da mensagem recebida.

		MessageProducer sender = session.createProducer(pedidoQueue);
		// Para enviar uma mensagem é preciso criar um objeto MessageProducer e
		// um objeto do tipo Message que deve ser populado com os dados que
		// devem ser enviados.

		MapMessage message = session.createMapMessage();
		message.setJMSReplyTo(pagamentoQueue);
		message.setStringProperty("MessageFormat", "Version 3.4");

		long idCliente = 0;

		do {
			idCliente = Console.readInt('\n' + "Informe o número do cliente(1, 2 ou 3): ");
		} while (idCliente == 0);

		message.setLong("ClienteID", idCliente);

		String resposta = "s";
		int i = 1;
		while ("s".equalsIgnoreCase(resposta)) {
			int idProduto = Console.readInt('\n' + "Informe o número do produto (1, 2 ou 3): ");
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
			numCartao = Console.readLine('\n' + "Informe o número de seu cartão de crédito: ");
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
