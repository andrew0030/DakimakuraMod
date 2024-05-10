package com.github.andrew0030.dakimakuramod.events;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.blocks.DakimakuraBlock;
import com.github.andrew0030.dakimakuramod.entities.dakimakura.Dakimakura;
import com.github.andrew0030.dakimakuramod.registries.DMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = DakimakuraMod.MODID)
public class BedEvents
{
    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.LeftClickBlock event)
    {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (state.isBed(level, pos, null)) // We check if the BlockState is a bed
        {
            List<Dakimakura> list = level.getEntitiesOfClass(Dakimakura.class, new AABB(pos));
            if (!list.isEmpty()) // We check if there is Dakimakuras on top of the Block
            {
                for (Dakimakura dakimakura : list)
                {
                    Vec3 bottomPos = dakimakura.position();
                    Vec3 topPos = bottomPos.relative(dakimakura.getRotation(), 1);
                    if (bottomPos.distanceToSqr(pos.getCenter()) <= 1 || topPos.distanceToSqr(pos.getCenter()) <= 1)
                    {
                        // Removes Entity and adds Item
                        if (!player.isCreative())
                            dakimakura.dropAsItem();
                        dakimakura.setRemoved(Entity.RemovalReason.DISCARDED);
                        // Adds Break Sound
                        level.playSound(player, pos, SoundType.WOOL.getBreakSound(), SoundSource.BLOCKS, (SoundType.WOOL.getVolume() + 1.0F) / 2.0F, SoundType.WOOL.getPitch() * 0.8F);
                        // Adds Break Particles
                        BlockPos offsetPos = bottomPos.distanceToSqr(pos.getCenter()) <= 1 ?
                                pos.relative(dakimakura.getRotation()) :
                                pos.relative(dakimakura.getRotation().getOpposite());
                        level.addDestroyBlockEffect(pos.above(), DMBlocks.DAKIMAKURA.get().defaultBlockState().setValue(DakimakuraBlock.FACING, dakimakura.getRotation()));
                        level.addDestroyBlockEffect(offsetPos.above(), DMBlocks.DAKIMAKURA.get().defaultBlockState().setValue(DakimakuraBlock.FACING, dakimakura.getRotation().getOpposite()));
                        // Cancels the event so the BedBlock doesn't get broken
                        if (player.isCreative())
                            event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBlockLeftClick(PlayerInteractEvent.RightClickBlock event)
    {
        Player player = event.getEntity();
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);

        if (state.isBed(level, pos, null)) // We check if the BlockState is a bed
        {
            List<Dakimakura> list = level.getEntitiesOfClass(Dakimakura.class, new AABB(pos));
            if (!list.isEmpty()) // We check if there is Dakimakuras on top of the Block
            {
                for (Dakimakura dakimakura : list)
                {
                    Vec3 bottomPos = dakimakura.position();
                    Vec3 topPos = bottomPos.relative(dakimakura.getRotation(), 1);
                    if (bottomPos.distanceToSqr(pos.getCenter()) <= 1 || topPos.distanceToSqr(pos.getCenter()) <= 1)
                    {
                        if (player.isShiftKeyDown())
                        {
                            player.swing(InteractionHand.MAIN_HAND);
                            if (event.getHand().equals(InteractionHand.MAIN_HAND))
                                dakimakura.setFlipped(!dakimakura.isFlipped());
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }
    }
}