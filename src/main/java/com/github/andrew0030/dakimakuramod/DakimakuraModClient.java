package com.github.andrew0030.dakimakuramod;

import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class DakimakuraModClient
{
    public static void init(IEventBus modEventBus)
    {
        modEventBus.addListener(DakimakuraModClient::setupClient);
    }

    private static void setupClient(FMLClientSetupEvent event)
    {
        event.enqueueWork(DMBlockEntities::registerBlockEntityRenderers);
    }
}