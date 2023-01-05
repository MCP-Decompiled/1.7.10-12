package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockMelon extends Block
{
    private IIcon melonTopIcon;
    private static final String __OBFID = "CL_00000267";

    protected BlockMelon()
    {
        super(Material.gourd);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side != 1 && side != 0 ? this.blockIcon : this.melonTopIcon;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Items.melon;
    }

    public int quantityDropped(Random random)
    {
        return 3 + random.nextInt(5);
    }

    public int quantityDroppedWithBonus(int maxBonus, Random random)
    {
        int var3 = this.quantityDropped(random) + random.nextInt(1 + maxBonus);

        if (var3 > 9)
        {
            var3 = 9;
        }

        return var3;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.melonTopIcon = reg.registerIcon(this.getTextureName() + "_top");
    }
}
