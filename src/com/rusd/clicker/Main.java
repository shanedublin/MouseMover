package com.rusd.clicker;


import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application{

	public static boolean runMouseThread = false;
	private TrayIcon trayIcon;
	
	public static void main(String[] args){
		launch(args);
	}
	
	public void start(Stage primaryStage){
		primaryStage.setTitle("Awesome Mouse Mover");
		MouseMove mm = new MouseMove();
		Text runningText = new Text("Stopped");
		runningText.setFont(Font.font(28));
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25,25,25,25));
		
		Button startButton = new Button();
		startButton.setText("Start");
		startButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new Thread(mm).start();
				startButton.setDisable(true);
				runningText.setText("Running");
				
			}
		});
		
		Button stopButton = new Button();
		stopButton.setText("Stop");
		stopButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mm.runThread = false;
				startButton.setDisable(false);
				runningText.setText("Stopped!");
			}
			
		});
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				hide(primaryStage);				
			}			
		});
		
//		primaryStage.maximizedProperty().addListener(new ChangeListener<Boolean>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				
//				
//			}
//		});
		Platform.setImplicitExit(false);
		
		StackPane root = new StackPane();
		root.getChildren().add(startButton);
		root.getChildren().add(stopButton);
		grid.add(runningText, 0, 0);
		GridPane.setColumnSpan(runningText, GridPane.REMAINING);
		grid.add(startButton, 0, 1);
		grid.add(stopButton, 1, 1);
		
		primaryStage.setScene(new Scene(grid,300,250));
		createTrayIcon(primaryStage);
		primaryStage.show();
		
		new Thread(mm).start();
		startButton.setDisable(true);
		runningText.setText("Running!");
		
		
	}
	
	
	public void createTrayIcon(final Stage stage){
		if(SystemTray.isSupported()){
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;
			
			try{
				image = Toolkit.getDefaultToolkit().getImage("Mouse Move");
			}catch (Exception e) {
				System.err.println("Error finding cursor" + e.getMessage());
			}
			
			final ActionListener closeListener = new ActionListener() {				
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Platform.exit();
					System.exit(0);					
				}
			};
			
			ActionListener showListener = new ActionListener() {				
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Platform.runLater(new Runnable() {						
						@Override
						public void run() {
							stage.show();
						}
					});
				}
			};
			
			PopupMenu popup = new PopupMenu();
			
			MenuItem showItem = new MenuItem("Show");
			showItem.addActionListener(showListener);
			popup.add(showItem);
			
			MenuItem closeItem = new MenuItem("Exit");
			closeItem.addActionListener(closeListener);
			popup.add(closeItem);
			
			
			trayIcon = new TrayIcon(image, "cursor.png",popup);
			trayIcon.addActionListener(showListener);
			trayIcon.setImageAutoSize(true);
			
			try {
				tray.add(trayIcon);
			} catch (Exception e) {
				System.err.println(e);
			}
			
		}else{
			System.out.println("System tray not support");
		}
	}
	
	private void hide(final Stage stage){
		Platform.runLater(new Runnable() {			
			@Override
			public void run() {
				if(SystemTray.isSupported()){
					stage.hide();
				}else{
					System.exit(0);
				}
			}
		});
	}
	
}
