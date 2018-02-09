package moe.plushie.dakimakuramod.proxies;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.dakimakura.DakiManager;
import moe.plushie.dakimakuramod.common.handler.SyncHandler;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.network.GuiHandler;
import moe.plushie.dakimakuramod.common.network.PacketHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

public class CommonProxy {
    
    private DakiManager dakimakuraManager;
    private ModBlocks modBlocks;
    private ModItems modItems;
    
    public void preInit(FMLPreInitializationEvent event) {
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        dakimakuraManager = new DakiManager(event.getSuggestedConfigurationFile().getParentFile().getParentFile());
        dakimakuraManager.loadPacks();
        modBlocks = new ModBlocks();
        modItems = new ModItems();
    }
    
    public void init(FMLInitializationEvent event) {
        modBlocks.registerTileEntities();
        new GuiHandler();
        PacketHandler.init();
        SyncHandler.init();
        if (ConfigHandler.addUnlockToLootChests) {
            addLootToChests();
        }
    }
    
    private void addLootToChests() {
        ItemStack itemStack = new ItemStack(ModItems.dakiDesign);
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
    }
    
    public void initRenderers() {
        DakimakuraMod.getLogger().error("Trying to init renderers on the server side.");
    }
    
    public void postInit(FMLPostInitializationEvent event) {
    }
    
    public MinecraftServer getServer() {
        return MinecraftServer.getServer();
    }
    
    public DakiManager getDakimakuraManager() {
        return dakimakuraManager;
    }
}
