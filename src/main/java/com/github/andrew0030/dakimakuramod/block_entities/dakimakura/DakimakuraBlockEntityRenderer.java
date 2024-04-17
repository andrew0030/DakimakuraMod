package com.github.andrew0030.dakimakuramod.block_entities.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.util.DMRenderTypes;
import com.github.andrew0030.dakimakuramod.util.obj.DakimakuraModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
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
        poseStack.pushPose();
        this.dakimakuraModel.render(poseStack, buffer.getBuffer(DMRenderTypes.getDakimakuraType(TEXTURE_BLANK)), packedLight);
        poseStack.popPose();
    }
}