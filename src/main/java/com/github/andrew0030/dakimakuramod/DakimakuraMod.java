package com.github.andrew0030.dakimakuramod;

import com.github.andrew0030.dakimakuramod.dakimakura.DakiManager;
import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import com.github.andrew0030.dakimakuramod.registries.DMBlocks;
import com.github.andrew0030.dakimakuramod.registries.DMEntities;
import com.github.andrew0030.dakimakuramod.registries.DMItems;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(DakimakuraMod.MODID)
public class DakimakuraMod
{
    public static final String MODID = "dakimakuramod";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static DakiManager dakimakuraManager;

    public DakimakuraMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::commonSetup);
        eventBus.addListener(this::serverStarting);
        eventBus.addListener(this::serverStopping);
        if (FMLEnvironment.dist == Dist.CLIENT)
            DakimakuraModClient.init(modEventBus);

        DMBlocks.BLOCKS.register(modEventBus);
        DMItems.ITEMS.register(modEventBus);
        DMEntities.ENTITIES.register(modEventBus);
        DMBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        DakimakuraMod.dakimakuraManager = new DakiManager(Minecraft.getInstance().gameDirectory);
    }

    private void serverStarting(final ServerStartingEvent event)
    {
        DakimakuraMod.getDakimakuraManager().loadPacks(false);
    }

    private void serverStopping(final ServerStoppingEvent event)
    {

    }

    /**
     * @return A DakimakuraManager Instance
     */
    public static DakiManager getDakimakuraManager()
    {
        return DakimakuraMod.dakimakuraManager;
    }
}