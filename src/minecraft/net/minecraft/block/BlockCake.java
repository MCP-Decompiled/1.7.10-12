package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCake extends Block
{
    private IIcon iconTop;
    private IIcon iconBottom;
    private IIcon iconInner;
    private static final String __OBFID = "CL_00000211";

    protected BlockCake()
    {
        super(Material.cake);
        this.setTickRandomly(true);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float)(1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        this.setBlockBounds(var7, 0.0F, var6, 1.0F - var6, var8, 1.0F - var6);
    }

    public void setBlockBoundsForItemRender()
    {
        float var1 = 0.0625F;
        float var2 = 0.5F;
        this.setBlockBounds(var1, 0.0F, var1, 1.0F - var1, var2, 1.0F - var1);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float)(1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + var7), (double)y, (double)((float)z + var6), (double)((float)(x + 1) - var6), (double)((float)y + var8 - var6), (double)((float)(z + 1) - var6));
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z);
        float var6 = 0.0625F;
        float var7 = (float)(1 + var5 * 2) / 16.0F;
        float var8 = 0.5F;
        return AxisAlignedBB.getBoundingBox((double)((float)x + var7), (double)y, (double)((float)z + var6), (double)((float)(x + 1) - var6), (double)((float)y + var8), (double)((float)(z + 1) - var6));
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 1 ? this.iconTop : (side == 0 ? this.iconBottom : (meta > 0 && side == 4 ? this.iconInner : this.blockIcon));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.iconInner = reg.registerIcon(this.getTextureName() + "_inner");
        this.iconTop = reg.registerIcon(this.getTextureName() + "_top");
        this.iconBottom = reg.registerIcon(this.getTextureName() + "_bottom");
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        this.func_150036_b(worldIn, x, y, z, player);
        return true;
    }

    public void onBlockClicked(World worldIn, int x, int y, int z, EntityPlayer player)
    {
        this.func_150036_b(worldIn, x, y, z, player);
    }

    private void func_150036_b(World p_150036_1_, int p_150036_2_, int p_150036_3_, int p_150036_4_, EntityPlayer p_150036_5_)
    {
        if (p_150036_5_.canEat(false))
        {
            p_150036_5_.getFoodStats().addStats(2, 0.1F);
            int var6 = p_150036_1_.getBlockMetadata(p_150036_2_, p_150036_3_, p_150036_4_) + 1;

            if (var6 >= 6)
            {
                p_150036_1_.setBlockToAir(p_150036_2_, p_150036_3_, p_150036_4_);
            }
            else
            {
                p_150036_1_.setBlockMetadataWithNotify(p_150036_2_, p_150036_3_, p_150036_4_, var6, 2);
            }
        }
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return !super.canPlaceBlockAt(worldIn, x, y, z) ? false : this.canBlockStay(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (!this.canBlockStay(worldIn, x, y, z))
        {
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return worldIn.getBlock(x, y - 1, z).getMaterial().isSolid();
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return null;
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Items.cake;
    }
}
