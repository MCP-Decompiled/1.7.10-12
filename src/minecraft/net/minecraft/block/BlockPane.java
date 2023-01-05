package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPane extends Block
{
    private final String field_150100_a;
    private final boolean field_150099_b;
    private final String field_150101_M;
    private IIcon field_150102_N;
    private static final String __OBFID = "CL_00000322";

    protected BlockPane(String p_i45432_1_, String p_i45432_2_, Material p_i45432_3_, boolean p_i45432_4_)
    {
        super(p_i45432_3_);
        this.field_150100_a = p_i45432_2_;
        this.field_150099_b = p_i45432_4_;
        this.field_150101_M = p_i45432_1_;
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return !this.field_150099_b ? null : super.getItemDropped(meta, random, fortune);
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return this.blockMaterial == Material.glass ? 41 : 18;
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return worldIn.getBlock(x, y, z) == this ? false : super.shouldSideBeRendered(worldIn, x, y, z, side);
    }

    public void addCollisionBoxesToList(World worldIn, int x, int y, int z, AxisAlignedBB mask, List list, Entity collider)
    {
        boolean var8 = this.canPaneConnectToBlock(worldIn.getBlock(x, y, z - 1));
        boolean var9 = this.canPaneConnectToBlock(worldIn.getBlock(x, y, z + 1));
        boolean var10 = this.canPaneConnectToBlock(worldIn.getBlock(x - 1, y, z));
        boolean var11 = this.canPaneConnectToBlock(worldIn.getBlock(x + 1, y, z));

        if ((!var10 || !var11) && (var10 || var11 || var8 || var9))
        {
            if (var10 && !var11)
            {
                this.setBlockBounds(0.0F, 0.0F, 0.4375F, 0.5F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
            }
            else if (!var10 && var11)
            {
                this.setBlockBounds(0.5F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
            }
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.4375F, 1.0F, 1.0F, 0.5625F);
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }

        if ((!var8 || !var9) && (var10 || var11 || var8 || var9))
        {
            if (var8 && !var9)
            {
                this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 0.5F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
            }
            else if (!var8 && var9)
            {
                this.setBlockBounds(0.4375F, 0.0F, 0.5F, 0.5625F, 1.0F, 1.0F);
                super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
            }
        }
        else
        {
            this.setBlockBounds(0.4375F, 0.0F, 0.0F, 0.5625F, 1.0F, 1.0F);
            super.addCollisionBoxesToList(worldIn, x, y, z, mask, list, collider);
        }
    }

    public void setBlockBoundsForItemRender()
    {
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        float var5 = 0.4375F;
        float var6 = 0.5625F;
        float var7 = 0.4375F;
        float var8 = 0.5625F;
        boolean var9 = this.canPaneConnectToBlock(worldIn.getBlock(x, y, z - 1));
        boolean var10 = this.canPaneConnectToBlock(worldIn.getBlock(x, y, z + 1));
        boolean var11 = this.canPaneConnectToBlock(worldIn.getBlock(x - 1, y, z));
        boolean var12 = this.canPaneConnectToBlock(worldIn.getBlock(x + 1, y, z));

        if ((!var11 || !var12) && (var11 || var12 || var9 || var10))
        {
            if (var11 && !var12)
            {
                var5 = 0.0F;
            }
            else if (!var11 && var12)
            {
                var6 = 1.0F;
            }
        }
        else
        {
            var5 = 0.0F;
            var6 = 1.0F;
        }

        if ((!var9 || !var10) && (var11 || var12 || var9 || var10))
        {
            if (var9 && !var10)
            {
                var7 = 0.0F;
            }
            else if (!var9 && var10)
            {
                var8 = 1.0F;
            }
        }
        else
        {
            var7 = 0.0F;
            var8 = 1.0F;
        }

        this.setBlockBounds(var5, 0.0F, var7, var6, 1.0F, var8);
    }

    public IIcon func_150097_e()
    {
        return this.field_150102_N;
    }

    public final boolean canPaneConnectToBlock(Block p_150098_1_)
    {
        return p_150098_1_.isFullBlock() || p_150098_1_ == this || p_150098_1_ == Blocks.glass || p_150098_1_ == Blocks.stained_glass || p_150098_1_ == Blocks.stained_glass_pane || p_150098_1_ instanceof BlockPane;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(this), 1, meta);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.field_150101_M);
        this.field_150102_N = reg.registerIcon(this.field_150100_a);
    }
}
