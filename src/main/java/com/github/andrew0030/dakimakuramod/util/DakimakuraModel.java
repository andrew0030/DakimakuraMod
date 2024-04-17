package com.github.andrew0030.dakimakuramod.util;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.resources.ResourceLocation;

public class DakimakuraModel
{
    // Model Paths
    private static final String MODEL_PATH = "models/obj/dakimakura.obj";
//    private static final String MODEL_PATH_LOD = "models/dakimakura-lod-%d.obj";
    // Model
    private final ObjModel DAKIMAKURA_MODEL;

    public DakimakuraModel()
    {
        DAKIMAKURA_MODEL = ObjModel.loadModel(new ResourceLocation(DakimakuraMod.MODID, MODEL_PATH));
    }

    public void render(PoseStack stack, VertexConsumer buffer)
    {
        stack.pushPose();
        stack.translate(0.5F, 0.5F, 0.5F);
        stack.scale(0.55F, 0.55F, 0.55F);
//        stack.translate(0, 0.35F, 0);
        stack.scale(1F, -1F, -1F);//TODO: maybe: -1 -1 1
        DAKIMAKURA_MODEL.render(stack, buffer, 1.0F, 1.0F, 1.0F, 14680064);
        stack.popPose();
    }
}