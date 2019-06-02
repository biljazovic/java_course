package hr.fer.zemris.java.hw01;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Program daje korisniku pristup skupu cijelih brojeva u koje korisnik može dodavati brojeve,
 * dobiti informaciju je li broj već u skupu, te ispisati sve brojeve sortirane uzlazno i silazno.
 *
 * Skup je implementiran uređenim binarnim stablom.
 *
 * -----------------------------
 * Primjer interakcije sa korisnikom:
 * Unesite broj > 10
 * Dodano.
 * Unesite broj > 50
 * Dodano.
 * Unesite broj > 4
 * Dodano.
 * Unesite broj > 12
 * Dodano.
 * Unesite broj > 10
 * Broj već postoji. Preskačem.
 * Unesite broj > bra
 * 'bra' nije cijeli broj.
 * Unesite broj > kraj
 * Ispis od najmanjeg: 4 10 12 50
 * Ispis od najvećeg: 50 12 10 4
 * -----------------------------
 *
 * @author Bruno Iljazović
 */
public class UniqueNumbers {

	/**
	 * Pomoćna struktura koja predstavlja jedan čvor u uređenom binarnom stablu.
	 * 
	 * left -> referenca na lijevo dijete, null ako ne postoji
	 * right -> referenca na desno dijete, null ako ne postoji
	 * value -> vrijednost koja je zapisana u čvoru
	 */
	static class TreeNode {
		TreeNode left, right;
		int value;
	}

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
		TreeNode root = null;
		
		while (true) {
			try {
				System.out.print("Unesite broj > ");
				row = reader.readLine();
				int number = Integer.parseInt(row);
				
				if (containsValue(root, number)) {
					System.out.println("Broj već postoji. Preskačem.");
				}
				else {
					root = addNode(root, number);
					System.out.println("Dodano.");
				}
			} catch (NumberFormatException ex) {
				if (row.equals("kraj")) {
					int sizeOfArray = treeSize(root);
					int[] array = new int[sizeOfArray];
					
					inorderTraversal(root, array, 0);
					
					if (sizeOfArray == 0) {
						System.out.println("Stablo je prazno.");
					}
					else {
						System.out.print("Ispis od najmanjeg:");
						for (int i = 0; i < sizeOfArray; ++i) {
							System.out.print(" " + array[i]);
						}
						System.out.print("\nIspis od najvećeg:");
						for (int i = sizeOfArray - 1; i >= 0; --i) {
							System.out.print(" " + array[i]);
						}
						System.out.println();
					}
					
					break;
				}
				System.out.println("'" + row + "'" + " nije cijeli broj.");
			} catch (IllegalArgumentException ex) {
				System.out.println("'" + row + "'" + " nije broj u dozvoljenom rasponu.");
			}
		}
	}

	/**
	 * Pomoćna metoda koja rekurzivno dodaje sve vrijednosti čvorova iz stabla u polje array
	 * u slijedu od najmanjeg prema najvećem koristeći Inorder obilazak binarnog stabla.
	 *
	 * @param root Referenca na korijen uređenog binarnog stabla
	 * @param array Polje intova koje će nakon cijelog obilaska stabla biti sortirano polje svih vrijednosti u stablu
	 * @param idNumber Broj vrijednosti koji je do sada već dodan u array
	 *
	 * @return Broj vrijednosti koje su već dodane u array
	 */
	public static int inorderTraversal(TreeNode root, int[] array, int idNumber) {
		if (root == null) {
			return idNumber;
		}

		idNumber = inorderTraversal(root.left, array, idNumber);
		array[idNumber++] = root.value;
		idNumber = inorderTraversal(root.right, array, idNumber);

		return idNumber;
	}

	/**
	 * Pomoćna metoda koja prima korijen uređenog binarno stabla i vrijednost koju treba dodati.
	 * Metoda dodaje čvor sa tom vrijednošću u stablo ako vrijednost već ne postoji.
	 *
	 * @param root Referenca na korijen uređenog binarnog stabla
	 * @param value Vrijednost koju treba dodati u stablo
	 * 
	 * @return Referenca na novi korijen stabla
	 */
	public static TreeNode addNode(TreeNode root, int value) {
		if (root == null) {
			root = new TreeNode();
			root.value = value;
			return root;
		}
		
		if (value < root.value) {
			root.left = addNode(root.left, value);
		}
		else if (value > root.value) {
			root.right = addNode(root.right, value);
		}

		return root;
	}

	/**
	 * Pomoćna metoda koja prima korijen uređenog stabla i vraća njegovu veličinu.
	 *
	 * @param root Referenca na korijen uređenog binarnog stabla
	 * 
	 * @return Veličina binarnog stabla (broj čvorova)
	 */
	public static int treeSize(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int leftSize = treeSize(root.left);
		int rightSize = treeSize(root.right);

		return leftSize + rightSize + 1;
	}

	/**
	 * Pomoćna metoda koja prima korijen uređenog stabla i vrijednost za koju
	 * provjerava postoji li u stablu.
	 *
	 * @param root Referenca na korijen uređenog binarnog stabla
	 * @param value Vrijednost za koju želimo provjeriti postoji li u stablu
	 * 
	 * @return true ako value postoji, a false ako ne postoji u stablu
	 */
	public static boolean containsValue(TreeNode root, int value) {
		if (root == null) {
			return false;
		}

		if (value == root.value) {
			return true;
		}
		else if (value < root.value) {
			return containsValue(root.left, value);
		}
		else {
			return containsValue(root.right, value);
		}
	}
}
