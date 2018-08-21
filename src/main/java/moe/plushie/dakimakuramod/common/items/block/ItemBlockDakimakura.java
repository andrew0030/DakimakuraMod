package moe.plushie.dakimakuramod.common.items.block;

import java.util.ArrayList;
import java.util.List;

import moe.plushie.dakimakuramod.DakimakuraMod;
import moe.plushie.dakimakuramod.common.block.BlockDakimakura;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.entities.EntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockDakimakura extends ModItemBlock {

    private static final String TAG_FLIPPED = "flipped";
    
    public ItemBlockDakimakura(Block block) {
        super(block);
        setMaxStackSize(1);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            items.add(new ItemStack(this, 1, 0));
            ArrayList<Daki> dakiList = DakimakuraMod.getProxy().getDakimakuraManager().getDakiList();
            for (int i = 0; i < dakiList.size(); i++) {
                ItemStack itemStack = new ItemStack(this, 1, 0);
                itemStack.setTagCompound(new NBTTagCompound());
                Daki daki = dakiList.get(i);
                DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
                items.add(itemStack);
            }
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
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    	ItemStack itemStack = playerIn.getHeldItem(handIn);
        if (playerIn.isSneaking()) {
            boolean flipped = isFlipped(itemStack);
            itemStack = setFlipped(itemStack, !flipped);
        }
        return new ActionResult(EnumActionResult.PASS, itemStack);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    	ItemStack stack = player.getHeldItem(hand);
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(facing);
        }
        if (stack.getCount() != 0 && player.canPlayerEdit(pos, facing, stack) && worldIn.mayPlace(this.block, pos, false, facing, (Entity)null)) {
            int rot = (MathHelper.floor((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
            EnumFacing[] rots = new EnumFacing[] {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
            EnumFacing rotation = rots[rot].getOpposite();
            if (canPlaceDakiAt(worldIn, player, stack, pos, facing, rotation)) {
                if (block.isBed(iblockstate, worldIn, pos, player)) {
                    placeAsEntity(worldIn, player, stack, pos, facing, rotation);
                } else {
                    placeDakiAt(worldIn, player, stack, pos, facing, rotation);
                }
                return EnumActionResult.SUCCESS;
            } else {
                return EnumActionResult.FAIL;
            }
        } else {
            return EnumActionResult.FAIL;
        }
    }
    
    private boolean canPlaceDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos, EnumFacing side, EnumFacing rotation) {
        if (canPlaceAtLocation(world, entityPlayer, itemStack, pos, side)) {
            if (side != EnumFacing.UP) {
                rotation = EnumFacing.UP;
            }
            if (side == EnumFacing.DOWN) {
                rotation = EnumFacing.UP;
                pos = pos.offset(EnumFacing.DOWN);
                //y--;
            }
            pos = pos.offset(rotation);
            //x += rotation.offsetX;
            //y += rotation.offsetY;
            //z += rotation.offsetZ;
            if (canPlaceAtLocation(world, entityPlayer, itemStack, pos, side)) {
                return true;
            }
        }
        return false;
    }
    
    private void placeDakiAt(World world, EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos, EnumFacing side, EnumFacing rotation) {
        IBlockState blockstate = this.block.getDefaultState();
        blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_STANDING, false);
        blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_DIRECTION, rotation);
        blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_TOP, false);
        
        if (side != EnumFacing.UP & side != EnumFacing.DOWN) {
            blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_DIRECTION, side.getOpposite());
            blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_STANDING, true);
            rotation = EnumFacing.UP;
        }
        if (side == EnumFacing.DOWN) {
            blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_STANDING, true);
            rotation = EnumFacing.UP;
            pos = pos.offset(EnumFacing.DOWN);
            //y--;
        }
        
                
        // Placing bottom part.
        placeBlockAt(itemStack, entityPlayer, world, pos, side, 0, 0, 0, blockstate);
        SoundType soundtype = this.block.getSoundType();
        world.playSound(entityPlayer, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        itemStack.shrink(1);
        
        world.scheduleBlockUpdate(pos, block, 1, 0);
        //world.markBlockForUpdate(x, y, z);
        
        blockstate = blockstate.withProperty(BlockDakimakura.PROPERTY_TOP, true);
        pos = pos.offset(rotation);
        
        // Placing top part.
        placeBlockAt(itemStack, entityPlayer, world, pos, side, 0, 0, 0, blockstate);
        world.scheduleBlockUpdate(pos, block, 1, 0);
        //world.markBlockForUpdate(x, y, z);
    }
    
    private boolean canPlaceAtLocation(World world, EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos, EnumFacing side) {
        IBlockState iblockstate = world.getBlockState(pos);
        Block block = iblockstate.getBlock();
        if (!entityPlayer.canPlayerEdit(pos, side, itemStack)) {
            return false;
        } else if (iblockstate.getMaterial().isSolid()) {
            return false;
        } else if (!block.isReplaceable(world, pos)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
        if (!world.setBlockState(pos, newState, 2)) {
            return false;
        }
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);
        }
        return true;
    }
    
    
    private void placeAsEntity(World world, EntityPlayer entityPlayer, ItemStack itemStack, BlockPos pos, EnumFacing side, EnumFacing rotation) {
        if (world.isRemote) {
            return;
        }
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        EntityDakimakura entityDakimakura = new EntityDakimakura(world);
        entityDakimakura.setPosition(pos.getX(), pos.getY(), pos.getZ());
        entityDakimakura.setDaki(daki);
        entityDakimakura.setFlipped(isFlipped(itemStack));
        entityDakimakura.setRotation(rotation);
        world.spawnEntity(entityDakimakura);
        DakimakuraMod.getLogger().info("Placing daki at " + pos);
        SoundType soundtype = this.block.getSoundType();
        world.playSound(entityPlayer, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        itemStack.shrink(1);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        Daki daki = DakiNbtSerializer.deserialize(stack.getTagCompound());
        if (daki != null) {
            String textFlip = I18n.format(stack.getUnlocalizedName() + ".tooltip.flip");
            tooltip.add(I18n.format(textFlip));
            daki.addInformation(stack, tooltip);
        } else {
            tooltip.add("Blank");
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
        Block block = world.getBlockState(pos).getBlock();
        if (block == Blocks.SNOW_LAYER && block.isReplaceable(world, pos)) {
            side = EnumFacing.UP;
        } else if (!block.isReplaceable(world, pos)) {
            pos = pos.offset(side);
        }
        return world.mayPlace(Blocks.STONE, pos, false, side, (Entity)null);
    }
}
