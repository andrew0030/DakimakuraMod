package moe.plushie.dakimakuramod.common.block;

import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    
    public static Block blockDakimakura;
    public static ItemBlock itemBlockDakimakura;
    
    public ModBlocks() {
        MinecraftForge.EVENT_BUS.register(this);
        blockDakimakura = new BlockDakimakura();
    }
    
    @SubscribeEvent
    public void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(blockDakimakura);
    }
    
    @SubscribeEvent
    public void registerItemBlocks(RegistryEvent.Register<Item> event) {
        itemBlockDakimakura = new ItemBlockDakimakura(blockDakimakura);
        event.getRegistry().register(itemBlockDakimakura.setRegistryName(blockDakimakura.getRegistryName()));
    }
    
    public void registerTileEntities() {
        registerTileEntity(TileEntityDakimakura.class, "dakimakura");
    }

    private void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String id) {
        //GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(LibModInfo.ID, "tileentity." + id));
        GameRegistry.registerTileEntity(tileEntityClass, "tileentity." + id);
    }
}
