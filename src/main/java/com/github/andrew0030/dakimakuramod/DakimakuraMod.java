package com.github.andrew0030.dakimakuramod;

import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(DakimakuraMod.MODID)
public class DakimakuraMod
{
    public static final String MODID = "dakimakuramod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public DakimakuraMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        // DMBlocks.BLOCKS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
//        LOGGER.info("Common Setup Called!");
    }
}