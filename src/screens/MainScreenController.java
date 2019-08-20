/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Lexema;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author gusta
 */
public class MainScreenController implements Initializable {

   @FXML
   private TextField inputText;

   @FXML
    private Button btn;

    @FXML
    private TableView<Lexema> tabela;

    @FXML
    private TableColumn<Lexema, String> tcLexema;

    @FXML
    private TableColumn<Lexema, String> tcToken;

    @FXML
    private TableColumn<Lexema, String> tcColunaInicial;

    @FXML
    private TableColumn<Lexema, String> tcColunaFinal;
    
    private ArrayList<Lexema> lex;
    private AnalisadorLexico ana;
    private ObservableList<Lexema> obsListLex;
    private File file;
    
    
    public void execute(String exp){
        
       //lex = calculadoralexico.CalculadoraLexico.execute(exp);
       lex = ana.execute(exp);
       obsListLex = FXCollections.observableArrayList(lex);
       tabela.setItems(obsListLex); 
    }
    
    @FXML
    void onClickExecute(ActionEvent event) {
        execute(inputText.getText());
    }
    
    @FXML
    void onClickOpen(ActionEvent event) throws FileNotFoundException {
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Arquivo");
        file =  fileChooser.showOpenDialog(((MenuItem)event.getTarget()).getParentPopup().getScene().getWindow());
        
        Scanner scanner = new Scanner(file);
        String exp = "";
        while(scanner.hasNext()){
           exp += scanner.nextLine(); 
        }
        
        
        inputText.setText(exp);
        execute(exp);
        
    }
    
      /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tcLexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        tcToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tcColunaInicial.setCellValueFactory(new PropertyValueFactory<>("coluna_inicial"));
        tcColunaFinal.setCellValueFactory(new PropertyValueFactory<>("coluna_final"));
        ana = new AnalisadorLexico();
    }    
    
}
