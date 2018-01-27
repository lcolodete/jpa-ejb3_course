package exercicio07;

public class Principal
{	public static void main (String[] args)
	{		
		// Obtendo uma thread
				
		EstendeThread umaThread = new EstendeThread("Thread numero 1");
		umaThread.start();
				
		// Obtendo outra thread
					
		EstendeThread outraThread = new EstendeThread("Thread numero 2");
		outraThread.start();
				
		System.out.println("Fim da execução do programa principal");
	}
}