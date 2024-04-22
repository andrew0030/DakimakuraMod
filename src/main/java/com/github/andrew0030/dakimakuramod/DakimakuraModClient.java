package com.github.andrew0030.dakimakuramod;

import com.github.andrew0030.dakimakuramod.entities.dakimakura.DakimakuraRenderer;
import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import com.github.andrew0030.dakimakuramod.registries.DMEntities;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DakimakuraModClient
{
    public static void init(IEventBus modEventBus)
    {
        modEventBus.addListener(DakimakuraModClient::setupClient);
        modEventBus.addListener(DakimakuraModClient::registerEntityRenderers);
    }

    private static void setupClient(FMLClientSetupEvent event)
    {
        event.enqueueWork(DMBlockEntities::registerBlockEntityRenderers);
    }

    private static void registerEntityRenderers(RegisterRenderers event)
    {
        event.registerEntityRenderer(DMEntities.DAKIMAKURA.get(), DakimakuraRenderer::new);
    }
}