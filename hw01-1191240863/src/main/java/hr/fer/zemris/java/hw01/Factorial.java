package hr.fer.zemris.java.hw01;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Korisnik unosi cijele brojeve na standardni ulaz u rasponu od 1 do 20 (ukljucivo).
 * Program ispisuje faktorijele brojeva na standardni izlaz.
 * Rad programa se prekida kada se unese "kraj".
 *
 * Primjer interakcije sa programom:
 *
 * -----------------------------
 * Unesite broj > 5
 * 5! = 120
 * Unesite broj > -1
 * '-1' nije broj u dozvoljenom rasponu.
 * Unesite broj > b12
 * 'b12' nije cijeli broj.
 * Unesite broj > 20
 * 20! = 2432902008176640000
 * Unesite broj > kraj
 * Doviđenja.
 * -----------------------------
 * 
 * @author Bruno Iljazović
 *
 */
public class Factorial {

	/**
	 * Metoda koja se poziva pokretanjem programa.
	 * 
	 * @param args Argumenti iz komandne linije.
	 * 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));

		String row = null;
		
		while (true) {
			try {
				System.out.print("Unesite broj > ");
				row = reader.readLine();
				int number = Integer.parseInt(row);
				
				long result = computeFactorial(number);
				
				System.out.println(row + "! = " + result);
			} catch (NumberFormatException ex) {
				if (row.equals("kraj")) {
					System.out.println("Doviđenja.");
					break;
				}
				System.out.println("'" + row + "'" + " nije cijeli broj.");
			} catch (IllegalArgumentException ex) {
				System.out.println("'" + row + "'" + " nije broj u dozvoljenom rasponu.");
			}
		}
	}
	
	/**
	 * Računa faktorijel cijelog broja iz raspona [1,20] prema formuli !x = 1 * 2 * ... * (x - 1) * x
	 * 
	 * @param number broj čiji faktorijel treba izračunati
	 * 
	 * @return faktorijel broja number uz uvjet da je broj u dozvoljenom rasponu
	 * 
	 * @throws IllegalArgumentException ako broj number nije u dozvoljenom rasponu
	 */
	public static long computeFactorial(int number) {
		if (number < 1 || number > 20) {
			throw new IllegalArgumentException();
		}
		
		long returnValue = 1;
		for (int i = 1; i <= number; ++i) {
			returnValue *= i;
		}
		
		return returnValue;
	}

}
