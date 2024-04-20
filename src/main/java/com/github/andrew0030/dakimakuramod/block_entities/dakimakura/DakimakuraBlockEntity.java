package com.github.andrew0030.dakimakuramod.block_entities.dakimakura;

import com.github.andrew0030.dakimakuramod.blocks.DakimakuraBlock;
import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;

public class DakimakuraBlockEntity extends BlockEntity
{
    public DakimakuraBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(DMBlockEntities.DAKIMAKURA.get(), pos, blockState);
    }

    @Override
    public AABB getRenderBoundingBox()
    {
        AABB aabb = super.getRenderBoundingBox();
        BlockState state = this.getBlockState();
        if(state.getValue(DakimakuraBlock.TOP))
            return aabb;
        if (state.getValue(DakimakuraBlock.FACE).equals(AttachFace.WALL))
            return aabb.expandTowards(0D, 0.9375D, 0D);
        return switch (state.getValue(DakimakuraBlock.FACING))
        {
            default -> aabb.expandTowards(0D, 0D, 0.9375D);
            case WEST -> aabb.expandTowards(-0.9375D, 0D, 0D);
            case NORTH -> aabb.expandTowards(0D, 0D, -0.9375D);
            case EAST -> aabb.expandTowards(0.9375D, 0D, 0D);
        };
    }
}