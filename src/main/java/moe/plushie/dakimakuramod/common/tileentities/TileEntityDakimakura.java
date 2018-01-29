package moe.plushie.dakimakuramod.common.tileentities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants.NBT;

public class TileEntityDakimakura extends TileEntity {
    
    private static final String TAG_FLIPPED = "flipped";
    
    private String packDirName;
    private String dakiDirName;
    private boolean flipped;
    
    public void setDaki(ItemStack itemStack) {
        packDirName = null;
        dakiDirName = null;
        if (itemStack.hasTagCompound()) {
            NBTTagCompound compound = itemStack.getTagCompound();
            if (compound.hasKey(ItemBlockDakimakura.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(ItemBlockDakimakura.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
                packDirName = compound.getString(ItemBlockDakimakura.TAG_DAKI_PACK_NAME);
                dakiDirName = compound.getString(ItemBlockDakimakura.TAG_DAKI_DIR_NAME);
            } 
        }
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public void setDaki(Daki daki) {
        this.packDirName = daki.getPackDirectoryName();
        this.dakiDirName = daki.getDakiDirectoryName();
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public Daki getDaki() {
        return DakimakuraMod.dakimakuraManager.getDakiFromMap(packDirName, dakiDirName);
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    @Override
    public boolean canUpdate() {
        return false;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(ItemBlockDakimakura.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(ItemBlockDakimakura.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
            packDirName = compound.getString(ItemBlockDakimakura.TAG_DAKI_PACK_NAME);
            dakiDirName = compound.getString(ItemBlockDakimakura.TAG_DAKI_DIR_NAME);
        }
        flipped = compound.getBoolean(TAG_FLIPPED);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (packDirName != null & dakiDirName != null) {
            compound.setString(ItemBlockDakimakura.TAG_DAKI_PACK_NAME, packDirName);
            compound.setString(ItemBlockDakimakura.TAG_DAKI_DIR_NAME, dakiDirName);
        }
        compound.setBoolean(TAG_FLIPPED, flipped);
    }
    
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 5, compound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    
    
    
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        AxisAlignedBB[] rots = {
                AxisAlignedBB.getBoundingBox(0, 0, 0, 1, 0.28F, 2),
                AxisAlignedBB.getBoundingBox(-1, 0, 0, 1, 0.28F, 1),
                AxisAlignedBB.getBoundingBox(0, 0, -1, 1, 0.28F, 1),
                AxisAlignedBB.getBoundingBox(0, 0, 0, 2, 0.28F, 1)
                };
        
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
        return rots[getBlockMetadata() & 3].offset(xCoord, yCoord, zCoord);
    }
}
