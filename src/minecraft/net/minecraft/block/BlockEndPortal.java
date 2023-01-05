package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEndPortal extends BlockContainer
{
    public static boolean field_149948_a;
    private static final String __OBFID = "CL_00000236";

    protected BlockEndPortal(Material p_i45404_1_)
    {
        super(p_i45404_1_);
        this.setLightLevel(1.0F);
    }

    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityEndPortal();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        float var5 = 0.0625F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var5, 1.0F);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side != 0 ? false : super.shouldSideBeRendered(worldIn, x, y, z, side);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider) {}

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (entityIn.ridingEntity == null && entityIn.riddenByEntity == null && !worldIn.isRemote)
        {
            entityIn.travelToDimension(1);
        }
    }

    public void randomDisplayTick(World worldIn, int x, int y, int z, Random random)
    {
        double var6 = (double)((float)x + random.nextFloat());
        double var8 = (double)((float)y + 0.8F);
        double var10 = (double)((float)z + random.nextFloat());
        double var12 = 0.0D;
        double var14 = 0.0D;
        double var16 = 0.0D;
        worldIn.spawnParticle("smoke", var6, var8, var10, var12, var14, var16);
    }

    public int getRenderType()
    {
        return -1;
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        if (!field_149948_a)
        {
            if (worldIn.provider.dimensionId != 0)
            {
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(0);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("portal");
    }

    public MapColor getMapColor(int meta)
    {
        return MapColor.obsidianColor;
    }
}
