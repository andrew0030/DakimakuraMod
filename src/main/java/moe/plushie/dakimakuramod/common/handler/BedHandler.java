package moe.plushie.dakimakuramod.common.handler;

import java.util.ArrayList;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BedHandler {
    
    public BedHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getFace() == null) {
            return;
        }
        World world = event.getWorld();
        EntityPlayer entityPlayer = event.getEntityPlayer();
        BlockPos blockPos = event.getPos();
        IBlockState blockState = world.getBlockState(blockPos);
        if (world.isRemote) {
            return;
        }
        if (!blockState.getBlock().isBed(blockState, world, blockPos, entityPlayer)) {
            return;
        }
        if (!entityPlayer.isSneaking()) {
            return;
        }
        DakimakuraMod.getLogger().info("Over block " + event.getHand());
        AxisAlignedBB aabb = new AxisAlignedBB(blockPos.add(-1, 0, -1), blockPos.add(1, 2, 1));
        ArrayList entityList = (ArrayList) world.getEntitiesWithinAABB(EntityDakimakura.class, aabb);
        if (entityList.isEmpty()) {
            return;
        }
        boolean flipped = false;
        
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity = (Entity) entityList.get(i);
            if (entity instanceof EntityDakimakura) {
                EntityDakimakura entityDaki = (EntityDakimakura) entity;
                if (entityDaki.isDakiOverBlock(blockPos)) {
                    if (event.getHand() == EnumHand.MAIN_HAND) {
                        entityDaki.flip();
                        flipped = true;
                    }
                    if (event.getHand() == EnumHand.OFF_HAND) {
                        entityDaki.dropAsItem();
                        entityDaki.setDead();
                    }
                }
            }
        }
        
        if (flipped) {
            event.setCanceled(true);
        }
    }
}
