package moe.plushie.dakimakuramod.client.handler;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class PlacementPreviewHandler {
    
    private final ModelDakimakura modelDakimakura;
    
    public PlacementPreviewHandler(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        EntityPlayer entityPlayer = event.player;
        World world = event.player.worldObj;
        MovingObjectPosition target = event.target;
        if (target != null && target.typeOfHit != MovingObjectType.BLOCK) {
            return;
        }
        float partialTicks = event.partialTicks;
        int x = target.blockX;
        int y = target.blockY;
        int z = target.blockZ;
        int side = target.sideHit;
        
        double xOff = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks;
        double yOff = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * partialTicks;
        double zOff = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks;
        
        if (event.currentItem != null && event.currentItem.getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
            Block block = world.getBlock(x, y, z);
            ForgeDirection sideDir = ForgeDirection.getOrientation(side);
            int rot = (MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            ForgeDirection[] rots = new ForgeDirection[] {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
            ForgeDirection rotation = rots[rot].getOpposite();
            boolean standing = false;
            if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) {
                sideDir = ForgeDirection.UP;
            } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
                x += sideDir.offsetX;
                y += sideDir.offsetY;
                z += sideDir.offsetZ;
            }
            if (sideDir != ForgeDirection.UP & sideDir != ForgeDirection.DOWN) {
                rotation = sideDir.getOpposite();
                standing = true;
            }
            if (sideDir == ForgeDirection.DOWN) {
                y--;
                standing = true;
            }
            GL11.glPushMatrix();
            GL11.glTranslated(-xOff + x, -yOff + y, -zOff + z);
            float scale = 0.0625F;
            GL11.glTranslatef(8F * scale, 2F * scale, 8F * scale);
            if (rotation == ForgeDirection.WEST) {
                GL11.glRotatef(-90, 0, 1, 0);
            }
            if (rotation == ForgeDirection.NORTH) {
                GL11.glRotatef(180, 0, 1, 0);
            }
            if (rotation == ForgeDirection.EAST) {
                GL11.glRotatef(90, 0, 1, 0);
            }
            GL11.glTranslatef(0F * scale, 0F * scale, 4F * scale);
            
            if (!standing) {
                GL11.glRotatef(90, 1, 0, 0);
            } else {
                GL11.glTranslatef(0F * scale, 10F * scale, 2F * scale);
            }
            
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
            GL11.glLineWidth(1.0F);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDepthMask(false);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            modelDakimakura.render(null);
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopAttrib();
            GL11.glPopMatrix();
        }
    }
}
