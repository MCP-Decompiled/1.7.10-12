package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Facing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPistonMoving extends BlockContainer
{
    private static final String __OBFID = "CL_00000368";

    public BlockPistonMoving()
    {
        super(Material.piston);
        this.setHardness(-1.0F);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return null;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z) {}

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        TileEntity var7 = worldIn.getTileEntity(x, y, z);

        if (var7 instanceof TileEntityPiston)
        {
            ((TileEntityPiston)var7).clearPistonTileEntity();
        }
        else
        {
            super.breakBlock(worldIn, x, y, z, blockBroken, meta);
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        return false;
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (!worldIn.isRemote && worldIn.getTileEntity(x, y, z) == null)
        {
            worldIn.setBlockToAir(x, y, z);
            return true;
        }
        else
        {
            return false;
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        if (!worldIn.isRemote)
        {
            TileEntityPiston var8 = this.func_149963_e(worldIn, x, y, z);

            if (var8 != null)
            {
                var8.getStoredBlockID().dropBlockAsItem(worldIn, x, y, z, var8.getBlockMetadata(), 0);
            }
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            worldIn.getTileEntity(x, y, z);
        }
    }

    public static TileEntity getTileEntity(Block p_149962_0_, int p_149962_1_, int p_149962_2_, boolean p_149962_3_, boolean p_149962_4_)
    {
        return new TileEntityPiston(p_149962_0_, p_149962_1_, p_149962_2_, p_149962_3_, p_149962_4_);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        TileEntityPiston var5 = this.func_149963_e(worldIn, x, y, z);

        if (var5 == null)
        {
            return null;
        }
        else
        {
            float var6 = var5.func_145860_a(0.0F);

            if (var5.isExtending())
            {
                var6 = 1.0F - var6;
            }

            return this.func_149964_a(worldIn, x, y, z, var5.getStoredBlockID(), var6, var5.getPistonOrientation());
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        TileEntityPiston var5 = this.func_149963_e(worldIn, x, y, z);

        if (var5 != null)
        {
            Block var6 = var5.getStoredBlockID();

            if (var6 == this || var6.getMaterial() == Material.air)
            {
                return;
            }

            var6.setBlockBoundsBasedOnState(worldIn, x, y, z);
            float var7 = var5.func_145860_a(0.0F);

            if (var5.isExtending())
            {
                var7 = 1.0F - var7;
            }

            int var8 = var5.getPistonOrientation();
            this.minX = var6.getBlockBoundsMinX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.minY = var6.getBlockBoundsMinY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.minZ = var6.getBlockBoundsMinZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
            this.maxX = var6.getBlockBoundsMaxX() - (double)((float)Facing.offsetsXForSide[var8] * var7);
            this.maxY = var6.getBlockBoundsMaxY() - (double)((float)Facing.offsetsYForSide[var8] * var7);
            this.maxZ = var6.getBlockBoundsMaxZ() - (double)((float)Facing.offsetsZForSide[var8] * var7);
        }
    }

    public AxisAlignedBB func_149964_a(World p_149964_1_, int p_149964_2_, int p_149964_3_, int p_149964_4_, Block p_149964_5_, float p_149964_6_, int p_149964_7_)
    {
        if (p_149964_5_ != this && p_149964_5_.getMaterial() != Material.air)
        {
            AxisAlignedBB var8 = p_149964_5_.getCollisionBoundingBoxFromPool(p_149964_1_, p_149964_2_, p_149964_3_, p_149964_4_);

            if (var8 == null)
            {
                return null;
            }
            else
            {
                if (Facing.offsetsXForSide[p_149964_7_] < 0)
                {
                    var8.minX -= (double)((float)Facing.offsetsXForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxX -= (double)((float)Facing.offsetsXForSide[p_149964_7_] * p_149964_6_);
                }

                if (Facing.offsetsYForSide[p_149964_7_] < 0)
                {
                    var8.minY -= (double)((float)Facing.offsetsYForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxY -= (double)((float)Facing.offsetsYForSide[p_149964_7_] * p_149964_6_);
                }

                if (Facing.offsetsZForSide[p_149964_7_] < 0)
                {
                    var8.minZ -= (double)((float)Facing.offsetsZForSide[p_149964_7_] * p_149964_6_);
                }
                else
                {
                    var8.maxZ -= (double)((float)Facing.offsetsZForSide[p_149964_7_] * p_149964_6_);
                }

                return var8;
            }
        }
        else
        {
            return null;
        }
    }

    private TileEntityPiston func_149963_e(IBlockAccess p_149963_1_, int p_149963_2_, int p_149963_3_, int p_149963_4_)
    {
        TileEntity var5 = p_149963_1_.getTileEntity(p_149963_2_, p_149963_3_, p_149963_4_);
        return var5 instanceof TileEntityPiston ? (TileEntityPiston)var5 : null;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(0);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("piston_top_normal");
    }
}
