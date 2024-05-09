package com.github.andrew0030.dakimakuramod.entities.dakimakura;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiTagSerializer;
import com.github.andrew0030.dakimakuramod.items.DakimakuraItem;
import com.github.andrew0030.dakimakuramod.registries.DMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class Dakimakura extends Entity
{
    private static final EntityDataAccessor<Integer> FACING = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FLIPPED = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.BOOLEAN);
    private String packName;
    private String dirName;

    public Dakimakura(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData()
    {
        this.getEntityData().define(FACING, 2); // 2: North in Direction
        this.getEntityData().define(FLIPPED, false);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        compound.putInt(DakiTagSerializer.FACING_KEY, this.getRotationInt());
        compound.putBoolean(DakiTagSerializer.FLIPPED_KEY, this.isFlipped());
        if (this.packName != null && this.dirName != null)
        {
            compound.putString(DakiTagSerializer.PACK_NAME_KEY, this.packName);
            compound.putString(DakiTagSerializer.DIR_NAME_KEY, this.dirName);
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {
        this.setRotationInt(compound.getInt(DakiTagSerializer.FACING_KEY));
        this.setFlipped(compound.getBoolean(DakiTagSerializer.FLIPPED_KEY));
        if (compound.contains(DakiTagSerializer.PACK_NAME_KEY, Tag.TAG_STRING) && compound.contains(DakiTagSerializer.DIR_NAME_KEY, Tag.TAG_STRING))
        {
            this.packName =  compound.getString(DakiTagSerializer.PACK_NAME_KEY);
            this.dirName =  compound.getString(DakiTagSerializer.DIR_NAME_KEY);
        }
    }

    @Override
    public void tick()
    {
        super.tick();
        if (!this.level().isClientSide())
        {
            BlockPos pos = this.blockPosition();
            BlockState state = this.level().getBlockState(pos);
            Block block = state.getBlock();
            if (!block.isBed(state, this.level(), pos, null))
            {
                this.dropAsItem();
                this.setRemoved(RemovalReason.DISCARDED);
            }
        }
    }

    /** Creates a {@link Daki} in the form of an {@link ItemEntity} and spawns it. */
    public void dropAsItem()
    {
        Daki daki = this.getDaki();
        ItemStack itemStack = new ItemStack(DMBlocks.DAKIMAKURA.get());
        if (daki != null)
        {
            itemStack.setTag(new CompoundTag());
            DakiTagSerializer.serialize(daki, itemStack.getTag());
            DakimakuraItem.setFlipped(itemStack, this.isFlipped());
        }
        ItemEntity itemEntity = new ItemEntity(this.level(), position().x(), position().y(), position().z(), itemStack);
        this.level().addFreshEntity(itemEntity);
    }

    public void setDaki(Daki daki)
    {
        this.packName = (daki != null) ? daki.getPackDirectoryName() : null;
        this.dirName = (daki != null) ? daki.getDakiDirectoryName() : null;
    }

    public Daki getDaki()
    {
        return DakimakuraMod.getDakimakuraManager().getDakiFromMap(this.packName, this.dirName);
    }

    public boolean isFlipped()
    {
        return this.getEntityData().get(FLIPPED);
    }

    public void setFlipped(boolean flipped)
    {
        this.getEntityData().set(FLIPPED, flipped);
    }

    public void setRotation(Direction facing)
    {
        this.getEntityData().set(FACING, facing.ordinal());
    }

    public void setRotationInt(int val)
    {
        this.setRotation(Direction.values()[val]);
    }

    public Direction getRotation()
    {
        return Direction.values()[this.getRotationInt()];
    }

    public int getRotationInt()
    {
        return this.getEntityData().get(FACING);
    }
}