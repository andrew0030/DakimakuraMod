package com.github.andrew0030.dakimakuramod.block_entities.dakimakura;

import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DakimakuraBlockEntity extends BlockEntity
{
    public DakimakuraBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(DMBlockEntities.DAKIMAKURA.get(), pos, blockState);
    }
}