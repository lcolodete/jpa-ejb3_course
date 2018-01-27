package exercicio07;

public class EstendeThread extends Thread
{	String umString;

	public EstendeThread(String str)
	{	umString = str;
	}
		
	public void run()
	{	System.out.println(umString + 
		                   " - Numero maluco = " + 
		                   NumeroMaluco.get());
		yield();

		System.out.println(umString + 
		                   " - Numero maluco = " + 
		                   NumeroMaluco.get());
	}
}