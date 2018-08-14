package moe.plushie.dakimakuramod.common.dakimakura;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.proxies.ClientProxy;

public class DakiImageData implements Callable<DakiImageData> {
    
    private final static String[] VALID_FILE_EXT = {"png", "jpg", "jpeg"};
    private final Daki daki;
    private BufferedImage bufferedImageFull;
    private byte[] textureFront;
    private byte[] textureBack;
    
    public DakiImageData(Daki daki) {
        this.daki = daki;
        load();
    }
    
    public DakiImageData(Daki daki, byte[] textureFront, byte[] textureBack) {
        this.daki = daki;
        this.textureFront = textureFront;
        this.textureBack = textureBack;
    }
    
    public void load() {
        DakimakuraMod.getLogger().info("Loading daki from disk: " + daki);
        File dir = DakimakuraMod.getProxy().getDakimakuraManager().getPackFolder();
        
        dir = new File(dir, daki.getPackDirectoryName());
        dir = new File(dir, daki.getDakiDirectoryName());
        
        File fileFront = null;
        File fileBack = null;
        
        if (daki.getImageFront() != null) {
            fileFront = new File(dir, daki.getImageFront());
        } else {
            for (int i = 0; i < VALID_FILE_EXT.length; i++) {
                if (new File(dir, "front." + VALID_FILE_EXT[i]).exists()) {
                    fileFront = new File(dir, "front." + VALID_FILE_EXT[i]);
                    break;
                }
            }
        }
        if (daki.getImageBack() != null) {
            fileBack = new File(dir, daki.getImageBack());
        } else {
            for (int i = 0; i < VALID_FILE_EXT.length; i++) {
                if (new File(dir, "back." + VALID_FILE_EXT[i]).exists()) {
                    fileBack = new File(dir, "back." + VALID_FILE_EXT[i]);
                    break;
                }
            }
        }
        
        InputStream inputstream = null;
        try {
            if (fileFront != null && fileFront.exists()) {
                inputstream = new FileInputStream(fileFront);
                textureFront = IOUtils.toByteArray(inputstream);
                IOUtils.closeQuietly(inputstream);
                inputstream.close();
            }
            if (fileBack != null && fileBack.exists()) {
                inputstream = new FileInputStream(fileBack);
                textureBack = IOUtils.toByteArray(inputstream);
                inputstream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    public Daki getDaki() {
        return daki;
    }
    
    public byte[] getTextureFront() {
        return textureFront;
    }
    
    public byte[] getTextureBack() {
        return textureBack;
    }
    
    public BufferedImage getBufferedImageFull() {
        return bufferedImageFull;
    }

    @Override
    public DakiImageData call() throws Exception {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        InputStream inputstream = null;
        try {
            if (textureFront != null) {
                inputstream = new ByteArrayInputStream(textureFront);
            } else {
                inputstream = getMissingTexture();
            }
            BufferedImage bufferedimageFront = ImageIO.read(inputstream);
            inputstream.close();
            
            if (textureBack != null) {
                inputstream = new ByteArrayInputStream(textureBack);
            } else {
                inputstream = getMissingTexture();
            }
            BufferedImage bufferedimageBack = ImageIO.read(inputstream);
            inputstream.close();
            
            int heightF = bufferedimageFront.getHeight();
            int heightB = bufferedimageBack.getHeight();
            
            int maxTexture = Math.max(heightF, heightB);
            int textureSize = getMaxTextureSize();
            textureSize = Math.min(textureSize, getNextPowerOf2(maxTexture));
            
            bufferedimageFront = resize(bufferedimageFront, textureSize / 2, textureSize);
            bufferedimageBack = resize(bufferedimageBack, textureSize / 2, textureSize);
            
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-bufferedimageBack.getWidth(null), 0);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            bufferedimageBack = op.filter(bufferedimageBack, null);
            
            bufferedImageFull = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_INT_RGB);
            
            Graphics2D g2d = bufferedImageFull.createGraphics();
            g2d.drawImage(bufferedimageFront, 0, 0, null);
            g2d.drawImage(bufferedimageBack, textureSize / 2, 0, null);
            g2d.dispose();
            
            textureFront = null;
            textureBack = null;
            
            return this;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            IOUtils.closeQuietly(inputstream);
        }
    }
    
    private InputStream getMissingTexture() {
        return DakiImageData.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/models/missing.png");
    }
    
    private int getNextPowerOf2(int value) {
        return (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(value - 1));
    }
    
    private int getMaxTextureSize() {
        int maxGpuSize = ((ClientProxy)DakimakuraMod.getProxy()).getMaxGpuTextureSize();
        int maxConfigSize = ConfigHandler.textureMaxSize;
        return Math.min(maxGpuSize, maxConfigSize);
    }
    
    private BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}
