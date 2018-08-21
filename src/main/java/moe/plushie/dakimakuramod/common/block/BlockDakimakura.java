package moe.plushie.dakimakuramod.common.block;

import java.util.Random;

import moe.plushie.dakimakuramod.common.dakimakura.Daki;
import moe.plushie.dakimakuramod.common.dakimakura.serialize.DakiNbtSerializer;
import moe.plushie.dakimakuramod.common.items.block.ItemBlockDakimakura;
import moe.plushie.dakimakuramod.common.tileentities.TileEntityDakimakura;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDakimakura extends AbstractModBlockContainer {
    
    private static final int META_BIT_STANDING = 0;
    private static final int META_BIT_POS_NEG = 1;
    private static final int META_BIT_X_Z = 2;
    private static final int META_BIT_TOP_BOT = 3;
    
    public static final PropertyBool PROPERTY_STANDING = PropertyBool.create("standing");
    public static final PropertyDirection PROPERTY_DIRECTION = PropertyDirection.create("rotation", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool PROPERTY_TOP = PropertyBool.create("top");
    public static final PropertyBool PROPERTY_FLIPPED = PropertyBool.create("flipped");
    
    protected BlockDakimakura() {
        super("dakimakura", Material.CLOTH, SoundType.CLOTH, true);
        setHardness(1.0F);
        setLightOpacity(0);
        setDefaultState(this.blockState.getBaseState().withProperty(PROPERTY_DIRECTION, EnumFacing.EAST).withProperty(PROPERTY_STANDING, false).withProperty(PROPERTY_TOP, false));
        translucent = true;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {PROPERTY_DIRECTION, PROPERTY_STANDING, PROPERTY_TOP, PROPERTY_FLIPPED});
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        boolean standing = state.getValue(PROPERTY_STANDING);
        EnumFacing rotation = state.getValue(PROPERTY_DIRECTION);
        boolean topPart = state.getValue(PROPERTY_TOP);
        int meta = 0;
        meta = setStandingOnMeta(meta, standing);
        meta = setRotationOnMeta(meta, rotation);
        meta = setTopPartOnMeta(meta, topPart);
        return meta;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        boolean standing = isStanding(meta);
        EnumFacing rotation = getRotation(meta);
        boolean topPart = isTopPart(meta);
        IBlockState result = this.getBlockState().getBaseState();
        result = result.withProperty(PROPERTY_STANDING, standing);
        result = result.withProperty(PROPERTY_DIRECTION, rotation);
        result = result.withProperty(PROPERTY_TOP, topPart);
        return result;
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
            state = state.withProperty(PROPERTY_FLIPPED, ((TileEntityDakimakura)tileEntity).isFlipped());
        }
        return state;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (entityPlayer.isSneaking()) {
            if (state.getValue(PROPERTY_TOP)) {
                if (state.getValue(PROPERTY_STANDING)) {
                    pos = pos.offset(EnumFacing.DOWN);
                } else {
                    EnumFacing rot = state.getValue(PROPERTY_DIRECTION);
                    pos = pos.offset(rot.getOpposite());
                }
            }
            if (!world.isRemote) {
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
                    ((TileEntityDakimakura)tileEntity).flip();
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(pos);
        Daki daki = DakiNbtSerializer.deserialize(itemStack.getTagCompound());
        if (tileEntity != null && tileEntity instanceof TileEntityDakimakura) {
            ((TileEntityDakimakura)tileEntity).setDaki(daki);
            ((TileEntityDakimakura)tileEntity).setFlipped(ItemBlockDakimakura.isFlipped(itemStack));
        }
    }
    
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (!world.isRemote) {
            if (!state.getValue(PROPERTY_TOP)) {
                TileEntity te = world.getTileEntity(pos);
                if (te != null && te instanceof TileEntityDakimakura) {
                    ItemStack itemStack = new ItemStack(ModBlocks.blockDakimakura);
                    Daki daki = ((TileEntityDakimakura)te).getDaki();
                    if (daki != null) {
                        itemStack.setTagCompound(DakiNbtSerializer.serialize(daki));
                        DakiNbtSerializer.setFlipped(itemStack.getTagCompound(), ((TileEntityDakimakura)te).isFlipped());
                    }
                    spawnItemInWorld(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, itemStack);
                }
            }
        }
        super.breakBlock(world, pos, state);
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
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
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        ItemStack itemStack = new ItemStack(this, 1, 0);
        TileEntity tileEntity = world.getTileEntity(pos);
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
    
    public static int setRotationOnMeta(int meta, EnumFacing rot) {
        // Set negative/positive bit.
        if (rot == EnumFacing.NORTH | rot == EnumFacing.WEST) {
            meta = setBit(meta, META_BIT_POS_NEG, false);
        } else if (rot == EnumFacing.SOUTH | rot == EnumFacing.EAST) {
            meta = setBit(meta, META_BIT_POS_NEG, true);
        }
        
        // Set x/z bit.
        if (rot == EnumFacing.EAST | rot == EnumFacing.WEST) {
            meta = setBit(meta, META_BIT_X_Z, true);
        } else if (rot == EnumFacing.NORTH | rot == EnumFacing.SOUTH) {
            meta = setBit(meta, META_BIT_X_Z, false);
        }
        
        return meta;
    }
    
    public static EnumFacing getRotation(int meta) {
        boolean xz = getBit(meta, META_BIT_X_Z) == 1;
        boolean posNeg = getBit(meta, META_BIT_POS_NEG) == 1;
        
        if (posNeg) {
            if (xz) {
                return EnumFacing.EAST;
            } else {
                return EnumFacing.SOUTH;
            }
        } else {
            if (xz) {
                return EnumFacing.WEST;
            } else {
                return EnumFacing.NORTH;
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
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        return getSelectedBoundingBox(blockState, worldIn, new BlockPos(0, 0, 0));
    }
    
    @Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
        boolean standing = blockState.getValue(PROPERTY_STANDING);
        EnumFacing rot = blockState.getValue(PROPERTY_DIRECTION);
        boolean topPart = blockState.getValue(PROPERTY_TOP);
        
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
                return new AxisAlignedBB(x1, y1, z1, x2, y2, z2).offset(pos);
            } else {
                return new AxisAlignedBB((x1 - 1F * rot.getFrontOffsetX()), (y1 - 1F * rot.getFrontOffsetY()), (z1 - 1F * rot.getFrontOffsetZ()), (x2 - 1F * rot.getFrontOffsetX()), (y2 - 1F * rot.getFrontOffsetY()), (z2 - 1F * rot.getFrontOffsetZ())).offset(pos);
            }
        } else {
            if (!topPart) {
                return new AxisAlignedBB(x1, y1, z1, x2, y2, z2).offset(pos);
            } else {
                return new AxisAlignedBB(x1, y1 - 1, z1, x2, y2 - 1, z2).offset(pos);
            }
        }
    }
    
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn) {
        IBlockState blockState = world.getBlockState(pos);
        boolean standing = blockState.getValue(PROPERTY_STANDING);
        EnumFacing rotation = blockState.getValue(PROPERTY_DIRECTION);
        boolean topPart = blockState.getValue(PROPERTY_TOP);
        
        if (!standing) {
            if (!topPart) {
                if (world.getBlockState(pos.offset(rotation)).getBlock() != this) {
                    world.setBlockToAir(pos);
                }
            } else {
                if (world.getBlockState(pos.offset(rotation.getOpposite())).getBlock() != this) {
                    world.setBlockToAir(pos);
                }
            }
        } else {
            if (!topPart) {
                if (world.getBlockState(pos.offset(EnumFacing.UP)).getBlock() != this) {
                    world.setBlockToAir(pos);
                }
            } else {
                if (world.getBlockState(pos.offset(EnumFacing.DOWN)).getBlock() != this) {
                    world.setBlockToAir(pos);
                }
            }
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityDakimakura();
    }
}
