package net.minecraft.item;

import java.util.List;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

public class ItemCoal extends Item
{
    private IIcon field_111220_a;
    private static final String __OBFID = "CL_00000002";

    public ItemCoal()
    {
        this.setHasSubtypes(true);
        this.setMaxDurability(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    public String getUnlocalizedName(ItemStack stack)
    {
        return stack.getMetadata() == 1 ? "item.charcoal" : "item.coal";
    }

    public void getSubItems(Item p_150895_1_, CreativeTabs p_150895_2_, List p_150895_3_)
    {
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 0));
        p_150895_3_.add(new ItemStack(p_150895_1_, 1, 1));
    }

    public IIcon getIconFromDamage(int p_77617_1_)
    {
        return p_77617_1_ == 1 ? this.field_111220_a : super.getIconFromDamage(p_77617_1_);
    }

    public void registerIcons(IIconRegister register)
    {
        super.registerIcons(register);
        this.field_111220_a = register.registerIcon("charcoal");
    }
}
