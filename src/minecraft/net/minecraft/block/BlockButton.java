package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockButton extends Block
{
    private final boolean wooden;
    private static final String __OBFID = "CL_00000209";

    protected BlockButton(boolean wooden)
    {
        super(Material.circuits);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabRedstone);
        this.wooden = wooden;
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public int tickRate(World worldIn)
    {
        return this.wooden ? 30 : 20;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, int x, int y, int z, int side)
    {
        return side == 2 && worldIn.getBlock(x, y, z + 1).isNormalCube() ? true : (side == 3 && worldIn.getBlock(x, y, z - 1).isNormalCube() ? true : (side == 4 && worldIn.getBlock(x + 1, y, z).isNormalCube() ? true : side == 5 && worldIn.getBlock(x - 1, y, z).isNormalCube()));
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x - 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x + 1, y, z).isNormalCube() ? true : (worldIn.getBlock(x, y, z - 1).isNormalCube() ? true : worldIn.getBlock(x, y, z + 1).isNormalCube()));
    }

    public int onBlockPlaced(World worldIn, int x, int y, int z, int side, float subX, float subY, float subZ, int meta)
    {
        int var10 = worldIn.getBlockMetadata(x, y, z);
        int var11 = var10 & 8;
        var10 &= 7;

        if (side == 2 && worldIn.getBlock(x, y, z + 1).isNormalCube())
        {
            var10 = 4;
        }
        else if (side == 3 && worldIn.getBlock(x, y, z - 1).isNormalCube())
        {
            var10 = 3;
        }
        else if (side == 4 && worldIn.getBlock(x + 1, y, z).isNormalCube())
        {
            var10 = 2;
        }
        else if (side == 5 && worldIn.getBlock(x - 1, y, z).isNormalCube())
        {
            var10 = 1;
        }
        else
        {
            var10 = this.findSolidSide(worldIn, x, y, z);
        }

        return var10 + var11;
    }

    private int findSolidSide(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x - 1, y, z).isNormalCube() ? 1 : (worldIn.getBlock(x + 1, y, z).isNormalCube() ? 2 : (worldIn.getBlock(x, y, z - 1).isNormalCube() ? 3 : (worldIn.getBlock(x, y, z + 1).isNormalCube() ? 4 : 1)));
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (this.canStay(worldIn, x, y, z))
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

            if (var7)
            {
                this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }

    private boolean canStay(World worldIn, int x, int y, int z)
    {
        if (!this.canPlaceBlockAt(worldIn, x, y, z))
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        this.setBlockBoundsFromMeta(var5);
    }

    private void setBlockBoundsFromMeta(int meta)
    {
        int var2 = meta & 7;
        boolean var3 = (meta & 8) > 0;
        float var4 = 0.375F;
        float var5 = 0.625F;
        float var6 = 0.1875F;
        float var7 = 0.125F;

        if (var3)
        {
            var7 = 0.0625F;
        }

        if (var2 == 1)
        {
            this.setBlockBounds(0.0F, var4, 0.5F - var6, var7, var5, 0.5F + var6);
        }
        else if (var2 == 2)
        {
            this.setBlockBounds(1.0F - var7, var4, 0.5F - var6, 1.0F, var5, 0.5F + var6);
        }
        else if (var2 == 3)
        {
            this.setBlockBounds(0.5F - var6, var4, 0.0F, 0.5F + var6, var5, var7);
        }
        else if (var2 == 4)
        {
            this.setBlockBounds(0.5F - var6, var4, 1.0F - var7, 0.5F + var6, var5, 1.0F);
        }
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player) {}

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        int var10 = worldIn.getBlockMetadata(x, y, z);
        int var11 = var10 & 7;
        int var12 = 8 - (var10 & 8);

        if (var12 == 0)
        {
            return true;
        }
        else
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var11 + var12, 3);
            worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
            this.updateNeighbor(worldIn, x, y, z, var11);
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
            return true;
        }
    }

    public void breakBlock(World worldIn, int x, int y, int z, Block blockBroken, int meta)
    {
        if ((meta & 8) > 0)
        {
            int var7 = meta & 7;
            this.updateNeighbor(worldIn, x, y, z, var7);
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
            return var7 == 5 && side == 1 ? 15 : (var7 == 4 && side == 2 ? 15 : (var7 == 3 && side == 3 ? 15 : (var7 == 2 && side == 4 ? 15 : (var7 == 1 && side == 5 ? 15 : 0))));
        }
    }

    public boolean canProvidePower()
    {
        return true;
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (!worldIn.isRemote)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);

            if ((var6 & 8) != 0)
            {
                if (this.wooden)
                {
                    this.activateButton(worldIn, x, y, z);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var6 & 7, 3);
                    int var7 = var6 & 7;
                    this.updateNeighbor(worldIn, x, y, z, var7);
                    worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
                    worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
                }
            }
        }
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.1875F;
        float var2 = 0.125F;
        float var3 = 0.125F;
        this.setBlockBounds(0.5F - var1, 0.5F - var2, 0.5F - var3, 0.5F + var1, 0.5F + var2, 0.5F + var3);
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (this.wooden)
            {
                if ((worldIn.getBlockMetadata(x, y, z) & 8) == 0)
                {
                    this.activateButton(worldIn, x, y, z);
                }
            }
        }
    }

    private void activateButton(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        int var6 = var5 & 7;
        boolean var7 = (var5 & 8) != 0;
        this.setBlockBoundsFromMeta(var5);
        List var9 = worldIn.getEntitiesWithinAABB(EntityArrow.class, AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)y + this.maxY, (double)z + this.maxZ));
        boolean var8 = !var9.isEmpty();

        if (var8 && !var7)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var6 | 8, 3);
            this.updateNeighbor(worldIn, x, y, z, var6);
            worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.6F);
        }

        if (!var8 && var7)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var6, 3);
            this.updateNeighbor(worldIn, x, y, z, var6);
            worldIn.markBlockRangeForRenderUpdate(x, y, z, x, y, z);
            worldIn.playSoundEffect((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, "random.click", 0.3F, 0.5F);
        }

        if (var8)
        {
            worldIn.scheduleBlockUpdate(x, y, z, this, this.tickRate(worldIn));
        }
    }

    private void updateNeighbor(World worldIn, int x, int y, int z, int p_150042_5_)
    {
        worldIn.notifyBlocksOfNeighborChange(x, y, z, this);

        if (p_150042_5_ == 1)
        {
            worldIn.notifyBlocksOfNeighborChange(x - 1, y, z, this);
        }
        else if (p_150042_5_ == 2)
        {
            worldIn.notifyBlocksOfNeighborChange(x + 1, y, z, this);
        }
        else if (p_150042_5_ == 3)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y, z - 1, this);
        }
        else if (p_150042_5_ == 4)
        {
            worldIn.notifyBlocksOfNeighborChange(x, y, z + 1, this);
        }
        else
        {
            worldIn.notifyBlocksOfNeighborChange(x, y - 1, z, this);
        }
    }

    public void registerIcons(IIconRegister reg) {}
}
