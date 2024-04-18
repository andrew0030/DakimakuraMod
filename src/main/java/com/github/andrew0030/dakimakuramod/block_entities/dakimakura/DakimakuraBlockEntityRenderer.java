package com.github.andrew0030.dakimakuramod.block_entities.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.blocks.DakimakuraBlock;
import com.github.andrew0030.dakimakuramod.util.DMRenderTypes;
import com.github.andrew0030.dakimakuramod.util.obj.DakimakuraModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

public class DakimakuraBlockEntityRenderer implements BlockEntityRenderer<DakimakuraBlockEntity>
{
    // Base Texture
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(DakimakuraMod.MODID, "textures/obj/blank.png");
    private final DakimakuraModel dakimakuraModel;

    public DakimakuraBlockEntityRenderer(BlockEntityRendererProvider.Context context)
    {
        this.dakimakuraModel = new DakimakuraModel();
    }

    @Override
    public void render(DakimakuraBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay)
    {
        Direction facing = blockEntity.getBlockState().getValue(DakimakuraBlock.FACING);
        // If the block is the top part we don't need to render anything
        if(blockEntity.getBlockState().getValue(DakimakuraBlock.TOP))
            return;

        poseStack.pushPose();
        poseStack.translate(0.5F, 0.5F, 0.5F);
        switch(facing)
        {
            default -> {}
            case SOUTH -> poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
            case WEST -> poseStack.mulPose(Axis.YN.rotationDegrees(270.0F));
            case EAST -> poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        }

        if(blockEntity.hasLevel())
        {
            if(!blockEntity.getBlockState().getValue(DakimakuraBlock.STANDING))
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
            poseStack.translate(0.0F, 0.5F, -0.39F);
        }

        this.dakimakuraModel.render(poseStack, buffer.getBuffer(DMRenderTypes.getDakimakuraType(TEXTURE_BLANK)), packedLight);
        poseStack.popPose();
    }
}