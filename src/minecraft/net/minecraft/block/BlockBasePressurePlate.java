package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockBasePressurePlate extends Block
{
    private String name;
    private static final String __OBFID = "CL_00000194";

    protected BlockBasePressurePlate(String name, Material materialIn)
    {
        super(materialIn);
        this.name = name;
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.setTickRandomly(true);
        this.setBlockBoundsFromMeta(this.getMetaFromPower(15));
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.setBlockBoundsFromMeta(worldIn.getBlockMetadata(x, y, z));
    }

    protected void setBlockBoundsFromMeta(int meta)
    {
        boolean var2 = this.getPowerFromMeta(meta) > 0;
        float var3 = 0.0625F;

        if (var2)
        {
            this.setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.03125F, 1.0F - var3);
        }
        else
        {
            this.setBlockBounds(var3, 0.0F, var3, 1.0F - var3, 0.0625F, 1.0F - var3);
        }
    }

    public int tickRate(World worldIn)
    {
        return 20;
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

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return true;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) || BlockFence.isFence(worldIn.getBlock(x, y - 1, z));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        boolean var6 = false;

        if (!World.doesBlockHaveSolidTopSurface(worldIn, x, y - 1, z) && !BlockFence.isFence(worldIn.getBlock(x, y - 1, z)))
        {
            var6 = true;
        }

        if (var6)
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            int var6 = this.getPowerFromMeta(worldIn.getBlockMetadata(x, y, z));

            if (var6 > 0)
            {
                this.setStateIfMobInteractsWithPlate(worldIn, x, y, z, var6);
            }
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            int var6 = this.getPowerFromMeta(worldIn.getBlockMetadata(x, y, z));

            if (var6 == 0)
            {
                this.setStateIfMobInteractsWithPlate(worldIn, x, y, z, var6);
            }
        }
    }

    protected void setStateIfMobInteractsWithPlate(World worldIn, int x, int y, int z, int power)
    {
        int var6 = this.getPlateState(worldIn, x, y, z);
        boolean var7 = power > 0;
        boolean var8 = var6 > 0;

        if (power != var6)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, this.getMetaFromPower(var6), 2);
            this.updateNeighbors(worldIn, x, y, z);
            worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
        }

        if (!var8 && var7)
        {
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        }
        else if (var8 && !var7)
        {
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.1D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (var8)
        {
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
        }
    }

    protected AxisAlignedBB getSensitiveAABB(int x, int y, int z)
    {
        float var4 = 0.125F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + var4), (double)y, (double)((float)z + var4), (double)((float)(x + 1) - var4), (double)y + 0.25D, (double)((float)(z + 1) - var4));
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if (this.getPowerFromMeta(meta) > 0)
        {
            this.updateNeighbors(worldIn, x, y, z);
        }

        super.breakBlock(worldIn, x, y, z, blockBroken, meta);
    }

    protected void updateNeighbors(World worldIn, int x, int y, int z)
    {
        worldIn.notifyBlocksOfNeighborChange(x, y, z, this);
        worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
    }

    public int isProvidingWeakPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return this.getPowerFromMeta(worldIn.getBlockMetadata(x, y, z));
    }

    public int isProvidingStrongPower(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 1 ? this.getPowerFromMeta(worldIn.getBlockMetadata(x, y, z)) : 0;
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.5F;
        float var2 = 0.125F;
        float var3 = 0.5F;
        this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    public int getMobilityFlag()
    {
        return 1;
    }

    protected abstract int getPlateState(World p_150065_1_, int p_150065_2_, int p_150065_3_, int p_150065_4_);

    protected abstract int getPowerFromMeta(int p_150060_1_);

    protected abstract int getMetaFromPower(int p_150066_1_);

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.name);
    }
}
