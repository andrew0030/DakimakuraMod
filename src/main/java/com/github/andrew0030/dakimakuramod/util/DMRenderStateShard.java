package com.github.andrew0030.dakimakuramod.util;

import net.minecraft.client.renderer.RenderStateShard;
import org.lwjgl.opengl.GL11;

public class DMRenderStateShard extends RenderStateShard.EmptyTextureStateShard
{
    protected int id;

    public DMRenderStateShard(int id) {
        super(
                () -> {
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
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