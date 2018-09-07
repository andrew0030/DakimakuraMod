package moe.plushie.dakimakuramod.common.entities;

import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityDakimakura extends Entity implements IEntityAdditionalSpawnData {

    private static final String TAG_FLIPPED = "flipped";
    private static final String TAG_ROTATION = "rotation";
    private static final DataParameter<Boolean> FLIPPED = EntityDataManager.<Boolean>createKey(EntityDakimakura.class, DataSerializers.BOOLEAN);
    
    private String packDirName;
    private String dakiDirName;
    private EnumFacing rotation;
    
    public EntityDakimakura(World world) {
        super(world);
        noClip = true;
        setSize(4, 1);
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(FLIPPED, Boolean.valueOf(false));
    }
    
    public void setDaki(Daki daki) {
        if (daki != null) {
            packDirName = daki.getPackDirectoryName();
            dakiDirName = daki.getDakiDirectoryName();
        } else {
            packDirName = null;
            dakiDirName = null;
        }
    }
    
    @Override
    public void onUpdate() {
        if (!worldObj.isRemote) {
            BlockPos pos = new BlockPos(posX, posY - 1, posZ);
            IBlockState blockState = worldObj.getBlockState(pos);
            Block block = blockState.getBlock();
            if (!block.isBed(blockState, worldObj, pos, null)) {
                dropAsItem();
                setDead();
            }
        }
    }
    
    public void dropAsItem() {
        Daki daki = getDaki();
        ItemStack itemStack = new ItemStack(ModBlocks.blockDakimakura);
        if (daki != null) {
            itemStack.setTagCompound(new NBTTagCompound());
            DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
            if (isFlipped()) {
                ItemBlockDakimakura.setFlipped(itemStack, true);
            }
        }
        EntityItem entityItem = new EntityItem(worldObj, posX + 0.5F, posY + 0.5F, posZ + 0.5F, itemStack);
        worldObj.spawnEntityInWorld(entityItem);
    }
    
    /*
    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int p_70056_9_) {
        setPosition(x, y, z);
        setRotation(yaw, pitch);
    }
     */
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(DakiNbtSerializer.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(DakiNbtSerializer.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
            packDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_PACK_NAME);
            dakiDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_DIR_NAME);
        }
        if (compound.hasKey(TAG_ROTATION, NBT.TAG_INT)) {
            rotation = EnumFacing.values()[(compound.getInteger(TAG_ROTATION))];
        }
        setFlipped(compound.getBoolean(TAG_FLIPPED));
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        if (packDirName != null & dakiDirName != null) {
            compound.setString(DakiNbtSerializer.TAG_DAKI_PACK_NAME, packDirName);
            compound.setString(DakiNbtSerializer.TAG_DAKI_DIR_NAME, dakiDirName);
        }
        if (rotation != null) {
            compound.setInteger(TAG_ROTATION, rotation.ordinal());
        }
        compound.setBoolean(TAG_FLIPPED, isFlipped());
    }
    
    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeDouble(posX);
        buf.writeDouble(posY);
        buf.writeDouble(posZ);
        if (packDirName != null & dakiDirName != null) {
            buf.writeBoolean(true);
            ByteBufUtils.writeUTF8String(buf, packDirName);
            ByteBufUtils.writeUTF8String(buf, dakiDirName);
        } else {
            buf.writeBoolean(false);
        }
        if (rotation != null) {
            buf.writeBoolean(true);
            buf.writeInt(rotation.ordinal());
        } else {
            buf.writeBoolean(false);
        }
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        posX = buf.readDouble();
        posY = buf.readDouble();
        posZ = buf.readDouble();
        if (buf.readBoolean()) {
            packDirName = ByteBufUtils.readUTF8String(buf);
            dakiDirName = ByteBufUtils.readUTF8String(buf);
        }
        if (buf.readBoolean()) {
            rotation = EnumFacing.values()[buf.readInt()];
        }
    }
    
    public Daki getDaki() {
        return DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(packDirName, dakiDirName);
    }
    
    public boolean isFlipped() {
        return this.dataManager.get(FLIPPED).booleanValue();
    }
    
    public void setFlipped(boolean flipped) {
        this.dataManager.set(FLIPPED, Boolean.valueOf(flipped));
    }
    
    public void flip() {
        setFlipped(!isFlipped());
    }
    
    public void setRotation(EnumFacing rotation) {
        this.rotation = rotation;
    }
    
    public EnumFacing getRotation() {
        return rotation;
    }
    
    public boolean isDakiOverBlock(BlockPos blockPos) {
        BlockPos pos = new BlockPos(posX, posY - 1, posZ);
        if (pos.equals(blockPos)) {
            return true;
        }
        pos = pos.offset(rotation);
        if (pos.equals(blockPos)) {
            return true;
        }
        return false;
    }
}
