package ui;

import drawables.Drawable;
import drawables.NPolygon;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ControlPanel extends JPanel {

    private JPanel controlPanel;

    private JPanel panellRadio;
    private ButtonGroup btnGroupRrawabletype;
    private JRadioButton radioLine;
    private JRadioButton radioPolygon;
    private JRadioButton radioRegular;

    private JComboBox itemSelect;
    private TextArea areaWithPoints;


    public ControlPanel(List<Drawable> drawables) {

        controlPanel = new JPanel(new BorderLayout());
        setLayout(new BorderLayout());
        setSize(200, 600);
        radioLine = new JRadioButton("Line");
        radioPolygon = new JRadioButton("Polygon", true);
        radioRegular = new JRadioButton("Regular Polygon");

        btnGroupRrawabletype = new ButtonGroup();
        btnGroupRrawabletype.add(radioLine);
        btnGroupRrawabletype.add(radioPolygon);
        btnGroupRrawabletype.add(radioRegular);

        panellRadio = new JPanel(new FlowLayout());
        panellRadio.add(radioLine);
        panellRadio.add(radioPolygon);
        panellRadio.add(radioRegular);
        add(panellRadio, BorderLayout.CENTER);

//        itemSelect = new JComboBox();
//
//        if (drawables.size() > 0) {
//            for (int i = 0; i < drawables.size(); i++) {
//                itemSelect.add(new JButton(drawables.get(i).getClass().getName()), i);
//            }
//        }

    }
}
