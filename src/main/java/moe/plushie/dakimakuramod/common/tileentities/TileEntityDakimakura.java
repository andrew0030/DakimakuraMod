package moe.plushie.dakimakuramod.common.tileentities;

import java.util.List;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.block.ModBlocks;
import moe.plushie.dakimakuramod.common.config.ConfigHandler;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDakimakura extends TileEntity {
    
    private String packDirName;
    private String dakiDirName;
    private boolean flipped;
    
    public void setDaki(Daki daki) {
        if (daki != null) {
            packDirName = daki.getPackDirectoryName();
            dakiDirName = daki.getDakiDirectoryName();
        } else {
            packDirName = null;
            dakiDirName = null;
        }
        markDirty();
        syncWithClients();
    }
    
    public Daki getDaki() {
        return DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(packDirName, dakiDirName);
    }
    
    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
        markDirty();
        syncWithClients();
    }
    
    public boolean isFlipped() {
        return flipped;
    }
    
    public void flip() {
        setFlipped(!flipped);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(DakiNbtSerializer.TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(DakiNbtSerializer.TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
            packDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_PACK_NAME);
            dakiDirName = compound.getString(DakiNbtSerializer.TAG_DAKI_DIR_NAME);
        }
        flipped = DakiNbtSerializer.isFlipped(compound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (packDirName != null & dakiDirName != null) {
            compound.setString(DakiNbtSerializer.TAG_DAKI_PACK_NAME, packDirName);
            compound.setString(DakiNbtSerializer.TAG_DAKI_DIR_NAME, dakiDirName);
        }
        DakiNbtSerializer.setFlipped(compound, flipped);
        return compound;
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound compound = super.getUpdateTag();
        writeToNBT(compound);
        return compound;
    }
    
    public Packet getDescriptionPacket() {
        return new SPacketUpdateTileEntity(getPos(), 5, getUpdateTag());
    }
    
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(getPos(), getBlockMetadata(), getUpdateTag());
    }
    
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        NBTTagCompound compound = packet.getNbtCompound();
        readFromNBT(compound);
        super.onDataPacket(net, packet);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public double getMaxRenderDistanceSquared() {
        return ConfigHandler.dakiRenderDist;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        IBlockState blockState = worldObj.getBlockState(getPos());
        if (blockState.getBlock() == ModBlocks.blockDakimakura) {
            boolean standing = blockState.getValue(BlockDakimakura.PROPERTY_STANDING);
            EnumFacing rotation = blockState.getValue(BlockDakimakura.PROPERTY_DIRECTION);
            if (standing) {
                return new AxisAlignedBB(pos, pos.add(1, 2, 1));
            }
            AxisAlignedBB[] rots = {
                    new AxisAlignedBB(0, 0, -1, 1, 0.28F, 1),
                    new AxisAlignedBB(0, 0, 0, 1, 0.28F, 2),
                    new AxisAlignedBB(-1, 0, 0, 1, 0.28F, 1),
                    new AxisAlignedBB(0, 0, 0, 2, 0.28F, 1)
                    };
            return rots[(rotation.ordinal() - 2) & 3].offset(pos);
        }
        return INFINITE_EXTENT_AABB;
    }
    
    public void syncWithClients() {
        if (!worldObj.isRemote) {
            syncWithNearbyPlayers(this);
        }
    }
    
    public static void syncWithNearbyPlayers(TileEntity tileEntity) {
        World world = tileEntity.getWorld();
        List<EntityPlayer> players = world.playerEntities;
        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP mp = (EntityPlayerMP)player;
                if (tileEntity.getDistanceSq(mp.posX, mp.posY, mp.posZ) < 64) {
                    mp.connection.sendPacket(tileEntity.getUpdatePacket());
                }
            }
        }
    }
}
