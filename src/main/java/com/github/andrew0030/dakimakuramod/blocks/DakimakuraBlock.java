package com.github.andrew0030.dakimakuramod.blocks;

import com.github.andrew0030.dakimakuramod.block_entities.dakimakura.DakimakuraBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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

import javax.annotation.Nullable;

public class DakimakuraBlock extends BaseEntityBlock
{
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
        if (!level.isClientSide() && player.isCreative())
        {
            boolean isStanding = state.getValue(STANDING);
            BlockPos blockpos = pos.relative(isStanding ? getVerticalDirection(state.getValue(TOP)) : getNeighbourDirection(state.getValue(TOP), state.getValue(FACING)));
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