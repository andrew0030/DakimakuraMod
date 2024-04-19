package com.github.andrew0030.dakimakuramod.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeUtil
{
    public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape)
    {
        int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < times; i++)
        {
            final VoxelShape[] rotatedShape = {Shapes.empty()};
            shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
            {
                double newMinX = 1 - maxZ;
                double newMaxX = 1 - minZ;
                rotatedShape[0] = Shapes.or(rotatedShape[0], Shapes.create(newMinX, minY, minX, newMaxX, maxY, maxX));
            });
            shape = rotatedShape[0];
        }
        return shape;
    }
}
