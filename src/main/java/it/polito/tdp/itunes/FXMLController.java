/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.AlbumAndBilancio;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	
    	Album a1 = cmbA1.getValue();
    	if (a1 == null) {
    		txtResult.appendText("Per favore selezionare un album dalla tendina!\n");
    		return;
    	}
    	
    	List<AlbumAndBilancio> successori = this.model.getAllSuccessori(a1);
    	if (successori == null) {
    		txtResult.appendText("Il vertice selezionato non ha vertici successori!\n");
    		return;
    	}
    	for (AlbumAndBilancio a : successori) {
    		txtResult.appendText(a.getAlbum() + ", bilancio = " + a.getBilancio() + "\n");
    	}
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	Album a1 = cmbA1.getValue();
    	if (a1 == null) {
    		txtResult.appendText("Per favore selezionare un album dalla tendina!\n");
    		return;
    	}
    	
    	Album a2 = cmbA2.getValue();
    	if (a2 == null) {
    		txtResult.appendText("Per favore selezionare un album dalla tendina!\n");
    		return;
    	}
    	
    	try {
    		int soglia = Integer.parseInt(txtX.getText());
    		
    		List<Album> percorso = this.model.calcolaPercorso(a1, a2, soglia);
    		if (percorso == null) {
    			txtResult.appendText("PERCORSO INESISTENTE!\n");
    		} else {
    			txtResult.appendText("Il percorso trovato è: \n");
    			for (Album a : percorso) {
    				txtResult.appendText(a + "\n");
    			}
    		}
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Per favore inserire una soglia valida!\n");
    		return;
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	cmbA1.getItems().clear();
    	cmbA2.getItems().clear();
    	
    	try {
    		int n = Integer.parseInt(txtN.getText());
    		this.model.creaGrafo(n);
    		
    		txtResult.setText("Grafo creato!\n");
        	txtResult.appendText("# Vertici " + this.model.getNumVertici() + "\n");
        	txtResult.appendText("# Archi " + this.model.getNumArchi() + "\n");
        	
        	// Dopo aver creato il grafo possiamo popolare la tendina degli album
        	cmbA1.getItems().addAll(this.model.getAllAlbums());
        	cmbA2.getItems().addAll(this.model.getAllAlbums());
        	
    	} catch (NumberFormatException e) {
    		txtResult.appendText("Per favore inserire un numero valido di canzoni!\n");
    		return;
    	}
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    }
}
