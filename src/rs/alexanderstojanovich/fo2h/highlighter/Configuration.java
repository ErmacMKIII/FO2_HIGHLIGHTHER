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
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class Configuration {

    private static final String CONFIG_PATH = "fo2_highlighter.ini";

    private static final String INPUT_DIR_PATH = "";
    private static final String OUTPUT_DIR_PATH = "";

    private static final Color DEF_IMPLANT_COLOR = new Color(252, 0, 0);

    private static final Color DEF_T4_COLOR = new Color(196, 96, 168);
    private static final Color DEF_T3_COLOR = new Color(60, 248, 0);
    private static final Color DEF_T2_COLOR = new Color(48, 88, 140);
    private static final Color DEF_T1_COLOR = new Color(100, 60, 20);
    private static final Color DEF_T0_COLOR = new Color(128, 128, 128);

    private static final Color DEF_BOOK_COLOR = new Color(228, 216, 12);
    private static final Color DEF_ORE_COLOR = new Color(56, 12, 52);
    private static final Color DEF_RESOURCE_COLOR = new Color(0, 108, 0);
    private static final Color DEF_CONTAINER_COLOR = new Color(220, 108, 0);

    private static final Color DEF_UNUSED_COLOR = new Color(252, 176, 176);

    private Color implantColor = DEF_IMPLANT_COLOR;
    private Color t4Color = DEF_T4_COLOR;
    private Color t3Color = DEF_T3_COLOR;
    private Color t2Color = DEF_T2_COLOR;
    private Color t1Color = DEF_T1_COLOR;
    private Color t0Color = DEF_T0_COLOR;
    private Color bookColor = DEF_BOOK_COLOR;
    private Color oreColor = DEF_ORE_COLOR;
    private Color resourcesColor = DEF_RESOURCE_COLOR;
    private Color containerColor = DEF_CONTAINER_COLOR;
    private Color unusedColor = DEF_UNUSED_COLOR;

    private File inDir = new File(INPUT_DIR_PATH);
    private File outDir = new File(OUTPUT_DIR_PATH);

    private boolean drawOutline = true;
    private boolean fillInterior = false;
    private boolean putLabels = false;

    private String fontName = Font.MONOSPACED;
    private int fontStyle = Font.BOLD;
    private int fontSize = 12;

    private Color readRGB(String str) {
        String[] split = str.trim().split("^\\(|,|\\)$");
        int red = Integer.parseInt(split[1]);
        int green = Integer.parseInt(split[2]);
        int blue = Integer.parseInt(split[3]);
        Color color = new Color(red, green, blue);
        return color;
    }

    private String writeRGB(Color color) {
        return "(" + color.getRed() + ", " + color.getGreen() + ", " + color.getBlue() + ")";
    }

    /**
     * Reads configuration from the .ini file
     */
    public void readConfigFile() {
        File cfg = new File(CONFIG_PATH);
        if (cfg.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(cfg));
                String line;
                while ((line = br.readLine()) != null) {
                    // replace all white space chars with empty string
                    String[] words = line.replaceAll("\\s", "").split("=");
                    if (words.length == 2) {
                        switch (words[0]) {
                            case "InputDirPath":
                                inDir = new File(words[1].replaceAll("\"", ""));
                                break;
                            case "OutputDirPath":
                                outDir = new File(words[1].replaceAll("\"", ""));
                                break;
                            case "ImplantColor":
                                implantColor = readRGB(words[1]);
                                break;
                            case "T4Color":
                                t4Color = readRGB(words[1]);
                                break;
                            case "T3Color":
                                t3Color = readRGB(words[1]);
                                break;
                            case "T2Color":
                                t2Color = readRGB(words[1]);
                                break;
                            case "T1Color":
                                t1Color = readRGB(words[1]);
                                break;
                            case "T0Color":
                                t0Color = readRGB(words[1]);
                                break;
                            case "BookColor":
                                bookColor = readRGB(words[1]);
                                break;
                            case "OreColor":
                                oreColor = readRGB(words[1]);
                                break;
                            case "ResourceColor":
                                resourcesColor = readRGB(words[1]);
                                break;
                            case "ContainerColor":
                                containerColor = readRGB(words[1]);
                                break;
                            case "UnusedColor":
                                unusedColor = readRGB(words[1]);
                                break;
                            case "DrawOutline":
                                drawOutline = Boolean.parseBoolean(words[1]);
                                break;
                            case "FillInterior":
                                fillInterior = Boolean.parseBoolean(words[1]);
                                break;
                            case "PutLabels":
                                putLabels = Boolean.parseBoolean(words[1]);
                                break;
                            case "FontName":
                                fontName = words[1];
                                break;
                            case "FontStyle":
                                fontStyle = Integer.parseInt(words[1]);
                                break;
                            case "FontSize":
                                fontSize = Integer.parseInt(words[1]);
                                break;
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                FO2HLogger.reportError(ex.getMessage(), ex);
            } catch (IOException ex) {
                FO2HLogger.reportError(ex.getMessage(), ex);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        FO2HLogger.reportError(ex.getMessage(), ex);
                    }
                }
            }
        }
    }

    /**
     * Writes configuration to the .ini file (on app exit)
     */
    public void writeConfigFile() {
        File cfg = new File(CONFIG_PATH);
        if (cfg.exists()) {
            cfg.delete();
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(cfg);
            pw.println("InputDirPath = " + "\"" + inDir.getPath() + "\"");
            pw.println("OutputDirPath = " + "\"" + outDir.getPath() + "\"");
            pw.println();
            pw.println("ImplantColor = " + writeRGB(implantColor));
            pw.println("T4Color = " + writeRGB(t4Color));
            pw.println("T3Color = " + writeRGB(t3Color));
            pw.println("T2Color = " + writeRGB(t2Color));
            pw.println("T1Color = " + writeRGB(t1Color));
            pw.println("T0Color = " + writeRGB(t0Color));
            pw.println();
            pw.println("BookColor = " + writeRGB(bookColor));
            pw.println("OreColor = " + writeRGB(oreColor));
            pw.println("ResourceColor = " + writeRGB(resourcesColor));
            pw.println("ContainerColor = " + writeRGB(containerColor));
            pw.println();
            pw.println("UnusuedColor = " + writeRGB(unusedColor));
            pw.println();
            pw.println("DrawOutline = " + drawOutline);
            pw.println("FillInterior = " + fillInterior);
            pw.println();
            pw.println("PutLabels = " + putLabels);
            pw.println("FontName = " + fontName);
            pw.println("FontStyle = " + fontStyle);
            pw.println("FontSize = " + fontSize);
        } catch (FileNotFoundException ex) {
            FO2HLogger.reportError(ex.getMessage(), ex);
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    public void reset() {
        // set defaults
        implantColor = DEF_IMPLANT_COLOR;
        t4Color = DEF_T4_COLOR;
        t3Color = DEF_T3_COLOR;
        t2Color = DEF_T2_COLOR;
        t1Color = DEF_T1_COLOR;
        t0Color = DEF_T0_COLOR;
        bookColor = DEF_BOOK_COLOR;
        oreColor = DEF_ORE_COLOR;
        resourcesColor = DEF_RESOURCE_COLOR;
        containerColor = DEF_CONTAINER_COLOR;
        unusedColor = DEF_UNUSED_COLOR;

        putLabels = false;
        fontName = Font.MONOSPACED;
        fontStyle = Font.BOLD;
        fontSize = 12;

        drawOutline = true;
        fillInterior = false;
        putLabels = false;

        inDir = new File(INPUT_DIR_PATH);
        outDir = new File(OUTPUT_DIR_PATH);

        // read config file if it exists
        readConfigFile();
    }

    public Color getImplantColor() {
        return implantColor;
    }

    public void setImplantColor(Color implantColor) {
        this.implantColor = implantColor;
    }

    public Color getT4Color() {
        return t4Color;
    }

    public void setT4Color(Color t4Color) {
        this.t4Color = t4Color;
    }

    public Color getT3Color() {
        return t3Color;
    }

    public void setT3Color(Color t3Color) {
        this.t3Color = t3Color;
    }

    public Color getT2Color() {
        return t2Color;
    }

    public void setT2Color(Color t2Color) {
        this.t2Color = t2Color;
    }

    public Color getT1Color() {
        return t1Color;
    }

    public void setT1Color(Color t1Color) {
        this.t1Color = t1Color;
    }

    public Color getT0Color() {
        return t0Color;
    }

    public void setT0Color(Color t0Color) {
        this.t0Color = t0Color;
    }

    public Color getBookColor() {
        return bookColor;
    }

    public void setBookColor(Color bookColor) {
        this.bookColor = bookColor;
    }

    public Color getOreColor() {
        return oreColor;
    }

    public void setOreColor(Color oreColor) {
        this.oreColor = oreColor;
    }

    public Color getResourcesColor() {
        return resourcesColor;
    }

    public void setResourcesColor(Color resourcesColor) {
        this.resourcesColor = resourcesColor;
    }

    public Color getContainerColor() {
        return containerColor;
    }

    public void setContainerColor(Color containerColor) {
        this.containerColor = containerColor;
    }

    public Color getUnusedColor() {
        return unusedColor;
    }

    public void setUnusedColor(Color unusedColor) {
        this.unusedColor = unusedColor;
    }

    public File getInDir() {
        return inDir;
    }

    public void setInDir(File inDir) {
        this.inDir = inDir;
    }

    public File getOutDir() {
        return outDir;
    }

    public void setOutDir(File outDir) {
        this.outDir = outDir;
    }

    public boolean isDrawOutline() {
        return drawOutline;
    }

    public boolean isFillInterior() {
        return fillInterior;
    }

    public void setDrawOutline(boolean drawOutline) {
        this.drawOutline = drawOutline;
    }

    public void setFillInterior(boolean fillInterior) {
        this.fillInterior = fillInterior;
    }

    public boolean isPutLabels() {
        return putLabels;
    }

    public void setPutLabels(boolean putLabels) {
        this.putLabels = putLabels;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public int getFontStyle() {
        return fontStyle;
    }

    public void setFontStyle(int fontStyle) {
        this.fontStyle = fontStyle;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

}
