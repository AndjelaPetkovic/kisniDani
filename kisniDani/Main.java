package kisniDani;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	
	/* 
	 * Aplikacija za pracenje ukupnog trajanja kise u pojedinim gradovima.
	 *  
	 * Opis zadatka dat je u pdf fajlu tekuceg direktorijuma.
	 * 
	 * Po pokretanju aplikacije otvara se prozor i omogucuje se korisniku da
	 * izabere zeljenu datoteku. Treba izabrati text file 'kisniDani' iz 
	 * tekuceg direktorijuma, u kome se nalaze podaci za neke gradove i
	 * hronoloskim redom su zapisani trenuci pocetka i kraja padaja kise
	 * u toku jedne nedelje.
	 * Podaci se cuvaju u HashMapi, gde kljuc mape predstavlja grad (objekat
	 * klase Grad) a vrednost kluca je lista sa vremenima pocetka i kraja
	 * padanja kise u tom gradu (ArrayList sa objektima klase Vreme)
	 */
	
	private static Stage primaryStage;	
	private static File file = null;
	
	private static HashMap<Grad, ArrayList<Vreme>> podaci =
									new HashMap<Grad, ArrayList<Vreme>>();
	private static Button izaberiBtn, prikaziBtn;	
	private static RadioButton postBrojBtn, ukupnoTrajanjeBtn,
								SrbijaBtn, CrnaGoraBtn, MakedonijaBtn;
	private static ToggleGroup tg1, tg2;
	private static TextArea prikaziTa;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Kisa");
		
		// koreni cvor i pomocna GUI fukncija
		VBox root = new VBox(10);
		createGUI(root);
		
		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		// korisnik ne moze da menja velicinu prozora
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
	
	public void createGUI(VBox root) {
		
		izaberiBtn = new Button("Izaberite datoteku...");
		// funkcija za biranje i ucitavanja datoteke
		izaberiDatoteku();
				
		Label labela = new Label("Prikaz");
		labela.setTextFill(Color.BLUE);
		
		VBox center = new VBox();
		
		// HBox gde ce biti smesteni svi RadioButton
		HBox buttonBox = new HBox(10);
		
		// dva RadioButton ce biti u levi vertikalni box
		VBox leftButtonBox = new VBox(10);
		
		/*da bi tacno jedan RadioButton smeo biti selektovan moraju
		 * da se stave u jednu ToggleGroup
		 */
		tg1 = new ToggleGroup();
		postBrojBtn = new RadioButton("postanskih brojeva, rastuce");
		postBrojBtn.setToggleGroup(tg1);
		postBrojBtn.setSelected(true);
		ukupnoTrajanjeBtn = new RadioButton("ukupnog trajanja svih kisa");
		ukupnoTrajanjeBtn.setToggleGroup(tg1);
		
		leftButtonBox.getChildren().addAll(postBrojBtn, ukupnoTrajanjeBtn);
		leftButtonBox.setStyle("-fx-border-color: blue");
		leftButtonBox.setPadding(new Insets(10));
		leftButtonBox.setPrefSize(285, 100);
		
		// tri RadioButton ce biti u desni vertikalni box
		VBox rightButtonBox = new VBox(10);
		tg2 = new ToggleGroup();
		SrbijaBtn = new RadioButton("Srbija");
		SrbijaBtn.setToggleGroup(tg2);
		SrbijaBtn.setSelected(true);
		CrnaGoraBtn = new RadioButton("Crna Gora");
		CrnaGoraBtn.setToggleGroup(tg2);
		MakedonijaBtn = new RadioButton("Makedonija");
		MakedonijaBtn.setToggleGroup(tg2);
		
		rightButtonBox.getChildren().addAll(SrbijaBtn, CrnaGoraBtn, MakedonijaBtn);
		rightButtonBox.setStyle("-fx-border-color: blue");
		rightButtonBox.setPadding(new Insets(10));
		rightButtonBox.setPrefSize(285, 100);
		
		buttonBox.getChildren().addAll(leftButtonBox, rightButtonBox);
		buttonBox.setAlignment(Pos.CENTER);
		
		center.getChildren().addAll(labela, buttonBox);
		center.setPadding(new Insets(5));
		
		prikaziBtn = new Button("Prikazi izvestaj");
		prikaziBtn.setDisable(true);
		// funkcija koja se aktivira kad se pritisne dugme 'Prikazi izvestaj'
		prikaziPodatke();
		
		prikaziTa = new TextArea();
		// korisnik ne moze sad da edituje TextArea
		prikaziTa.setEditable(false);
		prikaziTa.setPrefSize(480, 200);
		
		root.getChildren().addAll(izaberiBtn, center, prikaziBtn, prikaziTa);
		root.setPadding(new Insets(10));
		root.setAlignment(Pos.CENTER);
			
	}
	
	public void izaberiDatoteku() {
		/**
		 * Omogucuje korisniku da izabere fajl, 
		 * ako izabere ispravan nastavlja se sa ucitavanjem i podaci
		 * se cuvaju u HashMapi.
		 */
		
		izaberiBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				file = openFile();
				if (file != null) {
					// onemogucuje se dugme 'Izaberi' a omogucuje se dugme 'Prikazi'
					izaberiBtn.setDisable(true);
					prikaziBtn.setDisable(false);
					try {
						List<String> linije = Files.readAllLines(Paths.get(file.getName()), StandardCharsets.UTF_8);
						for(String linija : linije) {	
							try(Scanner scLinija = new Scanner(linija)) {								
								scLinija.useDelimiter(", ");
								String ime = scLinija.next();
								String drzava = scLinija.next();
								int postBr = scLinija.nextInt();
								Grad grad = new Grad(ime, drzava, postBr);
								ArrayList<Vreme> vremena = new ArrayList<Vreme>();
								/* ako grad ne postoji u listi dodaje se kao kljuc
								 * sa praznom ArrayList<Vreme> kao njegova vrednost
								 */
								if (!podaci.containsKey(grad))
									podaci.put(grad, vremena);
								Dani dan = Dani.valueOf(scLinija.next());
								String time = scLinija.next();
								try(Scanner scVreme = new Scanner(time)) {
									scVreme.useDelimiter(":");
									int sati = scVreme.nextInt();
									int minuti = scVreme.nextInt();
									Vreme vreme = new Vreme(dan, sati, minuti);
									/* ako grad vec postoji kao kljuc u listi na listu 
									 * vremena (njegovu vrednost) dodaje se jos jedan clan
									 * objekat klase Vreme koji je malopre napravljen
									 */
									if (podaci.containsKey(grad)) {
										ArrayList<Vreme> vremeGrada = podaci.get(grad);
										vremeGrada.add(vreme);
										podaci.put(grad, vremeGrada);
									}
								}
						
							}
						}		
					} catch (IOException e) {
						System.out.println("Greska pri radu sa datotekom.");
						System.exit(1); // prekid daljeg izvrsavanja programa
					}
				}
				
			}
			
		});
	}
	
	public static File openFile() {
		/**
		 * Funkcija za biranje fajla iz tekuceg direktorijuma.
		 * @return: ucitan fajl ili null ako korisnik ne izabere fajl
		 */

		File file = null;
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Open Resourse File");
		chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("text files", "*.txt"));
		file = chooser.showOpenDialog(primaryStage);
		
		return file;
	}
	
	public void prikaziPodatke() {
		/**
		 * Funkcija za prikazivanje ucitanih podataka.
		 * U zavisnosti od toga koji RadioButton je selektovan,
		 * odgovarajuci podaci se ispisuju u TextArea.
		 * 
		 * U desnom ButtonBox se nalaze drzave, prikazivace se samo
		 * gradovi iz drzave koja je selektovana.
		 * U levom ButtonBox korisnik bira da li hoce da se prikazu
		 * gradovi sortirani rastuce po postanskim brojevima ili
		 * opadajuce po vremenu ukupnog trajanja kise u tom gradu.
		 * 
		 */
		
		prikaziBtn.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (postBrojBtn.isSelected()) {
					ArrayList<Grad> izabraniGradovi = gradoviDateDrzave();
					Collections.sort(izabraniGradovi);
					prikaziTa.setText(Grad.listajGradove(izabraniGradovi));
				}
				else if (ukupnoTrajanjeBtn.isSelected()) {
					ArrayList<Grad> izabraniGradovi = kisniGradovi();
					prikaziTa.setText(Grad.listajGradove(izabraniGradovi));
				}			
			}		
		});
	}
	
	public ArrayList<Grad> gradoviDateDrzave() {
		/**
		 * Vraca listu gradova drzave koju je korisnik selektovao
		 * pomocu odgovarajuceg RadioButton
		 */

		Set<Grad> gradoviSet = podaci.keySet();
		ArrayList<Grad> gradoviList = new ArrayList<Grad>();
		RadioButton selectedBtn = (RadioButton) tg2.getSelectedToggle();
		String drzava = selectedBtn.getText();
		for (Grad grad : gradoviSet) {
			// izdvaja samo gradove iz selektovane drzave
			if (grad.getDrzava().equals(drzava))
				gradoviList.add(grad);
		}
		return gradoviList;
	}
	
	public ArrayList<Grad> kisniGradovi() {
		/**
		 * Vraca listu gradova drzave koju je korisnik selektovao
		 * pomocu odgovarajuceg RadioButton, sortiranim po ukupnom
		 * vremenu trajanju kise opadajuce
		 */
		ArrayList<Grad> izabraniGradovi = gradoviDateDrzave();
		for (Grad grad : izabraniGradovi) {
			ArrayList<Vreme> vremena = podaci.get(grad);
			// za svaki grad izracuna ukupno trajanje kise u minutima
			int totalRainMinutes = Vreme.ukupniKisniMinuti(vremena);
			Grad kisniGrad = new Grad(grad, totalRainMinutes);
			izabraniGradovi.set(izabraniGradovi.indexOf(grad), kisniGrad);
		}
		Collections.sort(izabraniGradovi, new Comparator<Grad>() {
			@Override
			public int compare(Grad g1, Grad g2) {
				return Integer.compare(g2.getUkupnoTrajanjeKise(), g1.getUkupnoTrajanjeKise());
			}			
		});
		return izabraniGradovi;
	}
}
