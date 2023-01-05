package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class BlockSand extends BlockFalling
{
    public static final String[] field_149838_a = new String[] {"default", "red"};
    private static IIcon sandIcon;
    private static IIcon redSandIcon;
    private static final String __OBFID = "CL_00000303";

    public IIcon getIcon(int side, int meta)
    {
        return meta == 1 ? redSandIcon : sandIcon;
    }

    public void registerIcons(IIconRegister reg)
    {
        sandIcon = reg.registerIcon("sand");
        redSandIcon = reg.registerIcon("red_sand");
    }

    public int damageDropped(int meta)
    {
        return meta;
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    public MapColor getMapColor(int meta)
    {
        return meta == 1 ? MapColor.dirtColor : MapColor.sandColor;
    }
}
