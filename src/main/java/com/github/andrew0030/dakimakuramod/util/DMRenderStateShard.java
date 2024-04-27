package com.github.andrew0030.dakimakuramod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class DMRenderStateShard extends RenderStateShard.EmptyTextureStateShard
{
    private final Optional<ResourceLocation> texture;
    protected boolean blur;
    protected boolean mipmap;

    public DMRenderStateShard(ResourceLocation texture, boolean blur, boolean mipmap) {
        super(
                () -> {
                    TextureManager texturemanager = Minecraft.getInstance().getTextureManager();
                    texturemanager.getTexture(texture).setFilter(blur, mipmap);
                    RenderSystem.setShaderTexture(0, texture);
                },
                () -> {

                });
        this.texture = Optional.of(texture);
        this.blur = blur;
        this.mipmap = mipmap;
    }

    @Override
    public String toString()
    {
        return this.name + "[" + this.texture + "(blur=" + this.blur + ", mipmap=" + this.mipmap + ")]";
    }

    protected Optional<ResourceLocation> cutoutTexture()
    {
        return this.texture;
    }
}