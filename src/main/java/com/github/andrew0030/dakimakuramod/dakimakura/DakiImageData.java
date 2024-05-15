package com.github.andrew0030.dakimakuramod.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.IDakiPack;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/** Used to handle loading the textures of a given {@link Daki} and storing them as byte arrays */
public class DakiImageData
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String[] VALID_FILE_EXT = {"png", "jpg", "jpeg"};
    private static final String DEFAULT_NAME_FRONT = "front";
    private static final String DEFAULT_NAME_BACK = "back";
    private final Daki daki;
    private byte[] textureFront;
    private byte[] textureBack;

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

    /** @return The Front Texture byte array */
    public byte[] getTextureFront()
    {
        return this.textureFront;
    }

    /** @return The Back Texture byte array */
    public byte[] getTextureBack()
    {
        return this.textureBack;
    }

    public void clearTextureData()
    {
        this.textureFront = null;
        this.textureBack = null;
    }

    /** Sets the texture byte arrays based on the Images of the {@link Daki}, which was passed to the {@link DakiImageData} object during initialization. */
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
        // Lastly, if no suitable image path is found, we notify the user and return null
        LOGGER.error(String.format("Failed finding any valid image for Daki: %s/%s", dakiDirectoryName, defaultImageName));
        return null;
    }
}