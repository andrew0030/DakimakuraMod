package com.github.andrew0030.dakimakuramod.util;

import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.client.DakiTexture;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryUtil;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ToDoRemove
{
    private byte[] textureFront;
    private byte[] textureBack;
    private ByteBuffer imageBuffer;
    private int size;
    public int id;

    public ToDoRemove() {}

    public void createImageBuffer()
    {
        try (InputStream inputStreamFront = this.textureFront != null ? new ByteArrayInputStream(this.textureFront) : this.getMissingTexture();
             InputStream inputStreamBack = this.textureBack != null ? new ByteArrayInputStream(this.textureBack) : this.getMissingTexture()) {

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
            textureSize = Math.min(textureSize, maxTexture);

            // Resize images if needed
            imageBufferFront = this.resize(imageBufferFront, frontWidth[0], frontHeight[0], textureSize / 3, textureSize, false);
            imageBufferBack = this.resize(imageBufferBack, backWidth[0], backHeight[0], textureSize / 3, textureSize, false);

            // Stores the combines front and back images into one buffer
            this.imageBuffer = this.combineImages(imageBufferFront, imageBufferBack, textureSize);
            this.size = textureSize;
            this.textureFront = null;
            this.textureBack = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean load()
    {
        if (this.imageBuffer == null)
            return false;

        try {
            int texture = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST); // LINEAR for smooth
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, 12, 36 * 2, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, this.imageBuffer);
            this.id = texture;
            //TODO make sure this is ok here I guess?
            MemoryUtil.memFree(this.imageBuffer);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        ByteBuffer resizedBuffer = MemoryUtil.memAlloc(newWidth * newHeight * 3); // 3 channels (RGB)
        // Resizes the image using STBImageResize
        //TODO: maybe look into using 16 for this for potentially better textures

        // STBImageResize.stbir_resize_uint16_generic();
        STBImageResize.stbir_resize_uint8(imgBuffer, oldWidth, oldHeight, 0, resizedBuffer, newWidth, newHeight, 0, 3); // 3 channels (RGB)
        // Frees the memory associated with the original input ByteBuffer
        MemoryUtil.memFree(imgBuffer);
        // Returns the resized image data ByteBuffer
        return resizedBuffer;
    }

    private ByteBuffer combineImages(ByteBuffer imageBufferFront, ByteBuffer imageBufferBack, int textureSize)
    {
        // Allocate memory for the combined image buffer
        ByteBuffer combinedBuffer = MemoryUtil.memAlloc(textureSize * textureSize * 3); // 3 channels (RGB)
        // Copy front image to the left side of combined image
        imageBufferFront.rewind(); // Resets position to start
        combinedBuffer.put(imageBufferFront);
        // Copy back image to the right side of combined image
        imageBufferBack.rewind(); // Resets position to start
        combinedBuffer.position(imageBufferFront.capacity()); // Start writing at the middle of the buffer
        combinedBuffer.put(imageBufferBack);
        // Reset position to start of the combined buffer
        combinedBuffer.rewind();

        MemoryUtil.memFree(imageBufferFront);
        MemoryUtil.memFree(imageBufferBack);

        return combinedBuffer;
    }

    private int getMaxTextureSize()
    {
        int maxGpuSize = DakimakuraModClient.getMaxGpuTextureSize();
//        int maxConfigSize = ConfigHandler.textureMaxSize;
//        return Math.min(maxGpuSize, maxConfigSize);
        return maxGpuSize; // TODO: add config support for max image size
    }

    private InputStream getMissingTexture()
    {
        return DakiTexture.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/obj/missing.png");
    }

    public void loadData()
    {
        // We get the Daki Pack from the DakiManager
//        IDakiPack dakiPack = DakimakuraMod.getDakimakuraManager().getDakiPack(this.daki.getPackDirectoryName());
        // We retrieve paths for the front and back Image of this Daki
        String pathFront = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "\\dakimakura-mod\\Vanilla Mobs\\Allay\\front.png";
        String pathBack = Minecraft.getInstance().gameDirectory.getAbsolutePath() + "\\dakimakura-mod\\Vanilla Mobs\\Allay\\back.png";
        // We convert the found files to byte arrays
        this.textureFront = getResource(pathFront);
        this.textureBack = getResource(pathBack);
    }

    public byte[] getResource(String path)
    {
        byte[] data = null;
        try (FileInputStream inputStream = new FileInputStream(path)) {
            data = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
}