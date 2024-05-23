package com.github.andrew0030.dakimakuramod.registries;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Collections;

public class DMCreativeTab
{
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, DakimakuraMod.MODID);

    public static final RegistryObject<CreativeModeTab> TTC_TAB = TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + DakimakuraMod.MODID + ".tab"))
            .icon(() -> new ItemStack(DMBlocks.DAKIMAKURA.get()))
            .displayItems((params, output) -> {
                output.accept(DMItems.DAKIMAKURA_DESIGN.get());
                output.accept(DMBlocks.DAKIMAKURA.get());
                ArrayList<Daki> dakiList = DakimakuraMod.getDakimakuraManager().getDakiList();
                Collections.sort(dakiList); // Sorts the list in ascending order
                for(Daki daki : dakiList)
                {
                    ItemStack itemStack = new ItemStack(DMBlocks.DAKIMAKURA.get());
                    itemStack.setTag(new CompoundTag());
                    DakiTagSerializer.serialize(daki, itemStack.getTag());
                    DakiTagSerializer.setFlipped(itemStack.getTag(), false);
                    output.accept(itemStack);
                }
            }).build()
    );

    /** Makes Creative Tabs Rebuild */
    public static void reloadTabContents()
    {
        CreativeModeTabs.CACHED_PARAMETERS = null;
    }
}