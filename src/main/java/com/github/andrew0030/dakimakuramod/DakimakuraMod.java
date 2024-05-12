package com.github.andrew0030.dakimakuramod;

import com.github.andrew0030.dakimakuramod.commands.DakiCommand;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiExtractor;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiManager;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiTextureManagerCommon;
import com.github.andrew0030.dakimakuramod.dakimakura.pack.IDakiPack;
import com.github.andrew0030.dakimakuramod.events.LoggedInEvent;
import com.github.andrew0030.dakimakuramod.netwok.DMNetwork;
import com.github.andrew0030.dakimakuramod.registries.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;

@Mod(DakimakuraMod.MODID)
public class DakimakuraMod
{
    public static final String MODID = "dakimakuramod";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static DakiManager dakiManager;
    private static DakiTextureManagerCommon dakiTextureManagerCommon;

    public DakimakuraMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus eventBus = MinecraftForge.EVENT_BUS;

        modEventBus.addListener(this::commonSetup);
        eventBus.addListener(this::registerCommands);
        eventBus.addListener(this::serverStarting);
        eventBus.addListener(this::serverStopping);
        eventBus.register(new LoggedInEvent());
        if (FMLEnvironment.dist == Dist.CLIENT)
            DakimakuraModClient.init(modEventBus);

        DMBlocks.BLOCKS.register(modEventBus);
        DMItems.ITEMS.register(modEventBus);
        DMEntities.ENTITIES.register(modEventBus);
        DMBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        DMCreativeTab.TABS.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event)
    {
        // Initializes the DakiManager, this should only happen once per Mod
        DakimakuraMod.dakiManager = new DakiManager(FMLPaths.GAMEDIR.get().toFile());
        // Initializes the DakiTextureManagerCommon
        DakimakuraMod.dakiTextureManagerCommon = new DakiTextureManagerCommon();
        // Loads the default DakiPack/s into the dakimakura-mod folder if needed
        DakiExtractor.extractDakis();
        // Registers Network Messages
        DMNetwork.registerMessages();
    }

    private void registerCommands(RegisterCommandsEvent event)
    {
        DakiCommand.createCommand(event.getDispatcher(), event.getBuildContext());
    }

    private void serverStarting(ServerStartingEvent event)
    {
        DakimakuraMod.getDakimakuraManager().loadPacks(false);
        DakimakuraMod.getTextureManagerCommon().serverStarted();
    }

    private void serverStopping(ServerStoppingEvent event)
    {
        DakimakuraMod.getTextureManagerCommon().serverStopped();
    }

    public static DakiManager getDakimakuraManager()
    {
        return DakimakuraMod.dakiManager;
    }

    public static DakiTextureManagerCommon getTextureManagerCommon()
    {
        return DakimakuraMod.dakiTextureManagerCommon;
    }

    //TODO probably remove this since we only have this in client now...
//    public static void setDakiList(ArrayList<IDakiPack> packs)
//    {
//        DakimakuraMod.getDakimakuraManager().setDakiList(packs);
//    }
}