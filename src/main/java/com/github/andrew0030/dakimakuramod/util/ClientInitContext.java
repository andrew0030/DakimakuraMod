package com.github.andrew0030.dakimakuramod.util;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientInitContext
{
    private final Consumer<IClientItemExtensions> consumer;
    private BlockEntityWithoutLevelRenderer renderer;

    public ClientInitContext(Consumer<IClientItemExtensions> consumer)
    {
        this.consumer = consumer;
    }

    public void registerRenderer(Supplier<BlockEntityWithoutLevelRenderer> renderer)
    {
        this.renderer = renderer.get();
    }

    public void finish()
    {
        if (renderer != null)
        {
            consumer.accept(new IClientItemExtensions()
            {
                @Override
                public BlockEntityWithoutLevelRenderer getCustomRenderer()
                {
                    return renderer;
                }
            });
        }
    }
}