package moe.plushie.dakimakuramod.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDakimakura extends AbstractModBlockContainer {

    private static ForgeDirection[] ROTATIONS = new ForgeDirection[] {ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.NORTH, ForgeDirection.EAST};
    
    protected BlockDakimakura() {
        super("dakimakura");
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
            ((TileEntityDakimakura)tileEntity).setDaki(itemStack);
            ((TileEntityDakimakura)tileEntity).setFlipped(livingBase.isSneaking());
        }
    }
    
    public static ForgeDirection getRotation(int meta) {
        return ROTATIONS[meta & 3];
    }
    
    public ForgeDirection getRotation(IBlockAccess blockAccess, int x, int y, int z) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        return getRotation(meta);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {}
    
    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        setBlockBoundsBasedOnState(world, x, y, z);
        return super.getCollisionBoundingBoxFromPool(world, x, y, z);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
        int meta = blockAccess.getBlockMetadata(x, y, z);
        ForgeDirection rot = getRotation(meta);
        float w1 = 0.2F;
        float w2 = 0.8F;
        float h1 = 0.01F;
        float h2 = 0.28F;
        float d1 = 0.1F;
        float d2 = 1.8F;
        
        float x1 = w1;
        float x2 = w2;
        float y1 = h1;
        float y2 = h2;
        float z1 = d1;
        float z2 = d2;
        
        switch (rot) {
        case NORTH:
            z1 = 1 - d2;
            z2 = 1 - d1;
            break;
        case EAST:
            x1 = d1;
            x2 = d2;
            z1 = w1;
            z2 = w2;
            break;
        case WEST:
            x1 = 1 - d2;
            x2 = 1 - d1;
            z1 = 1 - w2;
            z2 = 1 - w1;
            break;
        default:
            break;
        }
        
        
        if (meta < 4) {
            setBlockBounds(x1, y1, z1, x2, y2, z2);
        } else {
            setBlockBounds((x1 - 1F * rot.offsetX), y1, (z1 - 1F * rot.offsetZ), (x2 - 1F * rot.offsetX), y2, (z2 - 1F * rot.offsetZ));
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = getRotation(meta);
        if (meta < 4) {
            if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ) != this) {
                world.setBlockToAir(x, y, z);
            }
        } else {
            if (world.getBlock(x - dir.offsetX, y - dir.offsetY, z - dir.offsetZ) != this) {
                world.setBlockToAir(x, y, z);
            }
        }
    }
    
    @Override
    public Block setBlockName(String name) {
        GameRegistry.registerBlock(this, ItemBlockDakimakura.class, "block." + name);
        return super.setBlockName(name);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityDakimakura();
    }
    
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return -1;
    }
}
