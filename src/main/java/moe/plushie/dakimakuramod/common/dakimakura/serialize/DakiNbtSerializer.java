package moe.plushie.dakimakuramod.common.dakimakura.serialize;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public final class DakiNbtSerializer {
    
    public static final String TAG_DAKI_PACK_NAME = "dakiPackName";
    public static final String TAG_DAKI_DIR_NAME = "dakiDirName";
    public static final String TAG_FLIPPED = "flipped";
    
    public DakiNbtSerializer() {}
    
    public static NBTTagCompound serialize(Daki daki) {
        NBTTagCompound compound = new NBTTagCompound();
        return serialize(daki, compound);
    }
    
    public static NBTTagCompound serialize(Daki daki, NBTTagCompound compound) {
        compound.setString(TAG_DAKI_PACK_NAME, daki.getPackDirectoryName());
        compound.setString(TAG_DAKI_DIR_NAME, daki.getDakiDirectoryName());
        return compound;
    }
    
    public static void setFlipped(NBTTagCompound compound, boolean flipped) {
        compound.setBoolean(TAG_FLIPPED, flipped);
    }
    
    public static boolean isFlipped(NBTTagCompound compound) {
        if (compound == null) {
            return false;
        }
        return compound.getBoolean(TAG_FLIPPED);
    }
    
    public static Daki deserialize(NBTTagCompound compound) {
        if (compound == null) {
            return null;
        }
        if (compound.hasKey(TAG_DAKI_PACK_NAME, NBT.TAG_STRING) & compound.hasKey(TAG_DAKI_DIR_NAME, NBT.TAG_STRING)) {
            String packName = compound.getString(TAG_DAKI_PACK_NAME);
            String dirName = compound.getString(TAG_DAKI_DIR_NAME);
            return DakimakuraMod.getProxy().getDakimakuraManager().getDakiFromMap(packName, dirName);
        } else {
            return null;
        }
    }
}
