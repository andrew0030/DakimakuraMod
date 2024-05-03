package com.github.andrew0030.dakimakuramod.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderStateShard;


public class DMRenderStateShard extends RenderStateShard.EmptyTextureStateShard
{
    protected int id;

    public DMRenderStateShard(int id) {
        super(
                () -> {
                    RenderSystem.setShaderTexture(0, id);
                },
                () -> {}
        );
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "daki_" + this.name + "[id=" + this.id + "]";
    }
}