/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Erro;
import analisadorLexico.Lexema;
import analise.Analise;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import org.fxmisc.richtext.LineNumberFactory;

/**
 * FXML Controller class
 *
 * @author gusta
 */
public class MainScreenController implements Initializable {

    public static MainScreenController instance;
    @FXML
    private TextArea inputText;
    
    @FXML
    private TextArea console;

    @FXML
    private Button btn;

    @FXML
    private TableView<Object> tabela;

    @FXML
    private TableColumn<Lexema, String> tcLexema;

    @FXML
    private TableColumn<Lexema, String> tcToken;

    @FXML
    private TableColumn<Lexema, String> tcLinha;

    @FXML
    private TableColumn<Lexema, String> tcColunaInicial;

    @FXML
    private TableColumn<Lexema, String> tcColunaFinal;

    @FXML
    private TextFlow flowCount;

    @FXML
    private BorderPane bordePane;

    private ArrayList<Object> lex;
    private Analise ana;
    private ObservableList<Object> obsListLex;
    private File file;
    private ObservableList obsCount;
    private int lineCount;
    private CodeArea codeArea;

    public void execute(String exp) {

        //lex = calculadoralexico.CalculadoraLexico.execute(exp);
        lex = ana.executeLex(exp);
        ana.executeSint(lex);
        ana.executeSem(lex);
        obsListLex = FXCollections.observableArrayList(lex);
        tabela.setItems(obsListLex);
        
    }

    @FXML
    void onClickExecute(ActionEvent event) {

        execute(codeArea.getText());

    }

    @FXML
    void onClickOpen(ActionEvent event) throws FileNotFoundException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir Arquivo");
        file = fileChooser.showOpenDialog(((MenuItem) event.getTarget()).getParentPopup().getScene().getWindow());

//        Scanner scanner = new Scanner(file);
//        //String exp = "";
//        StringBuilder exp = new StringBuilder();
//        while(scanner.hasNext()){
//           exp.append(scanner.nextLine());
//           exp.append("\n");
//           
//        }
        try {
            byte[] encoded = Files.readAllBytes(file.toPath());
            String exp = new String(encoded, Charset.defaultCharset());
            codeArea.replaceText(exp);

        } catch (IOException ex) {
            Logger.getLogger(MainScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public void setConsole(String message){
     
        Platform.runLater(new Runnable() {
            @Override 
            public void run() {
                console.setText(message);
            }
        });
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        instance = this;
        
        console.setEditable(false);
        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.replaceText("program correto;\n" +
"int a, b, c;\n" +
"boolean d, e, f;\n" +
"\n" +
"procedure proc(var a1 : int);\n" +
"int a, b, c;\n" +
"boolean d, e, f;\n" +
"begin\n" +
"	a:=1;\n" +
"	if (a<1) then\n" +
"		a:=12\n" +
"end;\n" +
"\n" +
"begin\n" +
"	a:=2;\n" +
"	b:=10;\n" +
"	c:=11;\n" +
"	a:=b+c;\n" +
"	d:=true;\n" +
"	e:=false;\n" +
"	f:=true;\n" +
"	read(a);\n" +
"	write(b);\n" +
"	if (d) then\n" +
"	begin\n" +
"		a:=20;\n" +
"		b:=10*c;\n" +
"		c:=a div b\n" +
"	end;\n" +
"	while (a>1) do\n" +
"	begin\n" +
"		if (b>10) then\n" +
"			b:=2;\n" +
"		a:=a-1\n" +
"	end\n" +
"end.");
        bordePane.setCenter(new VirtualizedScrollPane<>(codeArea));

        tcLexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        tcToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tcLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tcColunaInicial.setCellValueFactory(new PropertyValueFactory<>("coluna_inicial"));
        tcColunaFinal.setCellValueFactory(new PropertyValueFactory<>("coluna_final"));
        
        
        
        tabela.setRowFactory(row -> new TableRow<Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                
                
                if (item instanceof Erro) {
                    setStyle(" -fx-background-color: red;"
                            + " -fx-text-fill: white;"
                            + "-fx-font-weight: bold; ");
                }else{
                    setStyle("");
                }
            }
        });
        ana = new Analise();

    }

}
