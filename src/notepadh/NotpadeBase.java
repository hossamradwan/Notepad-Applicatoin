package notepadh;


//import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public abstract class NotpadeBase extends BorderPane {

    protected final MenuBar menuBar;
    protected final Menu FileMenu;
    protected final MenuItem newItem;
    protected final MenuItem openItem;
    protected final MenuItem saveItem;
    protected final MenuItem exitItem;
    protected final Menu EditMenu;
    protected final MenuItem cutItem;
    protected final MenuItem copyItem;
    protected final MenuItem pasteItem;
    protected final MenuItem deleteItem;
    protected final MenuItem selectAllItem;
    protected final Menu helpMenu;
    protected final MenuItem aboutItem;
    protected final TextArea textArea;
    volatile boolean openFlag = false;
    File getOpenedFile;
    
    boolean newFlag = true;
    
    volatile boolean saveFlag = false;
    File getSavedFile;

    public NotpadeBase(Stage primary) {

        menuBar = new MenuBar();
        
        FileMenu = new Menu();
        
        newItem = new MenuItem();
        openItem = new MenuItem();
        saveItem = new MenuItem();
        exitItem = new MenuItem();
        
        EditMenu = new Menu();
        
        cutItem = new MenuItem();
        copyItem = new MenuItem();
        pasteItem = new MenuItem();
        deleteItem = new MenuItem();
        selectAllItem = new MenuItem();
        
        helpMenu = new Menu();
        
        aboutItem = new MenuItem();
        
        textArea = new TextArea();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(400.0);
        setPrefWidth(600.0);

        BorderPane.setAlignment(menuBar, javafx.geometry.Pos.CENTER);

        FileMenu.setMnemonicParsing(false);
        FileMenu.setText("File");

        newItem.setMnemonicParsing(false);
        newItem.setText("New");
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                newFlag = true;
                String sampleText = textArea.getText();
                File saveFile;
                if(openFlag == true)
                {
                    saveFile = getOpenedFile;
                    openFlag = false;
                    saveTextToFile(sampleText, saveFile);
                }
                else if (saveFlag == true)
                {
                   saveFile = getSavedFile;
                   saveFlag = false;
                   saveTextToFile(sampleText, saveFile);
                }
                else
                {
                    FileChooser saveFileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                    saveFileChooser.getExtensionFilters().add(extFilter);
                    saveFile = saveFileChooser.showSaveDialog(primary); 
                    saveTextToFile(sampleText, saveFile);
                }
                textArea.setText("");
            }
            
        });
        
        openItem.setMnemonicParsing(false);
        openItem.setText("Open");
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(new EventHandler<ActionEvent>()
        {
           public void handle(ActionEvent ev)
           {
               String sampleText = textArea.getText();
               File saveFile;
               if(openFlag == true)
                {
                    saveFile = getOpenedFile;
                    openFlag = false;
                    saveTextToFile(sampleText, saveFile);
                }
               else if(newFlag == true  && !sampleText.isEmpty())
               {
                    newFlag = false;
                    FileChooser saveFileChooser = new FileChooser();
                    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                    saveFileChooser.getExtensionFilters().add(extFilter);
                    saveFile = saveFileChooser.showSaveDialog(primary); 
                    saveTextToFile(sampleText, saveFile);
                   
               }
               FileChooser openFileChooser = new FileChooser();
               File openFile = openFileChooser.showOpenDialog(primary);
               openFlag = true;
               getOpenedFile = openFile;
               if(openFile != null)
               {
                   try 
                   {
                       FileInputStream of = new FileInputStream(openFile);
                       int openFileSize = (int) openFile.length();
                       byte[] openFileData = new byte[openFileSize];
                       of.read(openFileData);
                       textArea.setText(new String (openFileData));
                   } 
                   catch (FileNotFoundException ex) 
                   {
                       Logger.getLogger(NotpadeBase.class.getName()).log(Level.SEVERE, null, ex);
                   } catch (IOException ex) {
                       Logger.getLogger(NotpadeBase.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
           }
        });

        saveItem.setMnemonicParsing(false);
        saveItem.setText("Save");
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(new EventHandler<ActionEvent>()
        {
           public void handle(ActionEvent ev)
           {
                     
                FileChooser saveFileChooser = new FileChooser();
                //Set extension filter for text files
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                saveFileChooser.getExtensionFilters().add(extFilter);
                //Show save file dialog
                File saveFile;
                if(openFlag == true)
                {
                    saveFile = getOpenedFile;
                    openFlag = false;
                }
                else if (saveFlag == true)
                {
                   saveFile = getSavedFile;
                   saveFlag = false;
                }
                else
                {
                    saveFile = saveFileChooser.showSaveDialog(primary); 
                    getSavedFile = saveFile;
                }
                
                String sampleText = textArea.getText();
                //String sampleText = "hello";
                if (saveFile != null) {
                    saveTextToFile(sampleText, saveFile);
                }
                saveFlag = true;

           }
        });

        exitItem.setMnemonicParsing(false);
        exitItem.setText("Exit");
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                File saveFile;
                String sampleText = textArea.getText();
                if(openFlag == true)
                {
                    saveFile = getOpenedFile;
                    openFlag = false;
                    saveTextToFile(sampleText, saveFile);
                }
                else if (saveFlag == true)
                {
                   saveFile = getSavedFile;
                   saveFlag = false;
                   saveTextToFile(sampleText, saveFile);
                }
                else if(newFlag == true && !sampleText.isEmpty())
                {
                     newFlag = false;
                     FileChooser saveFileChooser = new FileChooser();
                     FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
                     saveFileChooser.getExtensionFilters().add(extFilter);
                     saveFile = saveFileChooser.showSaveDialog(primary); 
                     if(saveFile != null)
                     {
                         saveTextToFile(sampleText, saveFile);  
                     } 
                }
                Platform.exit();
            } 
        });
        

        EditMenu.setMnemonicParsing(false);
        EditMenu.setText("Edit");

        cutItem.setMnemonicParsing(false);
        cutItem.setText("Cut");
        cutItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                String selectedText =  textArea.getSelectedText();
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(selectedText);
                clipboard.setContent(content);
                textArea.deleteText(textArea.getSelection()); 
            } 
        });

        copyItem.setMnemonicParsing(false);
        copyItem.setText("Copy");
        copyItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                String selectedText =  textArea.getSelectedText();
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(selectedText);
                clipboard.setContent(content);
            } 
        });

        pasteItem.setMnemonicParsing(false);
        pasteItem.setText("Paste");
        pasteItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                java.awt.datatransfer.Clipboard c =Toolkit.getDefaultToolkit().getSystemClipboard();
                try 
                {
                    if(textArea.getSelection() != null)
                    {
                        textArea.deleteText(textArea.getSelection());  
                    }
                    int caretPosition = textArea.getCaretPosition();
                    String str = (String) c.getData(DataFlavor.stringFlavor);
                    textArea.insertText(caretPosition, str);
                } 
                catch (UnsupportedFlavorException ex) {
                    Logger.getLogger(NotpadeBase.class.getName()).log(Level.SEVERE, null, ex);
                } 
                catch (IOException ex) {
                    Logger.getLogger(NotpadeBase.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        });

        deleteItem.setMnemonicParsing(false);
        deleteItem.setText("Delete");
        deleteItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                textArea.deleteText(textArea.getSelection());   
            } 
        });

        selectAllItem.setMnemonicParsing(false);
        selectAllItem.setText("Select All");
        selectAllItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent ev)
            {
                textArea.selectAll();
            } 
        });
   
        helpMenu.setMnemonicParsing(false);
        helpMenu.setText("Help");

        aboutItem.setMnemonicParsing(false);
        aboutItem.setText("About Notepad+H");
        aboutItem.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
        Alert a = new Alert(AlertType.NONE); 
        aboutItem.setOnAction(new EventHandler<ActionEvent>()
        {
            public void handle(ActionEvent e) 
            { 
                a.setAlertType(AlertType.INFORMATION); 
                a.setContentText("This is Notepad+H version 1\nDeveloped by Hossam Radwan\nIOT Applications Developer"); 
                ImageView myImage = new ImageView(new Image(getClass().getResourceAsStream("hello.png"))); 
                a.setGraphic(myImage);
                

                a.show(); 
            }  
        });
        setTop(menuBar);

        BorderPane.setAlignment(textArea, javafx.geometry.Pos.CENTER);
        textArea.setPrefHeight(200.0);
        textArea.setPrefWidth(200.0);
        setCenter(textArea);

        FileMenu.getItems().add(newItem);
        FileMenu.getItems().add(openItem);
        FileMenu.getItems().add(saveItem);
        FileMenu.getItems().add(exitItem);
        menuBar.getMenus().add(FileMenu);
        EditMenu.getItems().add(cutItem);
        EditMenu.getItems().add(copyItem);
        EditMenu.getItems().add(pasteItem);
        EditMenu.getItems().add(deleteItem);
        EditMenu.getItems().add(selectAllItem);
        menuBar.getMenus().add(EditMenu);
        helpMenu.getItems().add(aboutItem);
        menuBar.getMenus().add(helpMenu);

    }
    private void saveTextToFile(String content, File file) 
    {
        try 
        {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(content);
            writer.close();
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(NotpadeBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
