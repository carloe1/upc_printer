package application;
	
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class Main extends Application {
	
	
	String jrxmlFileName 	= "doc/label_template/upc.jrxml";
	String jasperFileName 	= "doc/label_template/upc.jasper";
	String pdfFileName 		= "doc/labels/";
	Printer printer = new Printer();
	String currentStyleCode = "";
	Style currentStyle;
	Set<String> colors;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// Create the pdfs based on the excel data source
			Client client = new Client();
			HashMap<String, Style> style_map = client.start();
			
			// List that will contain everything that will populate the scene
			GridPane root = new GridPane();
			root.setMinSize(400, 400);
		    root.setPadding(new Insets(10, 10, 10, 10));
		    
		    //Setting the vertical and horizontal gaps between the columns 
		    root.setVgap(6); 
		    root.setHgap(6); 
		    root.setAlignment(Pos.CENTER);
		    
		    // Printer Combo Box ================================================================================================
		    ObservableList<String> printers = FXCollections.observableArrayList(Printer.getPrinterList());
		    ComboBox printerCombobox = new ComboBox(printers);
		    printerCombobox.setPromptText("Printer Name");
		    root.add(printerCombobox, 1, 0);
		    Text printerText = new Text();
		    printerText.setText("Printer: ");
		    root.add(printerText, 0, 0);
			
			// Creating a text field =============================================================================================
		    // Color ###########################################
 			Text colorText = new Text();
 			colorText.setText("Color: ");
 			root.add(colorText, 0, 2);
 			ComboBox colorCombobox = new ComboBox();
 			colorCombobox.setPromptText("Color Code");
		    
		    // Style ###########################################
		    Text styleText = new Text();
			styleText.setText("Style: ");
			root.add(styleText, 0, 1);
		    TextField styleField = new TextField();
			styleField.textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
			        currentStyleCode = newValue;
			        currentStyle = style_map.get(currentStyleCode);
			        
			        if (currentStyle != null) {
			        	colors = currentStyle.getColorSet();
			        	colorCombobox.getItems().clear();
			        	colorCombobox.getItems().addAll(colors);
			        }
			        else {
			        	colorCombobox.getItems().clear();
			        }
				}
			});
		    // Setting the position of the text field
			root.add(styleField, 1, 1);
			
			// Setting the position of the text field
			root.add(colorCombobox, 1, 2);
			
			// Size ############################################
			TextField sizeField = new TextField();
			// Setting the position of the text field
			root.add(sizeField, 1, 3);
			Text sizeText = new Text();
			sizeText.setText("Size: ");
			root.add(sizeText, 0, 3);
			
			// Quantity ########################################
			TextField qtyField = new TextField();
			// Setting the position of the text field
			root.add(qtyField, 1, 4);
			Text qtyText = new Text();
			qtyText.setText("Quantity: ");
			root.add(qtyText, 0, 4);
			
			
			// Create the Print Button ==============================================================================================
			Button printButton = new Button("Print");
			printButton.setOnAction( new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					// Get the Printer
					String printerName = (String) printerCombobox.getValue();
					
					// Get the Text
					String style = styleField.getText();
					String color = (String) colorCombobox.getValue();
					String size = sizeField.getText().toUpperCase();
					String qtyString = qtyField.getText();
					
					String fileName = pdfFileName + String.format("%s_%s_%s.pdf", style, color, size);
					
					try {
						int qty = Integer.parseInt(qtyString);
						for (int i=0; i < qty; i++) {
							printer.PrintReportToPrinter(printerName, fileName);
						}
						// Clear the Text Boxes
						styleField.clear();
						sizeField.clear();
						qtyField.clear();
					}
					catch (Exception e1) {
						System.out.println(fileName + " does not exists.");
					}
				}
			});
			root.add(printButton, 1, 5);
			
			Button clearButton = new Button("Clear");
			clearButton.setOnAction( new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent e) {
					styleField.clear();;
					sizeField.clear();
					qtyField.clear();
				}
			});
			root.add(clearButton, 2, 5);
			
			
			// Prepare the Scene======================================================================================================
			Scene scene = new Scene(root,400,300);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			
			// Prepare title and show the stage
			primaryStage.setTitle("UPC Label Printer");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
