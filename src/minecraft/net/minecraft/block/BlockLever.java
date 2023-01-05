package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLever extends Block
{
    private static final String __OBFID = "CL_00000264";

    protected BlockLever()
    {
        super(Material.circuits);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
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
        return 12;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        return side == 0 && worldIn.getBlock(x, y + 1, z).isNormalCube() ? true : (side == 1 && World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) ? true : (side == 2 && worldIn.getBlock(x, y, z + 1).isNormalCube() ? true : (side == 3 && worldIn.getBlock(x, y, z - 1).isNormalCube() ? true : (side == 4 && worldIn.getBlock(x + 1, y, z).isNormalCube() ? true : side == 5 && worldIn.getBlock(x - 1, y, z).isNormalCube()))));
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x - 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x + 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x, y, z - 1).isNormalCube() ? true : (worldIn.getBlock(x, y, z + 1).isNormalCube() ? true : (World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) ? true : worldIn.getBlock(x, y + 1, z).isNormalCube()))));
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var11 = meta & 8;
        int var10 = meta & 7;
        byte var12 = -1;

        if (side == 0 && worldIn.getBlock(x, y + 1, z).isNormalCube())
        {
            var12 = 0;
        }

        if (side == 1 && World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z))
        {
            var12 = 5;
        }

        if (side == 2 && worldIn.getBlock(x, y, z + 1).isNormalCube())
        {
            var12 = 4;
        }

        if (side == 3 && worldIn.getBlock(x, y, z - 1).isNormalCube())
        {
            var12 = 3;
        }

        if (side == 4 && worldIn.getBlock(x + 1, y, z).isNormalCube())
        {
            var12 = 2;
        }

        if (side == 5 && worldIn.getBlock(x - 1, y, z).isNormalCube())
        {
            var12 = 1;
        }

        return var12 + var11;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = worldIn.getBlockMetadata(x, y, z);
        int var8 = var7 & 7;
        int var9 = var7 & 8;

        if (var8 == invertMetadata(1))
        {
            if ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 1) == 0)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 5 | var9, 2);
            }
            else
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 6 | var9, 2);
            }
        }
        else if (var8 == invertMetadata(0))
        {
            if ((MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 1) == 0)
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 7 | var9, 2);
            }
            else
            {
                worldIn.setBlockMetadataWithNotify(x, y, z, 0 | var9, 2);
            }
        }
    }

    public static int invertMetadata(int p_149819_0_)
    {
        switch (p_149819_0_)
        {
            case 0:
                return 0;

            case 1:
                return 5;

            case 2:
                return 4;

            case 3:
                return 3;

            case 4:
                return 2;

            case 5:
                return 1;

            default:
                return -1;
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (this.func_149820_e(worldIn, x, y, z))
        {
            int var6 = worldIn.getBlockMetadata(x, y, z) & 7;
            boolean var7 = false;

            if (!worldIn.getBlock(x - 1, y, z).isNormalCube() && var6 == 1)
            {
                var7 = true;
            }

            if (!worldIn.getBlock(x + 1, y, z).isNormalCube() && var6 == 2)
            {
                var7 = true;
            }

            if (!worldIn.getBlock(x, y, z - 1).isNormalCube() && var6 == 3)
            {
                var7 = true;
            }

            if (!worldIn.getBlock(x, y, z + 1).isNormalCube() && var6 == 4)
            {
                var7 = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && var6 == 5)
            {
                var7 = true;
            }

            if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && var6 == 6)
            {
                var7 = true;
            }

            if (!worldIn.getBlock(x, y + 1, z).isNormalCube() && var6 == 0)
            {
                var7 = true;
            }

            if (!worldIn.getBlock(x, y + 1, z).isNormalCube() && var6 == 7)
            {
                var7 = true;
            }

            if (var7)
            {
                this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }

    private boolean func_149820_e(World p_149820_1_, int p_149820_2_, int p_149820_3_, int p_149820_4_)
    {
        if (!this.canPlaceBlockAt(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_))
        {
            this.dropBlockAsItem(p_149820_1_, p_149820_2_, p_149820_3_, p_149820_4_, p_149820_1_.getBlockMetadata(p_149820_2_, p_149820_3_, p_149820_4_), 0);
            p_149820_1_.setBlockToAir(p_149820_2_, p_149820_3_, p_149820_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z) & 7;
        float var6 = 0.1875F;

        if (var5 == 1)
        {
            this.setBlockBounds(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
        }
        else if (var5 == 2)
        {
            this.setBlockBounds(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
        }
        else if (var5 == 3)
        {
            this.setBlockBounds(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
        }
        else if (var5 == 4)
        {
            this.setBlockBounds(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
        }
        else if (var5 != 5 && var5 != 6)
        {
            if (var5 == 0 || var5 == 7)
            {
                var6 = 0.25F;
                this.setBlockBounds(0.5F - var6, 0.4F, 0.5F - var6, 0.5F + var6, 1.0F, 0.5F + var6);
            }
        }
        else
        {
            var6 = 0.25F;
            this.setBlockBounds(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
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
            int var10 = worldIn.getBlockMetadata(x, y, z);
            int var11 = var10 & 7;
            int var12 = 8 - (var10 & 8);
            worldIn.setBlockMetadataWithNotify(x, y, z, var11 + var12, 3);
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, var12 > 0 ? 0.6F : 0.5F);
            worldIn.notifyBlocksOfNeighborChange(x, y, z, this);

            if (var11 == 1)
            {
                worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            }
            else if (var11 == 2)
            {
                worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            }
            else if (var11 == 3)
            {
                worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            }
            else if (var11 == 4)
            {
                worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            }
            else if (var11 != 5 && var11 != 6)
            {
                if (var11 == 0 || var11 == 7)
                {
                    worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
                }
            }
            else
            {
                worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            }

            return true;
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if ((meta & 8) > 0)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y, z, this);
            int var7 = meta & 7;

            if (var7 == 1)
            {
                worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
            }
            else if (var7 == 2)
            {
                worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
            }
            else if (var7 == 3)
            {
                worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
            }
            else if (var7 == 4)
            {
                worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
            }
            else if (var7 != 5 && var7 != 6)
            {
                if (var7 == 0 || var7 == 7)
                {
                    worldIn.notifyBlocksOfNeighborChange(x, y + 1, z, this);
                }
            }
            else
            {
                worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
            }
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return (worldIn.getBlockMetadata(x, y, z) & 8) > 0 ? 15 : 0;
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        int var6 = worldIn.getBlockMetadata(x, y, z);

        if ((var6 & 8) == 0)
        {
            return 0;
        }
        else
        {
            int var7 = var6 & 7;
            return var7 == 0 && side == 0 ? 15 : (var7 == 7 && side == 0 ? 15 : (var7 == 6 && side == 1 ? 15 : (var7 == 5 && side == 1 ? 15 : (var7 == 4 && side == 2 ? 15 : (var7 == 3 && side == 3 ? 15 : (var7 == 2 && side == 4 ? 15 : (var7 == 1 && side == 5 ? 15 : 0)))))));
        }
    }

    public boolean canProvidePower()
    {
        return true;
    }
}
