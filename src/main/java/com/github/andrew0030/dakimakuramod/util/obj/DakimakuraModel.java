package com.github.andrew0030.dakimakuramod.util.obj;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.DakimakuraModClient;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.client.DakiTexture;
import com.github.andrew0030.dakimakuramod.util.DMRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DakimakuraModel
{
    // Base Texture
    private static final ResourceLocation TEXTURE_BLANK = new ResourceLocation(DakimakuraMod.MODID, "textures/obj/blank.png");
    // Model Paths
    private static final String MODEL_PATH = "models/obj/dakimakura.obj";
    private static final String MODEL_PATH_LOD = "models/obj/dakimakura-lod-%d.obj";
    // Models
    private final ObjModel DAKIMAKURA_MODEL;
    private final ObjModel[] DAKIMAKURA_MODEL_LODS;

    public DakimakuraModel()
    {
        DAKIMAKURA_MODEL = ObjModel.loadModel(new ResourceLocation(DakimakuraMod.MODID, MODEL_PATH));
        DAKIMAKURA_MODEL_LODS = new ObjModel[4];
        for (int i = 0; i < DAKIMAKURA_MODEL_LODS.length; i++)
            DAKIMAKURA_MODEL_LODS[i] = ObjModel.loadModel(new ResourceLocation(DakimakuraMod.MODID, String.format(MODEL_PATH_LOD, i + 1)));
    }

    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, Daki daki, BlockPos pos)
    {
        double distance = 0;
        if (Minecraft.getInstance().player != null)
            distance = Minecraft.getInstance().player.distanceToSqr(pos.getCenter());
        int lod = Mth.floor(distance / 200D);
        this.render(stack, buffer, packedLight, daki, lod);
    }

    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, Daki daki, double x, double y, double z)
    {
        double distance = 0;
        if (Minecraft.getInstance().player != null)
            distance = Minecraft.getInstance().player.distanceToSqr(x, y, z);
        int lod = Mth.floor(distance / 200D);
        this.render(stack, buffer, packedLight, daki, lod);
    }

    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, Daki daki, int lod)
    {
        lod = Mth.clamp(lod, 0, DAKIMAKURA_MODEL_LODS.length);
        stack.pushPose();
        stack.scale(0.6F, 0.6F, 0.6F);
        stack.scale(-1F, 1F, -1F);

        RenderType renderType = DMRenderTypes.getDakimakuraType(TEXTURE_BLANK);
        if(daki != null)
        {
            DakiTexture dakiTexture = DakimakuraModClient.getDakiTextureManager().getTextureForDaki(daki);
            renderType = dakiTexture.isLoaded() ?
                    DMRenderTypes.getSpecialDakimakuraType(dakiTexture.getId()) :
                    renderType;
        }

        ObjModel model = lod == 0 ? DAKIMAKURA_MODEL : DAKIMAKURA_MODEL_LODS[lod - 1];
        model.render(stack, buffer.getBuffer(renderType), packedLight);

        stack.popPose();
    }
}