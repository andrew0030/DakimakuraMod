package moe.plushie.dakimakuramod.common.handler;

import java.util.ArrayList;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class BedHandler {
    
    public BedHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.action == Action.RIGHT_CLICK_AIR) {
            return;
        }
        World world = event.world;
        EntityPlayer entityPlayer = event.entityPlayer;
        int x = event.x;
        int y = event.y;
        int z = event.z;
        Block block = world.getBlock(x, y, z);
        if (world.isRemote) {
            return;
        }
        if (!block.isBed(world, x, y, z, entityPlayer)) {
            return;
        }
        if (!entityPlayer.isSneaking()) {
            return;
        }
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(x - 1, y + 1, z - 1, x + 2, y + 2, z + 2);
        ArrayList entityList = (ArrayList) world.getEntitiesWithinAABB(EntityDakimakura.class, aabb);
        if (entityList.isEmpty()) {
            return;
        }
        boolean flipped = false;
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity = (Entity) entityList.get(i);
            if (entity instanceof EntityDakimakura) {
                EntityDakimakura entityDaki = (EntityDakimakura) entity;
                if (entityDaki.isDakiOverBlock(x, y, z)) {
                    if (event.action == Action.RIGHT_CLICK_BLOCK) {
                        entityDaki.flip();
                        flipped = true;
                    }
                    if (event.action == Action.LEFT_CLICK_BLOCK) {
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
