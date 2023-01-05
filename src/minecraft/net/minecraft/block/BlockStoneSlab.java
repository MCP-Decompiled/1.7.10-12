package net.minecraft.block;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockStoneSlab extends BlockSlab
{
    public static final String[] field_150006_b = new String[] {"stone", "sand", "wood", "cobble", "brick", "smoothStoneBrick", "netherBrick", "quartz"};
    private IIcon field_150007_M;
    private static final String __OBFID = "CL_00000320";

    public BlockStoneSlab(boolean p_i45431_1_)
    {
        super(p_i45431_1_, Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        int var3 = meta & 7;

        if (this.isFullBlock && (meta & 8) != 0)
        {
            side = 1;
        }

        return var3 == 0 ? (side != 1 && side != 0 ? this.field_150007_M : this.blockIcon) : (var3 == 1 ? Blocks.sandstone.getBlockTextureFromSide(side) : (var3 == 2 ? Blocks.planks.getBlockTextureFromSide(side) : (var3 == 3 ? Blocks.cobblestone.getBlockTextureFromSide(side) : (var3 == 4 ? Blocks.brick_block.getBlockTextureFromSide(side) : (var3 == 5 ? Blocks.stonebrick.getIcon(side, 0) : (var3 == 6 ? Blocks.nether_brick.getBlockTextureFromSide(1) : (var3 == 7 ? Blocks.quartz_block.getBlockTextureFromSide(side) : this.blockIcon)))))));
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("stone_slab_top");
        this.field_150007_M = reg.registerIcon("stone_slab_side");
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemFromBlock(Blocks.stone_slab);
    }

    protected ItemStack createStackedBlock(int meta)
    {
        return new ItemStack(Item.getItemFromBlock(Blocks.stone_slab), 2, meta & 7);
    }

    public String getFullSlabName(int p_150002_1_)
    {
        if (p_150002_1_ < 0 || p_150002_1_ >= field_150006_b.length)
        {
            p_150002_1_ = 0;
        }

        return super.getUnlocalizedName() + "." + field_150006_b[p_150002_1_];
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab))
        {
            for (int var4 = 0; var4 <= 7; ++var4)
            {
                if (var4 != 2)
                {
                    list.add(new ItemStack(itemIn, 1, var4));
                }
            }
        }
    }
}
