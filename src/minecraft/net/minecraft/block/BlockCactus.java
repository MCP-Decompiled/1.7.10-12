package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockCactus extends Block
{
    private IIcon iconTop;
    private IIcon iconBottom;
    private static final String __OBFID = "CL_00000210";

    protected BlockCactus()
    {
        super(Material.cactus);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public void updateTick(World worldIn, int x, int y, int z, Random random)
    {
        if (worldIn.isAirBlock(x, y + 1, z))
        {
            int var6;

            for (var6 = 1; worldIn.getBlock(x, y - var6, z) == this; ++var6)
            {
                ;
            }

            if (var6 < 3)
            {
                int var7 = worldIn.getBlockMetadata(x, y, z);

                if (var7 == 15)
                {
                    worldIn.setBlock(x, y + 1, z, this);
                    worldIn.setBlockMetadataWithNotify(x, y, z, 0, 4);
                    this.onNeighborBlockChange(worldIn, x, y + 1, z, this);
                }
                else
                {
                    worldIn.setBlockMetadataWithNotify(x, y, z, var7 + 1, 4);
                }
            }
        }
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        float var5 = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + var5), (double)y, (double)((float)z + var5), (double)((float)(x + 1) - var5), (double)((float)(y + 1) - var5), (double)((float)(z + 1) - var5));
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        float var5 = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + var5), (double)y, (double)((float)z + var5), (double)((float)(x + 1) - var5), (double)(y + 1), (double)((float)(z + 1) - var5));
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : this.blockIcon);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getRenderType()
    {
        return 13;
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return !super.canPlaceBlockAt(worldIn, x, y, z) ? false : this.canBlockStay(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            worldIn.breakBlock(x, y, z, true);
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        if (worldIn.getBlock(x - 1, y, z).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x + 1, y, z).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x, y, z - 1).getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.getBlock(x, y, z + 1).getMaterial().isSolid())
        {
            return false;
        }
        else
        {
            Block var5 = worldIn.getBlock(x, y - 1, z);
            return var5 == Blocks.cactus || var5 == Blocks.sand;
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0F);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.iconTop = reg.registerIcon(this.getTextureName() + "_top");
        this.iconBottom = reg.registerIcon(this.getTextureName() + "_bottom");
    }
}
