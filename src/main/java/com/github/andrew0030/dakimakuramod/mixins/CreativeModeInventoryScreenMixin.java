package com.github.andrew0030.dakimakuramod.mixins;

import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.registries.DMBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.ArrayList;
import java.util.Collections;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeModeInventoryScreenMixin
{
    @ModifyArg(method = "renderTabButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderItem(Lnet/minecraft/world/item/ItemStack;II)V"), index = 0)
    public ItemStack modifyVarRenderTabButton(ItemStack stack)
    {
        if(stack.is(DMBlocks.DAKIMAKURA.get().asItem()))
        {
            ArrayList<Daki> cachedDakis = DakimakuraModClient.getDakiTextureManager().getCachedDakis();
            if (!cachedDakis.isEmpty() && Minecraft.getInstance().player != null)
            {
                Collections.sort(cachedDakis);
                ItemStack newStack = new ItemStack(DMBlocks.DAKIMAKURA.get());
                Daki daki = cachedDakis.get(Minecraft.getInstance().player.tickCount / 30 % cachedDakis.size());
                CompoundTag compound = new CompoundTag();
                DakiTagSerializer.serialize(daki, compound);
                newStack.setTag(compound);
                return newStack;
            }
        }
        return stack;
    }
}