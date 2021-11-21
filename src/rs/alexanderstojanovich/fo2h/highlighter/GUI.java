/* 
 * Copyright (C) 2021 Alexander Stojanovich <coas91@rocketmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rs.alexanderstojanovich.fo2h.highlighter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import rs.alexanderstojanovich.fo2h.frm.Palette;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class GUI extends javax.swing.JFrame {

    private static final Configuration cfg = new Configuration();

    // cool it's our new logo :)
    private static final String LOGO_FILE_NAME = "fo2hlogo.png";
    // and logox variant with black outline
    private static final String LOGOX_FILE_NAME = "fo2hlogox.png";

    public static final String RESOURCES_DIR = "/rs/alexanderstojanovich/fo2h/res/";
    public static final String LICENSE_LOGO_FILE_NAME = "gplv3_logo.png";

    private Highligther task;

    // via several labels coloured differently
    private final JLabel[] colorVector = new JLabel[256];

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        initFO2HLogos();
        initColors(); // init item colors
        initPaths(); // init dir paths
        initMeths(); // init draw methods
        initPosition();
        initColorPanel();
    }

    // init Palette display (it's called Color Vector)
    private void initColorPanel() {
        int[] colors = Palette.getColors();
        if (colors != null) {
            for (int i = 0; i < colorVector.length; i++) {
                colorVector[i] = new JLabel();
                colorVector[i].setBackground(Color.BLACK);
                colorVector[i].setOpaque(true);
                colorVector[i].setSize(6, 6);
                colorVector[i].setBorder(new BevelBorder(BevelBorder.RAISED));

                Color col = new Color(Palette.getColors()[i]);

                colorVector[i].setBackground(col);
                colorVector[i].setToolTipText("Red = " + col.getRed()
                        + ", Green = " + col.getGreen() + ", Blue = " + col.getBlue());

                pnlPalCols.add(colorVector[i], new Integer(i));
            }
        }
    }

    private void initPaths() {
        this.txtFldInPath.setText(cfg.getInDir().getPath());
        this.txtFldOutPath.setText(cfg.getOutDir().getPath());
        this.txtFldInPath.setToolTipText(cfg.getInDir().getPath());
        this.txtFldOutPath.setToolTipText(cfg.getOutDir().getPath());
        if (!cfg.getInDir().getPath().isEmpty()
                && !cfg.getOutDir().getPath().isEmpty()) {
            btnGo.setEnabled(true);
        }
        final FileNameExtensionFilter dictiFilter = new FileNameExtensionFilter("Highlighter dictionary (*.txt)", "txt");
        this.fileLoadDictionary.setFileFilter(dictiFilter);
    }

    // Center the GUI window into center of the screen
    private void initPosition() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
    }

    // init both logos
    private void initFO2HLogos() {
        URL url_logo = getClass().getResource(RESOURCES_DIR + LOGO_FILE_NAME);
        URL url_logox = getClass().getResource(RESOURCES_DIR + LOGOX_FILE_NAME);
        if (url_logo != null && url_logox != null) {
            ImageIcon logo = new ImageIcon(url_logo);
            ImageIcon logox = new ImageIcon(url_logox);
            List<Image> icons = new ArrayList<>();
            icons.add(logo.getImage());
            icons.add(logox.getImage());
            this.setIconImages(icons);//.getScaledInstance(23, 14, Image.SCALE_SMOOTH));
        }
    }

    private void initColors() {
        Color implantColor = cfg.getImplantColor();
        this.btnImplantColor.setBackground(implantColor);
        this.btnImplantColor.setToolTipText("Red = " + implantColor.getRed()
                + ", Green = " + implantColor.getGreen() + ", Blue = " + implantColor.getBlue());

        Color t4Color = cfg.getT4Color();
        this.btnT4Color.setBackground(t4Color);
        this.btnT4Color.setToolTipText("Red = " + t4Color.getRed()
                + ", Green = " + t4Color.getGreen() + ", Blue = " + t4Color.getBlue());

        Color t3Color = cfg.getT3Color();
        this.btnT3Color.setBackground(t3Color);
        this.btnT3Color.setToolTipText("Red = " + t3Color.getRed()
                + ", Green = " + t3Color.getGreen() + ", Blue = " + t3Color.getBlue());

        Color t2Color = cfg.getT2Color();
        this.btnT2Color.setBackground(t2Color);
        this.btnT2Color.setToolTipText("Red = " + t2Color.getRed()
                + ", Green = " + t2Color.getGreen() + ", Blue = " + t2Color.getBlue());

        Color t1Color = cfg.getT1Color();
        this.btnT1Color.setBackground(t1Color);
        this.btnT1Color.setToolTipText("Red = " + t1Color.getRed()
                + ", Green = " + t1Color.getGreen() + ", Blue = " + t1Color.getBlue());

        Color t0Color = cfg.getT0Color();
        this.btnT0Color.setBackground(t0Color);
        this.btnT0Color.setToolTipText("Red = " + t0Color.getRed()
                + ", Green = " + t0Color.getGreen() + ", Blue = " + t0Color.getBlue());

        Color bookColor = cfg.getBookColor();
        this.btnBookColor.setBackground(bookColor);
        this.btnBookColor.setToolTipText("Red = " + bookColor.getRed()
                + ", Green = " + bookColor.getGreen() + ", Blue = " + bookColor.getBlue());

        Color oreColor = cfg.getOreColor();
        this.btnOreColor.setBackground(oreColor);
        this.btnOreColor.setToolTipText("Red = " + oreColor.getRed()
                + ", Green = " + oreColor.getGreen() + ", Blue = " + oreColor.getBlue());

        Color containerColor = cfg.getContainerColor();
        this.btnContainerColor.setBackground(containerColor);
        this.btnContainerColor.setToolTipText("Red = " + containerColor.getRed()
                + ", Green = " + containerColor.getGreen() + ", Blue = " + containerColor.getBlue());

        Color collectionColor = cfg.getCollectionColor();
        this.btnCollectionColor.setBackground(collectionColor);
        this.btnCollectionColor.setToolTipText("Red = " + collectionColor.getRed()
                + ", Green = " + collectionColor.getGreen() + ", Blue = " + collectionColor.getBlue());

        Color resourcesColor = cfg.getResourcesColor();
        this.btnResourceColor.setBackground(resourcesColor);
        this.btnResourceColor.setToolTipText("Red = " + resourcesColor.getRed()
                + ", Green = " + resourcesColor.getGreen() + ", Blue = " + resourcesColor.getBlue());

        Color unusedColor = cfg.getUnusedColor();
        this.btnUnusedColor.setBackground(unusedColor);
        this.btnUnusedColor.setToolTipText("Red = " + unusedColor.getRed()
                + ", Green = " + unusedColor.getGreen() + ", Blue = " + unusedColor.getBlue());
    }

    private void initMeths() {
        this.cbOutline.setSelected(cfg.isDrawOutline());
        this.cbFillInterior.setSelected(cfg.isFillInterior());
        this.cbPutLabels.setSelected(cfg.isPutLabels());

        this.cbRecurse.setSelected(cfg.isRecursive());
        this.cbForcePalCols.setSelected(cfg.isForcePaletteColors());
    }

    private void fileInOpen() {
        int returnVal = fileChooserInput.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            cfg.setInDir(fileChooserInput.getSelectedFile());
            txtFldInPath.setText(cfg.getInDir().getPath());
            txtFldInPath.setToolTipText(cfg.getInDir().getPath());
        }
        if (!cfg.getInDir().getPath().isEmpty()
                && !cfg.getOutDir().getPath().isEmpty()) {
            btnGo.setEnabled(true);
        }
    }

    private void fileOutOpen() {
        int returnVal = fileChooserOutput.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            cfg.setOutDir(fileChooserOutput.getSelectedFile());
            txtFldOutPath.setText(cfg.getOutDir().getPath());
            txtFldOutPath.setToolTipText(cfg.getOutDir().getPath());
        }
        if (!cfg.getInDir().getPath().isEmpty()
                && !cfg.getOutDir().getPath().isEmpty()) {
            btnGo.setEnabled(true);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileLoadDictionary = new javax.swing.JFileChooser();
        fileChooserInput = new javax.swing.JFileChooser();
        fileChooserOutput = new javax.swing.JFileChooser();
        pnlFilePaths = new javax.swing.JPanel();
        lblInput = new javax.swing.JLabel();
        txtFldInPath = new javax.swing.JTextField();
        btnChooseInPath = new javax.swing.JButton();
        lblOutput = new javax.swing.JLabel();
        txtFldOutPath = new javax.swing.JTextField();
        btnChoosePathOut = new javax.swing.JButton();
        cbRecurse = new javax.swing.JCheckBox();
        pnlPalette = new javax.swing.JPanel();
        pnlPalCols = new javax.swing.JPanel();
        cbForcePalCols = new javax.swing.JCheckBox();
        pnlOutlineColors = new javax.swing.JPanel();
        lblImplantColor = new javax.swing.JLabel();
        btnImplantColor = new javax.swing.JButton();
        lblT4Color = new javax.swing.JLabel();
        btnT4Color = new javax.swing.JButton();
        lblT3Color = new javax.swing.JLabel();
        btnT3Color = new javax.swing.JButton();
        lblT2Color = new javax.swing.JLabel();
        btnT2Color = new javax.swing.JButton();
        lblT1Color = new javax.swing.JLabel();
        btnT1Color = new javax.swing.JButton();
        lblT0Color = new javax.swing.JLabel();
        btnT0Color = new javax.swing.JButton();
        lblBookColor = new javax.swing.JLabel();
        btnBookColor = new javax.swing.JButton();
        lblOreColor = new javax.swing.JLabel();
        btnOreColor = new javax.swing.JButton();
        lblResourceColor = new javax.swing.JLabel();
        btnResourceColor = new javax.swing.JButton();
        lblContainerColor = new javax.swing.JLabel();
        btnContainerColor = new javax.swing.JButton();
        lblCollectionColor = new javax.swing.JLabel();
        btnCollectionColor = new javax.swing.JButton();
        lblUnusedColor = new javax.swing.JLabel();
        btnUnusedColor = new javax.swing.JButton();
        pnlWork = new javax.swing.JPanel();
        pnl01Effects = new javax.swing.JPanel();
        cbOutline = new javax.swing.JCheckBox();
        cbFillInterior = new javax.swing.JCheckBox();
        cbPutLabels = new javax.swing.JCheckBox();
        pnl02Unused = new javax.swing.JPanel();
        pnl03Progress = new javax.swing.JPanel();
        btnGo = new javax.swing.JButton();
        progBarWork = new javax.swing.JProgressBar();
        btnStop = new javax.swing.JButton();
        mainMenu = new javax.swing.JMenuBar();
        mainMenuFile = new javax.swing.JMenu();
        fileMenuLoadDictionary = new javax.swing.JMenuItem();
        fileMenuExit = new javax.swing.JMenuItem();
        mainMenuInfo = new javax.swing.JMenu();
        infoMenuAbout = new javax.swing.JMenuItem();
        infoMenuHelp = new javax.swing.JMenuItem();

        fileChooserInput.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        fileChooserOutput.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileChooserOutput.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FOnline2 Highlighter - LATVIA");
        setMinimumSize(new java.awt.Dimension(600, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 600));
        getContentPane().setLayout(new java.awt.GridLayout(2, 2));

        pnlFilePaths.setBorder(javax.swing.BorderFactory.createTitledBorder("Directory Paths"));
        pnlFilePaths.setLayout(new javax.swing.BoxLayout(pnlFilePaths, javax.swing.BoxLayout.PAGE_AXIS));

        lblInput.setText("Input data directory:");
        lblInput.setAlignmentY(0.0F);
        pnlFilePaths.add(lblInput);

        txtFldInPath.setEditable(false);
        txtFldInPath.setColumns(24);
        txtFldInPath.setAlignmentX(0.0F);
        txtFldInPath.setAlignmentY(0.0F);
        pnlFilePaths.add(txtFldInPath);

        btnChooseInPath.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/alexanderstojanovich/fo2h/res/dir_icon.png"))); // NOI18N
        btnChooseInPath.setText("Input directory");
        btnChooseInPath.setToolTipText("Choose input directory");
        btnChooseInPath.setAlignmentY(0.0F);
        btnChooseInPath.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnChooseInPath.setIconTextGap(16);
        btnChooseInPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseInPathActionPerformed(evt);
            }
        });
        pnlFilePaths.add(btnChooseInPath);

        lblOutput.setText("Output data directory:");
        lblOutput.setAlignmentY(0.0F);
        pnlFilePaths.add(lblOutput);

        txtFldOutPath.setEditable(false);
        txtFldOutPath.setColumns(24);
        txtFldOutPath.setAlignmentX(0.0F);
        txtFldOutPath.setAlignmentY(0.0F);
        pnlFilePaths.add(txtFldOutPath);

        btnChoosePathOut.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/alexanderstojanovich/fo2h/res/dir_icon.png"))); // NOI18N
        btnChoosePathOut.setText("Output directory");
        btnChoosePathOut.setToolTipText("Choose output directory");
        btnChoosePathOut.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnChoosePathOut.setIconTextGap(8);
        btnChoosePathOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoosePathOutActionPerformed(evt);
            }
        });
        pnlFilePaths.add(btnChoosePathOut);

        cbRecurse.setText("Recurse through Child Directories");
        cbRecurse.setToolTipText("Recurse through all sub directories from input path");
        cbRecurse.setAlignmentY(0.0F);
        cbRecurse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbRecurseActionPerformed(evt);
            }
        });
        pnlFilePaths.add(cbRecurse);

        getContentPane().add(pnlFilePaths);

        pnlPalette.setBorder(javax.swing.BorderFactory.createTitledBorder("Palette"));
        pnlPalette.setLayout(new java.awt.BorderLayout());

        pnlPalCols.setAlignmentX(0.0F);
        pnlPalCols.setAlignmentY(0.0F);
        pnlPalCols.setLayout(new java.awt.GridLayout(16, 16, 2, 2));
        pnlPalette.add(pnlPalCols, java.awt.BorderLayout.CENTER);

        cbForcePalCols.setText("Force Palette Colors");
        cbForcePalCols.setToolTipText("All Item Colors will be contrained to Palette ones");
        cbForcePalCols.setAlignmentY(0.0F);
        cbForcePalCols.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbForcePalColsActionPerformed(evt);
            }
        });
        pnlPalette.add(cbForcePalCols, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(pnlPalette);

        pnlOutlineColors.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Colors"));
        pnlOutlineColors.setLayout(new java.awt.GridLayout(12, 2, 2, 2));

        lblImplantColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblImplantColor.setText("Implant Color:");
        pnlOutlineColors.add(lblImplantColor);

        btnImplantColor.setBorder(null);
        btnImplantColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImplantColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnImplantColor);

        lblT4Color.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblT4Color.setText("Tier 4 Color:");
        pnlOutlineColors.add(lblT4Color);

        btnT4Color.setBorder(null);
        btnT4Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnT4ColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnT4Color);

        lblT3Color.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblT3Color.setText("Tier 3 Color:");
        pnlOutlineColors.add(lblT3Color);

        btnT3Color.setBorder(null);
        btnT3Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnT3ColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnT3Color);

        lblT2Color.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblT2Color.setText("Tier 2 Color:");
        pnlOutlineColors.add(lblT2Color);

        btnT2Color.setBorder(null);
        btnT2Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnT2ColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnT2Color);

        lblT1Color.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblT1Color.setText("Tier 1 Color:");
        pnlOutlineColors.add(lblT1Color);

        btnT1Color.setBorder(null);
        btnT1Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnT1ColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnT1Color);

        lblT0Color.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblT0Color.setText("Tier 0 Color:");
        pnlOutlineColors.add(lblT0Color);

        btnT0Color.setBorder(null);
        btnT0Color.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnT0ColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnT0Color);

        lblBookColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblBookColor.setText("Book Color:");
        pnlOutlineColors.add(lblBookColor);

        btnBookColor.setBorder(null);
        btnBookColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBookColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnBookColor);

        lblOreColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblOreColor.setText("Ore Color:");
        pnlOutlineColors.add(lblOreColor);

        btnOreColor.setBorder(null);
        btnOreColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOreColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnOreColor);

        lblResourceColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblResourceColor.setText("Resource Color:");
        pnlOutlineColors.add(lblResourceColor);

        btnResourceColor.setBorder(null);
        btnResourceColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResourceColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnResourceColor);

        lblContainerColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblContainerColor.setText("Container Color:");
        pnlOutlineColors.add(lblContainerColor);

        btnContainerColor.setBorder(null);
        btnContainerColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContainerColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnContainerColor);

        lblCollectionColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblCollectionColor.setText("Collection Color:");
        lblCollectionColor.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        pnlOutlineColors.add(lblCollectionColor);

        btnCollectionColor.setBorder(null);
        btnCollectionColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCollectionColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnCollectionColor);

        lblUnusedColor.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblUnusedColor.setText("Unused Color:");
        pnlOutlineColors.add(lblUnusedColor);

        btnUnusedColor.setBorder(null);
        btnUnusedColor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnusedColorActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnUnusedColor);

        getContentPane().add(pnlOutlineColors);

        pnlWork.setBorder(javax.swing.BorderFactory.createTitledBorder("Work & Effects"));
        pnlWork.setLayout(new javax.swing.BoxLayout(pnlWork, javax.swing.BoxLayout.PAGE_AXIS));

        pnl01Effects.setLayout(new java.awt.GridLayout(1, 2, 2, 2));

        cbOutline.setSelected(true);
        cbOutline.setText("Draw Outline");
        cbOutline.setToolTipText("Draw Outline on the Image Edges");
        cbOutline.setAlignmentY(0.0F);
        cbOutline.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cbOutline.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cbOutline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOutlineActionPerformed(evt);
            }
        });
        pnl01Effects.add(cbOutline);

        cbFillInterior.setText("Fill Interior");
        cbFillInterior.setToolTipText("Fill Interior of the Image");
        cbFillInterior.setAlignmentY(0.0F);
        cbFillInterior.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cbFillInterior.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cbFillInterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbFillInteriorActionPerformed(evt);
            }
        });
        pnl01Effects.add(cbFillInterior);

        cbPutLabels.setText("Put Labels");
        cbPutLabels.setToolTipText("Put a Label above Item");
        cbPutLabels.setAlignmentY(0.0F);
        cbPutLabels.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        cbPutLabels.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        cbPutLabels.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPutLabelsActionPerformed(evt);
            }
        });
        pnl01Effects.add(cbPutLabels);

        pnlWork.add(pnl01Effects);

        pnl02Unused.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnl02Unused.setLayout(new java.awt.BorderLayout());
        pnlWork.add(pnl02Unused);

        pnl03Progress.setLayout(new java.awt.GridLayout(1, 0));

        btnGo.setForeground(new java.awt.Color(51, 255, 51));
        btnGo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/alexanderstojanovich/fo2h/res/avenger.png"))); // NOI18N
        btnGo.setText("GO");
        btnGo.setToolTipText("Get the highlighter working");
        btnGo.setAlignmentY(0.0F);
        btnGo.setEnabled(false);
        btnGo.setPreferredSize(new java.awt.Dimension(70, 35));
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });
        pnl03Progress.add(btnGo);

        progBarWork.setAlignmentX(0.0F);
        progBarWork.setAlignmentY(1.0F);
        progBarWork.setPreferredSize(new java.awt.Dimension(75, 25));
        progBarWork.setStringPainted(true);
        pnl03Progress.add(progBarWork);

        btnStop.setForeground(new java.awt.Color(204, 0, 51));
        btnStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rs/alexanderstojanovich/fo2h/res/stop.png"))); // NOI18N
        btnStop.setText("STOP");
        btnStop.setToolTipText("Stops higlighter abruptly");
        btnStop.setAlignmentY(0.0F);
        btnStop.setEnabled(false);
        btnStop.setPreferredSize(new java.awt.Dimension(70, 35));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });
        pnl03Progress.add(btnStop);

        pnlWork.add(pnl03Progress);

        getContentPane().add(pnlWork);

        mainMenuFile.setText("File");

        fileMenuLoadDictionary.setText("Load Dictionary...");
        fileMenuLoadDictionary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuLoadDictionaryActionPerformed(evt);
            }
        });
        mainMenuFile.add(fileMenuLoadDictionary);

        fileMenuExit.setText("Exit");
        fileMenuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuExitActionPerformed(evt);
            }
        });
        mainMenuFile.add(fileMenuExit);

        mainMenu.add(mainMenuFile);

        mainMenuInfo.setText("Info");

        infoMenuAbout.setText("About");
        infoMenuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoMenuAboutActionPerformed(evt);
            }
        });
        mainMenuInfo.add(infoMenuAbout);

        infoMenuHelp.setText("How to use");
        infoMenuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoMenuHelpActionPerformed(evt);
            }
        });
        mainMenuInfo.add(infoMenuHelp);

        mainMenu.add(mainMenuInfo);

        setJMenuBar(mainMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnChooseInPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseInPathActionPerformed
        // TODO add your handling code here:
        fileInOpen();
    }//GEN-LAST:event_btnChooseInPathActionPerformed

    private void btnChoosePathOutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChoosePathOutActionPerformed
        // TODO add your handling code here:
        fileOutOpen();
    }//GEN-LAST:event_btnChoosePathOutActionPerformed

    private void btnGoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGoActionPerformed
        // TODO add your handling code here:  
        if (Highligther.DICTIONARY.isEmpty()) {
            JOptionPane.showMessageDialog(GUI.this, "Dictionary must be initialized!", "Dictionary requirement error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cfg.getInDir().exists()) {
            JOptionPane.showMessageDialog(GUI.this, "Input directory doesn't exist!", "Input directory error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cfg.isDrawOutline() && !cfg.isFillInterior() && !cfg.isPutLabels()) {
            JOptionPane.showMessageDialog(GUI.this, "Please select at least one drawing method!", "No drawing method error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        btnGo.setEnabled(false);
        btnStop.setEnabled(true);
        task = new Highligther(cfg) {
            @Override
            protected void done() {
                boolean stopped = isStopped();
                JOptionPane.showMessageDialog(GUI.this, stopped ? "Highlighter work stopped by the user!" : "Highlighter work successfully finished!", "Work Finished", stopped ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                btnGo.setEnabled(true);
                btnStop.setEnabled(false);
                progBarWork.setValue(0);
                progBarWork.validate();
            }

        };

        task.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    progBarWork.setValue(Math.round((float) evt.getNewValue()));
                    progBarWork.validate();
                }
            }
        });

        task.execute();
    }//GEN-LAST:event_btnGoActionPerformed

    private void btnImplantColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImplantColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Implant Color", GUI.cfg.getImplantColor());
        if (color != null) {
            GUI.cfg.setImplantColor(color);
            this.btnImplantColor.setBackground(color);
            this.btnImplantColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnImplantColorActionPerformed

    private void btnT4ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT4ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 4 Color", GUI.cfg.getT4Color());
        if (color != null) {
            GUI.cfg.setT4Color(color);
            this.btnT4Color.setBackground(color);
            this.btnT4Color.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnT4ColorActionPerformed

    private void btnT3ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT3ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 3 Color", GUI.cfg.getT3Color());
        if (color != null) {
            GUI.cfg.setT3Color(color);
            this.btnT3Color.setBackground(color);
            this.btnT3Color.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnT3ColorActionPerformed

    private void btnT2ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT2ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 2 Color", GUI.cfg.getT2Color());
        if (color != null) {
            GUI.cfg.setT2Color(color);
            this.btnT2Color.setBackground(color);
            this.btnT2Color.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnT2ColorActionPerformed

    private void btnT1ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT1ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 1 Color", GUI.cfg.getT1Color());
        if (color != null) {
            GUI.cfg.setT1Color(color);
            this.btnT1Color.setBackground(color);
            this.btnT1Color.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnT1ColorActionPerformed

    private void btnT0ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT0ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 0 Color", GUI.cfg.getT0Color());
        if (color != null) {
            GUI.cfg.setT0Color(color);
            this.btnT0Color.setBackground(color);
            this.btnT0Color.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnT0ColorActionPerformed

    private void btnBookColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Book Color", GUI.cfg.getBookColor());
        if (color != null) {
            GUI.cfg.setBookColor(color);
            this.btnBookColor.setBackground(color);
            this.btnBookColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnBookColorActionPerformed

    private void btnOreColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOreColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Ore Color", GUI.cfg.getOreColor());
        if (color != null) {
            GUI.cfg.setOreColor(color);
            this.btnOreColor.setBackground(color);
            this.btnOreColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnOreColorActionPerformed

    private void btnUnusedColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnusedColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Unused Color", GUI.cfg.getUnusedColor());
        if (color != null) {
            GUI.cfg.setUnusedColor(color);
            this.btnUnusedColor.setBackground(color);
            this.btnUnusedColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnUnusedColorActionPerformed

    private void fileMenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuExitActionPerformed
        // TODO add your handling code here:        
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_fileMenuExitActionPerformed

    private void infoMenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoMenuAboutActionPerformed
        // TODO add your handling code here:
        URL icon_url = getClass().getResource(RESOURCES_DIR + LICENSE_LOGO_FILE_NAME);
        if (icon_url != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("VERSION V1.6 - LATVIA (PUBLIC BUILD reviewed on 2021-11-21 at 02:00).\n");
            sb.append("This software is free software, \n");
            sb.append("licensed under GNU General Public License (GPL).\n");
            sb.append("\n");
            sb.append("Changelog for V1.6 LATVIA:\n");
            sb.append("\t- Changes to app design.\n");
            sb.append("\t- Feature to recurse through child directories.\n");
            sb.append("\t- Feature to force palette colors (substitution for non matching colors).\n");
            sb.append("\t- Error handling for erroneous dictionaries.\n");
            sb.append("\n");
            sb.append("Changelog for V1.5 KOREANS:\n");
            sb.append("\t- Feature to make custom item group.\n");
            sb.append("\t- Added Collection items item group.\n");
            sb.append("\t- Load dictionary from the menus.\n");
            sb.append("\t- Fixed misaligns when label drawing.\n");
            sb.append("\n");
            sb.append("Changelog for V1.4 JAPANESE:\n");
            sb.append("\t- Feature to put labels for the items.\n");
            sb.append("\t- Fixes for Dictionary & Item Colors.\n");
            sb.append("\n");
            sb.append("Changelog for V1.3 IKAROS:\n");
            sb.append("\t- Fixed FRM read/write again and so the consequent game crashes.\n");
            sb.append("\t- User can choose drawing methods.\n");
            sb.append("\n");
            sb.append("Changelog since V1.2 HUNS:\n");
            sb.append("\t- Changed FRM read/write.\n");
            sb.append("\t- Fixed jumping lockers and safes.\n");
            sb.append("\n");
            sb.append("Changelog since V1.1 GOTHS:\n");
            sb.append("\t- Added preview of palette for FRMs.\n");
            sb.append("\t- Changed default item colors.\n");
            sb.append("\t- Changed description of step 1 in \"How to use\" [Randall].\n");
            sb.append("\t- Fixed bad quality FRM images.\n");
            sb.append("\t- Fixed missing custom color for resources.\n");
            sb.append("\t- Fixed color not updating for buttons [Randall].\n");
            sb.append("\n");
            sb.append("Objective:\n");
            sb.append("The purpose of this program is\n");
            sb.append("colorizing onground and scenery items for FOnline series.\n");
            sb.append("\n");
            sb.append("Designated to use primarily for FOnline 2 Season 3.\n");
            sb.append("\n");
            sb.append("Copyright © 2021\n");
            sb.append("Alexander \"Ermac\" Stojanovich\n");
            ImageIcon icon = new ImageIcon(icon_url);
            JTextArea textArea = new JTextArea(sb.toString(), 15, 50);
            JScrollPane jsp = new JScrollPane(textArea);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, jsp, "About", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }//GEN-LAST:event_infoMenuAboutActionPerformed

    private void btnContainerColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContainerColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Container Color", GUI.cfg.getContainerColor());
        if (color != null) {
            GUI.cfg.setContainerColor(color);
            this.btnContainerColor.setBackground(color);
            this.btnContainerColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnContainerColorActionPerformed

    private void infoMenuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoMenuHelpActionPerformed
        // TODO add your handling code here:        
        URL icon_url = getClass().getResource(RESOURCES_DIR + LOGOX_FILE_NAME);
        if (icon_url != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("- FOR THE PURPOSE ABOUT THIS PROGRAM, \n");
            sb.append("check About. Make sure that you checked it first.\n");
            sb.append("\n");
            sb.append("- Applying highlighther consists of several steps:\n");
            sb.append("\t1. Extract fallout.dat, data01.zip, data03.zip, and data04.zip (in that order) to a single location, replace when prompted.\n");
            sb.append("\tYou will need a tool like DatExplorer to extract fallout.dat.\n");
            sb.append("\t2. Choose input directory where \"art > items\" is,\n");
            sb.append("\t3. Choose output where colorized items are gonna be stored,\n");
            sb.append("\t4. (Optional) Choose custom colors for the item categories,\n");
            sb.append("\t5. (Optional) Choose drawing methods for the item categories,\n");
            sb.append("\t6. Click GO to start the work and wait to complete.\n");
            sb.append("\n");
            sb.append("\t*Repeat steps 2-6 for \"art > scenery_new\".\n");
            sb.append("\n");
            sb.append("[Tip] *If you don't wanna to repeat steps 2-6 when applying highligthter use \"Recursive\".\n");
            sb.append("\n");
            sb.append("[Tip] If you wanna change items categories alter \"Dictionary.txt.\"\n");
            sb.append("Do it with caution.\n");
            sb.append("\n");
            sb.append("[Tip] Remember to back up your files before you start and where you're done.\n");
            sb.append("\n");
            sb.append("[Tip] For best results make sure that chosen colors are colors from the palette.\n");
            sb.append("\n");
            sb.append("[Tip] Dictionary can be shared with other players.\n");
            sb.append("Show them how it's made!");
            sb.append("\n");
            ImageIcon icon = new ImageIcon(icon_url);
            JTextArea textArea = new JTextArea(sb.toString(), 15, 50);
            JScrollPane jsp = new JScrollPane(textArea);
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(this, jsp, "How to use", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }//GEN-LAST:event_infoMenuHelpActionPerformed

    private void btnResourceColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResourceColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Resource Color", GUI.cfg.getResourcesColor());
        if (color != null) {
            GUI.cfg.setResourcesColor(color);
            this.btnResourceColor.setBackground(color);
            this.btnResourceColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnResourceColorActionPerformed

    private void cbOutlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOutlineActionPerformed
        // TODO add your handling code here:
        GUI.cfg.setDrawOutline(this.cbOutline.isSelected());
    }//GEN-LAST:event_cbOutlineActionPerformed

    private void cbFillInteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbFillInteriorActionPerformed
        // TODO add your handling code here:
        GUI.cfg.setFillInterior(this.cbFillInterior.isSelected());
    }//GEN-LAST:event_cbFillInteriorActionPerformed

    private void cbPutLabelsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPutLabelsActionPerformed
        // TODO add your handling code here:
        GUI.cfg.setPutLabels(this.cbPutLabels.isSelected());
    }//GEN-LAST:event_cbPutLabelsActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        // TODO add your handling code here:
        if (task != null) {
            task.setStopped(true);
        }
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnCollectionColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCollectionColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Collection Color", GUI.cfg.getCollectionColor());
        if (color != null) {
            GUI.cfg.setCollectionColor(color);
            this.btnCollectionColor.setBackground(color);
            this.btnCollectionColor.setToolTipText("Red = " + color.getRed()
                    + ", Green = " + color.getGreen() + ", Blue = " + color.getBlue());
        }
    }//GEN-LAST:event_btnCollectionColorActionPerformed

    private void fileMenuLoadDictionaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuLoadDictionaryActionPerformed
        // TODO add your handling code here:
        int returnVal = fileLoadDictionary.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            boolean ok = Highligther.loadDictionary(fileLoadDictionary.getSelectedFile());
            if (ok) {
                JOptionPane.showMessageDialog(GUI.this, "Dictionary successfully loaded!", "Dictionary", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JTextArea textArea = new JTextArea(Highligther.getDictionaryErrorMessage(), 15, 50);
                JScrollPane jsp = new JScrollPane(textArea);
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(GUI.this, jsp, "Dictionary Errors", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_fileMenuLoadDictionaryActionPerformed

    private void cbRecurseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRecurseActionPerformed
        // TODO add your handling code here:
        GUI.cfg.setRecursive(this.cbRecurse.isSelected());
    }//GEN-LAST:event_cbRecurseActionPerformed

    private void cbForcePalColsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbForcePalColsActionPerformed
        // TODO add your handling code here:
        GUI.cfg.setForcePaletteColors(this.cbForcePalCols.isSelected());
    }//GEN-LAST:event_cbForcePalColsActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FO2HLogger.init(args.length > 0 && args[0].equals("-debug"));
        cfg.readConfigFile();
        Palette.load("Fallout Palette.act");
        if (cfg.isLoadDictionaryOnStart()) {
            Highligther.initDictionary();
        }
        try {
            UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                GUI gui = new GUI();
                gui.setVisible(true);
                gui.pack();
                FO2HLogger.reportInfo("Higlighter initialized!", null);
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                cfg.writeConfigFile();
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookColor;
    private javax.swing.JButton btnChooseInPath;
    private javax.swing.JButton btnChoosePathOut;
    private javax.swing.JButton btnCollectionColor;
    private javax.swing.JButton btnContainerColor;
    private javax.swing.JButton btnGo;
    private javax.swing.JButton btnImplantColor;
    private javax.swing.JButton btnOreColor;
    private javax.swing.JButton btnResourceColor;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnT0Color;
    private javax.swing.JButton btnT1Color;
    private javax.swing.JButton btnT2Color;
    private javax.swing.JButton btnT3Color;
    private javax.swing.JButton btnT4Color;
    private javax.swing.JButton btnUnusedColor;
    private javax.swing.JCheckBox cbFillInterior;
    private javax.swing.JCheckBox cbForcePalCols;
    private javax.swing.JCheckBox cbOutline;
    private javax.swing.JCheckBox cbPutLabels;
    private javax.swing.JCheckBox cbRecurse;
    private javax.swing.JFileChooser fileChooserInput;
    private javax.swing.JFileChooser fileChooserOutput;
    private javax.swing.JFileChooser fileLoadDictionary;
    private javax.swing.JMenuItem fileMenuExit;
    private javax.swing.JMenuItem fileMenuLoadDictionary;
    private javax.swing.JMenuItem infoMenuAbout;
    private javax.swing.JMenuItem infoMenuHelp;
    private javax.swing.JLabel lblBookColor;
    private javax.swing.JLabel lblCollectionColor;
    private javax.swing.JLabel lblContainerColor;
    private javax.swing.JLabel lblImplantColor;
    private javax.swing.JLabel lblInput;
    private javax.swing.JLabel lblOreColor;
    private javax.swing.JLabel lblOutput;
    private javax.swing.JLabel lblResourceColor;
    private javax.swing.JLabel lblT0Color;
    private javax.swing.JLabel lblT1Color;
    private javax.swing.JLabel lblT2Color;
    private javax.swing.JLabel lblT3Color;
    private javax.swing.JLabel lblT4Color;
    private javax.swing.JLabel lblUnusedColor;
    private javax.swing.JMenuBar mainMenu;
    private javax.swing.JMenu mainMenuFile;
    private javax.swing.JMenu mainMenuInfo;
    private javax.swing.JPanel pnl01Effects;
    private javax.swing.JPanel pnl02Unused;
    private javax.swing.JPanel pnl03Progress;
    private javax.swing.JPanel pnlFilePaths;
    private javax.swing.JPanel pnlOutlineColors;
    private javax.swing.JPanel pnlPalCols;
    private javax.swing.JPanel pnlPalette;
    private javax.swing.JPanel pnlWork;
    private javax.swing.JProgressBar progBarWork;
    private javax.swing.JTextField txtFldInPath;
    private javax.swing.JTextField txtFldOutPath;
    // End of variables declaration//GEN-END:variables
}
