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
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import javax.imageio.ImageIO;
import javax.swing.SwingWorker;
import rs.alexanderstojanovich.fo2h.frm.FRM;
import rs.alexanderstojanovich.fo2h.frm.ImageData;
import rs.alexanderstojanovich.fo2h.frm.Palette;
import rs.alexanderstojanovich.fo2h.util.ColorSample;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class Highligther extends SwingWorker<Void, Void> {

    private static final float LUMA_RED_COEFF = 0.2126f;
    private static final float LUMA_GREEN_COEFF = 0.7152f;
    private static final float LUMA_BLUE_COEFF = 0.0722f;

    private static final String TEXTFILE = "Dictionary.txt";

    private final Configuration config;

    private final Font myFont;

    public static final Map<String, Obj> DICTIONARY = new HashMap<>();
    public static final Map<String, String> MAPPED_BY = new HashMap<>();

    private boolean stopped = false;

    private static String dictionaryErrorMessage;

    public Highligther(Configuration config) {
        this.config = config;
        this.myFont = new Font(config.getFontName(), config.getFontStyle(), config.getFontSize());
    }

    /**
     * Init with default Dictionary
     *
     * @return is initialization OK
     */
    public static boolean initDictionary() {
        DICTIONARY.clear();
        dictionaryErrorMessage = "";
        StringBuilder sb = new StringBuilder();
        boolean ok = true;
        BufferedReader br = null;
        int lineNum = 0;

        final File dictionary = new File(TEXTFILE);

        if (!dictionary.exists()) {
            FO2HLogger.reportError("File " + dictionary.getAbsolutePath() + " does not exist!", null);
            dictionaryErrorMessage = "File " + dictionary.getAbsolutePath() + " does not exist!";
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream(TEXTFILE);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String line;
            Obj obj = null;
            boolean labeled = false;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().equals("@LABELED")) {
                    labeled = true;
                } else if (line.startsWith("@") && !line.startsWith("@LABELED") && !line.startsWith("@CUSTOM")) {
                    obj = PredefObj.valueOf(line.trim().substring(1));
                    obj.setLabeled(labeled);
                    labeled = false;
                } else if (line.startsWith("@CUSTOM")) {
                    String[] items = line.trim().split("\\s+");
                    if (items.length == 4
                            && items[0].equals("@CUSTOM")
                            && items[1].matches("^[0-9]+$")
                            && items[2].matches("^[0-9]+$")
                            && items[3].matches("^[0-9]+$")) {
                        Color col = new Color(Integer.parseInt(items[1]), Integer.parseInt(items[2]), Integer.parseInt(items[3]));
                        obj = new CustomObj(col);
                        obj.setLabeled(labeled);
                    } else {
                        sb.append("Line ").append(lineNum).append(" : Invalid Color Definition!\n");
                        FO2HLogger.reportError("Line " + lineNum + " : Invalid Color Definition!", null);
                        ok = false;
                    }
                    labeled = false;
                } else if (obj != null) {
                    String[] things = line.trim().replaceAll("\"", "").split("=>", -1);
                    DICTIONARY.put(things[0].trim(), obj);
                    if (obj.isLabeled() && things.length == 2) {
                        MAPPED_BY.put(things[0].trim(), things[1].trim());
                    } else if (things.length > 2) {
                        sb.append("Line ").append(lineNum).append(" : Invalid \"MAPPED BY =>\" Definition!\n");
                        FO2HLogger.reportError("Line " + lineNum + " : Invalid \"MAPPED BY =>\" Definition!", null);
                        ok = false;
                    }
                } else if (!line.replaceAll("\\s+", "").isEmpty()) {
                    sb.append("Line ").append(lineNum).append(" : Invalid Definition!\n");
                    FO2HLogger.reportError("Line " + lineNum + " : Invalid Definition!", null);
                    ok = false;
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

        dictionaryErrorMessage = sb.toString();
        FO2HLogger.reportInfo("Dictionary initialized " + (ok ? "successfully!" : " with errors!"), null);
        return ok;
    }

    /**
     * Load Dictionary from the file
     *
     * @param dictionary file containing Dictionary
     * @return is load OK
     */
    public static boolean loadDictionary(File dictionary) {
        DICTIONARY.clear();
        dictionaryErrorMessage = "";
        StringBuilder sb = new StringBuilder();
        boolean ok = true;
        BufferedReader br = null;
        int lineNum = 0;

        if (!dictionary.exists()) {
            FO2HLogger.reportError("File " + dictionary.getAbsolutePath() + " does not exist!", null);
            dictionaryErrorMessage = "File " + dictionary.getAbsolutePath() + " does not exist!";
            return false;
        }

        try {
            FileInputStream fis = new FileInputStream(dictionary);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            br = new BufferedReader(isr);
            String line;
            Obj obj = null;
            boolean labeled = false;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().equals("@LABELED")) {
                    labeled = true;
                } else if (line.startsWith("@") && !line.startsWith("@LABELED") && !line.startsWith("@CUSTOM")) {
                    obj = PredefObj.valueOf(line.trim().substring(1));
                    obj.setLabeled(labeled);
                    labeled = false;
                } else if (line.startsWith("@CUSTOM")) {
                    String[] items = line.trim().split("\\s+");
                    if (items.length == 4
                            && items[0].equals("@CUSTOM")
                            && items[1].matches("^[0-9]+$")
                            && items[2].matches("^[0-9]+$")
                            && items[3].matches("^[0-9]+$")) {
                        Color col = new Color(Integer.parseInt(items[1]), Integer.parseInt(items[2]), Integer.parseInt(items[3]));
                        obj = new CustomObj(col);
                        obj.setLabeled(labeled);
                    } else {
                        sb.append("Line ").append(lineNum).append(" : Invalid Color Definition!\n");
                        FO2HLogger.reportError("Line " + lineNum + " : Invalid Color Definition!", null);
                        ok = false;
                    }
                    labeled = false;
                } else if (obj != null) {
                    String[] things = line.trim().replaceAll("\"", "").split("=>", -1);
                    DICTIONARY.put(things[0].trim(), obj);
                    if (obj.isLabeled() && things.length == 2) {
                        MAPPED_BY.put(things[0].trim(), things[1].trim());
                    } else if (things.length > 2) {
                        sb.append("Line ").append(lineNum).append(" : Invalid \"MAPPED BY =>\" Definition!\n");
                        FO2HLogger.reportError("Line " + lineNum + " : Invalid \"MAPPED BY =>\" Definition!", null);
                        ok = false;
                    }
                } else if (!line.replaceAll("\\s+", "").isEmpty()) {
                    sb.append("Line ").append(lineNum).append(" : Invalid Definition!\n");
                    FO2HLogger.reportError("Line " + lineNum + " : Invalid Definition!", null);
                    ok = false;
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

        dictionaryErrorMessage = sb.toString();
        FO2HLogger.reportInfo("Dictionary initialized " + (ok ? "successfully!" : " with errors!"), null);
        return ok;
    }

    /**
     * Gets item color from the dictionary.
     *
     * @param extLessFilename filename without extension as it is written in the
     * dictionary
     * @return item color
     */
    private Color getItemColor(String extLessFilename) {
        Obj obj = DICTIONARY.getOrDefault(extLessFilename, PredefObj.UNUSED);
        Color color = obj.getColor(config);
        if (config.isForcePaletteColors()) {
            color = Palette.substitute(color);
        }
        return color;
    }

    /**
     * Remove blue color from the image. Reason - blue is transparent for
     * Fallout.
     *
     * @param img parsed image
     */
    public static final void removeBlue(BufferedImage img) {
        for (int px = 0; px < img.getWidth(); px++) {
            for (int py = 0; py < img.getHeight(); py++) {
                Color pixCol = new Color(img.getRGB(px, py), true);
                if (pixCol.equals(Color.BLUE)) {
                    img.setRGB(px, py, 0);
                }
            }
        }
    }

    /**
     * Create outline around the image.
     *
     * @param img parsed image
     * @param outlineColor color of the outline (around)
     * @param fillInterior fill interior of the outline
     * @param drawOutline draws outline around the image
     */
    public static final void createOutline(BufferedImage img, Color outlineColor, boolean fillInterior, boolean drawOutline) {
        // outline & fill interior effect
        WritableRaster wr = img.copyData(null);
        for (int px = 0; px < img.getWidth(); px++) {
            for (int py = 0; py < img.getHeight(); py++) {
                Color pixCol = new Color(img.getRGB(px, py), true);
                // writtable raster must be associated with ARGB image!!
                ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                if (pixCol.getAlpha() > 0 && fillInterior) {
                    final Color itemCol = outlineColor;
                    float luma = (pixCol.getRed() * LUMA_RED_COEFF + pixCol.getGreen() * LUMA_GREEN_COEFF + pixCol.getBlue() * LUMA_BLUE_COEFF) / 255.0f;
                    int outRed = Math.min(Math.max(Math.round(luma * itemCol.getRed()), 0), 255);
                    int outGreen = Math.min(Math.max(Math.round(luma * itemCol.getGreen()), 0), 255);
                    int outBlue = Math.min(Math.max(Math.round(luma * itemCol.getBlue()), 0), 255);
                    final Color outCol = new Color(outRed, outGreen, outBlue);
                    img.setRGB(px, py, outCol.getRGB());
                } else if (pixCol.getAlpha() == 0 && cs.getAlpha() > 0 && drawOutline) {
                    img.setRGB(px, py, outlineColor.getRGB());
                }
            }
        }
    }

    /**
     * Puts label (a sign) bound to the image.
     *
     * @param img parsed image to put a label on
     * @param font parsed font
     * @param label draw string
     * @param color string color
     * @param fillInterior fill interior option
     * @return result image with label
     */
    public static final BufferedImage putLabel(BufferedImage img, Font font, String label, Color color, boolean fillInterior) {
        final int labelHeight = 2 * font.getSize();

        FontRenderContext frc = new FontRenderContext(null, true, true);

        Rectangle2D bounds = font.getStringBounds(label, frc);
        int fntWidth = (int) Math.round(bounds.getWidth());
        int fntHeight = (int) Math.round(bounds.getHeight());

        BufferedImage result = new BufferedImage(Math.max(fntWidth, img.getWidth()) + 2, (fntHeight + labelHeight + img.getHeight() + 1) + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D resG2D = result.createGraphics();

        resG2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        resG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        resG2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        resG2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        resG2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        resG2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        resG2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        resG2D.setColor(color);
        resG2D.drawRoundRect(1, 1, fntWidth, fntHeight, 2, 2);
        if (fillInterior) {
            float luma = (color.getRed() * LUMA_RED_COEFF + color.getGreen() * LUMA_GREEN_COEFF + color.getBlue() * LUMA_BLUE_COEFF) / 255.0f;
            Color grey = new Color(0.5f * luma, 0.5f * luma, 0.5f * luma, 1.0f);
            resG2D.setColor(grey);
            resG2D.fillRoundRect(1, 1, fntWidth, fntHeight, 1, 1);
        }
        resG2D.setColor(color);
        resG2D.setFont(font);

        resG2D.translate(0, -Math.round(bounds.getY()));
        resG2D.drawString(new String(label.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8), 1.0f, 1.0f);
        resG2D.translate(0, Math.round(bounds.getY()));

        resG2D.drawLine(fntWidth / 2, fntHeight + 1, fntWidth / 2, fntHeight + labelHeight + img.getHeight() / 2 - 1);
        resG2D.drawImage(img, (result.getWidth() - img.getWidth()) / 2, result.getHeight() - img.getHeight(), null);

        return result;
    }

    /**
     * Start highlighter working.
     *
     */
    public void work() {
        float oldProgress = 0.0f, progress = 0.0f;
        progress = 0.0f;
        if (DICTIONARY.isEmpty()) {
            progress = 100.0f;
            firePropertyChange("progress", oldProgress, progress);
            return;
        }

        if (!config.getInDir().exists()) {
            progress = 100.0f;
            firePropertyChange("progress", oldProgress, progress);
            return;
        }

        if (!config.getOutDir().exists()) {
            config.getOutDir().mkdirs();
        }

        stopped = false;

        FO2HLogger.reportInfo("Starting Higlighter work", null);
        if (config.getInDir().isDirectory()) {
            File[] fileArray = config.isRecursive() ? buildTree(config.getInDir()) : config.getInDir().listFiles();
            FO2HLogger.reportInfo("Processing " + fileArray.length + " files..", null);
            for (File srcFile : fileArray) {
                if (stopped) {
                    FO2HLogger.reportInfo("Highlighter stopped!", null);
                    break;
                }

                // if file is fofrm copy it to the output                                      
                if (srcFile.getName().toLowerCase().endsWith(".fofrm")) {
                    File dstFile = null;
                    if (config.isRecursive()) {
                        File dirs = srcFile;
                        StringBuilder sb = new StringBuilder();
                        while (!dirs.equals(config.getInDir())) {
                            dirs = dirs.getParentFile();
                            sb.insert(0, dirs.getName());
                            sb.insert(0, File.separator);
                        }
                        dstFile = new File(config.getOutDir() + File.separator + sb.toString() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                    } else {
                        dstFile = new File(config.getOutDir() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                    }
                    try {
                        Files.copy(srcFile.toPath(), dstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException ex) {
                        FO2HLogger.reportError(ex.getMessage(), ex);
                    }
                } else { // if it's png or FRM file
                    // get extensionless filename
                    String extLessFilename = srcFile.getName().replaceFirst("[.][^.]+$", "");
                    //----------------------------------------------------------
                    if (srcFile.getName().toLowerCase().endsWith(".frm")) {
                        FRM srcFRM = new FRM(srcFile);
                        //-----------------------------------------------------
                        List<ImageData> frames = srcFRM.getFrames();
                        final BufferedImage[] imgSrc = new BufferedImage[frames.size()];
                        final BufferedImage[] imgDst = new BufferedImage[frames.size()];
                        final int[] frameOffsetsX = new int[frames.size()];
                        final int[] frameOffsetsY = new int[frames.size()];

                        for (int i = 0; i < frames.size(); i++) {
                            imgSrc[i] = frames.get(i).toBufferedImage();
                            imgDst[i] = new BufferedImage(imgSrc[i].getWidth() + 2, imgSrc[i].getHeight() + 2, BufferedImage.TYPE_INT_ARGB);

                            Graphics2D graphics2D = imgDst[i].createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc[i], 1, 1, null);

                            // blue color removal
                            removeBlue(imgDst[i]);
                            // outline & fill interior effect
                            createOutline(imgDst[i], getItemColor(extLessFilename), config.isFillInterior(), config.isDrawOutline());

                            // label
                            Obj obj = DICTIONARY.get(extLessFilename);
                            boolean labeled = config.isPutLabels() && obj != null && obj.isLabeled();
                            if (labeled) {
                                imgDst[i] = putLabel(imgDst[i], myFont, MAPPED_BY.getOrDefault(extLessFilename, extLessFilename), getItemColor(extLessFilename), config.isFillInterior());
                            }

                            frameOffsetsX[i] = frames.get(i).getOffsetX();
                            frameOffsetsY[i] = frames.get(i).getOffsetY();
                        }
                        //-------------------------------------------------------
                        FRM dstFRM = new FRM(
                                srcFRM.getVersion(),
                                srcFRM.getFps(),
                                srcFRM.getActionFrame(),
                                srcFRM.getFramesPerDirection(),
                                srcFRM.getShiftX(),
                                srcFRM.getShiftY(),
                                imgDst,
                                frameOffsetsX,
                                frameOffsetsY
                        );

                        File outFile = null;
                        if (config.isRecursive()) {
                            File dirs = srcFile;
                            StringBuilder sb = new StringBuilder();
                            while (!dirs.equals(config.getInDir())) {
                                dirs = dirs.getParentFile();
                                sb.insert(0, dirs.getName());
                                sb.insert(0, File.separator);
                            }
                            outFile = new File(config.getOutDir() + File.separator + sb.toString() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                        } else {
                            outFile = new File(config.getOutDir() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                        }

                        if (outFile.exists()) {
                            outFile.delete();
                        }

                        outFile.mkdirs();

                        dstFRM.write(outFile);
                    } else if (srcFile.getName().toLowerCase().endsWith(".png")) {
                        BufferedImage imgSrc = null;
                        //------------------------------------------------------
                        try {
                            imgSrc = ImageIO.read(srcFile);
                        } catch (IOException ex) {
                            FO2HLogger.reportError(ex.getMessage(), ex);
                        }
                        //------------------------------------------------------
                        if (imgSrc != null) {
                            BufferedImage imgDst = new BufferedImage(imgSrc.getWidth() + 2, imgSrc.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
                            Graphics2D graphics2D = imgDst.createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                            graphics2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                            graphics2D.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

                            graphics2D.drawImage(imgSrc, 1, 1, null);
                            // blue color removal
                            removeBlue(imgDst);
                            // outline & fill interior effect
                            createOutline(imgDst, getItemColor(extLessFilename), config.isFillInterior(), config.isDrawOutline());

                            // label
                            Obj obj = DICTIONARY.get(extLessFilename);
                            if (config.isPutLabels() && obj != null && obj.isLabeled()) {
                                imgDst = putLabel(imgDst, myFont, MAPPED_BY.getOrDefault(extLessFilename, extLessFilename), getItemColor(extLessFilename), config.isFillInterior());
                            }

                            //--------------------------------------------------
                            File outFile = null;
                            if (config.isRecursive()) {
                                File dirs = srcFile;
                                StringBuilder sb = new StringBuilder();
                                while (!dirs.equals(config.getInDir())) {
                                    dirs = dirs.getParentFile();
                                    sb.insert(0, dirs.getName());
                                    sb.insert(0, File.separator);
                                }
                                outFile = new File(config.getOutDir() + File.separator + sb.toString() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".png"));
                            } else {
                                outFile = new File(config.getOutDir() + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".png"));
                            }

                            if (outFile.exists()) {
                                outFile.delete();
                            }

                            outFile.mkdirs();

                            try {
                                ImageIO.write(imgDst, "png", outFile);
                            } catch (IOException ex) {
                                FO2HLogger.reportError(ex.getMessage(), ex);
                            }
                        }
                    }
                }
                progress += 100.0f / fileArray.length;
                firePropertyChange("progress", oldProgress, progress);
            }
        }

        FO2HLogger.reportInfo("Highlighter work finished!", null);
        progress = 100.0f;
        firePropertyChange("progress", oldProgress, progress);
    }

    @Override
    protected Void doInBackground() throws Exception {
        work();
        return null;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public Configuration getConfig() {
        return config;
    }

    public Font getMyFont() {
        return myFont;
    }

    public static String getDictionaryErrorMessage() {
        return dictionaryErrorMessage;
    }

    private File[] buildTree(File inDir) {
        List<File> result = new ArrayList<>();

        Stack<File> stack = new Stack<>();
        stack.push(inDir);

        while (!stack.isEmpty()) {
            File file = stack.pop();
            if (file.isDirectory()) {
                String[] list = file.list();
                for (int i = list.length - 1; i >= 0; i--) {
                    File chldFile = new File(
                            file.getAbsolutePath() + File.separator + list[i]
                    );
                    stack.push(chldFile);
                }
            } else {
                result.add(file);
            }
        }

        File[] array = new File[result.size()];
        return result.toArray(array);
    }

}
