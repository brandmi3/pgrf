package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ColorChooser extends JFrame {

    private int color;
    private JPanel panel;
    private JColorChooser colorChooser;
    private JButton btnSubmit;

    public ColorChooser(String s, int c) throws HeadlessException {
        setVisible(true);
        setSize(600, 400);
        setTitle(s);
        panel = new JPanel(new BorderLayout());
        colorChooser = new JColorChooser();
        colorChooser.setColor(c);
        panel.add(colorChooser);
        btnSubmit = new JButton("Uložit");


        add(panel, BorderLayout.CENTER);
        add(btnSubmit, BorderLayout.SOUTH);
        revalidate();

        btnSubmit.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                color = colorChooser.getColor().getRGB();
                dispose();
            }
        });

    }

    public int getColor() {
        return color;
    }

    public ColorChooser setColor(int color) {
        this.color = color;
        return this;
    }

    public JColorChooser getColorChooser() {
        return colorChooser;
    }

    public ColorChooser setColorChooser(JColorChooser colorChooser) {
        this.colorChooser = colorChooser;
        return this;
    }

    public JButton getBtnSubmit() {
        return btnSubmit;
    }

    public ColorChooser setBtnSubmit(JButton btnSubmit) {
        this.btnSubmit = btnSubmit;
        return this;
    }
}
