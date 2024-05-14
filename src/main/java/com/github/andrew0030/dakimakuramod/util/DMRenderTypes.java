package com.github.andrew0030.dakimakuramod.util;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class DMRenderTypes extends RenderStateShard
{
    public DMRenderTypes(String name, Runnable setupTask, Runnable clearTask)
    {
        super(name, setupTask, clearTask);
    }

    public static RenderType getDakimakuraType(ResourceLocation texture)
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(texture, false, false))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderType.create(DakimakuraMod.MODID + "dakimakura", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, RenderType.TRANSIENT_BUFFER_SIZE, true, false, state);
    }

    public static RenderType getSpecialDakimakuraType(int id)
    {
        RenderType.CompositeState state = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_SOLID_SHADER)
                .setTextureState(new DMRenderStateShard(id))
                .setTransparencyState(NO_TRANSPARENCY)
                .setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY)
                .createCompositeState(true);
        return RenderType.create(DakimakuraMod.MODID + "dakimakura", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, RenderType.TRANSIENT_BUFFER_SIZE, true, false, state);
    }
}