package com.github.andrew0030.dakimakuramod.items.util;

import com.github.andrew0030.dakimakuramod.util.ClientInitContext;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public abstract class BEWLRBlockItem extends BlockItem
{
    public BEWLRBlockItem(Block block, Item.Properties properties)
    {
        super(block, properties);
    }

    public abstract void initializeClient(ClientInitContext ctx);

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer)
    {
        ClientInitContext ctx = new ClientInitContext(consumer);
        initializeClient(ctx);
        ctx.finish();
    }
}