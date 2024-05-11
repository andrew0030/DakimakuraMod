package com.github.andrew0030.dakimakuramod.util.obj;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.client.DakiTexture;
import com.github.andrew0030.dakimakuramod.util.DMRenderTypes;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DakimakuraModel
{
    // Base Texture
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(DakimakuraMod.MODID, "textures/obj/blank.png");
    // Model Paths
    private static final String MODEL_PATH = "models/obj/dakimakura.obj";
//    private static final String MODEL_PATH_LOD = "models/dakimakura-lod-%d.obj";
    // Model
    private final ObjModel DAKIMAKURA_MODEL;

    public DakimakuraModel()
    {
        DAKIMAKURA_MODEL = ObjModel.loadModel(new ResourceLocation(DakimakuraMod.MODID, MODEL_PATH));
    }

    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, Daki daki)
    {
        stack.pushPose();
        stack.scale(0.6F, 0.6F, 0.6F);
        stack.scale(-1F, 1F, -1F);

        if(daki != null)
        {
            DakiTexture dakiTexture = DakimakuraModClient.getDakiTextureManager().getTextureForDaki(daki);
            if (dakiTexture.isLoaded())
                DAKIMAKURA_MODEL.render(stack, buffer.getBuffer(DMRenderTypes.getSpecialDakimakuraType(dakiTexture.getId())), packedLight);
            else
                DAKIMAKURA_MODEL.render(stack, buffer.getBuffer(DMRenderTypes.getDakimakuraType(TEXTURE_BLANK)), packedLight);
        }
        else
        {
            DAKIMAKURA_MODEL.render(stack, buffer.getBuffer(DMRenderTypes.getDakimakuraType(TEXTURE_BLANK)), packedLight);
        }

        stack.popPose();
    }
}