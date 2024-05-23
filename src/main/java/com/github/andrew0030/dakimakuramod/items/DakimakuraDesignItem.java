package com.github.andrew0030.dakimakuramod.items;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.DakiManager;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.registries.DMItems;
import com.github.andrew0030.dakimakuramod.util.TranslationHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DakimakuraDesignItem extends Item
{
    public DakimakuraDesignItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack stack = player.getItemInHand(hand);
        DakiManager dakiManager = DakimakuraMod.getDakimakuraManager();
        // If the used design is already an "unlocked" one we don't do anything.
        if (DakiTagSerializer.deserialize(stack.getTag()) != null)
            return InteractionResultHolder.pass(stack);
        // If there is no available Dakimakuras, we have no designs to pick from, so we don't do anything.
        ArrayList<Daki> dakiList = dakiManager.getDakiList();
        if (dakiList.isEmpty())
            return InteractionResultHolder.pass(stack);

        if (!level.isClientSide())
        {
            Random rand = new Random(System.nanoTime());
            int randValue = rand.nextInt(dakiList.size());
            Daki daki = dakiList.get(randValue);
            this.giveDesignToPlayer(level, player, daki);
            stack.shrink(1);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.success(stack);
    }

    private void giveDesignToPlayer(Level level, Player player, Daki daki)
    {
        ItemStack stack = new ItemStack(DMItems.DAKIMAKURA_DESIGN.get());
        if (!stack.hasTag())
            stack.setTag(new CompoundTag());
        DakiTagSerializer.serialize(daki, stack.getTag());
        Inventory inventory = player.getInventory();
        if (!inventory.add(stack))
            player.drop(stack, false);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced)
    {
        List<Component> myToolTips = new ArrayList<>();

        Daki daki = DakiTagSerializer.deserialize(stack.getTag());
        if (daki != null)
        {
            myToolTips.add(Component.translatable("tooltip.dakimakuramod.dakimakura_design.usage"));
            daki.addInformation(stack, myToolTips);
        }
        else
        {
            myToolTips.add(Component.translatable("tooltip.dakimakuramod.dakimakura_design.unlock"));
        }

        TranslationHelper.formatTooltips(myToolTips);
        tooltip.addAll(myToolTips);
    }
}