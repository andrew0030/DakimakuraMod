package com.github.andrew0030.dakimakuramod.entities.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.util.DMRenderTypes;
import com.github.andrew0030.dakimakuramod.util.obj.DakimakuraModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DakimakuraRenderer extends EntityRenderer<Dakimakura>
{
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(DakimakuraMod.MODID, "textures/obj/blank.png");
    private final DakimakuraModel dakimakuraModel;

    public DakimakuraRenderer(EntityRendererProvider.Context context)
    {
        super(context);
        this.dakimakuraModel = new DakimakuraModel();
    }

    @Override
    public void render(Dakimakura entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight)
    {
        poseStack.pushPose();
        switch(entity.getRotation())
        {
            default -> {}
            case SOUTH -> poseStack.mulPose(Axis.YN.rotationDegrees(180.0F));
            case WEST -> poseStack.mulPose(Axis.YN.rotationDegrees(270.0F));
            case EAST -> poseStack.mulPose(Axis.YN.rotationDegrees(90.0F));
        }
        poseStack.translate(0.0F, 0.12F, -0.5F);
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        if (entity.isFlipped())
            poseStack.mulPose(Axis.YN.rotationDegrees(180));
        this.dakimakuraModel.render(poseStack, buffer.getBuffer(DMRenderTypes.getDakimakuraType(this.getTextureLocation(entity))), packedLight);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Dakimakura entity)
    {
        return TEXTURE_BLANK;
    }
}