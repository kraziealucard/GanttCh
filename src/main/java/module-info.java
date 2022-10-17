module com.example.ganttchart {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires org.jfree.fxgraphics2d;
    requires org.jfree.jfreechart;
    requires org.jfree.chart.fx;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.graphics;

    opens com.example.ganttchart to javafx.fxml;
    exports com.example.ganttchart;
    exports LogicLayer;
    opens LogicLayer to javafx.fxml;
    exports DataLayer;
    opens DataLayer to javafx.fxml;

}