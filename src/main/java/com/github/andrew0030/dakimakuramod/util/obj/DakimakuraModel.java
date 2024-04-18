package com.github.andrew0030.dakimakuramod.util.obj;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
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

    public void render(PoseStack stack, VertexConsumer buffer, int packedLight)
    {
        stack.pushPose();
        stack.scale(0.55F, 0.55F, 0.55F);
        stack.scale(-1F, 1F, -1F);
        DAKIMAKURA_MODEL.render(stack, buffer, packedLight);
        stack.popPose();
    }
}