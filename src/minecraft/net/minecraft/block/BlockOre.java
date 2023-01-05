package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockOre extends Block
{
    private static final String __OBFID = "CL_00000282";

    public BlockOre()
    {
        super(Material.rock);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return this == Blocks.coal_ore ? Items.coal : (this == Blocks.diamond_ore ? Items.diamond : (this == Blocks.lapis_ore ? Items.dye : (this == Blocks.emerald_ore ? Items.emerald : (this == Blocks.quartz_ore ? Items.quartz : Item.getItemFromBlock(this)))));
    }

    public int quantityDropped(Random random)
    {
        return this == Blocks.lapis_ore ? 4 + random.nextInt(5) : 1;
    }

    public int quantityDroppedWithBonus(int maxBonus, Random random)
    {
        if (maxBonus > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, random, maxBonus))
        {
            int var3 = random.nextInt(maxBonus + 2) - 1;

            if (var3 < 0)
            {
                var3 = 0;
            }

            return this.quantityDropped(random) * (var3 + 1);
        }
        else
        {
            return this.quantityDropped(random);
        }
    }

    public void dropBlockAsItemWithChance(World worldIn, int x, int y, int z, int meta, float chance, int fortune)
    {
        super.dropBlockAsItemWithChance(worldIn, x, y, z, meta, chance, fortune);

        if (this.getItemDropped(meta, worldIn.rand, fortune) != Item.getItemFromBlock(this))
        {
            int var8 = 0;

            if (this == Blocks.coal_ore)
            {
                var8 = MathHelper.getRandomIntegerInRange(worldIn.rand, 0, 2);
            }
            else if (this == Blocks.diamond_ore)
            {
                var8 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
            }
            else if (this == Blocks.emerald_ore)
            {
                var8 = MathHelper.getRandomIntegerInRange(worldIn.rand, 3, 7);
            }
            else if (this == Blocks.lapis_ore)
            {
                var8 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
            }
            else if (this == Blocks.quartz_ore)
            {
                var8 = MathHelper.getRandomIntegerInRange(worldIn.rand, 2, 5);
            }

            this.dropXpOnBlockBreak(worldIn, x, y, z, var8);
        }
    }

    public int damageDropped(int meta)
    {
        return this == Blocks.lapis_ore ? 4 : 0;
    }
}
