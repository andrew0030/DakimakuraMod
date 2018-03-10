package moe.plushie.dakimakuramod.common.handler;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.items.ModItems;
import moe.plushie.dakimakuramod.common.lib.LibModInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTableHandler {
    
    public LootTableHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        if (!ConfigHandler.addUnlockToLootChests) {
            return;
        }
        ResourceLocation[] validTables = new ResourceLocation[] {
                LootTableList.CHESTS_ABANDONED_MINESHAFT,
                LootTableList.CHESTS_DESERT_PYRAMID,
                LootTableList.CHESTS_END_CITY_TREASURE,
                LootTableList.CHESTS_IGLOO_CHEST,
                LootTableList.CHESTS_JUNGLE_TEMPLE,
                LootTableList.CHESTS_JUNGLE_TEMPLE_DISPENSER,
                LootTableList.CHESTS_NETHER_BRIDGE,
                LootTableList.CHESTS_SIMPLE_DUNGEON,
                LootTableList.CHESTS_STRONGHOLD_CORRIDOR,
                LootTableList.CHESTS_STRONGHOLD_CROSSING,
                LootTableList.CHESTS_STRONGHOLD_LIBRARY,
                LootTableList.CHESTS_VILLAGE_BLACKSMITH
        };
        boolean validTable = false;
        for (int i = 0; i < validTables.length; i++) {
            if (event.getName().equals(validTables[i])) {
                validTable = true;
                break;
            }
        }
        if (!validTable) {
            return;
        }
        LootPool pool = event.getTable().getPool("main");
        if (pool == null) {
            return;
        }
        DakimakuraMod.getLogger().info("Adding loot to " + event.getName());
        LootEntryItem loot = new LootEntryItem(ModItems.dakiDesign, 2, 1, new LootFunction[0], new LootCondition[0], LibModInfo.ID + ":daki-design");
        pool.addEntry(loot);
    }
}
