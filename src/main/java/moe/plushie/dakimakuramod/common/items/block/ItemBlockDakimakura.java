package moe.plushie.dakimakuramod.common.items.block;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemBlockDakimakura extends ModItemBlock {

    private static final String TAG_FLIPPED = "flipped";
    
    public ItemBlockDakimakura(Block block) {
        super(block);
        setMaxStackSize(1);
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
    
    public static boolean isFlipped(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        }
        if (!itemStack.hasTagCompound()) {
            return false;
        }
        return itemStack.getTagCompound().getBoolean(TAG_FLIPPED);
    }
    
    public static ItemStack setFlipped(ItemStack itemStack, boolean flipped) {
        if (itemStack == null) {
            return null;
        }
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }
        itemStack.getTagCompound().setBoolean(TAG_FLIPPED, flipped);
        return itemStack;
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        if (entityPlayer.isSneaking()) {
            boolean flipped = isFlipped(itemStack);
            itemStack = setFlipped(itemStack, !flipped);
        }
        return itemStack;
    }
    
    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block block = world.getBlock(x, y, z);
        ForgeDirection sideDir = ForgeDirection.getOrientation(side);
        if ((block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) | block == Blocks.tallgrass) {
            sideDir = ForgeDirection.UP;
            side = 1;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
            x += sideDir.offsetX;
            y += sideDir.offsetY;
            z += sideDir.offsetZ;
        }
        if (itemStack.stackSize == 0) {
            return false;
        } else if (y == 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        } else if (world.canPlaceEntityOnSide(this.field_150939_a, x, y, z, false, side, entityPlayer, itemStack)) {
            int rot = (MathHelper.floor_double((double)(entityPlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            ForgeDirection[] rots = new ForgeDirection[] {ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST};
            ForgeDirection rotation = rots[rot].getOpposite();
            if (canPlaceDakiAt(world, entityPlayer, itemStack, x, y, z, sideDir, rotation)) {
                if (block.isBed(world, x, y, z, entityPlayer) & side == 1) {
                    placeAsEntity(world, entityPlayer, itemStack, x, y, z, sideDir, rotation);
                } else {
                    placeDakiAt(world, entityPlayer, itemStack, x, y, z, sideDir, rotation);
                }
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private void placeAsEntity(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side, ForgeDirection rotation) {
        if (world.isRemote) {
            return;
        }
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        EntityDakimakura entityDakimakura = new EntityDakimakura(world);
        entityDakimakura.setPosition(x, y, z);
        entityDakimakura.setDaki(daki);
        entityDakimakura.setFlipped(isFlipped(itemStack));
        entityDakimakura.setRotation(rotation);
        world.spawnEntityInWorld(entityDakimakura);
        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
        --itemStack.stackSize;
    }
    
    private boolean canPlaceDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side, ForgeDirection rotation) {
        if (canPlaceAtLocation(world, entityPlayer, itemStack, x, y, z, side)) {
            if (side != ForgeDirection.UP) {
                rotation = ForgeDirection.UP;
            }
            if (side == ForgeDirection.DOWN) {
                rotation = ForgeDirection.UP;
                y--;
            }
            x += rotation.offsetX;
            y += rotation.offsetY;
            z += rotation.offsetZ;
            if (canPlaceAtLocation(world, entityPlayer, itemStack, x, y, z, side)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean canPlaceAtLocation(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side) {
        Block block = world.getBlock(x, y, z);
        if (!entityPlayer.canPlayerEdit(x, y, z, side.ordinal(), itemStack)) {
            return false;
        } else if (y >= 255 && this.field_150939_a.getMaterial().isSolid()) {
            return false;
        } else if (!block.isReplaceable(world, x, y, z)) {
            return false;
        }
        return true;
    }
    
    private void placeDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, int x, int y, int z, ForgeDirection side, ForgeDirection rotation) {
        int meta = 0;
        meta = BlockDakimakura.setRotationOnMeta(meta, rotation);
        meta = BlockDakimakura.setStandingOnMeta(meta, false);
        meta = BlockDakimakura.setTopPartOnMeta(meta, false);
        
        if (side != ForgeDirection.UP & side != ForgeDirection.DOWN) {
            meta = BlockDakimakura.setRotationOnMeta(meta, side.getOpposite());
            meta = BlockDakimakura.setStandingOnMeta(meta, true);
            rotation = ForgeDirection.UP;
        }
        if (side == ForgeDirection.DOWN) {
            meta = BlockDakimakura.setStandingOnMeta(meta, true);
            rotation = ForgeDirection.UP;
            y--;
        }
        
        // Placing bottom part.
        placeBlockAt(itemStack, entityPlayer, world, x, y, z, side.ordinal(), 0, 0, 0, meta);
        world.playSoundEffect((double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), this.field_150939_a.stepSound.func_150496_b(), (this.field_150939_a.stepSound.getVolume() + 1.0F) / 2.0F, this.field_150939_a.stepSound.getPitch() * 0.8F);
        --itemStack.stackSize;
        world.markBlockForUpdate(x, y, z);
        
        
        meta = BlockDakimakura.setTopPartOnMeta(meta, true);
        x += rotation.offsetX;
        y += rotation.offsetY;
        z += rotation.offsetZ;
        
        // Placing top part.
        placeBlockAt(itemStack, entityPlayer, world, x, y, z, side.ordinal(), 0, 0, 0, meta);
        world.markBlockForUpdate(x, y, z);
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
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advancedItemTooltips) {
        super.addInformation(itemStack, player, list, advancedItemTooltips);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (daki != null) {
            String textFlip = StatCollector.translateToLocal(itemStack.getUnlocalizedName() + ".tooltip.flip");
            list.add(StatCollector.translateToLocal(textFlip));
            daki.addInformation(itemStack, player, list, advancedItemTooltips);
        } else {
            list.add("Blank");
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean func_150936_a(World world, int x, int y, int z, int side, EntityPlayer entityPlayer, ItemStack itemStack) {
        Block block = world.getBlock(x, y, z);
        ForgeDirection sideDir = ForgeDirection.getOrientation(side);
        if (block == Blocks.snow_layer && (world.getBlockMetadata(x, y, y) & 7) < 1) {
            sideDir = ForgeDirection.UP;
            side = 0;
        } else if (block != Blocks.vine && block != Blocks.tallgrass && block != Blocks.deadbush && !block.isReplaceable(world, x, y, y)) {
            x += sideDir.offsetX;
            y += sideDir.offsetY;
            z += sideDir.offsetZ;
        }
        return world.canPlaceEntityOnSide(Blocks.stone, x, y, z, false, side, (Entity)null, itemStack);
    }
}
