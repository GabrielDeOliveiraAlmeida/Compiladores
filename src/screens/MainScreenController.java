/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens;

import analisadorLexico.AnalisadorLexico;
import analisadorLexico.Erro;
import analisadorLexico.Lexema;
import analisadorSemantico.Simbolos;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
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
import javafx.util.Callback;
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
    private TextArea consoleSem;

    @FXML
    private Button btn;

    @FXML
    private TabPane tabCodigo;

    @FXML
    private TableView<Object> tabelaLexica;

    @FXML
    private TableColumn<Lexema, String> tabLexLexema;

    @FXML
    private TableColumn<Lexema, String> tabLexToken;

    @FXML
    private TableColumn<Lexema, String> tabLexLinha;

    @FXML
    private TableColumn<Lexema, String> tabLexColunaInicial;

    @FXML
    private TableColumn<Lexema, String> tabLexColunaFinal;

    @FXML
    private TableView<Object> tabelaSemantica;

    @FXML
    private TableColumn<Simbolos, String> tabSemCadeia;

    @FXML
    private TableColumn<Simbolos, String> tabSemToken;

    @FXML
    private TableColumn<Simbolos, String> tabSemCategoria;

    @FXML
    private TableColumn<Simbolos, String> tabSemTipo;

    @FXML
    private TableColumn<Simbolos, String> tabSemEndereco;

    @FXML
    private TableColumn<Simbolos, String> tabSemEscopo;

    @FXML
    private TableColumn<Simbolos, String> tabSemUtilizada;

    @FXML
    private TextFlow flowCount;

    @FXML
    private BorderPane bordePaneCode;

    @FXML
    private BorderPane borderPaneInter;

    @FXML
    private TabPane tabConsole;

    private ArrayList<Object> lex;
    private ArrayList<Object> sem;
    private Analise ana;
    private ObservableList<Object> obsListLex;
    private ObservableList<Object> obsListSem;
    private File file;
    private ObservableList obsCount;
    private int lineCount;
    private CodeArea codeArea;
    private CodeArea interArea;

    public void execute(String exp) {

        lex = ana.executeLex(exp);
        ana.executeSint(lex);
        obsListLex = FXCollections.observableArrayList(lex);
        tabelaLexica.setItems(obsListLex);
        //if (ana.erros()) {
            sem = ana.executeSem(lex);
            setConsole("\nAnalise Semântica");
            obsListSem = FXCollections.observableArrayList(sem);
            tabelaSemantica.setItems(obsListSem);
//        }else{
//            setConsole("\nAnalise Semântica não realizada");
//        }
    }

    @FXML
    void onClickExecute(ActionEvent event) {

        int index = tabCodigo.getSelectionModel().getSelectedIndex();

        switch (index) {
            case 0:
                tabConsole.getSelectionModel().select(0);
                execute(codeArea.getText());
                break;
            case 1:
                tabConsole.getSelectionModel().select(3);
                ana.executeInter(ana.tratarEntrada(interArea.getText()));
                break;
        }
        console.setText("");
        consoleSem.setText("");
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

    public void setConsole(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                console.appendText(message + "\n");
            }
        });

    }

    public void setConsoleSem(String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                consoleSem.appendText(message + "\n");
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
        consoleSem.setEditable(false);

        codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

        codeArea.replaceText("program correto;\n"
                + "int a;\n"
                + "boolean d, e, f;\n"
                + "real b, c;\n"
                + "\n"
                + "\n"
                + "procedure proc(var a1 : int);\n"
                + "int a;\n"
                + "begin\n"
                + "	a:=10;\n"
                + "	if (a1<a) then\n"
                + "		d:=true\n"
                + "end;\n"
                + "\n"
                + "begin\n"
                + "	a:=2;\n"
                + "	b:=10.5;\n"
                + "	c:=b+a;\n"
                + "	d:=true;\n"
                + "	e:=false;\n"
                + "	f:=true;\n"
                + "	read(a);\n"
                + "	write(b);\n"
                + "	if (d) then\n"
                + "	begin\n"
                + "		a:=20;\n"
                + "		b:=10*c;\n"
                + "		c:=a div b\n"
                + "	end;\n"
                + "	while (a>1) do\n"
                + "	begin\n"
                + "		if (a>10) then\n"
                + "			b:=2.0;\n"
                + "		a:=a-1\n"
                + "	end\n"
                + "end.");
        bordePaneCode.setCenter(new VirtualizedScrollPane<>(codeArea));

        interArea = new CodeArea();
        interArea.setParagraphGraphicFactory(LineNumberFactory.get(interArea));
        interArea.replaceText("INPP\n"
                + "AMEM 1\n"
                + "AMEM 1\n"
                + "LEIT\n"
                + "ARMZ 0\n"
                + "LEIT\n"
                + "ARMZ 1\n"
                + "CRVL 0\n"
                + "CRVL 1\n"
                + "CMMA\n"
                + "DSVF 14\n"
                + "CRVL 0\n"
                + "IMPR\n"
                + "IMPE\n"
                + "NADA\n"
                + "PARA");
        borderPaneInter.setCenter(new VirtualizedScrollPane<>(interArea));

        tabLexLexema.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        tabLexToken.setCellValueFactory(new PropertyValueFactory<>("token"));
        tabLexLinha.setCellValueFactory(new PropertyValueFactory<>("linha"));
        tabLexColunaInicial.setCellValueFactory(new PropertyValueFactory<>("coluna_inicial"));
        tabLexColunaFinal.setCellValueFactory(new PropertyValueFactory<>("coluna_final"));

        tabSemCadeia.setCellValueFactory(new Callback<CellDataFeatures<Simbolos, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Simbolos, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getLex().getLexema());
            }
        });

        tabSemToken.setCellValueFactory(new Callback<CellDataFeatures<Simbolos, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<Simbolos, String> param) {
                return new SimpleObjectProperty<>(param.getValue().getLex().getToken().name());
            }
        });
        tabSemCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));
        tabSemTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tabSemEndereco.setCellValueFactory(new PropertyValueFactory<>("endereco"));
        tabSemEscopo.setCellValueFactory(new PropertyValueFactory<>("escopo"));
        tabSemUtilizada.setCellValueFactory(new PropertyValueFactory<>("utilizada"));

        tabelaLexica.setRowFactory(row -> new TableRow<Object>() {
            @Override
            public void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);

                if (item instanceof Erro) {
                    setStyle(" -fx-background-color: red;"
                            + " -fx-text-fill: white;"
                            + "-fx-font-weight: bold; ");
                } else {
                    setStyle("");
                }
            }
        });
        ana = new Analise();

    }

}
