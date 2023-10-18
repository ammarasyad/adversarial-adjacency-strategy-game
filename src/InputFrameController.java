import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The InputFrameController class.  It controls input from the users and validates it.
 * If validation is successful, the Adjacency game screen will pop up in a different window.
 *
 * @author Jedid Ahn
 */
public class InputFrameController {

    // public CheckBox isBotFirst;
    @FXML
    private TextField player1;

    @FXML
    private TextField player2;

    @FXML
    private ComboBox<String> numberOfRounds;

    @FXML
    private ComboBox<String> playerMode1;

    @FXML
    private ComboBox<String> playerMode2;


    /**
     * Initialize the dropdown ComboBox with a list of items that are allowed to be selected.
     * Select the first item in the list as the default value of the dropdown.
     */
    @FXML
    private void initialize() {
        ObservableList<String> numberOfRoundsDropdown = FXCollections.observableArrayList(
                "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28");
        this.numberOfRounds.setItems(numberOfRoundsDropdown);
        this.numberOfRounds.getSelectionModel().select(0);

        ObservableList<String> playerMode1Dropdown = FXCollections.observableArrayList(
                "Human", "Minimax Bot", "Local Search Bot", "Genetic Search Bot");
        this.playerMode1.setItems(playerMode1Dropdown);
        this.playerMode1.getSelectionModel().select(0);

        ObservableList<String> playerMode2Dropdown = FXCollections.observableArrayList(
                "Human", "Minimax Bot", "Local Search Bot", "Genetic Search Bot");
        this.playerMode2.setItems(playerMode2Dropdown);
        this.playerMode2.getSelectionModel().select(1);
    }


    /**
     * Reset player1 and player2 text fields and reset numberOfRounds dropdown to default value
     * if reset button is clicked.
     */
    @FXML
    private void reset() {
        this.player1.setText("");
        this.player2.setText("");
        this.numberOfRounds.getSelectionModel().select(0);
        this.playerMode1.getSelectionModel().select(0);
        this.playerMode2.getSelectionModel().select(1);
    }


    /**
     * Open OutputFrame controlled by OutputFrameController if play button is clicked and
     * all input have been successfully validated.
     *
     * @throws IOException To load the FXMLLoader to open the Adjacency game screen (output screen).
     */
    @FXML
    private void play() throws IOException {
        if (this.isInputFieldValidated()) {
            // Close primary stage/input frame.
            Stage primaryStage = (Stage) this.player1.getScene().getWindow();
            primaryStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("OutputFrame.fxml"));
            Parent root = loader.load();

            // Open the new frame.
            Stage secondaryStage = new Stage();
            secondaryStage.setTitle("Game Board Display");
            secondaryStage.setScene(new Scene(root));
            secondaryStage.setResizable(true);
            secondaryStage.show();

            // TODO : wait for render here


            // Get controller of output frame and pass input including player names and number of rounds chosen.
            OutputFrameController outputFC = loader.getController();

            // outputFC.getInput(this.player1.getText(), this.player2.getText(), this.numberOfRounds.getValue(), this.isBotFirst.isSelected(), this.playerMode1.getValue(), this.playerMode2.getValue());
            // force isBotFirst to be false
            new Thread(() -> outputFC.getInput(player1.getText(), player2.getText(), numberOfRounds.getValue(), false, playerMode1.getValue(), playerMode2.getValue())).start();
        }
    }


    /**
     * Return whether all input fields have been successfully validated or not.
     *
     * @return boolean
     */
    private boolean isInputFieldValidated() {
        String playerX = this.player1.getText();
        String playerO = this.player2.getText();
        String roundNumber = this.numberOfRounds.getValue();

        if (playerX.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Player 1 name is blank.").showAndWait();
            return false;
        }

        if (playerO.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Player 2 name is blank.").showAndWait();
            return false;
        }

        if (playerX.equals(playerO)) {
            new Alert(Alert.AlertType.ERROR, "Player 1 and Player 2 cannot have the same name.").showAndWait();
            return false;
        }

        if (roundNumber.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Number of rounds dropdown menu is blank.").showAndWait();
            return false;
        }

        return true;
    }
}