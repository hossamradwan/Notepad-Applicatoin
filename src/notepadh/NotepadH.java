/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepadh;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author hradw
 */
public class NotepadH extends Application 
{
    @Override
    public void start(Stage primaryStage) 
    {        
        NotpadeBase root = new NotpadeBase(primaryStage) {};
        
        
        Scene scene = new Scene(root, 600, 600);
        primaryStage.setTitle("Notepad+H");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) 
    {
        launch(args);
    }
}
