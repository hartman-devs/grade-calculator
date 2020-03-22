/**
 * This file is part of GradeCalculator.
 *
 * GradeCalculator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GradeCalculator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GradeCalculator. If not, see <https://www.gnu.org/licenses/>.
 */

package gradecalculator;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.*;
import java.math.BigDecimal;

/**
 * FXML Controller class managing the application's control.
 */
public class Controller {
    //<editor-fold desc="Properties of the Controller class.">
    @FXML private TextField examWeights;
    @FXML private TextField examGrades;
    @FXML private TextField examGradeResult;
    @FXML private TextField homeworkWeights;
    @FXML private TextField homeworkGrades;
    @FXML private TextField homeworkGradeResult;
    @FXML private TextField quizWeights;
    @FXML private TextField quizGrades;
    @FXML private TextField quizGradeResult;
    @FXML private TextField labWeights;
    @FXML private TextField labGrades;
    @FXML private TextField labGradeResult;
    @FXML private TextField inclassWorkWeights;
    @FXML private TextField inclassWorkGrades;
    @FXML private TextField inclassWorkResult;
    @FXML private TextField miscWorkWeights;
    @FXML private TextField miscWorkGrades;
    @FXML private TextField miscGradeResult;
    @FXML private TextField finalGradeResult;
    @FXML private TextField weightPossible;
    @FXML private AnchorPane mainAnchorPane;
    @FXML private AnchorPane weightAnchorPane;
    @FXML private AnchorPane gradeAnchorPane;
    @FXML private AnchorPane resultsAnchorPane;
    @FXML private Button calculateGradeButton;
          private HostServices hostServices;
    //</editor-fold>

    /**
     * FXML method used to initialize the controller for the model/view FXML file. This is run at the start of the FXML
     * window, and all changes made are persistent across the life of the window. This method sets the ChangeListeners
     * to limit text input in certain fields.
     */
    @FXML
    private void initialize()
    {
        /*
         * Add a ChangeListener for all TextField objects under the Weights pane. This listener prevents the user
         * from entering certain characters that would not be acceptable for a numeric field.
         * The characters allowed are: Period, Comma, Space, 0-9.
         */
        for(Node node : weightAnchorPane.getChildren())
        {
            if(node instanceof TextField)
            {
                ((TextField) node).textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(!(newValue.matches("[0-9., ]")))
                        {
                            ((TextField) node).setText(newValue.replaceAll("[^0-9., ]", ""));
                        }//end if
                    }//end changed(ObservableValue<>, String, String):void
                });//end ChangeListener<String>()
            }//end if
        }//end for

        /*
         * Add a ChangeListener for all TextField objects under the Grades pane. This listener prevents the user
         * from entering certain characters that would not be acceptable for a numeric field.
         * The characters allowed are: Period, Comma, Space, 0-9.
         */
        for(Node node : gradeAnchorPane.getChildren())
        {
            if(node instanceof TextField)
            {
                ((TextField) node).textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(!(newValue.matches("[0-9., ]")))
                        {
                            ((TextField) node).setText(newValue.replaceAll("[^0-9., ]", ""));
                        }//end if
                    }//end changed(ObservableValue<>, String, String):void
                });//end ChangeListener<String>()
            }//end if
        }//end for

        weightPossible.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!(newValue.matches("[0-9.]")))
                {
                    weightPossible.setText(newValue.replaceAll("[^0-9.]", ""));
                }//end if
            }//end changed(ObservableValue<>, String, String):void
        });//end ChangeListener<String>()
    }//end initialize():void

    /**
     * FXML method handling the action occurring when the user presses the "Calculate Grade" button. This method
     * handles the event by parsing user input in each text field and calculating the final grade, as well as the
     * completed weight in each category, by calling sub-methods specialized in certain actions.
     */
    @FXML
    protected void handleCGButtonClickAction()
    {
        String[] examWeights = this.examWeights.getText().replaceAll(" ", "").split(",");
        String[] examGrades = this.examGrades.getText().replaceAll(" ", "").split(",");
        String[] homeworkWeights = this.homeworkWeights.getText().replaceAll(" ", "").split(",");
        String[] homeworkGrades = this.homeworkGrades.getText().replaceAll(" ", "").split(",");
        String[] quizWeights = this.quizWeights.getText().replaceAll(" ", "").split(",");
        String[] quizGrades = this.quizGrades.getText().replaceAll(" ", "").split(",");
        String[] labWeights = this.labWeights.getText().replaceAll(" ", "").split(",");
        String[] labGrades = this.labGrades.getText().replaceAll(" ", "").split(",");
        String[] inclassWorkWeights = this.inclassWorkWeights.getText().replaceAll(" ", "").split(",");
        String[] inclassWorkGrades = this.inclassWorkGrades.getText().replaceAll(" ", "").split(",");
        String[] miscWorkWeights = this.miscWorkWeights.getText().replaceAll(" ", "").split(",");
        String[] miscWorkGrades = this.miscWorkGrades.getText().replaceAll(" ", "").split(",");
        BigDecimal errorValue = new BigDecimal(-1);
        BigDecimal totalWeight = BigDecimal.ZERO;
        BigDecimal finalGrade = BigDecimal.ZERO;

        for(Node node : weightAnchorPane.getChildren())
        {
            if(node instanceof TextField)
            {
                if(!((TextField) node).getText().matches("([\\d]*(\\.[\\d]+)?,[ ]?)*([\\d]*(\\.[\\d]+)?)"))
                {
                    anErrorOccurred("ERROR: Invalid input under \"Weights\" tab!");
                    return;
                }//end if
            }//end if
        }//end for

        for(Node node : gradeAnchorPane.getChildren())
        {
            if(node instanceof TextField)
            {
                if(!((TextField) node).getText().matches("([\\d]*(\\.[\\d]+)?,[ ]?)*([\\d]*(\\.[\\d]+)?)"))
                {
                    anErrorOccurred("ERROR: Invalid input under \"Grades\" tab!");
                    return;
                }//end if
            }//end if
        }//end for

        totalWeight = calculateTotalWeightEntered(examWeights, homeworkWeights, quizWeights, labWeights, inclassWorkWeights, miscWorkWeights);

        if(totalWeight.compareTo((new BigDecimal(weightPossible.getText()).divide(new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP))) != 0)
        {
            anErrorOccurred("ERROR: Weights entered do not equal " + weightPossible.getText() + "%!");
            return;
        }//end if

        BigDecimal examGrade = calculateGrade(examWeights, examGrades, "exam");
        if(examGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        BigDecimal homeworkGrade = calculateGrade(homeworkWeights, homeworkGrades, "homework");
        if(homeworkGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        BigDecimal quizGrade = calculateGrade(quizWeights, quizGrades, "quiz");
        if(quizGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        BigDecimal labGrade = calculateGrade(labWeights, labGrades, "lab");
        if(labGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        BigDecimal inclassWorkGrade = calculateGrade(inclassWorkWeights, inclassWorkGrades, "in-class");
        if(inclassWorkGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        BigDecimal miscWorkGrade = calculateGrade(miscWorkWeights, miscWorkGrades, "misc.");
        if(miscWorkGrade.compareTo(errorValue) <= 0)
        {
            return;
        }//end if

        finalGrade = examGrade.add(homeworkGrade).add(quizGrade).add(labGrade).add(inclassWorkGrade).add(miscWorkGrade);
        this.finalGradeResult.setText(finalGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.miscGradeResult.setText(miscWorkGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.inclassWorkResult.setText(inclassWorkGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.labGradeResult.setText(labGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.quizGradeResult.setText(quizGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.homeworkGradeResult.setText(homeworkGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
        this.examGradeResult.setText(examGrade.setScale(1, BigDecimal.ROUND_HALF_UP).toString());
    }//end handleButtonClickAction():void

    /**
     * FXML method handling the event that occurs when the user clicks the "About" menu item, by showing an "About"
     * dialog to the user.
     */
    @FXML
    private void handleAboutClickAction()
    {
        Hyperlink email = new Hyperlink();
        email.setText("hartman.devs@gmail.com");
        email.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                hostServices.showDocument("mailto:hartman.devs@gmail.com");
            }//end handle(ActionEvent):void
        });//end EventHandler<>

        TextFlow flow = new TextFlow(
                new Text("Thank you for using the Grade Calculator application, developed by Logan Hartman.\n" +
                         "This application is provided to you, the user, without any warranty, implied or otherwise.\n\n" +
                         "For any questions or concerns, please contact Logan Hartman at "),
                         email
        );

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().contentProperty().set(flow);
        alert.setTitle("About the Grade Calculator");
        alert.setHeaderText("About the Grade Calculator Application");
        alert.showAndWait();
    }//end handleAboutClickAction():void

    /**
     * Resets the scene back to its initial state on user request.
     */
    @FXML
    private void handleNewClickAction()
    {
        try
        {
            mainAnchorPane.getScene().setRoot(FXMLLoader.load(getClass().getResource("../../resources/fxml/application.fxml")));
        }//end try
        catch(Exception e)
        {
            anErrorOccurred("ERROR: Unable to perform requested action.");
        }//end catch(Exception)
    }//end handleNewClickAction():void

    /**
     * Reads data from a user selected file and attempts to set all data fields to the read data.
     */
    @FXML
    private void handleOpenClickAction()
    {
        File file = getFileToOpen();

        if(file != null)
        {
            readDataFromFile(file);
        }//end if
    }//end handleOpenClickAction():void

    /**
     * Writes data to a user selected file and attempts to write all current data fields containing user data.
     */
    @FXML
    private void handleSaveClickAction()
    {
        File file = getFileToSave();

        if(file != null)
        {
            writeDataToFile(file);
        }//end if
    }//end handleSaveClickAction()

    /**
     * Closes the application entirely on user request.
     */
    @FXML
    private void handleExitClickAction()
    {
        Platform.exit();
        System.exit(0);
    }//end handleExitClickAction():void

    /**
     * This method calculates the weighted value of a particular grade type when given the corresponding weights,
     * grades, and the category of grade being calculated.
     *
     * @param weights String[]; The weights corresponding with the grade type.
     * @param grades String[]; The grades to use when calculating the earned weight.
     * @param type String; The category of grade being calculated (used internally for error messages).
     * @return BigDecimal; Calculated grade if valid parameters, -1 if invalid parameters or error occurred.
     */
    private BigDecimal calculateGrade(String[] weights, String[] grades, String type)
    {
        BigDecimal grade = BigDecimal.ZERO;

        /*
         * Calculate the grade(s) and weight(s).
         * If there are more weights than grades, show an error (one weight is associated with 1+ grades).
         * If there is more than one weight, apply each weight to a single grade.
         * If there are more grades than exam, and there is a single exam weight, average the grades.
         * If there are more grades than exam, and there is more than one weight, show an error (1+
         *     weights are associated with 1+ grades, but the number of weights must equal the number of grades).
         */
        if(weights.length == 0 && grades.length == 0)
        {
            grade = BigDecimal.ZERO;
        }//end if
        else
        {
            if(weights.length == 0 || grades.length == 0)
            {
                if(weights.length == 0)
                {
                    anErrorOccurred("ERROR: " + type + " grade values were given, but no " + type + " weight values!");
                    grade = new BigDecimal(-1);
                }//end if
                else
                {
                    anErrorOccurred("ERROR: " + type + " weight values were given, but no " + type + " grade values!");
                    grade = new BigDecimal(-1);
                }//end else
            }//end if
            else if(weights.length > grades.length)
            {
                anErrorOccurred("ERROR: There cannot be more " + type + " weights than " + type + " grades!");
                grade = new BigDecimal(-1);
            }//end else...if
            else if(weights.length < grades.length)
            {
                if(weights.length == 1)
                {
                    for(String value : grades)
                    {
                        if(value.matches("[0-9]*[.]?[0-9]*"))
                        {
                            if(value != null && !value.isEmpty())
                            {
                                grade = grade.add(new BigDecimal(value));
                            }//end if
                        }//end if
                        else
                        {
                            anErrorOccurred("ERROR: " + value + " is not a numeric value!");
                            grade = new BigDecimal(-1);
                            break;
                        }//end else
                    }//end for

                    grade = grade.divide(new BigDecimal(grades.length), 10, BigDecimal.ROUND_HALF_UP);

                    if(weights[0].matches("[0-9]*[.]?[0-9]*"))
                    {
                        if(weights[0] != null && !weights[0].isEmpty())
                        {
                            grade = grade.multiply(new BigDecimal(weights[0]));
                        }//end if
                    }//end if
                    else
                    {
                        anErrorOccurred("ERROR: " + weights[0] + " is not a numeric value!");
                        grade = new BigDecimal(-1);
                    }//end else
                }//end if
                else
                {
                    anErrorOccurred("ERROR: If using more than one weight, number of weights must match number of grades!");
                    grade = new BigDecimal(-1);
                }//end else
            }//end else...if
            else if(weights.length == grades.length)
            {
                for(int i = 0; i < weights.length; i++)
                {
                    if(weights[i].matches("[0-9]*[.]?[0-9]*"))
                    {
                        if(grades[i].matches("[0-9]*[.]?[0-9]*"))
                        {
                            if(grades[i] != null && !grades[i].isEmpty() && weights[i] != null && !weights[i].isEmpty())
                            {
                                grade = new BigDecimal(grades[i]).multiply(new BigDecimal(weights[i])).add(grade);
                            }//end if
                        }//end if
                        else
                        {
                            anErrorOccurred("ERROR: " + grades[i] + " is not a numeric value!");
                            grade = new BigDecimal(-1);
                        }//end else
                    }//end if
                    else
                    {
                        anErrorOccurred("ERROR: " + weights[0] + " is not a numeric value!");
                        grade = new BigDecimal(-1);
                    }//end else
                }//end for
            }//end else...if
            else
            {
                anErrorOccurred("ERROR: UNKNOWN ERROR OCCURRED");
                grade = new BigDecimal(-1);
            }//end else
        }//end else

        return grade;
    }//end calculateGrade(String[], String[], String):BigDecimal

    /**
     * This method calculates the total weight entered by the user and returns it to the caller.
     *
     * @param weightArrays Array(String[]); Variable length argument containing all weight arrays for each grade type.
     * @return BigDecimal; Total weight calculated to compare against expected.
     */
    private BigDecimal calculateTotalWeightEntered(String[] ... weightArrays)
    {
        BigDecimal totalWeight = BigDecimal.ZERO;

        if(weightArrays.length != 0)
        {
            for (String[] weightSet : weightArrays)
            {
                for (String weight : weightSet)
                {
                    if(weight != null && !weight.isEmpty())
                    {
                        totalWeight = totalWeight.add(new BigDecimal(weight));
                    }//end if
                }//end for
            }//end for
        }//end if
        else
        {
            totalWeight = BigDecimal.ZERO;
        }//end else

        return totalWeight;
    }//end calculateTotalWeightEntered(String[] ... weightArrays):BigDecimal

    /**
     * Requests the user select a file to save data to, and returns it to the caller.
     *
     * @return File, File selected by user.
     */
    private File getFileToSave()
    {
        File file;
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        chooser.setTitle("Create/Select a File to Save");
        file = chooser.showSaveDialog(mainAnchorPane.getScene().getWindow());

        return file;
    }//end getFileToSave():File

    /**
     * Requests the user select a file to open, and returns it to the caller.
     *
     * @return File, File selected by user.
     */
    private File getFileToOpen()
    {
        File file;
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));
        chooser.setTitle("Select a File to Open");
        chooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("All Files", "*.*"),
            new FileChooser.ExtensionFilter("Text Files", "*.txt, *.text")
        );
        file = chooser.showOpenDialog(mainAnchorPane.getScene().getWindow());

        return file;
    }//end getFileToOpen():File

    /**
     * Writes the data currently in all text fields to a data file.
     *
     * @param fileToWrite File, File to write to.
     */
    private void writeDataToFile(File fileToWrite)
    {
        BufferedWriter writer = null;
        try
        {
            writer = new BufferedWriter(new FileWriter(fileToWrite));

            writer.write(this.weightPossible.getText() + "\n");
            writer.write(this.examWeights.getText() + "\n");
            writer.write(this.examGrades.getText() + "\n");
            writer.write(this.homeworkWeights.getText() + "\n");
            writer.write(this.homeworkGrades.getText() + "\n");
            writer.write(this.labWeights.getText() + "\n");
            writer.write(this.labGrades.getText() + "\n");
            writer.write(this.quizWeights.getText() + "\n");
            writer.write(this.quizGrades.getText() + "\n");
            writer.write(this.inclassWorkWeights.getText() + "\n");
            writer.write(this.inclassWorkGrades.getText() + "\n");
            writer.write(this.miscWorkWeights.getText() + "\n");
            writer.write(this.miscWorkGrades.getText() + "\n");
        }//end try
        catch(Exception e)
        {
            anErrorOccurred("ERROR: File could not be written to correctly.");
        }//end catch(Exception)
        finally
        {
            try
            {
                if(writer != null)
                {
                    writer.close();
                }//end if
            }//end try
            catch(Exception ex)
            {
                return;
            }//end catch(Exception)
        }//end finally
    }//end writeDataToFile(File):void

    /**
     * Reads the data in a data file and sets the value of all text fields correspondingly.
     *
     * @param fileToRead File, File to read from.
     */
    private void readDataFromFile(File fileToRead)
    {
        BufferedReader reader = null;
        try
        {
            reader = new BufferedReader(new FileReader(fileToRead));

            this.weightPossible.setText(reader.readLine());
            this.examWeights.setText(reader.readLine());
            this.examGrades.setText(reader.readLine());
            this.homeworkWeights.setText(reader.readLine());
            this.homeworkGrades.setText(reader.readLine());
            this.labWeights.setText(reader.readLine());
            this.labGrades.setText(reader.readLine());
            this.quizWeights.setText(reader.readLine());
            this.quizGrades.setText(reader.readLine());
            this.inclassWorkWeights.setText(reader.readLine());
            this.inclassWorkGrades.setText(reader.readLine());
            this.miscWorkWeights.setText(reader.readLine());
            this.miscWorkGrades.setText(reader.readLine());
        }//end try
        catch(Exception e)
        {
            anErrorOccurred("ERROR: File could not be loaded correctly. It may have been moved, deleted, or it may be corrupt.");
        }//end catch(Exception)
        finally
        {
            try
            {
                if(reader != null)
                {
                    reader.close();
                }//end if
            }//end try
            catch(Exception ex)
            {
                return;
            }//end catch(Exception)
        }//end finally
    }//end readDataFromFile(File):void

    /**
     * Shows an error alert with the specified message.
     *
     * @param messageText String; Text to use in the error message.
     */
    private void showErrorAlert(String messageText)
    {
        Alert alert;

        alert = new Alert(Alert.AlertType.ERROR, messageText);
        alert.showAndWait();
    }//end showErrorAlert(String):void

    /**
     * Wrapper for the showErrorAlert method, setting all results text fields to read "SEE ALERT" to notify the user
     * that an error occurred and could not be recovered from without correction.
     *
     * @param messageText
     */
    private void anErrorOccurred(String messageText)
    {
        for(Node node : resultsAnchorPane.getChildren())
        {
            if(node instanceof TextField)
            {
                ((TextField) node).setText("ERROR: SEE ALERT.");
            }//end if
        }//end for

        showErrorAlert(messageText);
    }//end anErrorOccurred(String):void

    /**
     * Sets the HostServices object (from the Main Application) for use in opening the user's email client.
     *
     * @param hostServices
     */
    public void setHostServices(HostServices hostServices)
    {
        this.hostServices = hostServices;
    }//end setHostServices(HostServices):void
}//end Controller.class
