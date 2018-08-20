package moe.plushie.dakimakuramod.common.items;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModItems {
    
    public static ItemDakiDesign dakiDesign;

    public ModItems() {
        MinecraftForge.EVENT_BUS.register(this);
        dakiDesign = new ItemDakiDesign();
    }
    
    @SubscribeEvent
    public void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(dakiDesign);
    }
}
