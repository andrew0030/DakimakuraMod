package moe.plushie.dakimakuramod.client.handler;

import org.lwjgl.opengl.GL11;

import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PlacementPreviewHandler {
    
    private final ModelDakimakura modelDakimakura;
    
    public PlacementPreviewHandler(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlightEvent(DrawBlockHighlightEvent event) {
        EntityPlayer entityPlayer = event.getPlayer();
        World world = event.getPlayer().getEntityWorld();
        RayTraceResult target = event.getTarget();
        if (target != null && target.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }
        float partialTicks = event.getPartialTicks();
        
        BlockPos pos = target.getBlockPos();
        EnumFacing facing = target.sideHit;
        
        double xOff = entityPlayer.lastTickPosX + (entityPlayer.posX - entityPlayer.lastTickPosX) * partialTicks;
        double yOff = entityPlayer.lastTickPosY + (entityPlayer.posY - entityPlayer.lastTickPosY) * partialTicks;
        double zOff = entityPlayer.lastTickPosZ + (entityPlayer.posZ - entityPlayer.lastTickPosZ) * partialTicks;
        
        if (entityPlayer.getHeldItemMainhand() != null && entityPlayer.getHeldItemMainhand().getItem() == Item.getItemFromBlock(ModBlocks.blockDakimakura)) {
            IBlockState iblockstate = world.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if (!block.isReplaceable(world, pos)) {
                pos = pos.offset(facing);
            }
            int rot = (MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            EnumFacing[] rots = new EnumFacing[] {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
            EnumFacing rotation = rots[rot].getOpposite();
            
            
            boolean standing = false;
            
            
            if (facing != EnumFacing.UP & facing != EnumFacing.DOWN) {
                standing = true;
                rotation = facing.getOpposite();
            }
            if (facing == EnumFacing.DOWN) {
                standing = true;
                pos = pos.offset(EnumFacing.DOWN);
            }
            
            GlStateManager.pushMatrix();
            GlStateManager.translate(-xOff + pos.getX(), -yOff + pos.getY(), -zOff + pos.getZ());
            float scale = 0.0625F;
            GlStateManager.translate(8F * scale, 2F * scale, 8F * scale);
            if (rotation == EnumFacing.WEST) {
                GlStateManager.rotate(-90, 0, 1, 0);
            }
            if (rotation == EnumFacing.NORTH) {
                GlStateManager.rotate(180, 0, 1, 0);
            }
            if (rotation == EnumFacing.EAST) {
                GlStateManager.rotate(90, 0, 1, 0);
            }
            if (block.isBed(world.getBlockState(pos), world, pos, entityPlayer) & facing == EnumFacing.UP) {
                GlStateManager.translate(0F * scale, -7F * scale, 0);
            }
            
            GlStateManager.translate(0F * scale, 0F * scale, 4F * scale);
            
            if (!standing) {
                GlStateManager.rotate(90, 1, 0, 0);
            } else {
                GlStateManager.translate(0F * scale, 10F * scale, 2F * scale);
            }
            
            GlStateManager.pushAttrib();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.color(0.0F, 0.0F, 0.0F, 0.4F);
            GlStateManager.glLineWidth(1.0F);
            GlStateManager.disableTexture2D();
            GlStateManager.depthMask(false);
            GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
            GlStateManager.disableLighting();
            GlStateManager.disableNormalize();
            modelDakimakura.render(null, 0);
            GlStateManager.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
            GlStateManager.depthMask(true);
            GlStateManager.enableTexture2D();
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }
}
