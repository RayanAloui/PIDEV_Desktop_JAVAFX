package visites.controllers;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import visites.entities.visiteurs;
import visites.services.VisiteursService;

public class AfficherVisiteur {

    @FXML
    private TableView<visiteurs> TableView;

    @FXML
    private TableColumn<visiteurs, String> adresse;

    @FXML
    private TableColumn<visiteurs, String> email;

    @FXML
    private TableColumn<visiteurs, String> nom;

    @FXML
    private TableColumn<visiteurs, String> prenom;

    @FXML
    private TableColumn<visiteurs, Integer> tel;

    @FXML
    void initialize(){
        VisiteursService vs=new VisiteursService();
        ObservableList<visiteurs> observableList= FXCollections.observableList(vs.getall());
        TableView.setItems(observableList);
        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        tel.setCellValueFactory(new PropertyValueFactory<>("tel"));
        adresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));


    }

}
