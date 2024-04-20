package com.github.andrew0030.dakimakuramod.util;

import com.mojang.logging.LogUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

import java.util.function.BiFunction;

public class VoxelShapeTransformer
{
    private static final Logger LOGGER = LogUtils.getLogger();
    private VoxelShape shape;

    private VoxelShapeTransformer(VoxelShape shape)
    {
        this.shape = shape;
    }

    /**
     * Initializes the VoxelShapeTransformer
     * @param shape The VoxelShape that will be transformed
     */
    public static VoxelShapeTransformer of(VoxelShape shape)
    {
        return new VoxelShapeTransformer(shape);
    }

    /**
     * Rotates the VoxelShape from a certain Horizontal Direction to another
     * @param from The Horizontal Direction the VoxelShape is currently facing
     * @param to The Horizontal Direction the VoxelShape should be facing
     */
    public VoxelShapeTransformer rotateHorizontally(Direction from, Direction to)
    {
        // If any of the Directions aren't Horizontal we return without making any changes
        if(!from.getAxis().isHorizontal() || !to.getAxis().isHorizontal())
        {
            LOGGER.warn("Found a non Horizontal Direction in VoxelShapeTransformer.rotateShape");
            LOGGER.warn("from: " + from.getName());
            LOGGER.warn("to: " + to.getName());
            LOGGER.warn("No changes have been made!");
            return this;
        }
        // If all Directions are Horizontal we proceed to rotate the VoxelShape
        int rotationCount = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
        for (int i = 0; i < rotationCount; i++)
        {
            final VoxelShape[] rotatedShape = {Shapes.empty()};
            this.shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> rotatedShape[0] = Shapes.or(rotatedShape[0], Shapes.create(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
            this.shape = rotatedShape[0];
        }
        return this;
    }

    /**
     * Mirrors the VoxelShape on a given Axis
     * @param axis The Axis on which the VoxelShape will be mirrored
     */
    public VoxelShapeTransformer mirror(Direction.Axis axis)
    {
        final VoxelShape[] flippedShape = {Shapes.empty()};
        this.shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) ->
        {
            switch (axis)
            {
                case X -> flippedShape[0] = Shapes.or(flippedShape[0], Shapes.create(1 - maxX, minY, minZ, 1 - minX, maxY, maxZ));
                case Y -> flippedShape[0] = Shapes.or(flippedShape[0], Shapes.create(minX, 1 - maxY, minZ, maxX, 1 - minY, maxZ));
                case Z -> flippedShape[0] = Shapes.or(flippedShape[0], Shapes.create(minX, minY, 1 - maxZ, maxX, maxY, 1 - minZ));
            }
        });
        this.shape = flippedShape[0];
        return this;
    }

    /**
     * Creates a VoxelShape Array of size 4, with the Direction order of S-W-N-E. Perfect to be used with Direction.get2DDataValue
     * @param facing The Horizontal Direction the VoxelShape is currently facing
     */
    public VoxelShape[] createHorizontalArray(Direction facing)
    {
        // If the Direction isn't Horizontal we return a size 4 Array filled with the current VoxelShape
        if(!facing.getAxis().isHorizontal())
        {
            LOGGER.warn("Found a non Horizontal Direction in VoxelShapeTransformer.createHorizontalArray");
            LOGGER.warn("facing: " + facing.getName());
            LOGGER.warn("Returning Array of current VoxelShape!");
            return new VoxelShape[] {this.getShape(), this.getShape(), this.getShape(), this.getShape()};
        }
        // If the Direction was Horizontal we continue normally
        VoxelShape[] values = new VoxelShape[4]; // An array that will hold all our transformed VoxelShape's
        VoxelShape originalShape = this.getShape(); // The current VoxelShape
        BiFunction<Direction, Direction, VoxelShape> rotateFunction = (from, to) ->
        {
            VoxelShapeTransformer transformer = VoxelShapeTransformer.of(originalShape);
            transformer.rotateHorizontally(from, to);
            return transformer.getShape();
        };
        // Rotates the VoxelShape accordingly and stores in the Array
        switch (facing)
        {
            case SOUTH ->
            {
                values[0] = originalShape;
                values[1] = rotateFunction.apply(Direction.SOUTH, Direction.WEST);
                values[2] = rotateFunction.apply(Direction.SOUTH, Direction.NORTH);
                values[3] = rotateFunction.apply(Direction.SOUTH, Direction.EAST);
            }
            case WEST ->
            {
                values[0] = rotateFunction.apply(Direction.WEST, Direction.SOUTH);
                values[1] = originalShape;
                values[2] = rotateFunction.apply(Direction.WEST, Direction.NORTH);
                values[3] = rotateFunction.apply(Direction.WEST, Direction.EAST);
            }
            case NORTH ->
            {
                values[0] = rotateFunction.apply(Direction.NORTH, Direction.SOUTH);
                values[1] = rotateFunction.apply(Direction.NORTH, Direction.WEST);
                values[2] = originalShape;
                values[3] = rotateFunction.apply(Direction.NORTH, Direction.EAST);
            }
            case EAST ->
            {
                values[0] = rotateFunction.apply(Direction.EAST, Direction.SOUTH);
                values[1] = rotateFunction.apply(Direction.EAST, Direction.WEST);
                values[2] = rotateFunction.apply(Direction.EAST, Direction.NORTH);
                values[3] = originalShape;
            }
        }
        return values;
    }

    /**
     * @return The VoxelShape with all transformations that were applied before
     */
    public VoxelShape getShape()
    {
        return this.shape;
    }
}