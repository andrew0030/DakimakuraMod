package moe.plushie.dakimakuramod.common.entities;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import moe.plushie.dakimakuramod.DakimakuraMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityDakimakura extends Entity implements IEntityAdditionalSpawnData {

    private static final String TAG_IMAGE_ID = "imageId";
    private static final String TAG_FLIPPED = "flipped";
    
    private int imageId = 0;
    private boolean flipped;
    
    public EntityDakimakura(World world, int x, int y, int z, int imageId, boolean flipped) {
        super(world);
        this.imageId = imageId;
        this.flipped = flipped;
        setPosition(x + 0.5F, y + 1, z + 0.5F);
        setSize(0.80F, 1.72F);
    }
    
    public EntityDakimakura(World world) {
        super(world);
    }
    
    @Override
    public AxisAlignedBB getBoundingBox() {
        return AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
    }
    
    @Override
    public AxisAlignedBB getCollisionBox(Entity p_70114_1_) {
        // TODO Auto-generated method stub
        return AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX + 1, posY + 1, posZ + 1);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        //setDead();
    }

    @Override
    protected void entityInit() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        imageId = compound.getInteger(TAG_IMAGE_ID);
        flipped = compound.getBoolean(TAG_FLIPPED);
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger(TAG_IMAGE_ID, imageId);
        compound.setBoolean(TAG_FLIPPED, flipped);
    }

    @Override
    public void writeSpawnData(ByteBuf buf) {
        buf.writeInt(imageId);
        buf.writeBoolean(flipped);
    }

    @Override
    public void readSpawnData(ByteBuf buf) {
        imageId = buf.readInt();
        flipped = buf.readBoolean();
    }
    
    @Override
    public boolean hitByEntity(Entity entity) {
        // TODO Auto-generated method stub
        DakimakuraMod.logger.info("poo");
        setDead();
        return false;
    }
    
    @Override
    public boolean interactFirst(EntityPlayer entityPlayer) {
        DakimakuraMod.logger.info("poo");
        setDead();
        return false;
    }
    
    public int getImageId() {
        return imageId;
    }
    
    public boolean isFlipped() {
        return flipped;
    }
}
