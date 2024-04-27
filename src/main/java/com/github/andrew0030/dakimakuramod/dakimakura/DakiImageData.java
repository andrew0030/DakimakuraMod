package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.IDakiPack;
import com.mojang.datafixers.util.Pair;
import org.apache.commons.io.IOUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

/**
 * Used to handle loading the textures of a given {@link Daki} and storing them as byte arrays
 */
public class DakiImageData implements Callable<DakiImageData>
{
    private static final String[] VALID_FILE_EXT = {"png", "jpg", "jpeg"};
    private static final String DEFAULT_NAME_FRONT = "front";
    private static final String DEFAULT_NAME_BACK = "back";
    private final Daki daki;
    private byte[] textureFront;
    private byte[] textureBack;
    private ByteBuffer imageBuffer;

    public DakiImageData(Daki daki)
    {
        this.daki = daki;
        this.load();
    }

    public DakiImageData(Daki daki, byte[] textureFront, byte[] textureBack)
    {
        this.daki = daki;
        this.textureFront = textureFront;
        this.textureBack = textureBack;
    }

    public Daki getDaki()
    {
        return this.daki;
    }

    public byte[] getTextureFront()
    {
        return this.textureFront;
    }

    public byte[] getTextureBack()
    {
        return this.textureBack;
    }

    public ByteBuffer getImageBuffer()
    {
        return this.imageBuffer;
    }

    /**
     * Used to free the memory assigned to the Image {@link ByteBuffer}
     */
    public void destroyBuffer()
    {
        MemoryUtil.memFree(this.imageBuffer);
    }

    /**
     * Sets the texture byte arrays based on the Images of the {@link Daki}, which was passed to the {@link DakiImageData} object during initialization
     */
    public void load()
    {
        // We get the Daki Pack from the DakiManager
        IDakiPack dakiPack = DakimakuraMod.getDakimakuraManager().getDakiPack(this.daki.getPackDirectoryName());
        // We retrieve paths for the front and back Image of this Daki
        String pathFront = this.findImagePath(dakiPack, this.daki.getDakiDirectoryName(), this.daki.getImageFront(), DEFAULT_NAME_FRONT);
        String pathBack = this.findImagePath(dakiPack, this.daki.getDakiDirectoryName(), this.daki.getImageBack(), DEFAULT_NAME_BACK);
        // We convert the found files to byte arrays
        this.textureFront = dakiPack.getResource(pathFront);
        this.textureBack = dakiPack.getResource(pathBack);
    }

    /**
     * @param dakiPack The {@link Daki} Pack that may be used to check for resources
     * @param dakiDirectoryName The {@link Daki} Directory Name
     * @param dakiImage The {@link Daki} front or back Image
     * @param defaultImageName The fallback default Image Name this Image path should use
     * @return An Image Path to the given {@link Daki} Image
     */
    private String findImagePath(IDakiPack dakiPack, String dakiDirectoryName, String dakiImage, String defaultImageName)
    {
        // If the Daki has an Image specified, we use that to create the path
        if (dakiImage != null)
            return dakiDirectoryName + "/" + dakiImage;
        // Otherwise, we loop over all the valid file types, and check if any of them exist
        for (String ext : VALID_FILE_EXT)
        {
            String imagePath = dakiDirectoryName + "/" + defaultImageName + "." + ext;
            if (dakiPack.resourceExists(imagePath))
                return imagePath; // If we find a match we use that path
        }
        return null; // Lastly, if no suitable image path is found, we return null
    }

    @Override
    public DakiImageData call()
    {
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        try (InputStream inputStreamFront = this.textureFront != null ? new ByteArrayInputStream(this.textureFront) : this.getMissingTexture();
             InputStream inputStreamBack = this.textureBack != null ? new ByteArrayInputStream(this.textureBack) : this.getMissingTexture()) {

            //TODO IMPORTANT: this needs to be moved to the client, and instead I should store a byte array
            // Load images using STBImage
            int[] frontWidth = new int[1];
            int[] frontHeight = new int[1];
            int[] frontComp = new int[1];
            int[] backWidth = new int[1];
            int[] backHeight = new int[1];
            int[] backComp = new int[1];
            ByteBuffer imageBufferFront = STBImage.stbi_load_from_memory(this.inputStreamToByteBuffer(inputStreamFront), frontWidth, frontHeight, frontComp, 3);
            ByteBuffer imageBufferBack = STBImage.stbi_load_from_memory(this.inputStreamToByteBuffer(inputStreamBack), backWidth, backHeight, backComp, 3);

            // We determine make sure the daki is within max size
            int maxTexture = Math.max(frontHeight[0], backHeight[0]);
            int textureSize = this.getMaxTextureSize();
            textureSize = Math.min(textureSize, this.getNextPowerOf2(maxTexture));

            // Resize images if needed
            imageBufferFront = this.resize(imageBufferFront, frontWidth[0], frontHeight[0], textureSize / 2, textureSize, this.daki.isSmooth());
            imageBufferBack = this.resize(imageBufferBack, backWidth[0], backHeight[0], textureSize / 2, textureSize, this.daki.isSmooth());

            // Stores the combines front and back images into one buffer
            this.imageBuffer = this.combineImages(imageBufferFront, imageBufferBack, textureSize);

            textureFront = null;
            textureBack = null;

            return this;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts the given {@link InputStream} to a {@link ByteBuffer}
     * @param inputStream The {@link InputStream} to be converted
     * @return A new {@link ByteBuffer} containing all the bytes from the {@link InputStream}
     */
    private ByteBuffer inputStreamToByteBuffer(InputStream inputStream) throws IOException
    {
        byte[] imageData = inputStream.readAllBytes();
        ByteBuffer buffer = ByteBuffer.allocateDirect(imageData.length);
        buffer.put(imageData);
        buffer.flip();
        return buffer;
    }

    private ByteBuffer resize(ByteBuffer imgBuffer, int oldWidth, int oldHeight, int newWidth, int newHeight, boolean isSmooth)
    {
        // Allocates memory for the resized image
        ByteBuffer resizedBuffer = ByteBuffer.allocateDirect(newWidth * newHeight * 3); // 3 channels (RGB)
        // Resizes the image using STBImageResize
        //TODO: maybe look into using 16 for this for potentially better textures
//        STBImageResize.stbir_resize_uint16_generic();
        STBImageResize.stbir_resize_uint8(imgBuffer, oldWidth, oldHeight, 0, resizedBuffer, newWidth, newHeight, 0, 3); // 3 channels (RGB)
        // Frees the memory associated with the original input ByteBuffer
        MemoryUtil.memFree(imgBuffer);
        // Returns the resized image data ByteBuffer
        return resizedBuffer;
    }

    private ByteBuffer combineImages(ByteBuffer imageBufferFront, ByteBuffer imageBufferBack, int textureSize)
    {
        // Allocate memory for the combined image buffer
        ByteBuffer combinedBuffer = ByteBuffer.allocateDirect(textureSize * textureSize * 3); // 3 channels (RGB)
        // Copy front image to the left side of combined image
        imageBufferFront.rewind(); // Resets position to start
        combinedBuffer.put(imageBufferFront);
        // Copy back image to the right side of combined image
        imageBufferBack.rewind(); // Resets position to start
        combinedBuffer.position(textureSize / 2 * 3); // Start writing at the middle of the buffer
        combinedBuffer.put(imageBufferBack);
        // Reset position to start of the combined buffer
        combinedBuffer.rewind();

        MemoryUtil.memFree(imageBufferFront);
        MemoryUtil.memFree(imageBufferBack);

        return combinedBuffer;
    }

    /**
     * @return An {@link InputStream} representing a missing texture
     */
    private InputStream getMissingTexture()
    {
        return DakiImageData.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/obj/missing.png");
    }

    /**
     * @param value The value for which to calculate the next power of 2.
     * @return The next power of 2 greater than or equal to the given value.
     */
    private int getNextPowerOf2(int value)
    {
        return (int) Math.pow(2, 32 - Integer.numberOfLeadingZeros(value - 1));
    }

    /**
     * @return The max texture size an image can be in pixels
     */
    private int getMaxTextureSize()
    {
        int maxGpuSize = DakimakuraModClient.getMaxGpuTextureSize();
//        int maxConfigSize = ConfigHandler.textureMaxSize;
//        return Math.min(maxGpuSize, maxConfigSize);
        return maxGpuSize; // TODO: add config support for max image size
    }
}