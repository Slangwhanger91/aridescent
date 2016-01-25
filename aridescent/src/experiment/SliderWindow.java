package experiment;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SliderWindow extends Application {
    Test3D model;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        model = new Test3D();
        model.start();

        VBox vbox = new VBox();
        TabPane tabs = new TabPane();
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        vbox.getChildren().add(tabs);

        /* Tab pages */
        String title = "gluPerspective()";
        String[] labels = { "gluPerspective_fovy", "gluPerspective_aspect",
                "gluPerspective_zNear", "gluPerspective_zFar", };
        Float[][] floats = {
                {0f, 180f, model.gluPerspective_fovy},
                {1f, 2f, model.gluPerspective_aspect},
                {-1f, 1f, model.gluPerspective_zNear},
                {0f, 90f, model.gluPerspective_zFar},
        };
        makeTabPage(tabs, title, labels, floats);

        String title2 = "glRotatef";
        String[] labels2 = { "glRotatef_angle", "glRotatef_x",
                "glRotatef_y", "glRotatef_z", };
        Float[][] floats2 = {
                {0f, 360f, model.glRotatef_angle},
                {0f, 1f, model.glRotatef_x},
                {0f, 1f, model.glRotatef_y},
                {0f, 1f, model.glRotatef_z},
        };
        makeTabPage(tabs, title2, labels2, floats2);

        /* Button menu */
        VBox menu = new VBox();
        menu.setPadding(new Insets(0, 10, 0, 0));
        menu.setAlignment(Pos.BOTTOM_RIGHT);
        Button applyButton = new Button("Apply");
        applyButton.setOnAction(event -> model.updateConfig());
        menu.getChildren().addAll(applyButton);
        vbox.getChildren().add(menu);

        Scene scene = new Scene(vbox, 640, 480);
        primaryStage.setTitle("sliderwindow");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void makeTabPage(TabPane tabs, String title, String[] labels, Float[][] floats) {
        Tab tab = new Tab(title);
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        tab.setContent(grid);
        tabs.getTabs().add(tab);


        for (int i = 0; i < labels.length; i++) {
            String key = labels[i];
            Label lbl = new Label(labels[i]);
            Slider slider = new Slider(floats[i][0], floats[i][1], floats[i][2]);
            slider.setSnapToTicks(true);
            //slider.setMinorTickCount(0);
            slider.setMajorTickUnit(0.1);
            Label value = new Label(Double.toString(slider.getValue()));

            slider.valueProperty().addListener((observable, oldValue, newValue) -> {
                value.setText(String.format("%f", newValue.doubleValue()));
                model.config.replace(key, newValue.floatValue());
            });

            GridPane.setConstraints(lbl, 0, i);
            grid.getChildren().add(lbl);
            GridPane.setConstraints(slider, 1, i);
            grid.getChildren().add(slider);
            GridPane.setConstraints(value, 2, i);
            grid.getChildren().add(value);
        }
    }
}
