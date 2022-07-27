package application;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;

public class controller implements Initializable {
	Connection con;
	PreparedStatement pst;
	int myIdx;
	int id;

	public void connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost/students registration", "root", "");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
		} catch (SQLException e) {
			// TODO: handle exception
		}
	}

	@FXML
	private Button addBtn;

	@FXML
	private TextField course;

	@FXML
	private TableColumn<Student, String> courseColmn;

	@FXML
	private Button dltBt;

	@FXML
	private TableColumn<Student, String> idColmn;

	@FXML
	private TextField mobile;

	@FXML
	private TableColumn<Student, String> mobileColmn;

	@FXML
	private TextField name;

	@FXML
	private TableColumn<Student, String> nameColmn;

	@FXML
	private TableView<Student> table;

	@FXML
	private Button updBt;

	@FXML
	void add(ActionEvent event) {

		String stname, mobile, course;
		stname = name.getText();

		mobile = this.mobile.getText();
		course = this.course.getText();
		try {
			pst = con.prepareStatement("insert into registration(Name,Mobile,Course)values(?,?,?)");
			pst.setString(1, stname);
			pst.setString(2, mobile);
			pst.setString(3, course);
			pst.executeUpdate();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("students registration");

			alert.setHeaderText("students registration");
			alert.setContentText("Record Added!");

			alert.showAndWait();

			table();

			name.setText("");
			this.mobile.setText("");
			this.course.setText("");
			name.requestFocus();
		} catch (SQLException ex) {
			Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void table() {
		connect();
		ObservableList<Student> students = FXCollections.observableArrayList();
		try {
			pst = con.prepareStatement("select ID,Name,Mobile,Course from registration");

			ResultSet rs = pst.executeQuery();
			{
				while (rs.next()) {
					Student st = new Student();
					st.setId(rs.getString("ID"));
					st.setName(rs.getString("Name"));
					st.setMobile(rs.getString("Mobile"));
					st.setCourse(rs.getString("Course"));
					students.add(st);
				}
			}
			table.setItems(students);
			idColmn.setCellValueFactory(f -> f.getValue().idProperty());
			nameColmn.setCellValueFactory(f -> f.getValue().nameProperty());
			mobileColmn.setCellValueFactory(f -> f.getValue().mobileProperty());
			courseColmn.setCellValueFactory(f -> f.getValue().courseProperty());

		}

		catch (SQLException ex) {
			Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
		}

		table.setRowFactory(tv -> {
			TableRow<Student> myRow = new TableRow<>();
			myRow.setOnMouseClicked(event -> {
				if (event.getClickCount() == 1 && (!myRow.isEmpty())) {
					myIdx = table.getSelectionModel().getSelectedIndex();
					id = Integer.parseInt(String.valueOf(table.getItems().get(myIdx).getId()));

					name.setText(table.getItems().get(myIdx).getName());
					this.mobile.setText(table.getItems().get(myIdx).getMobile());
					this.course.setText(table.getItems().get(myIdx).getCourse());

				}
			});
			return myRow;
		});

	}

	@FXML
	void delete(ActionEvent event) {
		myIdx = table.getSelectionModel().getSelectedIndex();
		id = Integer.parseInt(String.valueOf(table.getItems().get(myIdx).getId()));

		try {
			pst = con.prepareStatement("delete from registration where id = ? ");
			pst.setInt(1, id);
			pst.executeUpdate();

			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("students registration");

			alert.setHeaderText("students registration");
			alert.setContentText("Deleted!");

			alert.showAndWait();
			table();

		} catch (SQLException ex) {
			Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	void update(ActionEvent event) {

		String stname, mobile, course;

		myIdx = table.getSelectionModel().getSelectedIndex();
		id = Integer.parseInt(String.valueOf(table.getItems().get(myIdx).getId()));

		stname = name.getText();
		mobile = this.mobile.getText();
		course = this.course.getText();
		try {
			pst = con.prepareStatement("update registration set name = ?,mobile = ? ,course = ? where id = ? ");
			pst.setString(1, stname);
			pst.setString(2, mobile);
			pst.setString(3, course);
			pst.setInt(4, id);
			pst.executeUpdate();
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("students registration");

			alert.setHeaderText("students registration");
			alert.setContentText("Updated!");

			alert.showAndWait();
			table();

		} catch (SQLException ex) {
			Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		connect();
		table();

	}

}
