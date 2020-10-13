/*
 * Copyright (C) 2020 Alexander Stojanovich <coas91@rocketmail.com>
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
import java.awt.Image;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import rs.alexanderstojanovich.fo2h.frm.Palette;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class GUI extends javax.swing.JFrame {

    // cool it's our new logo :)
    private static final String LOGO_FILE_NAME = "fo2hlogo.png";
    // and logox variant with black outline
    private static final String LOGOX_FILE_NAME = "fo2hlogox.png";

    public static final String RESOURCES_DIR = "/rs/alexanderstojanovich/fo2h/res/";
    public static final String LICENSE_LOGO_FILE_NAME = "gplv3_logo.png";

    private final Highligther highligther = new Highligther();
    private File inDir, outDir;

    /**
     * Creates new form GUI
     */
    public GUI() {
        initComponents();
        initFO2HLogos();
        initColors();
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
        this.btnImplantColor.setBackground(highligther.getImplantColor());
        this.btnT4Color.setBackground(highligther.getT4Color());
        this.btnT3Color.setBackground(highligther.getT3Color());
        this.btnT2Color.setBackground(highligther.getT2Color());
        this.btnT1Color.setBackground(highligther.getT1Color());
        this.btnT0Color.setBackground(highligther.getT0Color());
        this.btnBookColor.setBackground(highligther.getBookColor());
        this.btnOreColor.setBackground(highligther.getOreColor());
        this.btnContainerColor.setBackground(highligther.getContainerColor());
        this.btnContainerColor1.setBackground(highligther.getResourcesColor());
        this.btnUnusedColor.setBackground(highligther.getUnusedColor());
    }

    private void fileInOpen() {
        int returnVal = fileChooserInput.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            inDir = fileChooserInput.getSelectedFile();
            txtFldInPath.setText(inDir.getAbsolutePath());
            txtFldInPath.setToolTipText(inDir.getAbsolutePath());
        }
        if (inDir != null && outDir != null) {
            btnGo.setEnabled(true);
        }
    }

    private void fileOutOpen() {
        int returnVal = fileChooserOutput.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            outDir = fileChooserOutput.getSelectedFile();
            txtFldOutPath.setText(outDir.getAbsolutePath());
            txtFldOutPath.setToolTipText(outDir.getAbsolutePath());
        }
        if (inDir != null && outDir != null) {
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

        fileChooserInput = new javax.swing.JFileChooser();
        fileChooserOutput = new javax.swing.JFileChooser();
        pnlFilePaths = new javax.swing.JPanel();
        lblOutput = new javax.swing.JLabel();
        btnChooseInPath = new javax.swing.JButton();
        lblInput = new javax.swing.JLabel();
        txtFldInPath = new javax.swing.JTextField();
        btnChoosePathOut = new javax.swing.JButton();
        txtFldOutPath = new javax.swing.JTextField();
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
        btnContainerColor1 = new javax.swing.JButton();
        lblContainerColor = new javax.swing.JLabel();
        btnContainerColor = new javax.swing.JButton();
        lblUnusedColor = new javax.swing.JLabel();
        btnUnusedColor = new javax.swing.JButton();
        btnGo = new javax.swing.JButton();
        progBarWork = new javax.swing.JProgressBar();
        mainMenu = new javax.swing.JMenuBar();
        mainMenuFile = new javax.swing.JMenu();
        fileMenuReset = new javax.swing.JMenuItem();
        fileMenuExit = new javax.swing.JMenuItem();
        mainMenuInfo = new javax.swing.JMenu();
        infoMenuAbout = new javax.swing.JMenuItem();
        infoMenuHelp = new javax.swing.JMenuItem();

        fileChooserInput.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        fileChooserOutput.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileChooserOutput.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FOnline2 Highlighter - GOTHS");
        setResizable(false);

        pnlFilePaths.setBorder(javax.swing.BorderFactory.createTitledBorder("Directory Paths"));

        lblOutput.setText("Output data directory:");

        btnChooseInPath.setText("Input dir...");
        btnChooseInPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseInPathActionPerformed(evt);
            }
        });

        lblInput.setText("Input data directory:");

        txtFldInPath.setEditable(false);

        btnChoosePathOut.setText("Output dir...");
        btnChoosePathOut.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChoosePathOutActionPerformed(evt);
            }
        });

        txtFldOutPath.setEditable(false);

        javax.swing.GroupLayout pnlFilePathsLayout = new javax.swing.GroupLayout(pnlFilePaths);
        pnlFilePaths.setLayout(pnlFilePathsLayout);
        pnlFilePathsLayout.setHorizontalGroup(
            pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilePathsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblInput, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblOutput))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFldOutPath, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(txtFldInPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnChooseInPath, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChoosePathOut))
                .addContainerGap())
        );
        pnlFilePathsLayout.setVerticalGroup(
            pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFilePathsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFldInPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblInput)
                    .addComponent(btnChooseInPath))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlFilePathsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFldOutPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOutput)
                    .addComponent(btnChoosePathOut))
                .addContainerGap())
        );

        pnlOutlineColors.setBorder(javax.swing.BorderFactory.createTitledBorder("Outline Colors"));
        pnlOutlineColors.setLayout(new java.awt.GridLayout(11, 2, 2, 2));

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

        btnContainerColor1.setBorder(null);
        btnContainerColor1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContainerColor1ActionPerformed(evt);
            }
        });
        pnlOutlineColors.add(btnContainerColor1);

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

        btnGo.setForeground(new java.awt.Color(51, 255, 51));
        btnGo.setText("GO");
        btnGo.setToolTipText("Get the highlighter working");
        btnGo.setEnabled(false);
        btnGo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });

        progBarWork.setStringPainted(true);

        mainMenuFile.setText("File");

        fileMenuReset.setText("Reset");
        fileMenuReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileMenuResetActionPerformed(evt);
            }
        });
        mainMenuFile.add(fileMenuReset);

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnGo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(progBarWork, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlOutlineColors, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlFilePaths, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlFilePaths, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(pnlOutlineColors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progBarWork, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 23, Short.MAX_VALUE))
        );

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
        if (!inDir.exists()) {
            JOptionPane.showMessageDialog(GUI.this, "Input directory doesn't exist!", "Input directory error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                while (highligther.getProgress() < 100.0f) {
                    progBarWork.setValue(Math.round(highligther.getProgress()));
                    progBarWork.validate();
                }
            }
        };
        timer.schedule(timerTask, 0L);
        Thread workThread = new Thread("Work Thread") {
            @Override
            public void run() {
                fileMenuReset.setEnabled(false);
                highligther.work(inDir, outDir);
                progBarWork.setValue(Math.round(highligther.getProgress()));
                progBarWork.validate();
                JOptionPane.showMessageDialog(GUI.this, "Highlighter work successfully finished!", "Work Finished", JOptionPane.INFORMATION_MESSAGE);
                timer.cancel();
                fileMenuReset.setEnabled(true);
            }
        };
        workThread.start();
    }//GEN-LAST:event_btnGoActionPerformed

    private void btnImplantColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImplantColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Implant Color", this.highligther.getImplantColor());
        if (color != null) {
            this.highligther.setImplantColor(color);
        }
    }//GEN-LAST:event_btnImplantColorActionPerformed

    private void btnT4ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT4ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 4 Color", this.highligther.getT4Color());
        if (color != null) {
            this.highligther.setT4Color(color);
        }
    }//GEN-LAST:event_btnT4ColorActionPerformed

    private void btnT3ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT3ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 3 Color", this.highligther.getT3Color());
        if (color != null) {
            this.highligther.setT3Color(color);
        }
    }//GEN-LAST:event_btnT3ColorActionPerformed

    private void btnT2ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT2ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 2 Color", this.highligther.getT2Color());
        if (color != null) {
            this.highligther.setT2Color(color);
        }
    }//GEN-LAST:event_btnT2ColorActionPerformed

    private void btnT1ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT1ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 1 Color", this.highligther.getT1Color());
        if (color != null) {
            this.highligther.setT1Color(color);
        }
    }//GEN-LAST:event_btnT1ColorActionPerformed

    private void btnT0ColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnT0ColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Tier 0 Color", this.highligther.getT0Color());
        if (color != null) {
            this.highligther.setT0Color(color);
        }
    }//GEN-LAST:event_btnT0ColorActionPerformed

    private void btnBookColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBookColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Book Color", this.highligther.getBookColor());
        if (color != null) {
            this.highligther.setBookColor(color);
        }
    }//GEN-LAST:event_btnBookColorActionPerformed

    private void btnOreColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOreColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Ore Color", this.highligther.getOreColor());
        if (color != null) {
            this.highligther.setOreColor(color);
        }
    }//GEN-LAST:event_btnOreColorActionPerformed

    private void btnUnusedColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnusedColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Ore Color", this.highligther.getUnusedColor());
        if (color != null) {
            this.highligther.setUnusedColor(color);
        }
    }//GEN-LAST:event_btnUnusedColorActionPerformed

    private void fileMenuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuExitActionPerformed
        // TODO add your handling code here:
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }//GEN-LAST:event_fileMenuExitActionPerformed

    private void fileMenuResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileMenuResetActionPerformed
        // TODO add your handling code here:
        Palette.reset();
        Palette.load("Fallout Palette.act");
        highligther.reset();
        initColors();
        txtFldInPath.setText("");
        txtFldOutPath.setText("");
        btnGo.setEnabled(false);
    }//GEN-LAST:event_fileMenuResetActionPerformed

    private void infoMenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoMenuAboutActionPerformed
        // TODO add your handling code here:
        URL icon_url = getClass().getResource(RESOURCES_DIR + LICENSE_LOGO_FILE_NAME);
        if (icon_url != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><b>VERSION v1.1 - GOTHS (PUBLIC BUILD reviewed on 2020-10-13 at 17:00).</b></html>\n");
            sb.append("<html><b>This software is free software, </b></html>\n");
            sb.append("<html><b>licensed under GNU General Public License (GPL).</b></html>\n");
            sb.append("\n");
            sb.append("Changelog:\n");
            sb.append("\t- Fixed bad quality FRM images.\n");
            sb.append("\t- Fixed missing custom color for resources.\n");
            sb.append("\n");
            sb.append("Objective:\n");
            sb.append("\tThe purpose of this program is\n");
            sb.append("\tcolorizing onground and scenery items for FOnline series.\n");
            sb.append("\n");
            sb.append("\tDesignated to use primarily for FOnline2 Season 3.\n");
            sb.append("\n");
            sb.append("<html><b>Copyright © 2020</b></html>\n");
            sb.append("<html><b>Alexander \"Ermac\" Stojanovich</b></html>\n");
            ImageIcon icon = new ImageIcon(icon_url);
            JOptionPane.showMessageDialog(this, sb.toString(), "About", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }//GEN-LAST:event_infoMenuAboutActionPerformed

    private void btnContainerColorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContainerColorActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Container Color", this.highligther.getContainerColor());
        if (color != null) {
            this.highligther.setContainerColor(color);
        }
    }//GEN-LAST:event_btnContainerColorActionPerformed

    private void infoMenuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoMenuHelpActionPerformed
        // TODO add your handling code here:        
        URL icon_url = getClass().getResource(RESOURCES_DIR + LOGOX_FILE_NAME);
        if (icon_url != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<html><b>- FOR THE PURPOSE ABOUT THIS PROGRAM, </b></html>\n");
            sb.append("<html><b>check About. Make sure that you checked it first.</b></html>\n");
            sb.append("\n");
            sb.append("- Applying highlighther consists of several steps:\n");
            sb.append("\t1. You need to find raw game files (.png and .FRM) for highlighther tool,\n");
            sb.append("\t2. Choose input directory where \"art > items\" is,\n");
            sb.append("\t3. Choose output where colorized items are gonna be stored,\n");
            sb.append("\t4. (Optional) Choose custom colors for the item categories,\n");
            sb.append("\t5. Click GO to start the work and wait to complete.\n");
            sb.append("\n");
            sb.append("\tRepeat steps 1-5 for \"art > scenery_new\".\n");
            sb.append("\n");
            sb.append("\t(Optional) If you wanna change items categories alter \"Dictionary.txt.\"\n");
            sb.append("\tDo it with caution. Remember to back up your files before you start and where you're done.\n");
            sb.append("\n");
            ImageIcon icon = new ImageIcon(icon_url);
            JOptionPane.showMessageDialog(this, sb.toString(), "How to use", JOptionPane.INFORMATION_MESSAGE, icon);
        }
    }//GEN-LAST:event_infoMenuHelpActionPerformed

    private void btnContainerColor1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContainerColor1ActionPerformed
        // TODO add your handling code here:
        Color color = JColorChooser.showDialog(this, "Choose Resource Color", this.highligther.getResourcesColor());
        if (color != null) {
            this.highligther.setResourcesColor(color);
        }
    }//GEN-LAST:event_btnContainerColor1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        FO2HLogger.init(args.length > 0 && args[0].equals("-debug"));
        Palette.load("Fallout Palette.act");
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
                new GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBookColor;
    private javax.swing.JButton btnChooseInPath;
    private javax.swing.JButton btnChoosePathOut;
    private javax.swing.JButton btnContainerColor;
    private javax.swing.JButton btnContainerColor1;
    private javax.swing.JButton btnGo;
    private javax.swing.JButton btnImplantColor;
    private javax.swing.JButton btnOreColor;
    private javax.swing.JButton btnT0Color;
    private javax.swing.JButton btnT1Color;
    private javax.swing.JButton btnT2Color;
    private javax.swing.JButton btnT3Color;
    private javax.swing.JButton btnT4Color;
    private javax.swing.JButton btnUnusedColor;
    private javax.swing.JFileChooser fileChooserInput;
    private javax.swing.JFileChooser fileChooserOutput;
    private javax.swing.JMenuItem fileMenuExit;
    private javax.swing.JMenuItem fileMenuReset;
    private javax.swing.JMenuItem infoMenuAbout;
    private javax.swing.JMenuItem infoMenuHelp;
    private javax.swing.JLabel lblBookColor;
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
    private javax.swing.JPanel pnlFilePaths;
    private javax.swing.JPanel pnlOutlineColors;
    private javax.swing.JProgressBar progBarWork;
    private javax.swing.JTextField txtFldInPath;
    private javax.swing.JTextField txtFldOutPath;
    // End of variables declaration//GEN-END:variables
}
