package com.github.andrew0030.dakimakuramod.blocks;

import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntity;
import com.github.andrew0030.dakimakuramod.util.VoxelShapeTransformer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DakimakuraBlock extends BaseEntityBlock
{
    // Wall Pillow Bounding Box
    private static final VoxelShape WALL_BOTTOM_SOUTH = Block.box(3, 1, 12, 13, 16, 16);
    private static final VoxelShape[] WALL_BOTTOM = VoxelShapeTransformer.of(WALL_BOTTOM_SOUTH).createHorizontalArray(Direction.SOUTH);
    private static final VoxelShape[] WALL_TOP = VoxelShapeTransformer.of(WALL_BOTTOM_SOUTH).mirror(Direction.Axis.Y).createHorizontalArray(Direction.SOUTH);
    // Floor Pillow Bounding Box
    private static final VoxelShape FLOOR_BOTTOM_SOUTH = Block.box(3, 0, 1, 13, 4, 16);
    private static final VoxelShape[] FLOOR_BOTTOM = VoxelShapeTransformer.of(FLOOR_BOTTOM_SOUTH).createHorizontalArray(Direction.SOUTH);
    private static final VoxelShape[] FLOOR_TOP = VoxelShapeTransformer.of(FLOOR_BOTTOM_SOUTH).mirror(Direction.Axis.Z).createHorizontalArray(Direction.SOUTH);
    // Ceiling Pillow Bounding Box
    private static final VoxelShape[] CEILING_BOTTOM = VoxelShapeTransformer.of(FLOOR_BOTTOM_SOUTH).mirror(Direction.Axis.Y).createHorizontalArray(Direction.SOUTH);
    private static final VoxelShape[] CEILING_TOP = VoxelShapeTransformer.of(FLOOR_BOTTOM_SOUTH).mirror(Direction.Axis.Y).mirror(Direction.Axis.Z).createHorizontalArray(Direction.SOUTH);
    // Block Properties
    public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final BooleanProperty TOP = BooleanProperty.create("top");

    public DakimakuraBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACE, AttachFace.FLOOR).setValue(FACING, Direction.NORTH).setValue(FLIPPED, false).setValue(TOP, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction clickedFace = context.getClickedFace();
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos relativePos = blockpos.relative(clickedFace.equals(Direction.DOWN) ? direction.getOpposite() : (clickedFace.getAxis().isHorizontal() ? Direction.UP : direction));
        // If there is Blocks in the way, or we are out of bounds, we return null
        if (!level.getBlockState(relativePos).canBeReplaced(context) || !level.getWorldBorder().isWithinBounds(relativePos))
            return null;
        // The Face the pillow is attached to
        AttachFace attachFace = clickedFace.equals(Direction.UP) ? AttachFace.FLOOR :
                clickedFace.equals(Direction.DOWN) ? AttachFace.CEILING :
                        AttachFace.WALL;
        // The Direction the pillow is facing
        Direction facingDirection = clickedFace.equals(Direction.DOWN) ? direction.getOpposite() :
                clickedFace.getAxis().isHorizontal() ? clickedFace.getOpposite() :
                        direction;
        // We create a state for the pillow using the values we determined
        return this.defaultBlockState().setValue(FACE, attachFace).setValue(FACING, facingDirection);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (!level.isClientSide())
        {
            boolean isWall = state.getValue(FACE).equals(AttachFace.WALL);
            BlockPos blockpos = pos.relative(isWall ? Direction.UP : state.getValue(FACING));
            level.setBlockAndUpdate(blockpos, state.setValue(TOP, true));
            state.updateNeighbourShapes(level, pos, 2);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide())
        {
            boolean isWall = state.getValue(FACE).equals(AttachFace.WALL);
            BlockPos blockpos = pos.relative(isWall ? getVerticalDirection(state.getValue(TOP)) : getNeighbourDirection(state.getValue(TOP), state.getValue(FACING)));
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(this))
                level.destroyBlock(blockpos, false, player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    private Direction getNeighbourDirection(boolean isTop, Direction direction)
    {
        return !isTop ? direction : direction.getOpposite();
    }

    private Direction getVerticalDirection(boolean isTop)
    {
        return isTop ? Direction.DOWN : Direction.UP;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        int direction = state.getValue(FACING).get2DDataValue();
        return switch (state.getValue(FACE))
        {
            default -> state.getValue(TOP) ? FLOOR_TOP[direction] : FLOOR_BOTTOM[direction];
            case WALL -> state.getValue(TOP) ? WALL_TOP[direction] : WALL_BOTTOM[direction];
            case CEILING -> state.getValue(TOP) ? CEILING_TOP[direction] : CEILING_BOTTOM[direction];
        };
    }

    @Override
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACE, FACING, FLIPPED, TOP);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DakimakuraBlockEntity(pos, state);
    }
}