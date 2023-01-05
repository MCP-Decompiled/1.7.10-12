package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockHugeMushroom extends Block
{
    private static final String[] field_149793_a = new String[] {"skin_brown", "skin_red"};
    private final int field_149792_b;
    private IIcon[] field_149794_M;
    private IIcon field_149795_N;
    private IIcon field_149796_O;
    private static final String __OBFID = "CL_00000258";

    public BlockHugeMushroom(Material p_i45412_1_, int p_i45412_2_)
    {
        super(p_i45412_1_);
        this.field_149792_b = p_i45412_2_;
    }

    public IIcon getIcon(int side, int meta)
    {
        return meta == 10 && side > 1 ? this.field_149795_N : (meta >= 1 && meta <= 9 && side == 1 ? this.field_149794_M[this.field_149792_b] : (meta >= 1 && meta <= 3 && side == 2 ? this.field_149794_M[this.field_149792_b] : (meta >= 7 && meta <= 9 && side == 3 ? this.field_149794_M[this.field_149792_b] : ((meta == 1 || meta == 4 || meta == 7) && side == 4 ? this.field_149794_M[this.field_149792_b] : ((meta == 3 || meta == 6 || meta == 9) && side == 5 ? this.field_149794_M[this.field_149792_b] : (meta == 14 ? this.field_149794_M[this.field_149792_b] : (meta == 15 ? this.field_149795_N : this.field_149796_O)))))));
    }

    public int quantityDropped(Random random)
    {
        int var2 = random.nextInt(10) - 7;

        if (var2 < 0)
        {
            var2 = 0;
        }

        return var2;
    }

    public Item getItemDropped(int meta, Random random, int fortune)
    {
        return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + this.field_149792_b);
    }

    public Item getItem(World worldIn, int x, int y, int z)
    {
        return Item.getItemById(Block.getIdFromBlock(Blocks.brown_mushroom) + this.field_149792_b);
    }

    public void registerIcons(IIconRegister reg)
    {
        this.field_149794_M = new IIcon[field_149793_a.length];

        for (int var2 = 0; var2 < this.field_149794_M.length; ++var2)
        {
            this.field_149794_M[var2] = reg.registerIcon(this.getTextureName() + "_" + field_149793_a[var2]);
        }

        this.field_149796_O = reg.registerIcon(this.getTextureName() + "_" + "inside");
        this.field_149795_N = reg.registerIcon(this.getTextureName() + "_" + "skin_stem");
    }
}
