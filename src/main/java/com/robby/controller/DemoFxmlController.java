package com.robby.controller;

import com.robby.dao.UserDaoImpl;
import com.robby.entity.User;
import com.robby.util.ViewUtil;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Robby
 */
public class DemoFxmlController implements Initializable, Observer {

    @FXML
    private Button btnDelete;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<User, String> colId;
    @FXML
    private TableColumn<User, String> colName;
    @FXML
    private TableColumn<User, String> colRole;
    private User selectedUser;
    @FXML
    private TableView<User> tableData;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtRole;

    private UserDaoImpl userDao;
    private ObservableList<User> users;

    @FXML
    private void deleteAction(ActionEvent event) {
        if (selectedUser != null) {
            Alert alertError = new Alert(Alert.AlertType.CONFIRMATION);
            alertError.setContentText("Are you sure want to delete?");
            alertError.setTitle("Confirmation");
            Optional<ButtonType> confirmation = alertError.showAndWait();
            if (confirmation.get() == ButtonType.OK) {
                getUserDao().deleteUser(selectedUser);
                btnDelete.setDisable(true);
                btnUpdate.setDisable(true);
                btnSave.setDisable(false);
            }
        } else {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setContentText("Please select data from table");
            alertError.setTitle("Error");
            alertError.show();
        }
    }

    public UserDaoImpl getUserDao() {
        if (userDao == null) {
            userDao = new UserDaoImpl();
            userDao.addObserver(this);
        }
        return userDao;
    }

    public ObservableList<User> getUsers() {
        if (users == null) {
            users = FXCollections.observableArrayList();
            users.addAll(getUserDao().getAllUsers());
        }
        return users;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableData.setItems(getUsers());
        colId.setCellValueFactory(data -> {
            User user = (User) data.getValue();
            return new SimpleStringProperty(user.getM_id());
        });
        colName.setCellValueFactory(data -> {
            User user = (User) data.getValue();
            return new SimpleStringProperty(user.getName());
        });
        colRole.setCellValueFactory(data -> {
            User user = (User) data.getValue();
            return new SimpleStringProperty(user.getRole());
        });
        tableData.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override
            public void changed(
                    ObservableValue<? extends User> observable, User oldValue, User newValue) {
                selectedUser = newValue;
                if (selectedUser != null) {
                    txtId.setText(selectedUser.getM_id());
                    txtName.setText(selectedUser.getName());
                    txtRole.setText(selectedUser.getRole());
                    btnDelete.setDisable(false);
                    btnUpdate.setDisable(false);
                    btnSave.setDisable(true);
                }
            }
        });
    }

    @FXML
    private void resetAction(ActionEvent event) {
        txtId.clear();
        txtName.clear();
        txtRole.clear();
        btnDelete.setDisable(true);
        btnUpdate.setDisable(true);
        btnSave.setDisable(false);
    }

    @FXML
    private void saveAction(ActionEvent event) {
        if (!ViewUtil.isEmptyField(txtId, txtName, txtRole)) {
            User user = new User();
            user.setM_id(txtId.getText().trim());
            user.setName(txtName.getText().trim());
            user.setRole(txtRole.getText().trim());
            getUserDao().addUser(user);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all field");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof UserDaoImpl) {
            getUsers().clear();
            getUsers().addAll(getUserDao().getAllUsers());
            txtId.clear();
            txtName.clear();
            txtRole.clear();
        }
    }

    @FXML
    private void updateAction(ActionEvent event) {
        if (selectedUser != null && !ViewUtil.isEmptyField(txtId, txtName, txtRole)) {
            selectedUser.setM_id(txtId.getText().trim());
            selectedUser.setName(txtName.getText().trim());
            selectedUser.setRole(txtRole.getText().trim());
            getUserDao().updateUser(selectedUser);
            selectedUser = null;
            btnDelete.setDisable(true);
            btnUpdate.setDisable(true);
            btnSave.setDisable(false);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You are not cleard to update/ You must fill all field");
            alert.setTitle("Error");
            alert.show();
        }
    }

}
