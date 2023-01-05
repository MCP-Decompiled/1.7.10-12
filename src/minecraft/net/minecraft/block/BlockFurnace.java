package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockFurnace extends BlockContainer
{
    private final Random field_149933_a = new Random();
    private final boolean isBurning;
    private static boolean field_149934_M;
    private IIcon iconTop;
    private IIcon iconFront;
    private static final String __OBFID = "CL_00000248";

    protected BlockFurnace(boolean p_i45407_1_)
    {
        super(Material.rock);
        this.isBurning = p_i45407_1_;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.furnace);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);
        this.func_149930_e(worldIn, x, y, z);
    }

    private void func_149930_e(World p_149930_1_, int p_149930_2_, int p_149930_3_, int p_149930_4_)
    {
        if (!p_149930_1_.isRemote)
        {
            Block var5 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ - 1);
            Block var6 = p_149930_1_.getBlock(p_149930_2_, p_149930_3_, p_149930_4_ + 1);
            Block var7 = p_149930_1_.getBlock(p_149930_2_ - 1, p_149930_3_, p_149930_4_);
            Block var8 = p_149930_1_.getBlock(p_149930_2_ + 1, p_149930_3_, p_149930_4_);
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

            p_149930_1_.setBlockMetadataWithNotify(p_149930_2_, p_149930_3_, p_149930_4_, var9, 2);
        }
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconTop : (side == 0 ? this.iconTop : (side != meta ? this.blockIcon : this.iconFront));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("furnace_side");
        this.iconFront = reg.registerIcon(this.isBurning ? "furnace_front_on" : "furnace_front_off");
        this.iconTop = reg.registerIcon("furnace_top");
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        if (this.isBurning)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            float var7 = (float)x + 0.5F;
            float var8 = (float)y + 0.0F + random.nextFloat() * 6.0F / 16.0F;
            float var9 = (float)z + 0.5F;
            float var10 = 0.52F;
            float var11 = random.nextFloat() * 0.6F - 0.3F;

            if (var6 == 4)
            {
                worldIn.spawnParticle("smoke", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double)(var7 - var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 5)
            {
                worldIn.spawnParticle("smoke", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double)(var7 + var10), (double)var8, (double)(var9 + var11), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 2)
            {
                worldIn.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 - var10), 0.0D, 0.0D, 0.0D);
            }
            else if (var6 == 3)
            {
                worldIn.spawnParticle("smoke", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
                worldIn.spawnParticle("flame", (double)(var7 + var11), (double)var8, (double)(var9 + var10), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            TileEntityFurnace var10 = (TileEntityFurnace)worldIn.getTileEntity(x, y, z);

            if (var10 != null)
            {
                player.func_146101_a(var10);
            }

            return true;
        }
    }

    public static void updateFurnaceBlockState(boolean p_149931_0_, World p_149931_1_, int p_149931_2_, int p_149931_3_, int p_149931_4_)
    {
        int var5 = p_149931_1_.getBlockMetadata(p_149931_2_, p_149931_3_, p_149931_4_);
        TileEntity var6 = p_149931_1_.getTileEntity(p_149931_2_, p_149931_3_, p_149931_4_);
        field_149934_M = true;

        if (p_149931_0_)
        {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, Blocks.lit_furnace);
        }
        else
        {
            p_149931_1_.setBlock(p_149931_2_, p_149931_3_, p_149931_4_, Blocks.furnace);
        }

        field_149934_M = false;
        p_149931_1_.setBlockMetadataWithNotify(p_149931_2_, p_149931_3_, p_149931_4_, var5, 2);

        if (var6 != null)
        {
            var6.validate();
            p_149931_1_.setTileEntity(p_149931_2_, p_149931_3_, p_149931_4_, var6);
        }
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityFurnace();
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (var7 == 0)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 2, 2);
        }

        if (var7 == 1)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 5, 2);
        }

        if (var7 == 2)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 3, 2);
        }

        if (var7 == 3)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 4, 2);
        }

        if (itemIn.hasDisplayName())
        {
            ((TileEntityFurnace)worldIn.getTileEntity(x, y, z)).setCustomInventoryName(itemIn.getDisplayName());
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if (!field_149934_M)
        {
            TileEntityFurnace var7 = (TileEntityFurnace)worldIn.getTileEntity(x, y, z);

            if (var7 != null)
            {
                for (int var8 = 0; var8 < var7.getSizeInventory(); ++var8)
                {
                    ItemStack var9 = var7.getStackInSlot(var8);

                    if (var9 != null)
                    {
                        float var10 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float var11 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;
                        float var12 = this.field_149933_a.nextFloat() * 0.8F + 0.1F;

                        while (var9.stackSize > 0)
                        {
                            int var13 = this.field_149933_a.nextInt(21) + 10;

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
                            var14.motionX = (double)((float)this.field_149933_a.nextGaussian() * var15);
                            var14.motionY = (double)((float)this.field_149933_a.nextGaussian() * var15 + 0.2F);
                            var14.motionZ = (double)((float)this.field_149933_a.nextGaussian() * var15);
                            worldIn.spawnEntityInWorld(var14);
                        }
                    }
                }

                worldIn.updateNeighborsAboutBlockChange(x, y, z, blockBroken);
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public boolean hasComparatorInputOverride()
    {
        return true;
    }

    public int getComparatorInputOverride(World worldIn, int x, int y, int z, int side)
    {
        return Container.calcRedstoneFromInventory((IInventory)worldIn.getTileEntity(x, y, z));
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemFromBlock(Blocks.furnace);
    }
}
