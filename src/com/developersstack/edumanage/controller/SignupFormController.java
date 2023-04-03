package com.developersstack.edumanage.controller;

import com.developersstack.edumanage.db.Database;
import com.developersstack.edumanage.model.User;
import com.developersstack.edumanage.util.security.PasswordManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SignupFormController {
    public AnchorPane context;
    public TextField txtFirstName;
    public PasswordField txtPassword;
    public TextField txtEmail;
    public TextField txtLastName;


    public void alreadyHaveAnAccountOnAction(ActionEvent actionEvent) throws IOException {
        setUi("LoginForm");
    }
    private void setUi(String location) throws IOException {
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(
                FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.centerOnScreen();
    }
    public void signUpOnAction(ActionEvent actionEvent) throws IOException {
        String email = txtEmail.getText().toLowerCase();
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String password = new PasswordManager().encrypt(txtPassword.getText().trim());
        User createUser = new User(firstName, lastName, email, password);
        try {
            boolean isSaved = signup(createUser);
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION, "Welcome!").show();
                setUi("LoginForm");
            }else{
                new Alert(Alert.AlertType.WARNING, "Try Again!").show();
            }
        }catch (SQLException | ClassNotFoundException e1){
            new Alert(Alert.AlertType.ERROR, e1.toString()).show();
        }

    }

    //================================
    private boolean signup(User user) throws ClassNotFoundException, SQLException {
        // load the driver
        Class.forName("com.mysql.cj.jdbc.Driver"); // com.mysql.jdbc.Driver(deprecate)
        // Create a Connection
        Connection connection =
        DriverManager.getConnection("jdbc:mysql://localhost:3306/lms3","root","1234");
        // write a SQl
        String sql ="INSERT INTO User VALUES ('"+user.getEmail()+"','"+user.getFirstName()+"','"+user.getLastName()+"','"+user.getPassword()+"')";
        // INSERT INTO user VALUES('h@gmail.com','hasika','sandaruwan','1234');
        // create statement
        Statement statement = connection.createStatement();
        // set sql into the statement and execute
        return statement.executeUpdate(sql)>0;
    }
}
