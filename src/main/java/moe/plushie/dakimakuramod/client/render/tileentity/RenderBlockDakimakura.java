package moe.plushie.dakimakuramod.client.render.tileentity;

import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBlockDakimakura extends TileEntitySpecialRenderer {

    public static Daki lastItemDaki = null;
    
    private final ModelDakimakura modelDakimakura;
    
    public RenderBlockDakimakura(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
    }
    
    public void renderTileEntityAt(TileEntityDakimakura tileEntity, double x, double y, double z, float partialTickTime, int destroyStage) {
        if (tileEntity != null) {
            IBlockState blockState = tileEntity.getWorld().getBlockState(tileEntity.getPos());
            Block block = blockState.getBlock();
            
            if (block != ModBlocks.blockDakimakura) {
                return;
            }
            boolean standing = blockState.getValue(BlockDakimakura.PROPERTY_STANDING);
            EnumFacing rot = blockState.getValue(BlockDakimakura.PROPERTY_DIRECTION);
            boolean topPart = blockState.getValue(BlockDakimakura.PROPERTY_TOP);
            if (topPart) {
                return;
            }
            GlStateManager.pushMatrix();
            Minecraft mc = Minecraft.getMinecraft();
            mc.mcProfiler.startSection("dakimakura");
            
            float scale = 0.0625F;
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(8F * scale, 2F * scale, 8F * scale);
            if (rot == EnumFacing.WEST) {
                GlStateManager.rotate(-90, 0, 1, 0);
            }
            if (rot == EnumFacing.NORTH) {
                GlStateManager.rotate(180, 0, 1, 0);
            }
            if (rot == EnumFacing.EAST) {
                GlStateManager.rotate(90, 0, 1, 0);
            }
            GlStateManager.translate(0F * scale, 0F * scale, 4F * scale);
            
            if (!standing) {
                GlStateManager.rotate(90, 1, 0, 0);
            } else {
                GlStateManager.translate(0F * scale, 10F * scale, 2F * scale);
            }
            
            if (tileEntity.isFlipped()) {
                GlStateManager.rotate(180, 0, 1, 0);
            }
            modelDakimakura.render(tileEntity.getDaki(), tileEntity.getPos());
            GlStateManager.popMatrix();
            mc.mcProfiler.endSection();
        } else {
            modelDakimakura.render(lastItemDaki, 0);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        renderTileEntityAt((TileEntityDakimakura)tileEntity, x, y, z, partialTicks, destroyStage);
    }
}
