package moe.plushie.dakimakuramod.common.entities;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

public class EntityDakimakura extends Entity implements IEntityAdditionalSpawnData {

    private static final String TAG_FLIPPED = "flipped";
    private static final String TAG_ROTATION = "rotation";
    
    private String packDirName;
    private String dakiDirName;
    private boolean flipped;
    private ForgeDirection rotation;
    
    public EntityDakimakura(World world, int x, int y, int z, Daki daki, boolean flipped, ForgeDirection rotation) {
        super(world);
        setDaki(daki);
        this.flipped = flipped;
        this.rotation = rotation;
        setPosition(x, y, z);
        setSize(1.0F, 1.0F);
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
    
    public EntityDakimakura(World world) {
        super(world);
        noClip = true;
        width = 4;
        height = 1;
    }
    
    @Override
    public void onUpdate() {
        int x = MathHelper.floor_double(posX);
        int y = MathHelper.floor_double(posY) - 1;
        int z = MathHelper.floor_double(posZ);
        Block block = worldObj.getBlock(x, y, z);
        if (!block.isBed(worldObj, x, y, z, null)) {
            setDead();
        }
    }
    
    @Override
    public boolean isInRangeToRenderDist(double p_70112_1_) {
        // TODO Auto-generated method stub
        return super.isInRangeToRenderDist(p_70112_1_);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int p_70056_9_) {
        setPosition(x, y, z);
        setRotation(yaw, pitch);
    }

    @Override
    protected void entityInit() {
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
        compound.setBoolean(TAG_FLIPPED, flipped);
    }
    
    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        if (compound.hasKey(DakiNbtSerializer.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(DakiNbtSerializer.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
            packDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_PACK_NAME);
            dakiDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_DIR_NAME);
        }
        if (compound.hasKey(TAG_ROTATION, NBT.TAG_INT)) {
            rotation = ForgeDirection.getOrientation(compound.getInteger(TAG_ROTATION));
        }
        flipped = compound.getBoolean(TAG_FLIPPED);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
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
        buf.writeBoolean(flipped);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        if (buf.readBoolean()) {
            packDirName = ByteBufUtils.readUTF8String(buf);
            dakiDirName = ByteBufUtils.readUTF8String(buf);
        }
        if (buf.readBoolean()) {
            rotation = ForgeDirection.getOrientation(buf.readInt());
        }
        flipped = buf.readBoolean();
    }
    
    public Daki getDaki() {
        return DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(packDirName, dakiDirName);
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    public ForgeDirection getRotation() {
        return rotation;
    }
}
