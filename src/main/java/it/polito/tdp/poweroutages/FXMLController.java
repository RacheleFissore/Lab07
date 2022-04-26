/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.poweroutages;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.Poweroutages;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbNerc"
    private ComboBox<Nerc> cmbNerc; // Value injected by FXMLLoader

    @FXML // fx:id="txtYears"
    private TextField txtYears; // Value injected by FXMLLoader

    @FXML // fx:id="txtHours"
    private TextField txtHours; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    private Model model;
    
    @FXML
    void doRun(ActionEvent event) {
    	txtResult.clear();
    	// System.out.print(cmbNerc.getValue().getId()); --> permette di accedere all'id dell'elemento selezionato
    	String ore = txtHours.getText();
    	String anni = txtYears.getText();
    	
    	if(cmbNerc.getValue() != null && ore != "" && anni != "") {
    		String nercValue = cmbNerc.getValue().getValue(); // La classe Nerc ha come attributi int id e String value, quindi per prendere il
															  // valore dalla combobox devo accedere al getValue che mi restituisce un oggetto
															  // Nerc e poi accedo al suo getValue per ottenere il campo String
    		List<Poweroutages> listaBlackout = model.trova_eventi(nercValue, new LinkedList<Poweroutages>(),Integer.parseInt(anni), Integer.parseInt(ore));
    		int cntPersone = 0;
    		int cntOre = 0;
    		for(Poweroutages poweroutages : listaBlackout) {
    			cntPersone += poweroutages.getPersoneCoinvolte();
    			cntOre += poweroutages.getOreDisservizio();
    		}
    		
    		txtResult.appendText("Tot people affected: " + cntPersone + "\nTot hours of outages: " + cntOre + "\n");
    		for(Poweroutages poweroutages : listaBlackout) {
    			txtResult.appendText(poweroutages + "\n");
    		}
    	}
    	else {
    		txtResult.setText("Completare tutti i campi");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbNerc != null : "fx:id=\"cmbNerc\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtYears != null : "fx:id=\"txtYears\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtHours != null : "fx:id=\"txtHours\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        
        // Utilizzare questo font per incolonnare correttamente i dati;
        txtResult.setStyle("-fx-font-family: monospace");
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	for(Nerc cNerc : model.getNercList()) {
    		cmbNerc.getItems().add(cNerc);
    		
    	}
    }
}
