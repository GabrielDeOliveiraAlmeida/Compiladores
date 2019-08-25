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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author gusta
 */
public class MainScreenController implements Initializable {

   @FXML
   private TextArea inputText;

   @FXML
    private Button btn;

    @FXML
    private TableView<Lexema> tabela;

    @FXML
    private TableColumn<Lexema, String> tcLexema;

    @FXML
    private TableColumn<Lexema, String> tcToken;

    @FXML
    private TableColumn<Lexema,String> tcLinha;
    
    @FXML
    private TableColumn<Lexema, String> tcColunaInicial;

    @FXML
    private TableColumn<Lexema, String> tcColunaFinal;
    
   @FXML
    private TextFlow flowCount;
    @FXML
    private ScrollPane scrollCount;
    
    private ArrayList<Lexema> lex;
    private AnalisadorLexico ana;
    private ObservableList<Lexema> obsListLex;
    private File file;
    private ObservableList obsCount;
    private int lineCount;
    
   
    
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
    void keyPressed_Enter(KeyEvent event) {
        switch(event.getCode()){
            case ENTER:
                Text t = new Text();
                t.setFont(Font.font("System", 13));
                t.setText(lineCount + "\n");
                obsCount.add(t);
                lineCount++;
                break;
            case BACK_SPACE:
                if(lineCount!=2){
                    obsCount.remove(lineCount-2);
                    lineCount--; 
                }
                break;
                
        }
        scrollCount.setVvalue(1.0);
        
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
       
        
        obsCount = flowCount.getChildren();
        Text t = new Text();
        t.setFont(Font.font("System", 13));
        t.setText("1\n");
        obsCount.add(t);
         lineCount=2;
        scrollCount.setFitToHeight(true);
        
        scrollCount.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent event) {
            if (event.getDeltaY() != 0) {
                event.consume();
            }
        }
        });
        
        
        
        tcLexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        tcToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tcLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tcColunaInicial.setCellValueFactory(new PropertyValueFactory<>("coluna_inicial"));
        tcColunaFinal.setCellValueFactory(new PropertyValueFactory<>("coluna_final"));
        ana = new AnalisadorLexico();
        
    }    
    
}
