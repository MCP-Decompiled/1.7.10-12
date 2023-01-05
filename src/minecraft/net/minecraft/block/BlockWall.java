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

public class BlockWall extends Block
{
    public static final String[] field_150092_a = new String[] {"normal", "mossy"};
    private static final String __OBFID = "CL_00000331";

    public BlockWall(Block p_i45435_1_)
    {
        super(p_i45435_1_.blockMaterial);
        this.setHardness(p_i45435_1_.blockHardness);
        this.setResistance(p_i45435_1_.blockResistance / 3.0F);
        this.setStepSound(p_i45435_1_.stepSound);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return meta == 1 ? Blocks.mossy_cobblestone.getBlockTextureFromSide(side) : Blocks.cobblestone.getBlockTextureFromSide(side);
    }

    public int getRenderType()
    {
        return 32;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, int x, int y, int z)
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        boolean var5 = this.canConnectWallTo(worldIn, x, y, z - 1);
        boolean var6 = this.canConnectWallTo(worldIn, x, y, z + 1);
        boolean var7 = this.canConnectWallTo(worldIn, x - 1, y, z);
        boolean var8 = this.canConnectWallTo(worldIn, x + 1, y, z);
        float var9 = 0.25F;
        float var10 = 0.75F;
        float var11 = 0.25F;
        float var12 = 0.75F;
        float var13 = 1.0F;

        if (var5)
        {
            var11 = 0.0F;
        }

        if (var6)
        {
            var12 = 1.0F;
        }

        if (var7)
        {
            var9 = 0.0F;
        }

        if (var8)
        {
            var10 = 1.0F;
        }

        if (var5 && var6 && !var7 && !var8)
        {
            var13 = 0.8125F;
            var9 = 0.3125F;
            var10 = 0.6875F;
        }
        else if (!var5 && !var6 && var7 && var8)
        {
            var13 = 0.8125F;
            var11 = 0.3125F;
            var12 = 0.6875F;
        }

        this.setBlockBounds(var9, 0.0F, var11, var10, var13, var12);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World worldIn, int x, int y, int z)
    {
        this.setBlockBoundsBasedOnState(worldIn, x, y, z);
        this.maxY = 1.5D;
        return super.getCollisionBoundingBoxFromPool(worldIn, x, y, z);
    }

    public boolean canConnectWallTo(IBlockAccess p_150091_1_, int p_150091_2_, int p_150091_3_, int p_150091_4_)
    {
        Block var5 = p_150091_1_.getBlock(p_150091_2_, p_150091_3_, p_150091_4_);
        return var5 != this && var5 != Blocks.fence_gate ? (var5.blockMaterial.isOpaque() && var5.renderAsNormalBlock() ? var5.blockMaterial != Material.gourd : false) : true;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return side == 0 ? super.shouldSideBeRendered(worldIn, x, y, z, side) : true;
    }

    public void registerIcons(IIconRegister reg) {}
}
