module ru.iu3.GUI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ru.iu3.GUI to javafx.fxml;
    exports ru.iu3.GUI;
}