package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.PositionImpl;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.util.IRegistry;
import net.minecraft.util.RegistryDefaulted;
import net.minecraft.world.World;

public class BlockDispenser extends BlockContainer
{
    public static final IRegistry dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
    protected Random field_149942_b = new Random();
    protected IIcon field_149944_M;
    protected IIcon field_149945_N;
    protected IIcon field_149946_O;
    private static final String __OBFID = "CL_00000229";

    protected BlockDispenser()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public int tickRate(World worldIn)
    {
        return 4;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.func_149938_m(worldIn, x, y, z);
    }

    private void func_149938_m(World p_149938_1_, int p_149938_2_, int p_149938_3_, int p_149938_4_)
    {
        if (!p_149938_1_.isRemote)
        {
            Block var5 = p_149938_1_.getBlock(p_149938_2_, p_149938_3_, p_149938_4_ - 1);
            Block var6 = p_149938_1_.getBlock(p_149938_2_, p_149938_3_, p_149938_4_ + 1);
            Block var7 = p_149938_1_.getBlock(p_149938_2_ - 1, p_149938_3_, p_149938_4_);
            Block var8 = p_149938_1_.getBlock(p_149938_2_ + 1, p_149938_3_, p_149938_4_);
            byte var9 = 3;

            if (var5.isFullBlock() && !var6.isFullBlock())
            {
                var9 = 3;
            }

            if (var6.isFullBlock() && !var5.isFullBlock())
            {
                var9 = 2;
            }

            if (var7.isFullBlock() && !var8.isFullBlock())
            {
                var9 = 5;
            }

            if (var8.isFullBlock() && !var7.isFullBlock())
            {
                var9 = 4;
            }

            p_149938_1_.setBlockMetadataWithNotify(p_149938_2_, p_149938_3_, p_149938_4_, var9, 2);
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        int var3 = meta & 7;
        return side == var3 ? (var3 != 1 && var3 != 0 ? this.field_149945_N : this.field_149946_O) : (var3 != 1 && var3 != 0 ? (side != 1 && side != 0 ? this.blockIcon : this.field_149944_M) : this.field_149944_M);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("furnace_side");
        this.field_149944_M = reg.registerIcon("furnace_top");
        this.field_149945_N = reg.registerIcon(this.getTextureName() + "_front_horizontal");
        this.field_149946_O = reg.registerIcon(this.getTextureName() + "_front_vertical");
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityDispenser var10 = (TileEntityDispenser)worldIn.getTileEntity(x, y, z);

            if (var10 != null)
            {
                player.func_146102_a(var10);
            }

            return true;
        }
    }

    protected void func_149941_e(World p_149941_1_, int p_149941_2_, int p_149941_3_, int p_149941_4_)
    {
        BlockSourceImpl var5 = new BlockSourceImpl(p_149941_1_, p_149941_2_, p_149941_3_, p_149941_4_);
        TileEntityDispenser var6 = (TileEntityDispenser)var5.getBlockTileEntity();

        if (var6 != null)
        {
            int var7 = var6.func_146017_i();

            if (var7 < 0)
            {
                p_149941_1_.playAuxSFX(1001, p_149941_2_, p_149941_3_, p_149941_4_, 0);
            }
            else
            {
                ItemStack var8 = var6.getStackInSlot(var7);
                IBehaviorDispenseItem var9 = this.func_149940_a(var8);

                if (var9 != IBehaviorDispenseItem.itemDispenseBehaviorProvider)
                {
                    ItemStack var10 = var9.dispense(var5, var8);
                    var6.setInventorySlotContents(var7, var10.stackSize == 0 ? null : var10);
                }
            }
        }
    }

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_)
    {
        return (IBehaviorDispenseItem)dispenseBehaviorRegistry.getObject(p_149940_1_.getItem());
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        boolean var6 = worldIn.isBlockIndirectlyGettingPowered(x, y, z) || worldIn.isBlockIndirectlyGettingPowered(x, y + 1, z);
        int var7 = worldIn.getBlockMetadata(x, y, z);
        boolean var8 = (var7 & 8) != 0;

        if (var6 && !var8)
        {
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
            worldIn.setBlockMetadataWithNotify(x, y, z, var7 | 8, 4);
        }
        else if (!var6 && var8)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var7 & -9, 4);
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            this.func_149941_e(worldIn, x, y, z);
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDispenser();
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = BlockPistonBase.determineOrientation(worldIn, x, y, z, placer);
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);

        if (itemIn.hasDisplayName())
        {
            ((TileEntityDispenser)worldIn.getTileEntity(x, y, z)).func_146018_a(itemIn.getDisplayName());
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityDispenser var7 = (TileEntityDispenser)worldIn.getTileEntity(x, y, z);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.field_149942_b.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.field_149942_b.nextFloat() * 0.8F + 0.1F;
                    float var12 = this.field_149942_b.nextFloat() * 0.8F + 0.1F;

                    while (var9.stackSize > 0)
                    {
                        int var13 = this.field_149942_b.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        EntityItem var14 = new EntityItem(worldIn, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(var9.getItem(), var13, var9.getMetadata()));

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }

                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.field_149942_b.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.field_149942_b.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.field_149942_b.nextGaussian() * var15);
                        worldIn.spawnEntityInWorld(var14);
                    }
                }
            }

            worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public static IPosition getIPositionFromBlockSource(IBlockSource p_149939_0_)
    {
        EnumFacing var1 = getFacingDirection(p_149939_0_.getBlockMetadata());
        double var2 = p_149939_0_.getX() + 0.7D * (double)var1.getFrontOffsetX();
        double var4 = p_149939_0_.getY() + 0.7D * (double)var1.getFrontOffsetY();
        double var6 = p_149939_0_.getZ() + 0.7D * (double)var1.getFrontOffsetZ();
        return new PositionImpl(var2, var4, var6);
    }

    public static EnumFacing getFacingDirection(int p_149937_0_)
    {
        return EnumFacing.getFront(p_149937_0_ & 7);
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(x, y, z));
    }
}
