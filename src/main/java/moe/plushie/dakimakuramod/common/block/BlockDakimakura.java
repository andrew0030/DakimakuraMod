package moe.plushie.dakimakuramod.common.block;

import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockDakimakura extends AbstractModBlockContainer {
    
    private static final String TAG_FLIPPED = "flipped";
    private static final int META_BIT_STANDING = 0;
    private static final int META_BIT_POS_NEG = 1;
    private static final int META_BIT_X_Z = 2;
    private static final int META_BIT_TOP_BOT = 3;
    
    protected BlockDakimakura() {
        super("dakimakura", Material.cloth, soundTypeCloth, true);
        setHardness(1.0F);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int size, float hitX, float hitY, float hitZ) {
        if (entityPlayer.isSneaking()) {
            int meta = world.getBlockMetadata(x, y, z);
            if (isTopPart(meta)) {
                if (isStanding(meta)) {
                    y -= 1;
                } else {
                    ForgeDirection rot = getRotation(meta);
                    x -= rot.offsetX;
                    z -= rot.offsetZ;
                }
            }
            if (!world.isRemote) {
                TileEntity tileEntity = world.getTileEntity(x, y, z);
                if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
                    ((TileEntityDakimakura)tileEntity).flip();
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase livingBase, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
            ((TileEntityDakimakura)tileEntity).setDaki(daki);
            ((TileEntityDakimakura)tileEntity).setFlipped(ItemBlockDakimakura.isFlipped(itemStack));
        }
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, Block block, int metadata) {
        if (!world.isRemote) {
            if (!isTopPart(metadata)) {
                TileEntity te = world.getTileEntity(x, y, z);
                if (te != null && te instanceof TileEntityDakimakura) {
                    ItemStack itemStack = new ItemStack(ModBlocks.blockDakimakura);
                    Daki daki = ((TileEntityDakimakura)te).getDaki();
                    if (daki != null) {
                        itemStack.setTagCompound(DakiNbtSerializer.serialize(daki));
                        DakiNbtSerializer.setFlipped(itemStack.getTagCompound(), ((TileEntityDakimakura)te).isFlipped());
                    }
                    spawnItemInWorld(world, x + 0.5F, y + 0.5F, z + 0.5F, itemStack);
                }
            }
        }
        super.breakBlock(world, x, y, z, block, metadata);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
        return true;
    }
    
    private static void spawnItemInWorld(World world, double x, double y, double z, ItemStack itemStack) {
        EntityItem entityItem = new EntityItem(world, x, y, z, itemStack);
        world.spawnEntityInWorld(entityItem);
    }
    
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
    
    @Override
    public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z, EntityPlayer player) {
        ItemStack itemStack = new ItemStack(this, 1, 0);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
            Daki daki = ((TileEntityDakimakura)tileEntity).getDaki();
            if (daki != null) {
                itemStack.setTagCompound(new NBTTagCompound());
                DakiNbtSerializer.serialize(daki, itemStack.getTagCompound());
            }
        }
        return itemStack;
    }
    
    private static int getBit(int value, int index) {
        return (value >> index) & 1;
    }
    
    private static int setBit(int value, int index, boolean on) {
        if (on) {
            return value | (1 << index);
        } else {
            return value & ~(1 << index);
        }
    }
    
    public static int setRotationOnMeta(int meta, ForgeDirection rot) {
        // Set negative/positive bit.
        if (rot == ForgeDirection.NORTH | rot == ForgeDirection.WEST) {
            meta = setBit(meta, META_BIT_POS_NEG, false);
        } else if (rot == ForgeDirection.SOUTH | rot == ForgeDirection.EAST) {
            meta = setBit(meta, META_BIT_POS_NEG, true);
        }
        
        // Set x/z bit.
        if (rot == ForgeDirection.EAST | rot == ForgeDirection.WEST) {
            meta = setBit(meta, META_BIT_X_Z, true);
        } else if (rot == ForgeDirection.NORTH | rot == ForgeDirection.SOUTH) {
            meta = setBit(meta, META_BIT_X_Z, false);
        }
        
        return meta;
    }
    
    public static ForgeDirection getRotation(int meta) {
        boolean xz = getBit(meta, META_BIT_X_Z) == 1;
        boolean posNeg = getBit(meta, META_BIT_POS_NEG) == 1;
        
        if (posNeg) {
            if (xz) {
                return ForgeDirection.EAST;
            } else {
                return ForgeDirection.SOUTH;
            }
        } else {
            if (xz) {
                return ForgeDirection.WEST;
            } else {
                return ForgeDirection.NORTH;
            }
        }
    }
    
    public static int setStandingOnMeta(int meta, boolean standing) {
        return setBit(meta, META_BIT_STANDING, standing);
    }
    
    public static boolean isStanding(int meta) {
        return getBit(meta, META_BIT_STANDING) == 1;
    }
    
    public static int setTopPartOnMeta(int meta, boolean topPart) {
        return setBit(meta, META_BIT_TOP_BOT, topPart);
    }
    
    public static boolean isTopPart(int meta) {
        return getBit(meta, META_BIT_TOP_BOT) == 1;
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
        boolean standing = isStanding(meta);
        boolean topPart = isTopPart(meta);
        
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
        
        if (!standing) {
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
        }
        if (standing) {
            y1 = d1;
            y2 = d2;
            switch (rot) {
            case SOUTH:
                z1 = 1 - h2;
                z2 = 1 - h1;
                break;
            case NORTH:
                z1 = h1;
                z2 = h2;
                break;
            case EAST:
                x1 = 1 - h2;
                x2 = 1 - h1;
                z1 = w1;
                z2 = w2;
                break;
            case WEST:
                x1 = h1;
                x2 = h2;
                z1 = w1;
                z2 = w2;
                break;
            default:
                break;
            }
        }
        if (!standing) {
            if (!topPart) {
                setBlockBounds(x1, y1, z1, x2, y2, z2);
            } else {
                setBlockBounds((x1 - 1F * rot.offsetX), (y1 - 1F * rot.offsetY), (z1 - 1F * rot.offsetZ), (x2 - 1F * rot.offsetX), (y2 - 1F * rot.offsetY), (z2 - 1F * rot.offsetZ));
            }
        } else {
            if (!topPart) {
                setBlockBounds(x1, y1, z1, x2, y2, z2);
            } else {
                setBlockBounds(x1, y1 - 1, z1, x2, y2 - 1, z2);
            }
        }
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        int meta = world.getBlockMetadata(x, y, z);
        ForgeDirection dir = getRotation(meta);
        boolean topPart = isTopPart(meta);
        boolean standing = isStanding(meta);
        if (!standing) {
            if (!topPart) {
                if (world.getBlock(x + dir.offsetX, y, z + dir.offsetZ) != this) {
                    world.setBlockToAir(x, y, z);
                }
            } else {
                if (world.getBlock(x - dir.offsetX, y, z - dir.offsetZ) != this) {
                    world.setBlockToAir(x, y, z);
                }
            }
        } else {
            if (!topPart) {
                if (world.getBlock(x, y + 1, z) != this) {
                    world.setBlockToAir(x, y, z);
                }
            } else {
                if (world.getBlock(x, y - 1, z) != this) {
                    world.setBlockToAir(x, y, z);
                }
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
