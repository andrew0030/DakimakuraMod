package com.github.andrew0030.dakimakuramod.registries;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.items.DakimakuraDesignItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class DMItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DakimakuraMod.MODID);

    public static final RegistryObject<Item> DAKIMAKURA_DESIGN = ITEMS.register("dakimakura_design", () -> new DakimakuraDesignItem(new Item.Properties().stacksTo(16)));

    public static void registerItemProperties()
    {
        ItemProperties.register(DAKIMAKURA_DESIGN.get(), new ResourceLocation(DakimakuraMod.MODID, "unlocked"), (stack, level, entity, seed) -> DakiTagSerializer.deserialize(stack.getTag()) == null ? 0.0F : 1.0F);
    }
}