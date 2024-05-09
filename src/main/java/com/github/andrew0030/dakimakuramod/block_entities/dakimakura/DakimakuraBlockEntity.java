package com.github.andrew0030.dakimakuramod.block_entities.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.blocks.DakimakuraBlock;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.registries.DMBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class DakimakuraBlockEntity extends BlockEntity
{
    private String packDirName;
    private String dakiDirName;
    private boolean flipped;

    public DakimakuraBlockEntity(BlockPos pos, BlockState blockState)
    {
        super(DMBlockEntities.DAKIMAKURA.get(), pos, blockState);
    }

    /** Used to synchronize the TileEntity with the client when the chunk it is in is loaded */
    @Override
    public CompoundTag getUpdateTag()
    {
        CompoundTag compound = new CompoundTag();
        this.saveToNBT(compound);
        return compound;
    }

    /** Used to synchronize the TileEntity with the client when the chunk it is in is loaded */
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket()
    {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(CompoundTag compound)
    {
        super.saveAdditional(compound);
        this.saveToNBT(compound);
    }

    @Override
    public void load(CompoundTag compound)
    {
        super.load(compound);
        this.loadFromNBT(compound);
    }

    /** Used to save data to the NBT */
    private void saveToNBT(CompoundTag compound)
    {
        if (this.packDirName != null && this.dakiDirName != null)
        {
            compound.putString(DakiTagSerializer.PACK_NAME_KEY, this.packDirName);
            compound.putString(DakiTagSerializer.DIR_NAME_KEY, this.dakiDirName);
        }
        DakiTagSerializer.setFlipped(compound, this.flipped);
    }

    /** Used to load data from the NBT */
    private void loadFromNBT(CompoundTag compound)
    {
        if (compound.contains(DakiTagSerializer.PACK_NAME_KEY, Tag.TAG_STRING) && compound.contains(DakiTagSerializer.DIR_NAME_KEY, Tag.TAG_STRING))
        {
            this.packDirName = compound.getString(DakiTagSerializer.PACK_NAME_KEY);
            this.dakiDirName = compound.getString(DakiTagSerializer.DIR_NAME_KEY);
        }
        this.flipped = DakiTagSerializer.isFlipped(compound);
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

    public Daki getDaki()
    {
        return DakimakuraMod.getDakimakuraManager().getDakiFromMap(this.packDirName, this.dakiDirName);
    }

    public void setDaki(Daki daki)
    {
        this.packDirName = daki != null ? daki.getPackDirectoryName() : null;
        this.dakiDirName = daki != null ? daki.getDakiDirectoryName() : null;
        this.setChanged();
        this.syncWithClients();
    }

    public boolean isFlipped()
    {
        return this.flipped;
    }

    public void setFlipped(boolean flipped)
    {
        this.flipped = flipped;
        this.setChanged();
        this.syncWithClients();
    }

    public void flip()
    {
        this.setFlipped(!this.flipped);
    }

    public void syncWithClients()
    {
        if (this.getLevel() != null && !this.getLevel().isClientSide())
            DakimakuraBlockEntity.syncWithNearbyPlayers(this.getLevel(), this);
    }

    public static void syncWithNearbyPlayers(Level level, BlockEntity blockEntity)
    {
        List<Player> players = new ArrayList<>(level.players());
        for (Player player : players)
            if (player instanceof ServerPlayer serverPlayer)
                if (serverPlayer.distanceToSqr(blockEntity.getBlockPos().getX(), blockEntity.getBlockPos().getY(), blockEntity.getBlockPos().getZ()) < 64)
                    serverPlayer.connection.send(blockEntity.getUpdatePacket());
    }
}