package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSign extends BlockContainer
{
    private Class field_149968_a;
    private boolean field_149967_b;
    private static final String __OBFID = "CL_00000306";

    protected BlockSign(Class p_i45426_1_, boolean p_i45426_2_)
    {
        super(Material.wood);
        this.field_149967_b = p_i45426_2_;
        this.field_149968_a = p_i45426_1_;
        float var3 = 0.25F;
        float var4 = 1.0F;
        this.setBlockBounds(0.5F - var3, 0.0F, 0.5F - var3, 0.5F + var3, var4, 0.5F + var3);
    }

    public IIcon getIcon(int side, int meta)
    {
        return Blocks.planks.getBlockTextureFromSide(side);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        return null;
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        return super.getSelectedBoundingBoxFromPool(worldIn, x, y, z);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        if (!this.field_149967_b)
        {
            int var5 = worldIn.getBlockMetadata(x, y, z);
            float var6 = 0.28125F;
            float var7 = 0.78125F;
            float var8 = 0.0F;
            float var9 = 1.0F;
            float var10 = 0.125F;
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

            if (var5 == 2)
            {
                this.setBlockBounds(var8, var6, 1.0F - var10, var9, var7, 1.0F);
            }

            if (var5 == 3)
            {
                this.setBlockBounds(var8, var6, 0.0F, var9, var7, var10);
            }

            if (var5 == 4)
            {
                this.setBlockBounds(1.0F - var10, var6, var8, 1.0F, var7, var9);
            }

            if (var5 == 5)
            {
                this.setBlockBounds(0.0F, var6, var8, var10, var7, var9);
            }
        }
    }

    public int getRenderType()
    {
        return -1;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return true;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        try
        {
            return (TileEntity)this.field_149968_a.newInstance();
        }
        catch (Exception var4)
        {
            throw new RuntimeException(var4);
        }
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.sign;
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        boolean var6 = false;

        if (this.field_149967_b)
        {
            if (!worldIn.getBlock(x, y - 1, z).getMaterial().isSolid())
            {
                var6 = true;
            }
        }
        else
        {
            int var7 = worldIn.getBlockMetadata(x, y, z);
            var6 = true;

            if (var7 == 2 && worldIn.getBlock(x, y, z + 1).getMaterial().isSolid())
            {
                var6 = false;
            }

            if (var7 == 3 && worldIn.getBlock(x, y, z - 1).getMaterial().isSolid())
            {
                var6 = false;
            }

            if (var7 == 4 && worldIn.getBlock(x + 1, y, z).getMaterial().isSolid())
            {
                var6 = false;
            }

            if (var7 == 5 && worldIn.getBlock(x - 1, y, z).getMaterial().isSolid())
            {
                var6 = false;
            }
        }

        if (var6)
        {
            this.dropBlockAsItem(worldIn, x, y, z, worldIn.getBlockMetadata(x, y, z), 0);
            worldIn.setBlockToAir(x, y, z);
        }

        super.onNeighborBlockChange(worldIn, x, y, z, neighbor);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.sign;
    }

    public void registerIcons(IIconRegister reg) {}
}
