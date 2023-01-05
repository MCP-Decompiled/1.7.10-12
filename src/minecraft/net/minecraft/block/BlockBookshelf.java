package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockBookshelf extends Block
{
    private static final String __OBFID = "CL_00000206";

    public BlockBookshelf()
    {
        super(Material.wood);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side != 1 && side != 0 ? super.getIcon(side, meta) : Blocks.planks.getBlockTextureFromSide(side);
    }

    public int quantityDropped(Random random)
    {
        return 3;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.book;
    }
}
