package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCarpet extends Block
{
    private static final String __OBFID = "CL_00000338";

    protected BlockCarpet()
    {
        super(Material.carpet);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setBlockBoundsFromMeta(0);
    }

    public IIcon getIcon(int side, int meta)
    {
        return Blocks.wool.getIcon(side, meta);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        byte var5 = 0;
        float var6 = 0.0625F;
        return AxisAlignedBB.getBoundingBox((double)x + this.minX, (double)y + this.minY, (double)z + this.minZ, (double)x + this.maxX, (double)((float)y + (float)var5 * var6), (double)z + this.maxZ);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBoundsFromMeta(0);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        this.setBlockBoundsFromMeta(worldIn.getBlockMetadata(x, y, z));
    }

    protected void setBlockBoundsFromMeta(int meta)
    {
        byte var2 = 0;
        float var3 = (float)(1 * (1 + var2)) / 16.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, var3, 1.0F);
    }

    public boolean canPlaceBlockAt(World worldIn, int x, int y, int z)
    {
        return super.canPlaceBlockAt(worldIn, x, y, z) && this.canBlockStay(worldIn, x, y, z);
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        this.func_150090_e(worldIn, x, y, z);
    }

    private boolean func_150090_e(World p_150090_1_, int p_150090_2_, int p_150090_3_, int p_150090_4_)
    {
        if (!this.canBlockStay(p_150090_1_, p_150090_2_, p_150090_3_, p_150090_4_))
        {
            this.dropBlockAsItem(p_150090_1_, p_150090_2_, p_150090_3_, p_150090_4_, p_150090_1_.getBlockMetadata(p_150090_2_, p_150090_3_, p_150090_4_), 0);
            p_150090_1_.setBlockToAir(p_150090_2_, p_150090_3_, p_150090_4_);
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean canBlockStay(World worldIn, int x, int y, int z)
    {
        return !worldIn.isAirBlock(x, y - 1, z);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 1 ? true : super.shouldSideBeRendered(worldIn, x, y, z, side);
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            list.add(new ItemStack(itemIn, 1, var4));
        }
    }

    public void registerIcons(IIconRegister reg) {}
}
