package application;

//import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javafx.application.Application;
import javafx.fxml.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.stage.*;

public class Main extends Application  {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Archivitos.fxml"));
		Scene scene = new Scene(root, 640, 375);
		primaryStage.setTitle("Practica 1");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	@FXML
	private void cargarArchivos(ActionEvent event) {
		JFileChooser jf = new JFileChooser();
		int returnVal = jf.showOpenDialog(null);
		
		if(returnVal != JFileChooser.APPROVE_OPTION) {
			
			//File[] archivos = jf.getSelectedFiles();
			return;
		
		}
		
	}

	public static void main(String[] args) {
		try {
		      
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		}catch (Exception e) { 
			System.err.println("Error: " + e.getMessage()); 
		}
		launch(args);
	}
}
