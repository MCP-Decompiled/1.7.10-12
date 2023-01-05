package net.minecraft.block;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockChest extends BlockContainer
{
    private final Random rand = new Random();
    public final int chestType;
    private static final String __OBFID = "CL_00000214";

    protected BlockChest(int type)
    {
        super(Material.wood);
        this.chestType = type;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 22;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (worldIn.getBlock(x, y, z - 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x, y, z + 1) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
        }
        else if (worldIn.getBlock(x - 1, y, z) == this)
        {
            this.setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
        else if (worldIn.getBlock(x + 1, y, z) == this)
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
        }
        else
        {
            this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        }
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.initMetadata(worldIn, x, y, z);
        Block var5 = worldIn.getBlock(x, y, z - 1);
        Block var6 = worldIn.getBlock(x, y, z + 1);
        Block var7 = worldIn.getBlock(x - 1, y, z);
        Block var8 = worldIn.getBlock(x + 1, y, z);

        if (var5 == this)
        {
            this.initMetadata(worldIn, x, y, z - 1);
        }

        if (var6 == this)
        {
            this.initMetadata(worldIn, x, y, z + 1);
        }

        if (var7 == this)
        {
            this.initMetadata(worldIn, x - 1, y, z);
        }

        if (var8 == this)
        {
            this.initMetadata(worldIn, x + 1, y, z);
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        Block var7 = worldIn.getBlock(x, y, z - 1);
        Block var8 = worldIn.getBlock(x, y, z + 1);
        Block var9 = worldIn.getBlock(x - 1, y, z);
        Block var10 = worldIn.getBlock(x + 1, y, z);
        byte var11 = 0;
        int var12 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var12 == 0)
        {
            var11 = 2;
        }

        if (var12 == 1)
        {
            var11 = 5;
        }

        if (var12 == 2)
        {
            var11 = 3;
        }

        if (var12 == 3)
        {
            var11 = 4;
        }

        if (var7 != this && var8 != this && var9 != this && var10 != this)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var11, 3);
        }
        else
        {
            if ((var7 == this || var8 == this) && (var11 == 4 || var11 == 5))
            {
                if (var7 == this)
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z - 1, var11, 3);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z + 1, var11, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, var11, 3);
            }

            if ((var9 == this || var10 == this) && (var11 == 2 || var11 == 3))
            {
                if (var9 == this)
                {
                    worldIn.setBlockMetadataWithNotify(x - 1, y, z, var11, 3);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x + 1, y, z, var11, 3);
                }

                worldIn.setBlockMetadataWithNotify(x, y, z, var11, 3);
            }
        }

        if (itemIn.hasDisplayName())
        {
            ((TileEntityChest)worldIn.getTileEntity(x, y, z)).setCustomName(itemIn.getDisplayName());
        }
    }

    public void initMetadata(World p_149954_1_, int p_149954_2_, int p_149954_3_, int p_149954_4_)
    {
        if (!p_149954_1_.isRemote)
        {
            Block var5 = p_149954_1_.getBlock(p_149954_2_, p_149954_3_, p_149954_4_ - 1);
            Block var6 = p_149954_1_.getBlock(p_149954_2_, p_149954_3_, p_149954_4_ + 1);
            Block var7 = p_149954_1_.getBlock(p_149954_2_ - 1, p_149954_3_, p_149954_4_);
            Block var8 = p_149954_1_.getBlock(p_149954_2_ + 1, p_149954_3_, p_149954_4_);
            boolean var9 = true;
            int var10;
            Block var11;
            int var12;
            Block var13;
            boolean var14;
            byte var15;
            int var16;

            if (var5 != this && var6 != this)
            {
                if (var7 != this && var8 != this)
                {
                    var15 = 3;

                    if (var5.isFullBlock() && !var6.isFullBlock())
                    {
                        var15 = 3;
                    }

                    if (var6.isFullBlock() && !var5.isFullBlock())
                    {
                        var15 = 2;
                    }

                    if (var7.isFullBlock() && !var8.isFullBlock())
                    {
                        var15 = 5;
                    }

                    if (var8.isFullBlock() && !var7.isFullBlock())
                    {
                        var15 = 4;
                    }
                }
                else
                {
                    var10 = var7 == this ? p_149954_2_ - 1 : p_149954_2_ + 1;
                    var11 = p_149954_1_.getBlock(var10, p_149954_3_, p_149954_4_ - 1);
                    var12 = var7 == this ? p_149954_2_ - 1 : p_149954_2_ + 1;
                    var13 = p_149954_1_.getBlock(var12, p_149954_3_, p_149954_4_ + 1);
                    var15 = 3;
                    var14 = true;

                    if (var7 == this)
                    {
                        var16 = p_149954_1_.getBlockMetadata(p_149954_2_ - 1, p_149954_3_, p_149954_4_);
                    }
                    else
                    {
                        var16 = p_149954_1_.getBlockMetadata(p_149954_2_ + 1, p_149954_3_, p_149954_4_);
                    }

                    if (var16 == 2)
                    {
                        var15 = 2;
                    }

                    if ((var5.isFullBlock() || var11.isFullBlock()) && !var6.isFullBlock() && !var13.isFullBlock())
                    {
                        var15 = 3;
                    }

                    if ((var6.isFullBlock() || var13.isFullBlock()) && !var5.isFullBlock() && !var11.isFullBlock())
                    {
                        var15 = 2;
                    }
                }
            }
            else
            {
                var10 = var5 == this ? p_149954_4_ - 1 : p_149954_4_ + 1;
                var11 = p_149954_1_.getBlock(p_149954_2_ - 1, p_149954_3_, var10);
                var12 = var5 == this ? p_149954_4_ - 1 : p_149954_4_ + 1;
                var13 = p_149954_1_.getBlock(p_149954_2_ + 1, p_149954_3_, var12);
                var15 = 5;
                var14 = true;

                if (var5 == this)
                {
                    var16 = p_149954_1_.getBlockMetadata(p_149954_2_, p_149954_3_, p_149954_4_ - 1);
                }
                else
                {
                    var16 = p_149954_1_.getBlockMetadata(p_149954_2_, p_149954_3_, p_149954_4_ + 1);
                }

                if (var16 == 4)
                {
                    var15 = 4;
                }

                if ((var7.isFullBlock() || var11.isFullBlock()) && !var8.isFullBlock() && !var13.isFullBlock())
                {
                    var15 = 5;
                }

                if ((var8.isFullBlock() || var13.isFullBlock()) && !var7.isFullBlock() && !var11.isFullBlock())
                {
                    var15 = 4;
                }
            }

            p_149954_1_.setBlockMetadataWithNotify(p_149954_2_, p_149954_3_, p_149954_4_, var15, 3);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        int var5 = 0;

        if (worldIn.getBlock(x - 1, y, z) == this)
        {
            ++var5;
        }

        if (worldIn.getBlock(x + 1, y, z) == this)
        {
            ++var5;
        }

        if (worldIn.getBlock(x, y, z - 1) == this)
        {
            ++var5;
        }

        if (worldIn.getBlock(x, y, z + 1) == this)
        {
            ++var5;
        }

        return var5 > 1 ? false : (this.isDoubleChest(worldIn, x - 1, y, z) ? false : (this.isDoubleChest(worldIn, x + 1, y, z) ? false : (this.isDoubleChest(worldIn, x, y, z - 1) ? false : !this.isDoubleChest(worldIn, x, y, z + 1))));
    }

    private boolean isDoubleChest(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y, z) != this ? false : (worldIn.getBlock(x - 1, y, z) == this ? true : (worldIn.getBlock(x + 1, y, z) == this ? true : (worldIn.getBlock(x, y, z - 1) == this ? true : worldIn.getBlock(x, y, z + 1) == this)));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
        TileEntityChest var6 = (TileEntityChest)worldIn.getTileEntity(x, y, z);

        if (var6 != null)
        {
            var6.updateContainingBlockInfo();
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntityChest var7 = (TileEntityChest)worldIn.getTileEntity(x, y, z);

        if (var7 != null)
        {
            for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
            {
                ItemStack var9 = var7.getStackInSlot(var8);

                if (var9 != null)
                {
                    float var10 = this.rand.nextFloat() * 0.8F + 0.1F;
                    float var11 = this.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem var14;

                    for (float var12 = this.rand.nextFloat() * 0.8F + 0.1F; var9.stackSize > 0; worldIn.spawnEntityInWorld(var14))
                    {
                        int var13 = this.rand.nextInt(21) + 10;

                        if (var13 > var9.stackSize)
                        {
                            var13 = var9.stackSize;
                        }

                        var9.stackSize -= var13;
                        var14 = new EntityItem(worldIn, (double)((float)x + var10), (double)((float)y + var11), (double)((float)z + var12), new ItemStack(var9.getItem(), var13, var9.getMetadata()));
                        float var15 = 0.05F;
                        var14.motionX = (double)((float)this.rand.nextGaussian() * var15);
                        var14.motionY = (double)((float)this.rand.nextGaussian() * var15 + 0.2F);
                        var14.motionZ = (double)((float)this.rand.nextGaussian() * var15);

                        if (var9.hasTagCompound())
                        {
                            var14.getEntityItem().setTagCompound((NBTTagCompound)var9.getTagCompound().copy());
                        }
                    }
                }
            }

            worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            IInventory var10 = this.getInventory(worldIn, x, y, z);

            if (var10 != null)
            {
                player.displayGUIChest(var10);
            }

            return true;
        }
    }

    public IInventory getInventory(World p_149951_1_, int p_149951_2_, int p_149951_3_, int p_149951_4_)
    {
        Object var5 = (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_);

        if (var5 == null)
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_ + 1, p_149951_4_).isNormalCube())
        {
            return null;
        }
        else if (isOcelotSittingOnTop(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_ + 1, p_149951_4_).isNormalCube() || isOcelotSittingOnTop(p_149951_1_, p_149951_2_ - 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this && (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_ + 1, p_149951_4_).isNormalCube() || isOcelotSittingOnTop(p_149951_1_, p_149951_2_ + 1, p_149951_3_, p_149951_4_)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this && (p_149951_1_.getBlock(p_149951_2_, p_149951_3_ + 1, p_149951_4_ - 1).isNormalCube() || isOcelotSittingOnTop(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ - 1)))
        {
            return null;
        }
        else if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this && (p_149951_1_.getBlock(p_149951_2_, p_149951_3_ + 1, p_149951_4_ + 1).isNormalCube() || isOcelotSittingOnTop(p_149951_1_, p_149951_2_, p_149951_3_, p_149951_4_ + 1)))
        {
            return null;
        }
        else
        {
            if (p_149951_1_.getBlock(p_149951_2_ - 1, p_149951_3_, p_149951_4_) == this)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_ - 1, p_149951_3_, p_149951_4_), (IInventory)var5);
            }

            if (p_149951_1_.getBlock(p_149951_2_ + 1, p_149951_3_, p_149951_4_) == this)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (IInventory)var5, (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_ + 1, p_149951_3_, p_149951_4_));
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ - 1) == this)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ - 1), (IInventory)var5);
            }

            if (p_149951_1_.getBlock(p_149951_2_, p_149951_3_, p_149951_4_ + 1) == this)
            {
                var5 = new InventoryLargeChest("container.chestDouble", (IInventory)var5, (TileEntityChest)p_149951_1_.getTileEntity(p_149951_2_, p_149951_3_, p_149951_4_ + 1));
            }

            return (IInventory)var5;
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        TileEntityChest var3 = new TileEntityChest();
        return var3;
    }

    public boolean canProvidePower()
    {
        return this.chestType == 1;
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        if (!this.canProvidePower())
        {
            return 0;
        }
        else
        {
            int var6 = ((TileEntityChest)worldIn.getTileEntity(x, y, z)).numPlayersUsing;
            return MathHelper.clamp_int(var6, 0, 15);
        }
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 1 ? this.isProvidingWeakPower(worldIn, x, y, z, side) : 0;
    }

    private static boolean isOcelotSittingOnTop(World p_149953_0_, int p_149953_1_, int p_149953_2_, int p_149953_3_)
    {
        Iterator var4 = p_149953_0_.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox((double)p_149953_1_, (double)(p_149953_2_ + 1), (double)p_149953_3_, (double)(p_149953_1_ + 1), (double)(p_149953_2_ + 2), (double)(p_149953_3_ + 1))).iterator();
        EntityOcelot var6;

        do
        {
            if (!var4.hasNext())
            {
                return false;
            }

            Entity var5 = (Entity)var4.next();
            var6 = (EntityOcelot)var5;
        }
        while (!var6.isSitting());

        return true;
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        return Container.calcRedstoneFromInventory(this.getInventory(worldIn, x, y, z));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("planks_oak");
    }
}
