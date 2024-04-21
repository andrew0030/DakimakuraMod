package com.github.andrew0030.dakimakuramod.items;

import com.github.andrew0030.dakimakuramod.block_entities.util.DMBlockEntityWithoutLevelRenderer;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.items.util.BEWLRBlockItem;
import com.github.andrew0030.dakimakuramod.util.ClientInitContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class DakimakuraItem extends BEWLRBlockItem
{
    public DakimakuraItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    @Override
    public void initializeClient(ClientInitContext ctx)
    {
        ctx.registerRenderer(DMBlockEntityWithoutLevelRenderer::new);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, level, tooltip, flag);
//        Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound()); TODO: update tooltip properly
//        if (daki != null) {
//            String textFlip = I18n.format(stack.getUnlocalizedName() + ".tooltip.flip");
//            tooltip.add(I18n.format(textFlip));
//            daki.addInformation(stack, tooltip);
//        } else {
//            tooltip.add(I18n.format(stack.getUnlocalizedName() + ".tooltip.blank"));
//        }
    }
}