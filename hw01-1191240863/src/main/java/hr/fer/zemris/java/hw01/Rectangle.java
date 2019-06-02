package hr.fer.zemris.java.hw01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Program preko standardnog ulaza unosi širinu i visinu pravokutnika.
 * Ispisuje površinu i opseg pravokutnika. Ako je pokrenut sa argumentima
 * onda ne pita korisnika za širinu i visinu već odmah ispisuje rezultate.
 * 
 * Primjer interakcije sa korisnikom:
 * -------------------------------------------------------------------------
 * Unesite širinu > b
 * 'b' se ne može protumačiti kao broj.
 * Unesite širinu > 12
 * Unesite visinu > -10
 * Unijeli ste negativnu vrijednost.
 * Unesite visinu > 0
 * Unijeli ste nulu.
 * Unesite visinu > 3.14
 * Pravokutnik širine 12.0 i visine 3.14 ima površinu 37.68 i opseg 30.28.
 * --------------------------------------------------------------------------
 * 
 * @author Bruno Iljazović
 *
 */
public class Rectangle {

	/**
	 * Metoda koja se poziva pokretanjem programa. 
	 * 
	 * @param args Argumenti iz komandne linije. Treba ih biti 0 ili 2.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 2) {
			try {
				double width = Double.parseDouble(args[0]);
				double height = Double.parseDouble(args[1]);
				
				computeAndPrint(width, height);
			} catch (NumberFormatException ex) {
				System.out.println("Širina i visina pravokutnika moraju biti pozitivni decimalni brojevi.");
			}
		}
		else if (args.length == 0) {
			BufferedReader reader = new BufferedReader (new InputStreamReader(System.in));

			double width = readDouble(reader, "Unesite širinu > ");
			double height = readDouble(reader, "Unesite visinu > ");

			computeAndPrint(width, height);
		}
		else {
			System.out.println("Broj argumenata iz komandne linije nije odgavarajući (mora biti 0 ili 2!)");
		}
	}
	
	/**
	 * Pomoćna metoda kojom se korisnika pita za unos odgovarajućom porukom message. Ako korisnik ne unese pozitivan decimalan
	 * broj ispisuje se odgavarajuća poruka te korisnika se pita ponovno. Kada se unese pozitivan realan broj, 
	 * metoda vraća upisani broj u tipu double.
	 * 
	 * @param reader BufferedReader kojim se čita korisnikov unos
	 * @param message Poruka koja se ispisuje prije svakog unosa korisnika
	 * 
	 * @return Broj koji je korisnik upisao
	 * 
	 * @throws IOException
	 */
	public static double readDouble(BufferedReader reader, String message) throws IOException {
		String row = null;

		while (true) {
			try {
				System.out.print(message);
				row = reader.readLine();
				double number = Double.parseDouble(row);

				if (number < 0) {
					System.out.println("Unijeli ste negativnu vrijednost.");
				}
				else if (number == 0) {
					System.out.println("Unijeli ste nulu.");
				}
				
				else {
					return number;
				}
			} catch (NumberFormatException ex) {
				System.out.println("'" + row + "'" + " se ne može protumačiti kao broj.");
			}
		}
	}

	/**
	 * Metoda koja prima dva broja tipa double opisani dolje te ispisuje površinu i opseg pravokutnika
	 * opisanog tim brojevima. Očekuje se da su parametri pozitivni brojevi. Inače, metoda baca iznimku.
	 * 
	 * @param width Širina prvavokutnika
	 * @param height Visina pravokutnika
	 * 
	 * @throws NumberFormatException Kada je barem jedan od argumenata negativan ili nula.
	 */
	public static void computeAndPrint(double width, double height) {
		if (width <= 0 || height <= 0) {
			throw new NumberFormatException();
		}
		
		double area = width * height;
		double circumference = (width + height) * 2;

		System.out.println("Pravokutnik širine " + width + " i visine " + height + " ima površinu " + area + " i opseg " + circumference + ".");
	}
	
}
