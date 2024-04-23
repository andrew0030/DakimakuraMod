package com.github.andrew0030.dakimakuramod.entities.dakimakura;

import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import com.github.andrew0030.dakimakuramod.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class Dakimakura extends Entity
{
    private static final EntityDataAccessor<Integer> FACING = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FLIPPED = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.BOOLEAN);
    // TODO: these 2 lines can probably be normal Strings, since if they are only handled by the Server we don't need to sync them with the Client
    private static final EntityDataAccessor<String> PACK_DIR_NAME = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<String> DAKI_DIR_NAME = SynchedEntityData.defineId(Dakimakura.class, EntityDataSerializers.STRING);

    public Dakimakura(EntityType<?> entityType, Level level)
    {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData()
    {
        this.getEntityData().define(FACING, 2); // 2: North in Direction
        this.getEntityData().define(FLIPPED, false);
        this.getEntityData().define(PACK_DIR_NAME, "");
        this.getEntityData().define(DAKI_DIR_NAME, "");
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound)
    {
        compound.putInt(DakiNbtSerializer.TAG_FACING, this.getRotationInt());
        compound.putBoolean(DakiNbtSerializer.TAG_FLIPPED, this.isFlipped());
//        if (!StringUtil.isNullOrEmpty(this.getEntityData().get(PACK_DIR_NAME)) && !StringUtil.isNullOrEmpty(this.getEntityData().get(DAKI_DIR_NAME)))
//        {
        compound.putString(DakiNbtSerializer.TAG_DAKI_PACK_NAME, this.getEntityData().get(PACK_DIR_NAME));
        compound.putString(DakiNbtSerializer.TAG_DAKI_DIR_NAME, this.getEntityData().get(DAKI_DIR_NAME));
//        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound)
    {
        this.setRotationInt(compound.getInt(DakiNbtSerializer.TAG_FACING));
        this.setFlipped(compound.getBoolean(DakiNbtSerializer.TAG_FLIPPED));
//        if (compound.contains(DakiNbtSerializer.TAG_DAKI_PACK_NAME, Tag.TAG_STRING) && compound.contains(DakiNbtSerializer.TAG_DAKI_DIR_NAME, Tag.TAG_STRING))
//        {
        this.getEntityData().set(PACK_DIR_NAME, compound.getString(DakiNbtSerializer.TAG_DAKI_PACK_NAME));
        this.getEntityData().set(DAKI_DIR_NAME, compound.getString(DakiNbtSerializer.TAG_DAKI_DIR_NAME));
//        }
    }

    public void setDaki(Daki daki)
    {
        this.getEntityData().set(PACK_DIR_NAME, (daki != null) ? daki.getPackDirectoryName() : "");
        this.getEntityData().set(DAKI_DIR_NAME, (daki != null) ? daki.getDakiDirectoryName() : "");
    }

    @Override
    public void tick()
    {
        super.tick();
//        if (!this.level().isClientSide())
//        {
//            BlockPos pos = new BlockPos(this.blockPosition().getX(), this.blockPosition().getY() - 1, this.blockPosition().getZ());
//            BlockState state = this.level().getBlockState(pos);
//            Block block = state.getBlock();
//            if (!block.isBed(state, this.level(), pos, null))
//            {
//                dropAsItem();
//                this.setRemoved(RemovalReason.DISCARDED);
//            }
//        }
    }

//    public Daki getDaki()
//    {
//        return DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(packDirName, dakiDirName);
//    }

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

//    public boolean isDakiOverBlock(BlockPos blockPos) {
//        BlockPos pos = new BlockPos(posX, posY - 1, posZ);
//        if (pos.equals(blockPos)) {
//            return true;
//        }
//        pos = pos.offset(rotation);
//        if (pos.equals(blockPos)) {
//            return true;
//        }
//        return false;
//    }
}