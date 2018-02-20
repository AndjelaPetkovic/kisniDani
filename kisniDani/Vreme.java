package kisniDani;

import java.util.ArrayList;

public class Vreme {
	
	private Dani dan;
	private int sat, minut;
	
	public Vreme(Dani dan, int sat, int minut) {
		this.dan = dan;
		this.sat = sat;
		this.minut = minut;
	}	

	public static int kisniMinuti(Vreme vreme1, Vreme vreme2) {
		/**
		 * Racuna razliku u minutima izmedju dva data vremena, 
		 * objekata klase Vreme.
		 * 
		 * @param vreme1: pocetno vreme
		 * @param vreme2: krajnje vreme
		 * @return: minute kao int
		 */
		int dani = vreme2.dan.ordinal() - vreme1.dan.ordinal();
		int sati = dani*24 + vreme2.sat - vreme1.sat;
		int minuti = sati*60 + vreme2.minut - vreme1.minut;
		
		return minuti;	
	}
	
	public static int ukupniKisniMinuti(ArrayList<Vreme> vremena) {
		/**
		 * Za datu listu vremena, gde svaki neparan clan predstavlja
		 * vreme pocetka kise, a svaki paran vreme kad je kisa prestala,
		 * racuna se ukupno trajanje kise izrazeno u minutima
		 * 
		 * @param ArrayList vremena: lista sa clanovima objekta klase Vreme
		 * @return: ukupno trajanje u minutima kao int
		 */
		int ukupniMinuti = 0;
		for(int i = 0; i < vremena.size()-1; i += 2) {
			Vreme vreme1 = vremena.get(i);
			Vreme vreme2 = vremena.get(i+1);
			ukupniMinuti += kisniMinuti(vreme1, vreme2);
		}
		return ukupniMinuti;
	}
	
	public static String trajanjeKise(int ukupniMinuti) {
		/**
		 * Za dati int koji predstavlja ukupno vreme trajanja kise,
		 * izrazeno u minutima, racuna koliko je to u satima i vraca
		 * String u formi hh:mm.
		 * 
		 * @param ukupniMinuti: ukupno trajanje u minutima kao int
		 * @return: String format vremena, u formi hh:mm
		 */
		String sati = Integer.toString(ukupniMinuti / 60);
		StringBuilder minuti = new StringBuilder(Integer.toString(ukupniMinuti % 60));
		if (minuti.length() == 1)
			minuti.insert(0, "0");		
		return sati + ":" + minuti;			
	}
	
	public String toString() {
		return dan + ", " + sat + ":" + minut;
	}

}
