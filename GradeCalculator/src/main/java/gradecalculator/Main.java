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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Primary class hosting the application start point.
 */
public class Main extends Application {
    /**
     * FXML application, holding secondary control after the method is called by main().
     *
     * @param primaryStage Stage, Reference to the primary stage of the application.
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/application.fxml"));
        Parent root = loader.load();

        primaryStage.setTitle("Grade Calculator");
        primaryStage.setScene(new Scene(root, 600, 425));
        primaryStage.sizeToScene();
        primaryStage.show();
        primaryStage.setMinWidth(primaryStage.getWidth());
        primaryStage.setMinHeight(primaryStage.getHeight());
        primaryStage.setResizable(false);

        Controller controller = loader.getController();
        controller.setHostServices(getHostServices());
    }//end start(Stage):void

    /**
     * Main method holding top-level control over the program.
     *
     * @param args String[], Command-line program arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }//end main(String[]):void
}//end Main.class
