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
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import rs.alexanderstojanovich.fo2h.frm.FRM;
import rs.alexanderstojanovich.fo2h.frm.ImageData;
import rs.alexanderstojanovich.fo2h.util.ColorSample;
import rs.alexanderstojanovich.fo2h.util.FO2HLogger;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public class Highligther {

    private static final String TEXTFILE = "Dictionary.txt";

    private float progress = 0.0f;

    public static enum Obj {
        UNUSED,
        IMPLANTS, T4_WEAPONS, T4_ARMORS, T4_AMMO, T4_ITEMS,
        T3_WEAPONS, T3_ARMORS, T3_AMMO, T3_ITEMS,
        T2_WEAPONS, T2_ARMORS, T2_AMMO, T2_ITEMS,
        T1_WEAPONS, T1_ARMORS, T1_AMMO, T1_ITEMS,
        T0_WEAPONS, T0_ARMORS, T0_AMMO, T0_ITEMS,
        RESOURCES, BOOKS, ORES, CONTAINERS
    };

    public static final Map<String, Obj> DICTIONARY = new HashMap<>();

    private Color implantColor = new Color(255, 0, 0);
    private Color t4Color = new Color(200, 0, 150);
    private Color t3Color = new Color(60, 183, 44);
    private Color t2Color = new Color(0, 175, 255);
    private Color t1Color = new Color(0, 200, 200);
    private Color t0Color = new Color(128, 128, 128);
    private Color bookColor = new Color(255, 255, 0);
    private Color oreColor = new Color(255, 255, 255);
    private Color resourcesColor = new Color(0, 255, 255);
    private Color containerColor = new Color(175, 0, 255);
    private Color unusedColor = new Color(167, 107, 107);

    public Highligther() {
        init();
    }

    private void init() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(TEXTFILE));
            String line;
            Obj obj = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@")) {
                    obj = Obj.valueOf(line.trim().substring(1));
                } else {
                    DICTIONARY.put(line.trim(), obj);
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

    private Color getOutlineColor(String extLessFilename) {
        Color result;
        Obj obj = DICTIONARY.getOrDefault(extLessFilename, Obj.UNUSED);
        switch (obj) {
            case IMPLANTS:
                result = implantColor;
                break;
            case T4_WEAPONS:
            case T4_ARMORS:
            case T4_AMMO:
            case T4_ITEMS:
                result = t4Color;
                break;
            case T3_WEAPONS:
            case T3_ARMORS:
            case T3_AMMO:
            case T3_ITEMS:
                result = t3Color;
                break;
            case T2_WEAPONS:
            case T2_ARMORS:
            case T2_AMMO:
            case T2_ITEMS:
                result = t2Color;
                break;
            case T1_WEAPONS:
            case T1_ARMORS:
            case T1_AMMO:
            case T1_ITEMS:
                result = t1Color;
                break;
            case T0_WEAPONS:
            case T0_ARMORS:
            case T0_AMMO:
            case T0_ITEMS:
                result = t0Color;
                break;
            case BOOKS:
                result = bookColor;
                break;
            case ORES:
                result = oreColor;
                break;
            case RESOURCES:
                result = resourcesColor;
                break;
            case CONTAINERS:
                result = containerColor;
                break;
            case UNUSED:
            default:
                result = unusedColor;
                break;
        }
        return result;
    }

    /**
     * Start highlighter working.
     *
     * @param inDir path to input directory (where files are fetched from)
     * @param outDir path to output directory (where modified files are stored
     * to)
     */
    public void work(File inDir, File outDir) {
        progress = 0.0f;
        if (!inDir.exists()) {
            progress = 100.0f;
            return;
        }

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        if (inDir.isDirectory()) {
            File[] fileArray = inDir.listFiles();
            for (File srcFile : fileArray) {
                // if file is fofrm copy it to the output                                      
                if (srcFile.getName().toLowerCase().endsWith(".fofrm")) {
                    File dstFile = new File(outDir + File.separator + srcFile.getName());
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
                        BufferedImage imgSrc = null;

                        FRM srcFRM = new FRM();
                        srcFRM.read(srcFile);
                        List<ImageData> frames = srcFRM.getFrames();
                        if (!frames.isEmpty()) {
                            ImageData imgData = frames.get(0);
                            imgSrc = imgData.toBufferedImage();
                        }

                        if (imgSrc != null) {
                            BufferedImage imgDst = new BufferedImage(imgSrc.getWidth() + 2, imgSrc.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
                            imgDst.createGraphics().drawImage(imgSrc, 1, 1, null);
                            // blue color removal
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    if (pixCol.equals(Color.BLUE)) {
                                        imgDst.setRGB(px, py, 0);
                                    }
                                }
                            }
                            // outline effect
                            WritableRaster wr = imgDst.copyData(null);
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    // writtable raster must be associated with ARGB image!!
                                    ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                                    if (pixCol.getAlpha() < 255 && cs.getAlpha() > 0) {
                                        imgDst.setRGB(px, py, getOutlineColor(extLessFilename).getRGB());
                                    }
                                }
                            }

                            final BufferedImage[] dstImages = {imgDst};

                            FRM dstFRM = new FRM(
                                    srcFRM.getVersion(),
                                    srcFRM.getFps(),
                                    srcFRM.getActionFrame(),
                                    srcFRM.getFramesPerDirection(),
                                    srcFRM.getFrameSize(),
                                    dstImages
                            );
                            File outFile = new File(outDir + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".FRM"));
                            dstFRM.write(outFile);
                        }
                    } else if (srcFile.getName().toLowerCase().endsWith(".png")) {
                        BufferedImage imgSrc = null;

                        try {
                            imgSrc = ImageIO.read(srcFile);
                        } catch (IOException ex) {
                            FO2HLogger.reportError(ex.getMessage(), ex);
                        }

                        if (imgSrc != null) {
                            BufferedImage imgDst = new BufferedImage(imgSrc.getWidth() + 2, imgSrc.getHeight() + 2, BufferedImage.TYPE_INT_ARGB);
                            imgDst.createGraphics().drawImage(imgSrc, 1, 1, null);
                            // blue color removal
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    if (pixCol.equals(Color.BLUE)) {
                                        imgDst.setRGB(px, py, 0);
                                    }
                                }
                            }
                            // outline effect
                            WritableRaster wr = imgDst.copyData(null);
                            for (int px = 0; px < imgDst.getWidth(); px++) {
                                for (int py = 0; py < imgDst.getHeight(); py++) {
                                    Color pixCol = new Color(imgDst.getRGB(px, py), true);
                                    // writtable raster must be associated with ARGB image!!
                                    ColorSample cs = ColorSample.getGaussianBlurSample(wr, px, py);
                                    if (pixCol.getAlpha() < 255 && cs.getAlpha() > 0) {
                                        imgDst.setRGB(px, py, getOutlineColor(extLessFilename).getRGB());
                                    }
                                }
                            }

                            File outFile = new File(outDir + File.separator + srcFile.getName().replaceFirst("[.][^.]+$", ".png"));

                            try {
                                ImageIO.write(imgDst, "png", outFile);
                            } catch (IOException ex) {
                                FO2HLogger.reportError(ex.getMessage(), ex);
                            }
                        }
                    }
                }
                progress += 100.0f / fileArray.length;
            }
        }
        progress = 100.0f;
    }

    public void reset() {
        implantColor = new Color(255, 0, 0);
        t4Color = new Color(200, 0, 150);
        t3Color = new Color(60, 183, 44);
        t2Color = new Color(0, 175, 255);
        t1Color = new Color(0, 200, 200);
        t0Color = new Color(128, 128, 128);
        bookColor = new Color(255, 255, 0);
        oreColor = new Color(255, 255, 255);
        containerColor = new Color(175, 0, 255);
        resourcesColor = new Color(0, 255, 255);
        unusedColor = new Color(167, 107, 107);
        DICTIONARY.clear();
        init();
    }

    public float getProgress() {
        return progress;
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

}