package ui;

import com.sun.deploy.util.StringUtils;
import drawables.*;
import drawables.Point;
import utils.Item;
import utils.Renderer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ColorChooserUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ControlPanel extends JPanel {

    PgrfFrame pgrfFrame;
    private JPanel controlPanel;

    private DrawableType type = DrawableType.N_OBJECT;


    private JComboBox itemSelect;

    private JPanel centerPanel;
    private JTextArea areaWithPoints;

    private JPanel managePanel;
    private JButton btnSave;
    private JButton btnDelete;

    private JPanel panelColors;
    private JButton btnBorder;
    private JButton btnSeed;
    private JButton btnFill;
    private JCheckBox allowFill;
    private JCheckBox patternFill;
    private ColorChooser colorChooser;
    private JPanel seedPanel;

    private List<Drawable> drawables;
    private NPolygon nPolygon;

    public ControlPanel(PgrfFrame pgrfFrame) {

        this.pgrfFrame = pgrfFrame;
        controlPanel = new JPanel(new BorderLayout());
        setSize(100, 1);
        drawables = new ArrayList<>();

        /* **** top menu **** */
        panelColors = new JPanel(new FlowLayout());
        btnBorder = new JButton("O");
        btnFill = new JButton("V");
        btnSeed = new JButton("S");
        allowFill = new JCheckBox("Vybarvovat");
        patternFill = new JCheckBox("S texturou");

        TitledBorder title;
        title = BorderFactory.createTitledBorder("Ohraničení - Výplň - Zapnutí");
        title.setTitleJustification(TitledBorder.LEFT);
        panelColors.setBorder(title);
        panelColors.add(btnBorder);
        panelColors.add(btnFill);

        seedPanel = new JPanel(new BorderLayout());
        seedPanel.add(allowFill, BorderLayout.NORTH);
        seedPanel.add(btnSeed, BorderLayout.CENTER);
        seedPanel.add(patternFill, BorderLayout.SOUTH);
        panelColors.add(seedPanel);

        controlPanel.add(panelColors, BorderLayout.NORTH);



        /* ***center panel *** */
        centerPanel = new JPanel(new BorderLayout());


        itemSelect = new JComboBox();
        itemSelect.setSize(50, 30);
        initDrawables();

        areaWithPoints = new JTextArea();
        areaWithPoints.setText("");
        areaWithPoints.setRows(10);
        areaWithPoints.setSize(100, 150);
        centerPanel.add(new JScrollPane(areaWithPoints), BorderLayout.CENTER);


        managePanel = new JPanel(new FlowLayout());
        btnSave = new JButton("Uložit");
        managePanel.add(btnSave);
        btnDelete = new JButton("Smazat Polygon");
        managePanel.add(btnDelete);
        centerPanel.add(managePanel, BorderLayout.SOUTH);

        controlPanel.add(centerPanel, BorderLayout.CENTER);

        /* *** color buttons *** */

        // controlPanel.add(panelColors, BorderLayout.SOUTH);

        /* *** listeners *** */

        itemSelect.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    int id = ((Item) e.getItem()).getId();
                    nPolygon = (NPolygon) drawables.get(id);
                    initPoints(nPolygon);
                    btnBorder.setBackground(new Color(nPolygon.getBorderColor()));
                    if (nPolygon.isFilled()) {
                        btnFill.setBackground(new Color(nPolygon.getFillColor()));
                    } else {
                        btnFill.setBackground(Color.WHITE);
                    }
                }
            }
        });
        btnBorder.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                colorChooser = new ColorChooser("Barva ohraničení", nPolygon != null ? nPolygon.getColor() : Color.WHITE.getRGB());
                colorChooser.getBtnSubmit().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        int color = colorChooser.getColor();
                        nPolygon.setColor(color);
                        btnBorder.setBackground(new Color(color));
                        btnBorder.setForeground(getContrastColor(new Color(color)));
                    }
                });
            }
        });
        btnFill.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                colorChooser = new ColorChooser("Barva výplně", nPolygon != null ? nPolygon.getFillColor() : Color.WHITE.getRGB());
                colorChooser.getBtnSubmit().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        int color = colorChooser.getColor();
                        nPolygon.setFillColor(color);
                        btnFill.setBackground(new Color(color));
                        btnFill.setForeground(getContrastColor(new Color(color)));

                        pgrfFrame.setFillMode(true);
                        nPolygon.setFilled(true);
                        nPolygon.fillScanLine(pgrfFrame.getRenderer());

                    }
                });
            }
        });
        btnSeed.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                colorChooser = new ColorChooser("Barva ohraničení", Color.BLUE.getRGB());
                colorChooser.getBtnSubmit().addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseReleased(MouseEvent e) {
                        int color = colorChooser.getColor();
                        pgrfFrame.setSeedColor(color);
                        btnSeed.setBackground(new Color(color));
                        btnSeed.setForeground(getContrastColor(new Color(color)));
                    }
                });
            }
        });
        allowFill.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    //selected
                    pgrfFrame.setFillMode(true);
                } else {
                    pgrfFrame.setFillMode(false);
                }

            }
        });
        patternFill.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == 1) {
                    //selected
                    pgrfFrame.setFillWithPattern(true);
                } else {
                    pgrfFrame.setFillWithPattern(false);
                }

            }
        });
        btnDelete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawables.size()>1&&nPolygon != drawables.get(0)) {
                    drawables.remove(nPolygon);
                    nPolygon = null;
                    initDrawables();
                    areaWithPoints.setText("");
                    initPoints((NPolygon) drawables.get(0));
                    pgrfFrame.setFillMode(false);
                }
            }
        });
        btnSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                validatePoints(areaWithPoints.getText());
            }
        });

        /* *** vraceni finalniho panelu *** */
        add(controlPanel, BorderLayout.WEST);
    }

    private Color getContrastColor(Color color) {
        Color c = new Color(
                255 - color.getRed(),
                255 - color.getGreen(),
                255 - color.getBlue());
        return c;
    }

    private void validatePoints(String s) {
        if (s.equals("")) {
            areaWithPoints.setBackground(Color.RED);
            return;
        }
        s = s.replace("\n", "");
        int separatorCount = (int) s.chars().filter(ch -> ch == ';').count();
        String pattern = "^(\\d{1,4}:?\\d{1,4})$";
        int startIndex = 0;

        List<Point> points = new ArrayList<>();

        for (int i = 0; i < separatorCount; i++) {
            int endIndex = s.indexOf(";");
            String point = s.substring(startIndex + 1, endIndex - 1);
            if (point.matches(pattern)) {
                int doubleDotIndex = point.indexOf(":");
                int x = new Integer(point.substring(startIndex, doubleDotIndex));
                int y = new Integer(point.substring(doubleDotIndex + 1, point.length()));
                points.add(new Point(x, y));

                s = s.substring(endIndex + 1);
            } else {

                areaWithPoints.setBackground(Color.RED);
                return;
            }
        }
        areaWithPoints.setBackground(Color.WHITE);
        nPolygon.setPoints(points);
        pgrfFrame.setFillMode(false);
    }

    private void initPoints(NPolygon nPolygon) {
        String s = "";
        for (int i = 0; i < nPolygon.getPoints().size(); i++) {
            Point p = nPolygon.getPoint(i);
            s += ("[" + p.getX() + ":" + p.getY() + "];\n");
        }
        areaWithPoints.setText(s);
        areaWithPoints.setBackground(Color.WHITE);
    }

    public List<Drawable> getDrawables() {
        return drawables;
    }

    public ControlPanel setDrawables(List<Drawable> drawables) {
        this.drawables = drawables;
        initDrawables();
        return this;
    }

    private void initDrawables() {
        itemSelect.removeAllItems();
        for (int i = 0; i < drawables.size(); i++) {
            itemSelect.addItem(new Item(i, i + 1 + ". Polygon"));
        }
        if (drawables.size() > 0) {
            NPolygon nPolygon = (NPolygon) drawables.get(0);
            initPoints(nPolygon);
            btnBorder.setBackground(new Color(nPolygon.getBorderColor()));
            if (nPolygon.isFilled())
                btnFill.setBackground(new Color(nPolygon.getFillColor()));
        }
        centerPanel.add(itemSelect, BorderLayout.NORTH);
        centerPanel.revalidate();
        controlPanel.revalidate();

    }
}
