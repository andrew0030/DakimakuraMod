package com.github.andrew0030.dakimakuramod;

import com.github.andrew0030.dakimakuramod.dakimakura.client.DakiTextureManagerClient;
import com.github.andrew0030.dakimakuramod.entities.dakimakura.DakimakuraRenderer;
import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import com.github.andrew0030.dakimakuramod.registries.DMEntities;
import com.mojang.logging.LogUtils;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;

public class DakimakuraModClient
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private static int maxGpuTextureSize;
    private static DakiTextureManagerClient dakiTextureManagerClient;

    public static void init(IEventBus modEventBus)
    {
        modEventBus.addListener(DakimakuraModClient::setupClient);
        modEventBus.addListener(DakimakuraModClient::registerEntityRenderers);

        IEventBus eventBus = MinecraftForge.EVENT_BUS;
        eventBus.register(dakiTextureManagerClient = new DakiTextureManagerClient());
    }

    private static void setupClient(FMLClientSetupEvent event)
    {
//        DakimakuraModClient.dakiTextureManagerClient = new DakiTextureManagerClient();

        event.enqueueWork(() -> {
            DMBlockEntities.registerBlockEntityRenderers();
            // Gets the max size an image can be
            DakimakuraModClient.maxGpuTextureSize = GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE);
            LOGGER.info(String.format("Max GPU texture size: %d.", DakimakuraModClient.maxGpuTextureSize));
        });
    }

    private static void registerEntityRenderers(RegisterRenderers event)
    {
        event.registerEntityRenderer(DMEntities.DAKIMAKURA.get(), DakimakuraRenderer::new);
    }

    /**
     * Retrieves the maximum texture size supported by the GPU
     * @return The maximum GPU texture size in pixels
     */
    public static int getMaxGpuTextureSize()
    {
        return DakimakuraModClient.maxGpuTextureSize;
    }

    public static DakiTextureManagerClient getDakiTextureManager()
    {
        return DakimakuraModClient.dakiTextureManagerClient;
    }
}