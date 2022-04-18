package application.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Phrase;
//import com.itextpdf.text.pdf.PdfPCell;
//import com.itextpdf.text.pdf.PdfPTable;
//import com.itextpdf.text.pdf.PdfWriter;

import application.java.ArbreStagiaire;
import application.java.Noeud;
import application.java.Recherche;
import application.java.Stagiaire;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class InterfaceAdministrateurCtrl implements Initializable {
	
	//raccourcis pour se rendre sur les diff�rentes interfaces
    private static final String AJOUT_STAGIAIRE = "/application/interfaces/InterfaceAjoutStagiaire.fxml";
    private static final String MODIFIER_STAGIAIRE = "/application/interfaces/InterfaceModifierStagiaire.fxml";
    private static final String A_PROPOS = "/application/interfaces/InterfacePropos.fxml";
    private static final String AUTHENTIFICATION = "/application/interfaces/InterfaceAuthentification.fxml";
    private static final String DOCUMENT_PDF = "C:\\Users\\farah\\Desktop\\FormationIsika\\ProjetsEclipse\\LogicielGestionStagiaires\\ListeStagiaire.pdf";
    
    //le squelettes des controllers que l'on importe de FXML
    @FXML
    public TableColumn<Stagiaire, String> nomS;
    @FXML
    public TableColumn<Stagiaire, String> prenomS;
    @FXML
    public TableColumn<Stagiaire, String> dptS;
    @FXML
    public TableColumn<Stagiaire, String> promoS;
    @FXML
    public TableColumn<Stagiaire, String> anneeS;
    @FXML
    public TableView<Stagiaire> tblS;
    @FXML
    private TextField stgrTotal;
    @FXML
    private Button ajoutBtn;
    @FXML
    private Button modifBtn;
    @FXML
    private Button propos;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button delBtn;
    @FXML
    private TextField nomMdf;
    @FXML
    private TextField prenomMdf;
    @FXML
    private TextField dptMdf;
    @FXML
    private TextField promoMdf;
    @FXML
    private TextField anneeMdf;
    @FXML
    private Button validerBtn;
    @FXML
    private RadioButton nomRadioBtn;
    @FXML
    private RadioButton dptRadioBtn;
    @FXML
    private RadioButton anneeRadioBtn;
    @FXML
    private TextField critere;
    @FXML
    private Button rechercheSimpleBtn;
    @FXML
    private ToggleGroup nda;

    
    @FXML
    private Button refresh;

    static Stagiaire stagiaireMdf;
    
    
    // je d�clare ma variable ArbreStagaire afin de r�cup�rer via Observable List ma liste de stagigiaire que je parcours
    static ArbreStagiaire monArbre = new ArbreStagiaire();
    ObservableList<Stagiaire> observableArrayList = FXCollections.observableArrayList(Recherche.parcoursStagiaire(monArbre));
    
    
    //j'initialise ma varibale en demande les arguments suivants: nom,prenom,dpts,promotions,ann�e
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
	nomS.setCellValueFactory(new PropertyValueFactory<Stagiaire, String>("nom"));
	prenomS.setCellValueFactory(new PropertyValueFactory<Stagiaire, String>("prenom"));
	dptS.setCellValueFactory(new PropertyValueFactory<Stagiaire, String>("departement"));
	promoS.setCellValueFactory(new PropertyValueFactory<Stagiaire, String>("promotion"));
	anneeS.setCellValueFactory(new PropertyValueFactory<Stagiaire, String>("annee"));
	
	//je rappel dans ma table tous mes elements listes
	tblS.setItems(observableArrayList);
	
	
	// je souhaite avoir le nombre de stagiare total en parcourant mon arbre
	stgrTotal.setText(String.valueOf(Recherche.parcoursStagiaire(monArbre).size()));
	
	
	//cet evenement me permet de suppimer un element de ma liste stagiaire
	delBtn.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		supprStagiaire();
	    }
	});
	
	
	//cet �venement me permet d'une part de selectionner mon stagiaire que je souhaite modifier
	modifBtn.setOnAction((EventHandler<ActionEvent>) new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		Stagiaire stgrMdf = tblS.getSelectionModel().getSelectedItem();
		if (stgrMdf != null) {
		    try {
			allerVersInterfaceModifierStagiaire(stgrMdf);
			//suprime de mon arbre un element de ma liste 
			monArbre.supprimer(stgrMdf);
		    } catch (IOException e) {
			e.printStackTrace();
		    }
		}
	    }
	});

    }

    // METHODE POUR ALLER VERS INTERFACE AJOUTER STAGIAIRE//
    public void allerVersInterfaceAjoutStagiaire() throws IOException {
	ajoutBtn.getScene().getWindow();
	FXMLLoader loader = new FXMLLoader(getClass().getResource(AJOUT_STAGIAIRE));
	Pane rootPane = (Pane) loader.load();
	Scene scene = new Scene(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());
	Stage proposStage = new Stage();
	proposStage.setTitle("Ajouter un Stagiaire");
	proposStage.setScene(scene);
	proposStage.show();
    }

    // METHODE POUR ALLER VERS INTERFACE MODIFIER STAGIAIRE//
    public void allerVersInterfaceModifierStagiaire(Stagiaire stagaire) throws IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(MODIFIER_STAGIAIRE));
	Pane rootPane = (Pane) loader.load();
	Scene scene = new Scene(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());
	Stage modifStage = new Stage();
	modifStage.setTitle("Modifier le Stagiaire");
	modifStage.setScene(scene);
	modifStage.show();
    }

    // METHODE POUR FERMER L'APPLICATION//
    @FXML
    public void closeWindow() {
	Platform.exit();
    }

    // METHODE POUR ALLER A LA FENETRE A PROPOS//
    @FXML
    private void AllerVersPropos() throws IOException {
	FXMLLoader loader = new FXMLLoader(getClass().getResource(A_PROPOS));
	Pane rootPane = (Pane) loader.load();
	Scene scene = new Scene(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());
	Stage modifStage = new Stage();
	modifStage.setTitle("Modifier le Stagiaire");
	modifStage.setScene(scene);
	modifStage.show();
    }

    // METHODE POUR SE DECONNECTER//
    @FXML
    private void deconnexion() throws IOException {
	Stage proposStage = (Stage) ajoutBtn.getScene().getWindow();
	// decoBtn.getScene().getWindow();
	FXMLLoader loader = new FXMLLoader(getClass().getResource(AUTHENTIFICATION));
	Pane rootPane = (Pane) loader.load();
	Scene scene = new Scene(rootPane, rootPane.getPrefWidth(), rootPane.getPrefHeight());
	// Stage proposStage = new Stage();
	proposStage.setTitle("Annuaire Informatis� par FHF");
	proposStage.setScene(scene);
	proposStage.show();
    }

    // IMPRIMER LA LISTE EN PDF//
//    public void imprimerPdf() throws IOException, DocumentException {
//	pdf(Recherche.parcoursStagiaire(monArbre));
//	Alert alert = new Alert(AlertType.INFORMATION);
//	alert.setTitle("Document PDF");
//	alert.setHeaderText(null);
//	alert.setContentText("La liste des stagiaire en format PDF est pr�t � �tre imprim�e");
//	alert.showAndWait();
//    }

    // METHODE POUR PRE REMPLIR LES CHAMPS DANS FENETRE MODIFICATION STAGIAIRE//
    static Stagiaire stg;

    public void selectionnerTbl() {

	stg = tblS.getSelectionModel().getSelectedItem();
	if (stg != null) {
	    nomS.setText(stg.getNom());
	    prenomS.setText(stg.getPrenom());
	    dptS.setText(stg.getDepartement());
	    promoS.setText(stg.getPromotion());
	    anneeS.setText(stg.getAnnee());
	}

    }

    // METHODE SUPPRIMER STAGIAIRE//
    @FXML
    public void supprStagiaire() {
	Stagiaire stagiaire = tblS.getSelectionModel().getSelectedItem();
	if (stagiaire != null) {
	    Alert suppressionAlerte = new Alert(AlertType.CONFIRMATION);
	    suppressionAlerte.setTitle("Suppression d'un stagiaire");
	    suppressionAlerte.setHeaderText("�tes-vous s�r de vouloir supprimer ce stagiaire ?");
	    Optional<ButtonType> option = suppressionAlerte.showAndWait();
	    if (option.get() == ButtonType.OK) {
		monArbre.supprimer(stagiaire);
		tblS.getSelectionModel().clearSelection();
		tblS.getItems().clear();
		tblS.getItems().addAll(Recherche.parcoursStagiaire(monArbre));

	    } else {
	    }
	}
    }

    // METHODE DE RECHERCHE//
    // PAR NOM//
    public static List<Stagiaire> chercherNom(String cle, ArbreStagiaire arbre) {
	List<Stagiaire> listRechNom = new ArrayList<>();
	return chercherNom(cle, arbre.getRacine(), listRechNom);
    }

    private static List<Stagiaire> chercherNom(String cle, Noeud r, List<Stagiaire> listResult) {
	if (r == null) {
	    return listResult;
	}
	chercherNom(cle, r.getGauche(), listResult);
	if (cle.compareTo(r.getStagiaire().getNom()) == 0) {
	    listResult.add(r.getStagiaire());
	}
	chercherNom(cle, r.getDroit(), listResult);
	return listResult;
    }

    // PAR DEPARTEMENT//
    public static List<Stagiaire> chercherDepartement(String cle, ArbreStagiaire arbre) {
	List<Stagiaire> listRechDep = new ArrayList<>();
	return chercherDepartement(cle, arbre.getRacine(), listRechDep);
    }

    private static List<Stagiaire> chercherDepartement(String cle, Noeud r, List<Stagiaire> listResult) {
	if (r == null) {
	    return listResult;
	}
	chercherDepartement(cle, r.getGauche(), listResult);
	if (cle.compareTo(r.getStagiaire().getDepartement()) == 0) {
	    listResult.add(r.getStagiaire());
	}
	chercherDepartement(cle, r.getDroit(), listResult);
	return listResult;
    }

    // PAR ANNEE//
    public static List<Stagiaire> chercherAnnee(String cle, ArbreStagiaire arbre) {
	List<Stagiaire> listRechAnnee = new ArrayList<>();
	return chercherAnnee(cle, arbre.getRacine(), listRechAnnee);
    }

    private static List<Stagiaire> chercherAnnee(String cle, Noeud r, List<Stagiaire> listResult) {
	if (r == null) {
	    return listResult;
	}
	chercherAnnee(cle, r.getGauche(), listResult);
	if (cle.compareTo(r.getStagiaire().getAnnee()) == 0) {
	    listResult.add(r.getStagiaire());

	}

	chercherAnnee(cle, r.getDroit(), listResult);
	return listResult;
    }

    @FXML
    static List<Stagiaire> listRech = new ArrayList<>();
    
    
    
    public void rechercheSimple() throws IOException {
	nomRadioBtn.setToggleGroup(nda);
   	dptRadioBtn.setToggleGroup(nda);
   	anneeRadioBtn.setToggleGroup(nda);
	// RECHERCHE PAR NOM//
	if (nomRadioBtn.isSelected()) {
	    String nomChercher = critere.getText().toUpperCase();
	    listRech = chercherNom(nomChercher, monArbre);
	    tblS.getItems().clear();
	    tblS.getItems().addAll(listRech); 
	}
	// RECHERCHE PAR DEPARTEMENT//
	if (dptRadioBtn.isSelected()) {
	    String dptChercher = critere.getText();
	    listRech = chercherDepartement(dptChercher, monArbre);
	    tblS.getItems().clear();
	    tblS.getItems().addAll(listRech);
	}
	// RECHERCHE PAR ANNEE//
	if (anneeRadioBtn.isSelected()) {
	    String anneechercher = critere.getText();;
	    listRech = chercherAnnee(anneechercher, monArbre);
	    tblS.getItems().clear();
	    tblS.getItems().addAll(listRech);
	}

    }

    @FXML
    public void refresh() throws IOException {
	tblS.getItems().clear();
	tblS.getItems().addAll(Recherche.parcoursStagiaire(monArbre)); 
    }

    // METHODE POUR IMPRIMER LA LISTE SOUS FORMAT PDF//
//    private void pdf(List<Stagiaire> list) throws FileNotFoundException, DocumentException {
//	FileOutputStream fos = new FileOutputStream(new File(DOCUMENT_PDF));
//	Document doc = new Document();
//	PdfWriter.getInstance((com.itextpdf.text.Document) doc, fos);
//	doc.open();
//	doc.add(new Phrase("Liste des stagiaires\n"));
//	doc.add(new Phrase(
//		"***********************************************************************************" + "\n"));
//	doc.add(new Phrase("Liste g�n�r�e le " + LocalDate.now() + "\n"));
//	doc.add(new Phrase(
//		"***********************************************************************************" + "\n"));
//	doc.add(new Phrase("Nombre de stagiaires : " + Recherche.parcoursStagiaire(monArbre).size() + "\n"));
//	doc.add(new Phrase("***********************************************************************************"));
//	PdfPTable table = new PdfPTable(5);
//	PdfPCell cell1 = new PdfPCell(new Phrase("Nom"));
//	PdfPCell cell2 = new PdfPCell(new Phrase("Prenom"));
//	PdfPCell cell3 = new PdfPCell(new Phrase("Departement"));
//	PdfPCell cell4 = new PdfPCell(new Phrase("Promotion"));
//	PdfPCell cell5 = new PdfPCell(new Phrase("Ann�e"));
//
//	table.addCell(cell1);
//	table.addCell(cell2);
//	table.addCell(cell3);
//	table.addCell(cell4);
//	table.addCell(cell5);
//
//	table.setWidthPercentage(100);
//
//	for (Stagiaire stagiaireTemp : list) {
//	    table.addCell(new Phrase(stagiaireTemp.getNom()));
//	    table.addCell(new Phrase(stagiaireTemp.getPrenom()));
//	    table.addCell(new Phrase(stagiaireTemp.getDepartement()));
//	    table.addCell(new Phrase(stagiaireTemp.getPromotion()));
//	    table.addCell(new Phrase(stagiaireTemp.getAnnee()));
//	}
//	doc.add(table);
//	doc.close();
//    }

}