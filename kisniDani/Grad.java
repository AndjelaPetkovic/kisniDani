package kisniDani;

import java.util.ArrayList;
import java.util.Objects;

public class Grad implements Comparable<Grad>{

	private String ime, drzava;
	private int postBr, ukupnoTrajanjeKise;
	
	// osnovni konstruktor
	public Grad(String ime, String drzava, int postCode) {
		this.ime = ime;
		this.drzava = drzava;
		this.postBr = postCode;
	}
	
	// konstruktor koji gradu pridruzuje ukupno trajanje kise u min
	public Grad(final Grad g, int ukupnoTrajanjeKise) {
		this(g.ime, g.drzava, g.postBr);
		this.ukupnoTrajanjeKise = ukupnoTrajanjeKise;
	}
	
	// ostale get() metode nam nisu potrebne
	public String getDrzava() {
		return drzava;
	}
	public int getUkupnoTrajanjeKise() {
		return ukupnoTrajanjeKise;
	}

	public String toString() {
		/**
		 * Vraca String reprezentaciju datog grada
		 */
		String city = ime + ", " + drzava + " (" + postBr + ")";
		
		/* ako ima podataka o ukupnom trajanju kise u tom gradu i to
		 * se prikazuje u String formatu
		 */
		if (ukupnoTrajanjeKise != 0)
			city += " " + Vreme.trajanjeKise(ukupnoTrajanjeKise);	
		return city;
	}
	
	public static String listajGradove(ArrayList<Grad> gradovi) {
		/**
		 * Vraca String reprezentaciju date liste gradova.
		 */
		
		StringBuilder sb = new StringBuilder();
		for (Grad grad : gradovi) {
			sb.append(grad + "\n");
		}		
		return sb.toString();
	}
	
	// komparator koji redja gradove po zip brojevima 
	public int compareTo(Grad g) {
		int result = Integer.compare(this.postBr, g.postBr);
		if (result != 0)
			return result;
		result = drzava.compareTo(g.drzava);
		if (result != 0)
			return result;
		return ime.compareTo(g.ime);
	}
	
	/* kad objekat treba da bude kljuc u HashMapi obavezno je predefinisati
	 * metode equals() i hashCode().
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Grad))
			return false;
		Grad other = (Grad) obj;
		return compareTo(other) == 0;
	}	
	public int hashCode() {
		return Objects.hash(ime, drzava, postBr);
	}

}
