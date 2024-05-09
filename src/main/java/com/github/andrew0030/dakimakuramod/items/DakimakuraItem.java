package com.github.andrew0030.dakimakuramod.items;

import com.github.andrew0030.dakimakuramod.block_entities.util.DMBlockEntityWithoutLevelRenderer;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.entities.dakimakura.Dakimakura;
import com.github.andrew0030.dakimakuramod.items.util.BEWLRBlockItem;
import com.github.andrew0030.dakimakuramod.registries.DMEntities;
import com.github.andrew0030.dakimakuramod.util.ClientInitContext;
import com.github.andrew0030.dakimakuramod.util.TranslationHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

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
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
    {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown())
        {
            boolean flipped = DakimakuraItem.isFlipped(itemStack);
            itemStack = DakimakuraItem.setFlipped(itemStack, !flipped);
            return InteractionResultHolder.consume(itemStack);
        }
        return InteractionResultHolder.pass(itemStack);
    }

    @Override
    public InteractionResult useOn(UseOnContext context)
    {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        ItemStack stack = context.getItemInHand();

        if (state.isBed(level, pos, null))
        {
            // If there is already a Bottom of a Dakimakura on the clicked BedBlock, we stop placement logic
            if (this.isDakiEntityOnBlock(level, pos)) return InteractionResult.FAIL;
            // If the BedBlock doesn't have a Dakimakura on it, we place one
            Daki daki = DakiTagSerializer.deserialize(stack.getTag());
            Dakimakura dakimakura = new Dakimakura(DMEntities.DAKIMAKURA.get(), level);
            dakimakura.setPos(pos.getX() + 0.5D, pos.getY() + 0.5625D, pos.getZ() + 0.5D);
            dakimakura.setDaki(daki);
            dakimakura.setFlipped(DakimakuraItem.isFlipped(stack));
            dakimakura.setRotation(context.getHorizontalDirection());
            level.addFreshEntity(dakimakura);
//            DakimakuraMod.getLogger().info("Placing daki at " + pos);
            level.playSound(player, pos, SoundType.WOOL.getPlaceSound(), SoundSource.BLOCKS, (SoundType.WOOL.getVolume() + 1.0F) / 2.0F, SoundType.WOOL.getPitch() * 0.8F);
            if (player != null && !player.isCreative())
                stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context)
    {
        return this.useOn(context);
    }

    private boolean isDakiEntityOnBlock(Level level, BlockPos pos)
    {
        List<Dakimakura> list = level.getEntitiesOfClass(Dakimakura.class, new AABB(pos));
        for (Dakimakura dakimakura : list)
            if(dakimakura.distanceToSqr(pos.getCenter()) <= 1)
                return true; // We check if the distance to the block is 1 so only the bottom part of a Dakimakura counts
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flag)
    {
        super.appendHoverText(stack, level, tooltip, flag);
        Daki daki = DakiTagSerializer.deserialize(stack.getTag());
        if (daki != null)
        {
            tooltip.add(Component.translatable("tooltip.dakimakuramod.dakimakura.flip"));
            daki.addInformation(stack, tooltip);
        }
        else
        {
            tooltip.add(Component.translatable("tooltip.dakimakuramod.dakimakura.blank"));
        }
    }

    public static boolean isFlipped(ItemStack itemStack)
    {
        if (itemStack == null || !itemStack.hasTag())
            return false;
        return DakiTagSerializer.isFlipped(itemStack.getTag());
    }

    public static ItemStack setFlipped(ItemStack itemStack, boolean flipped)
    {
        if (itemStack == null)
            return null;
        CompoundTag compound = itemStack.getTag() != null ? itemStack.getTag() : new CompoundTag();
        itemStack.setTag(compound);
        DakiTagSerializer.setFlipped(compound, flipped);
        return itemStack;
    }
}