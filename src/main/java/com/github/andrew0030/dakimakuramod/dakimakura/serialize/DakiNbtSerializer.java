package com.github.andrew0030.dakimakuramod.dakimakura.serialize;

import com.github.andrew0030.dakimakuramod.DakimakuraMod;
import com.github.andrew0030.dakimakuramod.dakimakura.Daki;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public final class DakiNbtSerializer
{
    public static final String TAG_DAKI_PACK_NAME = "DakiPackName";
    public static final String TAG_DAKI_DIR_NAME = "DakiDirName";
    public static final String TAG_FACING = "Facing";
    public static final String TAG_FLIPPED = "Flipped";

    /**
     * Gets and stores the Daki pack name and directory name in a new {@link CompoundTag}
     * @param daki The Daki object that will be serialized
     * @return A new {@link CompoundTag} containing the pack name and directory name
     */
    public static CompoundTag serialize(Daki daki)
    {
        return serialize(daki, new CompoundTag());
    }

    /**
     * Gets and stores the {@link Daki} pack name and directory name in a given {@link CompoundTag}
     * @param daki The {@link Daki} object that will be serialized
     * @param compound The {@link CompoundTag} that will hold the values
     * @return The given {@link CompoundTag} containing the {@link Daki} pack name and directory name
     */
    public static CompoundTag serialize(Daki daki, CompoundTag compound)
    {
        compound.putString(TAG_DAKI_PACK_NAME, daki.getPackDirectoryName());
        compound.putString(TAG_DAKI_DIR_NAME, daki.getDakiDirectoryName());
        return compound;
    }

    public static void setFlipped(CompoundTag compound, boolean flipped)
    {
        compound.putBoolean(TAG_FLIPPED, flipped);
    }

    public static boolean isFlipped(CompoundTag compound)
    {
        if (compound == null)
            return false;
        return compound.getBoolean(TAG_FLIPPED);
    }

    public static Daki deserialize(CompoundTag compound)
    {
        if (compound == null)
            return null;
        if (compound.contains(TAG_DAKI_PACK_NAME, Tag.TAG_STRING) && compound.contains(TAG_DAKI_DIR_NAME, Tag.TAG_STRING))
        {
            String packName = compound.getString(TAG_DAKI_PACK_NAME);
            String dirName = compound.getString(TAG_DAKI_DIR_NAME);
            return DakimakuraMod.getDakimakuraManager().getDakiFromMap(packName, dirName);
        }
        return null;
    }
}