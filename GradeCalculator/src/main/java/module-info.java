module gradecalculator {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;

    opens gradecalculator to javafx.fxml,javafx.graphics;
}