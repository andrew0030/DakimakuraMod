package com.github.andrew0030.dakimakuramod.blocks;

import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntity;
import com.github.andrew0030.dakimakuramod.util.VoxelShapeUtil;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class DakimakuraBlock extends BaseEntityBlock
{
    // Bounding Box
    // Standing Pillow
    private static final VoxelShape STANDING_BOTTOM_SOUTH = Block.box(3, 1, 12, 13, 16, 16);
    private static final VoxelShape STANDING_BOTTOM_WEST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.WEST, STANDING_BOTTOM_SOUTH);
    private static final VoxelShape STANDING_BOTTOM_NORTH = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.NORTH, STANDING_BOTTOM_SOUTH);
    private static final VoxelShape STANDING_BOTTOM_EAST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.EAST, STANDING_BOTTOM_SOUTH);
    private static final VoxelShape[] STANDING_BOTTOM = new VoxelShape[] {STANDING_BOTTOM_SOUTH, STANDING_BOTTOM_WEST, STANDING_BOTTOM_NORTH, STANDING_BOTTOM_EAST};
    private static final VoxelShape STANDING_TOP_SOUTH = Block.box(3, 0, 12, 13, 15, 16);
    private static final VoxelShape STANDING_TOP_WEST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.WEST, STANDING_TOP_SOUTH);
    private static final VoxelShape STANDING_TOP_NORTH = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.NORTH, STANDING_TOP_SOUTH);
    private static final VoxelShape STANDING_TOP_EAST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.EAST, STANDING_TOP_SOUTH);
    private static final VoxelShape[] STANDING_TOP = new VoxelShape[] {STANDING_TOP_SOUTH, STANDING_TOP_WEST, STANDING_TOP_NORTH, STANDING_TOP_EAST};
    // Lying Pillow
    private static final VoxelShape LYING_BOTTOM_SOUTH = Block.box(3, 0, 1, 13, 4, 16);
    private static final VoxelShape LYING_BOTTOM_WEST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.WEST, LYING_BOTTOM_SOUTH);
    private static final VoxelShape LYING_BOTTOM_NORTH = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.NORTH, LYING_BOTTOM_SOUTH);
    private static final VoxelShape LYING_BOTTOM_EAST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.EAST, LYING_BOTTOM_SOUTH);
    private static final VoxelShape[] LYING_BOTTOM = new VoxelShape[] {LYING_BOTTOM_SOUTH, LYING_BOTTOM_WEST, LYING_BOTTOM_NORTH, LYING_BOTTOM_EAST};
    private static final VoxelShape LYING_TOP_SOUTH = Block.box(3, 0, 0, 13, 4, 15);
    private static final VoxelShape LYING_TOP_WEST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.WEST, LYING_TOP_SOUTH);
    private static final VoxelShape LYING_TOP_NORTH = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.NORTH, LYING_TOP_SOUTH);
    private static final VoxelShape LYING_TOP_EAST = VoxelShapeUtil.rotateShape(Direction.SOUTH, Direction.EAST, LYING_TOP_SOUTH);
    private static final VoxelShape[] LYING_TOP = new VoxelShape[] {LYING_TOP_SOUTH, LYING_TOP_WEST, LYING_TOP_NORTH, LYING_TOP_EAST};
    // Block Properties
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty FLIPPED = BooleanProperty.create("flipped");
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final BooleanProperty STANDING = BooleanProperty.create("standing");

    public DakimakuraBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FLIPPED, false).setValue(TOP, false).setValue(STANDING, false));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        Direction clickedDirection = context.getClickedFace();
        boolean isStanding = clickedDirection.getAxis().isHorizontal();
        Direction direction = context.getHorizontalDirection();
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos relativePos = blockpos.relative(isStanding ? Direction.UP : direction);
        return level.getBlockState(relativePos).canBeReplaced(context) && level.getWorldBorder().isWithinBounds(relativePos) ? this.defaultBlockState().setValue(FACING, isStanding ? clickedDirection.getOpposite() : direction).setValue(STANDING, isStanding) : null;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        if (!level.isClientSide())
        {
            boolean isStanding = state.getValue(STANDING);
            BlockPos blockpos = pos.relative(isStanding ? Direction.UP : state.getValue(FACING));
            level.setBlockAndUpdate(blockpos, state.setValue(TOP, true));
            state.updateNeighbourShapes(level, pos, 2);
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player)
    {
        if (!level.isClientSide())
        {
            boolean isStanding = state.getValue(STANDING);
            BlockPos blockpos = pos.relative(isStanding ? getVerticalDirection(state.getValue(TOP)) : getNeighbourDirection(state.getValue(TOP), state.getValue(FACING)));
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(this))
                level.destroyBlock(blockpos, false, player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
    {
        int direction = state.getValue(FACING).get2DDataValue();
        if(state.getValue(STANDING))
            return state.getValue(TOP) ? STANDING_TOP[direction] : STANDING_BOTTOM[direction];
        return state.getValue(TOP) ? LYING_TOP[direction] : LYING_BOTTOM[direction];
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
    public RenderShape getRenderShape(BlockState state)
    {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(FACING, FLIPPED, TOP, STANDING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new DakimakuraBlockEntity(pos, state);
    }
}