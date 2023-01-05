package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFenceGate extends BlockDirectional
{
    private static final String __OBFID = "CL_00000243";

    public BlockFenceGate()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public IIcon getIcon(int side, int meta)
    {
        return Blocks.planks.getBlockTextureFromSide(side);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return !worldIn.getBlock(x, y - 1, z).getMaterial().isSolid() ? false : super.canPlaceBlockAt(worldIn, x, y, z);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        return isFenceGateOpen(var5) ? null : (var5 != 2 && var5 != 0 ? AxisAlignedBB.getBoundingBox((double)((float)x + 0.375F), (double)y, (double)z, (double)((float)x + 0.625F), (double)((float)y + 1.5F), (double)(z + 1)) : AxisAlignedBB.getBoundingBox((double)x, (double)y, (double)((float)z + 0.375F), (double)(x + 1), (double)((float)y + 1.5F), (double)((float)z + 0.625F)));
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = getDirection(worldIn.getBlockMetadata(x, y, z));

        if (var5 != 2 && var5 != 0)
        {
            this.setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
        }
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
        return isFenceGateOpen(worldIn.getBlockMetadata(x, y, z));
    }

    public int getRenderType()
    {
        return 21;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = (MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) % 4;
        worldIn.setBlockMetadataWithNotify(x, y, z, var7, 2);
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        int var10 = worldIn.getBlockMetadata(x, y, z);

        if (isFenceGateOpen(var10))
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, var10 & -5, 2);
        }
        else
        {
            int var11 = (MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) % 4;
            int var12 = getDirection(var10);

            if (var12 == (var11 + 2) % 4)
            {
                var10 = var11;
            }

            worldIn.setBlockMetadataWithNotify(x, y, z, var10 | 4, 2);
        }

        worldIn.playAuxSFXAtEntity(player, 1003, x, y, z, 0);
        return true;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!worldIn.isRemote)
        {
            int var6 = worldIn.getBlockMetadata(x, y, z);
            boolean var7 = worldIn.isBlockIndirectlyGettingPowered(x, y, z);

            if (var7 || neighbor.canProvidePower())
            {
                if (var7 && !isFenceGateOpen(var6))
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var6 | 4, 2);
                    worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1003, x, y, z, 0);
                }
                else if (!var7 && isFenceGateOpen(var6))
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var6 & -5, 2);
                    worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1003, x, y, z, 0);
                }
            }
        }
    }

    public static boolean isFenceGateOpen(int p_149896_0_)
    {
        return (p_149896_0_ & 4) != 0;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }

    public void registerIcons(IIconRegister reg) {}
}
