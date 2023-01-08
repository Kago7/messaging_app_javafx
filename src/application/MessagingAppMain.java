package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MessagingAppMain extends Application {

	// init vars
	static File message;
	static FileWriter out;
	static BufferedWriter buffwrite;
	static FileReader in;
	static BufferedReader buffread;
	TextField typeMsg;
	TextArea logs;

	public void start(Stage primaryStage) {
		try {
			//setup stage and scene
			Pane root = new Pane();
			Scene scene = new Scene(root, 400, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setTitle("Chat Test");
			
			// set window event for close x button
						primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
							public void handle(WindowEvent e) {
								// create the one possible alerts
								Alert alert = new Alert(AlertType.CONFIRMATION);
								
								//close alert setup
								alert.setContentText("Are you sure you want to exit?");
								alert.setTitle("Chat Test");
								alert.setHeaderText(null);
								alert.getButtonTypes().clear();
								alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
								Optional<ButtonType> result = alert.showAndWait();

								// check if user presses no/yes. if yes close file streams and output average
								// info. if no, return main screen
								if (result.get() == ButtonType.NO) {
									e.consume();
								} else {
									try {
										buffwrite.close();
										buffread.close();
										in.close();
										out.close();
									} catch (Exception e3) {
										System.out.println("cant close file streams");
									}
									
								}
							}
						});
			
			//setup two labels
			Label title = new Label("Chat Log");
			Label msg = new Label("Message");
			title.setFont(Font.font("Comic Sans MS",16));
			msg.setFont(Font.font("Comic Sans MS",16));
			title.setLayoutX(5);
			title.setLayoutY(5);
			msg.setLayoutX(5);
			msg.setLayoutY(500);
			
			//setup text area
			logs = new TextArea();
			logs.setEditable(false);
			logs.setWrapText(true);
			logs.setLayoutX(title.getLayoutX());
			logs.setLayoutY(title.getLayoutY()+35);
			logs.setPrefSize(scene.getWidth()-15, 450);
			
			//setup previous chat logs
			try {
				String temp;
				while ((temp = buffread.readLine()) != null) {
					logs.appendText(temp+"\n\n");
				}
			} catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
			
			//setup send button
			Button send = new Button("Send");
			send.setLayoutX(scene.getWidth()-50);
			send.setLayoutY(scene.getHeight()-35);
			send.setOnAction(e -> sent());
			
			//setup textfield
			typeMsg = new TextField();
			typeMsg.setLayoutX(msg.getLayoutX());
			typeMsg.setLayoutY(scene.getHeight()-35);
			typeMsg.setPrefSize(scene.getWidth()-75, send.getHeight());
			
			//get the children
			root.getChildren().addAll(title,msg,send,typeMsg,logs);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//method when text is sent
	public void sent() {
		
		//check if any text is entered first
		if (!typeMsg.getText().equals("")) {
			logs.appendText(typeMsg.getText()+"\n\n");
			
			try {
			buffwrite.write(typeMsg.getText());
			buffwrite.newLine();
			buffwrite.flush();
			}
			catch (IOException e) {
				System.out.println(e.getLocalizedMessage());
			}
		}
		
	}

	public static void main(String[] args) throws IOException {
		// init file readers and writers global
		message = new File("message.txt");
		if (!message.exists()) {
			message.createNewFile();
		}
		out = new FileWriter(message, true);
		buffwrite = new BufferedWriter(out);

		in = new FileReader(message);
		buffread = new BufferedReader(in);

		launch(args);
	}
}
