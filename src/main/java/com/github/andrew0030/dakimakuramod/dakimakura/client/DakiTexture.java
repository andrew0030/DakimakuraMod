package com.github.andrew0030.dakimakuramod.dakimakura.client;

import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.InputStream;
import java.nio.ByteBuffer;

public class DakiTexture extends AbstractTexture
{
    private static long lastLoad;
    private boolean requested = false;
    private final Daki daki;
//    private BufferedImage bufferedImageFull;
    private ByteBuffer imageBuffer;

    public DakiTexture(Daki daki)
    {
        this.daki = daki;
    }

    public boolean isLoaded()
    {
        if (this.id == -1)
        {
            if (lastLoad + 25 < System.currentTimeMillis())
            {
                if (load())
                {
                    lastLoad = System.currentTimeMillis();
                }
                else
                {
                    if (!requested)
                    {
                        DakiTextureManagerClient textureManager = DakimakuraModClient.getDakiTextureManager();
                        int requests = textureManager.getTextureRequests().get();
                        if (requests < 2)
                        {
                            textureManager.getTextureRequests().incrementAndGet();
                            if (daki != null)
                            {
                                requested = true;
                                PacketHandler.NETWORK_WRAPPER.sendToServer(new MessageClientRequestTextures(daki));
                            }
                        }
                    }
                    else
                    {
                        if (this.imageBuffer != null)
                            this.load();
                    }
                }
            }
            return false;
        }
        return true;
    }

    // TODO move texture logic into here
    public void setImageBuffer(ByteBuffer imageBuffer)
    {
        this.imageBuffer = imageBuffer;
    }

    @Override
    protected void finalize() throws Throwable
    {
//        deleteGlTexture();
        this.releaseId();
        super.finalize();
    }

    private boolean load()
    {
        if (this.imageBuffer == null)
            return false;

//        deleteGlTexture();
        this.releaseId();
        try {
//            int sizeW = bufferedImageFull.getWidth();
//            int sizeH = bufferedImageFull.getHeight();
            //DakimakuraMod.getLogger().info(String.format("Uploading texture of size %d*%d to GPU for daki %s", sizeW, sizeH, daki.toString()));
//            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedImageFull, false, false);

            //TODO make sure this works? Probably replace with a cleaner implementation
            NativeImage image = NativeImage.read(NativeImage.Format.RGB, this.imageBuffer);
            TextureUtil.prepareImage(NativeImage.InternalGlFormat.RGB, this.getId(), 0, image.getWidth(), image.getHeight());
            image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), false, false);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private InputStream getMissingTexture()
    {
        return DakiTexture.class.getClassLoader().getResourceAsStream("assets/dakimakuramod/textures/obj/missing.png");
    }

    @Override
    public void load(ResourceManager resourceManager) {}
}