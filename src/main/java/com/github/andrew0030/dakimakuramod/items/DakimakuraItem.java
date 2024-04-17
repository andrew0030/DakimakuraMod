package com.github.andrew0030.dakimakuramod.items;

import com.github.andrew0030.dakimakuramod.block_entities.util.DMBlockEntityWithoutLevelRenderer;
import com.github.andrew0030.dakimakuramod.items.util.BEWLRBlockItem;
import com.github.andrew0030.dakimakuramod.util.ClientInitContext;
import net.minecraft.world.level.block.Block;

public class DakimakuraItem extends BEWLRBlockItem
{
    public DakimakuraItem(Block block, Properties properties)
    {
        super(block, properties);
    }

    @Override
    public void initializeClient(ClientInitContext ctx)
    {
        ctx.registerRenderer(DMBlockEntityWithoutLevelRenderer::new);
    }
}