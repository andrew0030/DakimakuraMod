package moe.plushie.dakimakuramod.common.items.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockDakimakura extends ModItemBlock {

    public ItemBlockDakimakura(Block block) {
        super(block);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(Item item, CreativeTabs creativeTabs, List list) {
        list.add(new ItemStack(item, 1, 0));
        ArrayList<Daki> dakiList = DakimakuraMod.getProxy().getDakimakuraManager().getDakiList();
        for (int i = 0; i < dakiList.size(); i++) {
            ItemStack itemStack = new ItemStack(item, 1, 0);
            itemStack.setTagCompound(new NBTTagCompound());
            Daki daki = dakiList.get(i);
            DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
            list.add(itemStack);
        }
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        DakimakuraMod.logger.info("pie");
        if (super.onItemUse(itemStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ)) {
            int rot = (MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            ForgeDirection[] rots = new ForgeDirection[] {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
            ForgeDirection dir = rots[rot].getOpposite();
            
            int meta = 0;
            DakimakuraMod.logger.info("Setting standing as: " + false);
            meta = BlockDakimakura.setRotationOnMeta(meta, dir);
            
            meta = BlockDakimakura.setStandingOnMeta(meta, false);
            meta = BlockDakimakura.setTopPartOnMeta(meta, false);
            
            
            
            int offX = x;
            int offY = y;
            int offZ = z;
            ForgeDirection offset1 = ForgeDirection.getOrientation(side);
            offX += offset1.offsetX;
            offY += offset1.offsetY;
            offZ += offset1.offsetZ;
            
            if (offset1 != ForgeDirection.UP) {
                
                dir = ForgeDirection.UP;
                meta = BlockDakimakura.setRotationOnMeta(meta, dir);
                meta = BlockDakimakura.setStandingOnMeta(meta, true);
                DakimakuraMod.logger.info("Not up or down " + offset1);
            }
            
            super.placeBlockAt(itemStack, entityPlayer, world, offX, offY, offZ, side, hitX, hitY, hitZ, meta);
            world.markBlockForUpdate(offX, offY, offZ);
            
            
            meta = BlockDakimakura.setTopPartOnMeta(meta, true);
            offX += dir.offsetX;
            offY += dir.offsetY;
            offZ += dir.offsetZ;
            
            super.placeBlockAt(itemStack, entityPlayer, world, offX, offY, offZ, side, hitX, hitY, hitZ, meta);
            world.markBlockForUpdate(offX, offY, offZ);
            
            
        } else {
            
        }
        
        return true;
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
        if (!world.setBlock(x, y, z, field_150939_a, metadata, 2)) {
            return false;
        }
        if (world.getBlock(x, y, z) == field_150939_a) {
            field_150939_a.onBlockPlacedBy(world, x, y, z, player, stack);
            field_150939_a.onPostBlockPlaced(world, x, y, z, metadata);
        }
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean par4) {
        super.addInformation(itemStack, player, list, par4);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (daki != null) {
            list.add("");
            list.add("---DEBUG---");
            list.add("PackDirectory: " + daki.getPackDirectoryName());
            list.add("DakiDirectory: " + daki.getDakiDirectoryName());
            list.add("Romaji: " + daki.getRomajiName());
            list.add("Original: " + daki.getOriginalName());
        } else {
            list.add("Blank");
        }
    }
}
