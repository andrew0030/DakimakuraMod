package moe.plushie.dakimakuramod.client.render.tileentity;

import moe.plushie.dakimakuramod.client.model.ModelDakimakura;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderBlockDakimakura extends TileEntitySpecialRenderer {

    private final ModelDakimakura modelDakimakura;
    
    public RenderBlockDakimakura(ModelDakimakura modelDakimakura) {
        this.modelDakimakura = modelDakimakura;
    }
    
    public void renderTileEntityAt(TileEntityDakimakura tileEntity, double x, double y, double z, float partialTickTime, int destroyStage) {
        if (tileEntity != null) {
            int meta = tileEntity.getBlockMetadata();
            if (BlockDakimakura.isTopPart(meta)) {
                return;
            }
        }
        
        /*
        Minecraft mc = Minecraft.getMinecraft();
        mc.mcProfiler.startSection("dakimakura");
        EnumFacing rot = BlockDakimakura.getRotation(meta);
        boolean standing = BlockDakimakura.isStanding(meta);
        
        float scale = 0.0625F;
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glTranslatef(8F * scale, 2F * scale, 8F * scale);
        if (rot == ForgeDirection.WEST) {
            GL11.glRotatef(-90, 0, 1, 0);
        }
        if (rot == ForgeDirection.NORTH) {
            GL11.glRotatef(180, 0, 1, 0);
        }
        if (rot == ForgeDirection.EAST) {
            GL11.glRotatef(90, 0, 1, 0);
        }
        GL11.glTranslatef(0F * scale, 0F * scale, 4F * scale);
        
        if (!standing) {
            GL11.glRotatef(90, 1, 0, 0);
        } else {
            GL11.glTranslatef(0F * scale, 10F * scale, 2F * scale);
        }
        
        if (tileEntity.isFlipped()) {
            GL11.glRotatef(180, 0, 1, 0);
        }*/
        
        if (tileEntity != null) {
            modelDakimakura.render(tileEntity.getDaki());
        } else {
            modelDakimakura.render(null);
        }
        
        
        
        /*
        GL11.glPopMatrix();
        Minecraft.getMinecraft().mcProfiler.endSection();
        */
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks, int destroyStage) {
        renderTileEntityAt((TileEntityDakimakura)tileEntity, x, y, z, partialTicks, destroyStage);
    }
}
