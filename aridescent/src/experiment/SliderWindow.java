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
        tabs.prefWidthProperty().bind(vbox.widthProperty());
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        vbox.getChildren().add(tabs);

        /* Tab pages */
        String title = "gluPerspective";
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

        String title3 = "glTranslatef";
        String[] labels3 = {
                "glTranslatef_x",
                "glTranslatef_y",
                "glTranslatef_z",
        };
        Float[][] floats3 = {
                {0f, 100f, model.glTranslatef_x},
                {0f, 100f, model.glTranslatef_y},
                {-100f, 100f, model.glTranslatef_z},
        };
        makeTabPage(tabs, title3, labels3, floats3);

        String title4 = "gluLookAt";
        String[] labels4 = {
                "gluLookAt_eyex",
                "gluLookAt_eyey",
                "gluLookAt_eyez",
                "gluLookAt_centerx",
                "gluLookAt_centery",
                "gluLookAt_centerz",
                "gluLookAt_upx",
                "gluLookAt_upy",
                "gluLookAt_upz",
        };
        Float[][] floats4 = {
                {-100f, 800f, model.gluLookAt_eyex},
                {-100f, 600f, model.gluLookAt_eyey},
                {-60f, 60f, model.gluLookAt_eyez},
                {-100f, 800f, model.gluLookAt_centerx},
                {-100f, 600f, model.gluLookAt_centery},
                {-60f, 60f, model.gluLookAt_centerz},
                {0f, 1f, model.gluLookAt_upx},
                {0f, 1f, model.gluLookAt_upy},
                {0f, 1f, model.gluLookAt_upz},
        };
        makeTabPage(tabs, title4, labels4, floats4);

        /* Button menu */
        VBox menu = new VBox();
        menu.setPadding(new Insets(0, 10, 0, 0));
        menu.setAlignment(Pos.BOTTOM_RIGHT);
        Button applyButton = new Button("UPDATE");
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
        grid.prefWidthProperty().bind(tabs.widthProperty());
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        tab.setContent(grid);
        tabs.getTabs().add(tab);


        for (int i = 0; i < labels.length; i++) {
            String key = labels[i];
            Label lbl = new Label(labels[i]);
            TextField input = new TextField(Double.toString(floats[i][2]));
            Label value = new Label(input.getText());

            input.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    float f = Float.valueOf(newValue);
                    model.config.replace(key, f);
                    value.setText(String.format("%f", f));
                } catch(NumberFormatException nfe) {

                }
            });

            GridPane.setConstraints(lbl, 0, i);
            grid.getChildren().add(lbl);
            GridPane.setConstraints(input, 1, i);
            grid.getChildren().add(input);
            GridPane.setConstraints(value, 2, i);
            grid.getChildren().add(value);
        }
    }
}