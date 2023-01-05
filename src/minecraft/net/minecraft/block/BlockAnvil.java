package net.minecraft.block;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAnvil extends BlockFalling
{
    public static final String[] anvilDamageNames = new String[] {"intact", "slightlyDamaged", "veryDamaged"};
    private static final String[] anvilIconNames = new String[] {"anvil_top_damaged_0", "anvil_top_damaged_1", "anvil_top_damaged_2"};
    public int anvilRenderSide;
    private IIcon[] anvilIcons;
    private static final String __OBFID = "CL_00000192";

    protected BlockAnvil()
    {
        super(Material.anvil);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public IIcon getIcon(int side, int meta)
    {
        if (this.anvilRenderSide == 3 && side == 1)
        {
            int var3 = (meta >> 2) % this.anvilIcons.length;
            return this.anvilIcons[var3];
        }
        else
        {
            return this.blockIcon;
        }
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon("anvil_base");
        this.anvilIcons = new IIcon[anvilIconNames.length];

        for (int var2 = 0; var2 < this.anvilIcons.length; ++var2)
        {
            this.anvilIcons[var2] = reg.registerIcon(anvilIconNames[var2]);
        }
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn)
    {
        int var7 = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int var8 = worldIn.getBlockMetadata(x, y, z) >> 2;
        ++var7;
        var7 %= 4;

        if (var7 == 0)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 2 | var8 << 2, 2);
        }

        if (var7 == 1)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 3 | var8 << 2, 2);
        }

        if (var7 == 2)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 0 | var8 << 2, 2);
        }

        if (var7 == 3)
        {
            worldIn.setBlockMetadataWithNotify(x, y, z, 1 | var8 << 2, 2);
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (worldIn.isRemote)
        {
            return true;
        }
        else
        {
            player.displayGUIAnvil(x, y, z);
            return true;
        }
    }

    public int getRenderType()
    {
        return 35;
    }

    public int damageDropped(int meta)
    {
        return meta >> 2;
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, int x, int y, int z)
    {
        int var5 = worldIn.getBlockMetadata(x, y, z) & 3;

        if (var5 != 3 && var5 != 1)
        {
            this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
        }
        else
        {
            this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
        }
    }

    public void getSubBlocks(Item itemIn, CreativeTabs tab, List list)
    {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }

    protected void onStartFalling(EntityFallingBlock p_149829_1_)
    {
        p_149829_1_.setHurtEntities(true);
    }

    public void playSoundWhenFallen(World p_149828_1_, int p_149828_2_, int p_149828_3_, int p_149828_4_, int p_149828_5_)
    {
        p_149828_1_.playAuxSFX(1022, p_149828_2_, p_149828_3_, p_149828_4_, 0);
    }

    public boolean shouldSideBeRendered(IBlockAccess worldIn, int x, int y, int z, int side)
    {
        return true;
    }
}
