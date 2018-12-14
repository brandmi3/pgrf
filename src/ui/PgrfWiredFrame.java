package ui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import solids.*;
import transforms.*;
import utils.Transformer;

public class PgrfWiredFrame extends JFrame {

    static int FPS = 1000 / 30;
    static int width = 800;
    static int height = 600;
    private JPanel panel;
    private Transformer transformer;
    private Camera camera;
    private List<Solid> solids;
    private Solid axisSolid;
    private Solid pruzina;
    private Solid chosen;
    private boolean chosen_all = true;
    private BufferedImage img;
    private JPanel controlPanel;
    private JComboBox searchField;
    private JLabel lbl_chosenSolid;
    private int beginX, beginY;
    private double moveX, moveY, moveZ;
    private boolean animate;

    public static void main(String[] args) {
        PgrfWiredFrame frame = new PgrfWiredFrame();
        frame.init(width, height);
    }

    private void gui() {
        panel = new JPanel(new BorderLayout());
        controlPanel = new JPanel();
        controlPanel.setMinimumSize(new Dimension(300, 300));
        searchField = new JComboBox();
        lbl_chosenSolid = new JLabel();

        JLabel header_main = new JLabel("=== Ovládací Panel ===");
        JLabel header_move = new JLabel("=== Změna pozice ===");
        JLabel header_rot = new JLabel("=== Změna rotace ===");
        JLabel header_dim = new JLabel("=== Změna Pohledu ===");
        JLabel header_extra = new JLabel("=== Extra ===");

        JLabel lbl_popis = new JLabel("<html>" +
                "<p>" +
                "W,A,S,D - pohyb<br>" +
                "Ctrl - dolu <br>" +
                "Space - nahoru <br>" +
                "R - Reset Kamery <br>" +
                "Drag Myší - rozhlížení <br>" +
                "Kolečko Myší - zoom"
                + "</p></html>");

        TitledBorder title;
        title = BorderFactory.createTitledBorder("Výběr objektu");
        title.setTitleJustification(TitledBorder.LEFT);
        searchField.setBorder(title);

        searchField.addItem("Všechny");
        for (int i = 0; i < solids.size(); i++) {
            if(solids.get(i) instanceof Cube || solids.get(i) instanceof Pyramid)
            searchField.addItem(solids.get(i));
        }


        lbl_chosenSolidSettext();
        title = BorderFactory.createTitledBorder("Vybral jsi:");
        title.setTitleJustification(TitledBorder.LEFT);
        lbl_chosenSolid.setBorder(title);

        controlPanel.setBounds(61, 11, 81, 140);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JSpinner spinnerMove_X = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0.1));
        JSpinner spinnerMove_Y = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0.1));
        JSpinner spinnerMove_Z = new JSpinner(new SpinnerNumberModel(1, Integer.MIN_VALUE, Integer.MAX_VALUE, 0.1));

        JButton btnRot_X = new JButton("Rotace X");
        JButton btnRot_Y = new JButton("Rotace Y");
        JButton btnRot_Z = new JButton("Rotace Z");

        JButton btnPer = new JButton("Persperktivní");
        JButton btnOrt = new JButton("Ortogonální");

        JButton btnAnim = new JButton("Animace");

        header_main.setMaximumSize(new Dimension(Integer.MAX_VALUE, header_main.getMinimumSize().height));
        controlPanel.add(header_main);
        searchField.setMaximumSize(new Dimension(Integer.MAX_VALUE, searchField.getMinimumSize().height));
        controlPanel.add(searchField);
        lbl_chosenSolid.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl_chosenSolid.getMinimumSize().height));
        controlPanel.add(lbl_chosenSolid);

        header_move.setMaximumSize(new Dimension(Integer.MAX_VALUE, header_move.getMinimumSize().height));
        controlPanel.add(header_move);
        spinnerMove_X.setMaximumSize(new Dimension(Integer.MAX_VALUE, spinnerMove_X.getMinimumSize().height));
        controlPanel.add(spinnerMove_X);
        spinnerMove_Y.setMaximumSize(new Dimension(Integer.MAX_VALUE, spinnerMove_Y.getMinimumSize().height));
        controlPanel.add(spinnerMove_Y);
        spinnerMove_Z.setMaximumSize(new Dimension(Integer.MAX_VALUE, spinnerMove_Z.getMinimumSize().height));
        controlPanel.add(spinnerMove_Z);

        header_rot.setMaximumSize(new Dimension(Integer.MAX_VALUE, header_rot.getMinimumSize().height));
        controlPanel.add(header_rot);
        btnRot_X.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRot_X.getMinimumSize().height));
        controlPanel.add(btnRot_X);
        btnRot_Y.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRot_Y.getMinimumSize().height));
        controlPanel.add(btnRot_Y);
        btnRot_Z.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnRot_Z.getMinimumSize().height));
        controlPanel.add(btnRot_Z);

        header_dim.setMaximumSize(new Dimension(Integer.MAX_VALUE, header_dim.getMinimumSize().height));
        controlPanel.add(header_dim);
        btnPer.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnPer.getMinimumSize().height));
        controlPanel.add(btnPer);
        btnOrt.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnOrt.getMinimumSize().height));
        controlPanel.add(btnOrt);

        header_extra.setMaximumSize(new Dimension(Integer.MAX_VALUE, header_extra.getMinimumSize().height));
        controlPanel.add(header_extra);
        btnAnim.setMaximumSize(new Dimension(Integer.MAX_VALUE, btnAnim.getMinimumSize().height));
        controlPanel.add(btnAnim);

        lbl_popis.setMaximumSize(new Dimension(Integer.MAX_VALUE, lbl_popis.getMinimumSize().height));
        controlPanel.add(lbl_popis);

        add(panel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.EAST);
        revalidate();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
            }


        });
        spinnerMove_X.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double x = 0.1;
                if (chosen_all) {
                    if (solids.get(0).getVerticies().get(0).getX() > (double) spinnerMove_X.getValue()) {
                        x = -0.1;
                    }
                } else if (chosen.getVerticies().get(0).getX() > (double) spinnerMove_X.getValue()) {
                    x = -0.1;
                }
                Vec3D vec = new Vec3D(x, 0, 0);
                moveWithSolids(vec);
            }
        });
        spinnerMove_Y.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double y = 0.1;
                if (chosen_all) {
                    if (solids.get(0).getVerticies().get(0).getY() > (double) spinnerMove_Y.getValue()) {
                        y = -0.1;
                    }
                } else if (chosen.getVerticies().get(0).getY() > (double) spinnerMove_Y.getValue()) {
                    y = -0.1;
                }
                Vec3D vec = new Vec3D(0, y, 0);
                moveWithSolids(vec);
            }
        });
        spinnerMove_Z.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                double z = 0.1;
                if (chosen_all) {
                    if (solids.get(0).getVerticies().get(0).getZ() > (double) spinnerMove_Z.getValue()) {
                        z = -0.1;
                    }
                } else if (chosen.getVerticies().get(0).getZ() > (double) spinnerMove_Z.getValue()) {
                    z = -0.1;
                }
                Vec3D vec = new Vec3D(0, 0, z);
                moveWithSolids(vec);
            }
        });
        btnRot_X.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double x = 0.1;
                Vec3D vec = new Vec3D(x, 0, 0);
                rotate(vec);
            }
        });
        btnRot_Y.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double y = 0.1;
                Vec3D vec = new Vec3D(0, y, 0);
                rotate(vec);
            }
        });
        btnRot_Z.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                double z = 0.1;
                Vec3D vec = new Vec3D(0, 0, z);
                rotate(vec);
            }
        });

        btnPer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                transformer.setProjection(new Mat4PerspRH(1, 1, 1, 100));

            }
        });
        btnOrt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                transformer.setProjection(new Mat4OrthoRH(20, 20, -1, 1));

            }
        });
        btnAnim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                animate = !animate;

            }
        });


        searchField.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (e.getItem().equals("Všechny")) {
                        chosen_all = true;
                        spinnerMove_X.setValue(solids.get(0).getVerticies().get(0).getX());
                        spinnerMove_Y.setValue(solids.get(0).getVerticies().get(0).getY());
                        spinnerMove_Y.setValue(solids.get(0).getVerticies().get(0).getZ());
                    } else {
                        chosen_all = false;
                        chosen = ((Solid) e.getItem());
                        spinnerMove_X.setValue(chosen.getVerticies().get(0).getX());
                        spinnerMove_Y.setValue(chosen.getVerticies().get(0).getY());
                        spinnerMove_Y.setValue(chosen.getVerticies().get(0).getZ());
                    }

                    lbl_chosenSolidSettext();
                }
            }
        });
    }

    private void lbl_chosenSolidSettext() {
        if (chosen_all) {
            lbl_chosenSolid.setText("Všechny");
        } else {
            lbl_chosenSolid.setText(chosen.toString());
        }

    }

    private void init(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // nastavení frame
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(width, height);

        setTitle("Drátový model");
        solids = new ArrayList<>();

        transformer = new Transformer(img);
        transformer.setProjection(new Mat4PerspRH(1, 1, -1, 100));
        camera = new Camera(new Vec3D(9.54, 9.01, 8.26),
                -2.47, -0.39, 1.0, true);

        //solids.add(new Cube(1));

        int cubeCount = 5;
        for (int i = 0; i < cubeCount; i++) {
            Solid solid;
            if (i % 2 == 0) {
                solid = new Cube(1);
            } else {
                solid = new Pyramid(1);
            }

            for (int v = 0; v < solid.getVerticies().size(); v++) {
                Point3D point3D = solid.getVerticies().get(v);
                Point3D newPoint = point3D
                        .mul(new Mat4Transl(0, 2, 0))
                        .mul(new Mat4RotZ((double) i * 2d * Math.PI / (double) cubeCount));
                solid.getVerticies().set(v, newPoint);
            }
            solids.add(solid);
        }
        Axis axis = new Axis();
        for (int i = 0; i < axis.getVerticies().size(); i++) {
            Point3D point3D = axis.getVerticies().get(i);
            axis.getVerticies().set(i, point3D.mul(new Mat4Scale(4)));
        }
        solids.add(new FergusonCubic());

        axisSolid = axis;
        pruzina = new CurvedLine();
        gui();

        // listeners
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                beginX = e.getX();
                beginY = e.getY();
                super.mousePressed(e);
            }


        });
        panel.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {

                if (e.getWheelRotation() < 0) {
                    double scale = transformer.getModel().get(0, 0);
                    scale += 0.1;
                    transformer.setModel(new Mat4Scale(scale));
                } else {
                    double scale = transformer.getModel().get(0, 0);
                    scale -= 0.1;
                    transformer.setModel(new Mat4Scale(scale));
                }
            }
        });
        panel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                camera = camera.addAzimuth(-(Math.PI / 1000) * (beginX - e.getX()));
                camera = camera.addZenith(-(Math.PI / 1000) * (beginY - e.getY()));
                beginX = e.getX();
                beginY = e.getY();
            }


        });
        addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    /*POHYB*/
                    case KeyEvent.VK_A:
                        camera = camera.left(0.1);
                        break;
                    case KeyEvent.VK_W:
                        camera = camera.forward(0.1);
                        break;
                    case KeyEvent.VK_S:
                        camera = camera.backward(0.1);
                        break;
                    case KeyEvent.VK_D:
                        camera = camera.right(0.1);
                        break;
                    case KeyEvent.VK_SPACE:
                        camera = camera.up(0.1);
                        break;
                    case KeyEvent.VK_CONTROL:
                        camera = camera.down(0.1);
                        break;

                    case KeyEvent.VK_C:
                        rotate(new Vec3D(0.1, 0.3, 0.1));
                        break;
                    case KeyEvent.VK_M:
                        transformer.setProjection(new Mat4OrthoRH(20, 20, -1, 1));
                        System.out.println("Pohled ortogonalni");
                        break;
                    case KeyEvent.VK_N:
                        transformer.setProjection(new Mat4PerspRH(1, 1, 1, 100));
                        System.out.println("pohled perspektivni");
                        break;
                    case KeyEvent.VK_L:
                        transformer.setModel(new Mat4Scale(2));
                        System.out.println("Pohled ortogonalni");
                        break;
                    case KeyEvent.VK_R:
                        resetCamera();
                        break;
                    case KeyEvent.VK_2:
                        camera = camera.down(0.1);
                        break;
                }
                super.keyReleased(e);
            }


        });

        // timer pro refresh draw()
        setLocationRelativeTo(null);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                draw();
            }
        }, 100, FPS);
        Timer timer_animation = new Timer();
        timer_animation.schedule(new TimerTask() {
            @Override
            public void run() {
                if (animate) {
                    chosen_all = true;
                    rotate(new Vec3D(0.1, 0.1, 0.2));
                }
            }
        }, 100, FPS);
    }


    private void rotate(Vec3D vec3D) {
        for (int i = 0; i < solids.size(); i++) {
            Solid solid = solids.get(i);
            if (solid == chosen || chosen_all)
                for (int v = 0; v < solid.getVerticies().size(); v++) {
                    Point3D point3D = solid.getVerticies().get(v);
                    System.out.println();
                    Point3D newPoint = point3D
                            .mul(new Mat4RotZ(-(double) i * 2d * Math.PI / (double) 5))
                            .mul(new Mat4Transl(0, -2, 0))
                            .mul(new Mat4RotXYZ(vec3D.getX(), vec3D.getY(), vec3D.getZ()))
                            .mul(new Mat4Transl(0, 2, 0))
                            .mul(new Mat4RotZ((double) i * 2d * Math.PI / (double) 5));

                    solid.getVerticies().set(v, newPoint);

                }
        }

    }


    private void moveWithSolids(Vec3D vec3D) {
        for (int i = 0; i < solids.size(); i++) {
            Solid solid = solids.get(i);
            if (solid == chosen || chosen_all)
                for (int v = 0; v < solid.getVerticies().size(); v++) {
                    Point3D point3D = solid.getVerticies().get(v);

                    Point3D newPoint = point3D
                            .mul(new Mat4Transl(vec3D));

                    solid.getVerticies().set(v, newPoint);

                }
        }
    }


    private void resetCamera() {
        // TODO SPRÁVNÉ HODNOTY
        moveX = 0;
        moveY = 0;
        moveZ = 0;
        camera = new Camera(new Vec3D(9.54, 9.01, 8.26),
                -2.47, -0.39, 1.0, true);
    }

    private void changePositions() {
        for (int i = 0; i < solids.size(); i++) {
            Solid solid = solids.get(i);
            for (int v = 0; v < solid.getVerticies().size(); v++) {
                Point3D point3D = solid.getVerticies().get(v);

                System.out.println();
                Point3D newPoint = point3D
                        .mul(new Mat4Transl(0, 0.1, 0));

                solid.getVerticies().set(v, newPoint);

            }
        }

    }

    private void draw() {
        // clear
        img.getGraphics().fillRect(0, 0, img.getWidth(), img.getHeight());


        transformer.setView(camera.getViewMatrix());


        for (Solid solid : solids) {
            transformer.drawWireFrame(solid);
        }
        transformer.drawWireFrame(axisSolid);
        for (Point3D point3D : axisSolid.getVerticies()) {
            transformer.drawAxisName(point3D);
        }
        transformer.drawWireFrame(pruzina);

        panel.getGraphics().drawImage(img, 0, 0, null);
        panel.paintComponents(getGraphics());
    }
}