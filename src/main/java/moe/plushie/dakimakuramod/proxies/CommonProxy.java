package moe.plushie.dakimakuramod.proxies;

import java.util.ArrayList;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.crafting.CraftingManager;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.DakiExtractor;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.dakimakura.DakiTextureManagerCommon;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import moe.plushie.dakimakuramod.common.handler.BedHandler;
import moe.plushie.dakimakuramod.common.handler.MobLootHandler;
import moe.plushie.dakimakuramod.common.handler.SyncHandler;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class CommonProxy {
    
    private ConfigHandler configHandler;
    private DakiManager dakimakuraManager;
    private DakiTextureManagerCommon textureManagerCommon;
    private ModBlocks modBlocks;
    private ModItems modItems;
    
    public void preInit(FMLPreInitializationEvent event) {
        configHandler = new ConfigHandler(event.getSuggestedConfigurationFile());
        dakimakuraManager = new DakiManager(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
        textureManagerCommon = new DakiTextureManagerCommon();
        modBlocks = new ModBlocks();
        modItems = new ModItems();
        EntityRegistry.registerModEntity(EntityDakimakura.class, "entityDakimakura", 1, DakimakuraMod.getInstance(), 64, 100, false);
    }
    
    public void init(FMLInitializationEvent event) {
        CraftingManager.init();
        modBlocks.registerTileEntities();
        PacketHandler.init();
        SyncHandler.init();
        new MobLootHandler();
        new BedHandler();
        if (ConfigHandler.addUnlockToLootChests) {
            addLootToChests();
        }
    }
    
    public void preInitRenderers() {
    }
    
    private void addLootToChests() {
        ItemStack itemStack = new ItemStack(ModItems.dakiDesign);
        /*
        WeightedRandomChestContent chestContent = new WeightedRandomChestContent(itemStack, 1, 2, 5);
        ChestGenHooks.getInfo(ChestGenHooks.MINESHAFT_CORRIDOR).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_DESERT_CHEST).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_CHEST).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.PYRAMID_JUNGLE_DISPENSER).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CORRIDOR).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_CROSSING).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.STRONGHOLD_LIBRARY).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.VILLAGE_BLACKSMITH).addItem(chestContent);
        ChestGenHooks.getInfo(ChestGenHooks.DUNGEON_CHEST).addItem(chestContent);
        */
    }
    
    public void initRenderers() {
    }
    
    public void postInit(FMLPostInitializationEvent event) {
        DakiExtractor.extractDakis();
    }
    
    public MinecraftServer getServer() {
        return null;
        //return MinecraftServer.getServer();
    }
    
    public DakiManager getDakimakuraManager() {
        return dakimakuraManager;
    }
    
    public DakiTextureManagerCommon getTextureManagerCommon() {
        return textureManagerCommon;
    }

    public void setDakiList(ArrayList<Daki> dakiList) {
        dakimakuraManager.setDakiList(dakiList);
    }
}
