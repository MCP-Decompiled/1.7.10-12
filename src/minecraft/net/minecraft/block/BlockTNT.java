package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.util.IIcon;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTNT extends Block
{
    private IIcon field_150116_a;
    private IIcon field_150115_b;
    private static final String __OBFID = "CL_00000324";

    public BlockTNT()
    {
        super(Material.tnt);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    public IIcon getIcon(int side, int meta)
    {
        return side == 0 ? this.field_150115_b : (side == 1 ? this.field_150116_a : this.blockIcon);
    }

    public void onBlockAdded(World worldIn, int x, int y, int z)
    {
        super.onBlockAdded(worldIn, x, y, z);

        if (worldIn.isBlockIndirectlyGettingPowered(x, y, z))
        {
            this.onBlockDestroyedByPlayer(worldIn, x, y, z, 1);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public void onNeighborBlockChange(World worldIn, int x, int y, int z, Block neighbor)
    {
        if (worldIn.isBlockIndirectlyGettingPowered(x, y, z))
        {
            this.onBlockDestroyedByPlayer(worldIn, x, y, z, 1);
            worldIn.setBlockToAir(x, y, z);
        }
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }

    public void onBlockDestroyedByExplosion(World worldIn, int x, int y, int z, Explosion explosionIn)
    {
        if (!worldIn.isRemote)
        {
            EntityTNTPrimed var6 = new EntityTNTPrimed(worldIn, (double)((float)x + 0.5F), (double)((float)y + 0.5F), (double)((float)z + 0.5F), explosionIn.getExplosivePlacedBy());
            var6.fuse = worldIn.rand.nextInt(var6.fuse / 4) + var6.fuse / 8;
            worldIn.spawnEntityInWorld(var6);
        }
    }

    public void onBlockDestroyedByPlayer(World worldIn, int x, int y, int z, int meta)
    {
        this.func_150114_a(worldIn, x, y, z, meta, (EntityLivingBase)null);
    }

    public void func_150114_a(World p_150114_1_, int p_150114_2_, int p_150114_3_, int p_150114_4_, int p_150114_5_, EntityLivingBase p_150114_6_)
    {
        if (!p_150114_1_.isRemote)
        {
            if ((p_150114_5_ & 1) == 1)
            {
                EntityTNTPrimed var7 = new EntityTNTPrimed(p_150114_1_, (double)((float)p_150114_2_ + 0.5F), (double)((float)p_150114_3_ + 0.5F), (double)((float)p_150114_4_ + 0.5F), p_150114_6_);
                p_150114_1_.spawnEntityInWorld(var7);
                p_150114_1_.playSoundAtEntity(var7, "game.tnt.primed", 1.0F, 1.0F);
            }
        }
    }

    public boolean onBlockActivated(World worldIn, int x, int y, int z, EntityPlayer player, int side, float subX, float subY, float subZ)
    {
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == Items.flint_and_steel)
        {
            this.func_150114_a(worldIn, x, y, z, 1, player);
            worldIn.setBlockToAir(x, y, z);
            player.getCurrentEquippedItem().damageItem(1, player);
            return true;
        }
        else
        {
            return super.onBlockActivated(worldIn, x, y, z, player, side, subX, subY, subZ);
        }
    }

    public void onEntityCollidedWithBlock(World worldIn, int x, int y, int z, Entity entityIn)
    {
        if (entityIn instanceof EntityArrow && !worldIn.isRemote)
        {
            EntityArrow var6 = (EntityArrow)entityIn;

            if (var6.isBurning())
            {
                this.func_150114_a(worldIn, x, y, z, 1, var6.shootingEntity instanceof EntityLivingBase ? (EntityLivingBase)var6.shootingEntity : null);
                worldIn.setBlockToAir(x, y, z);
            }
        }
    }

    public boolean canDropFromExplosion(Explosion explosionIn)
    {
        return false;
    }

    public void registerIcons(IIconRegister reg)
    {
        this.blockIcon = reg.registerIcon(this.getTextureName() + "_side");
        this.field_150116_a = reg.registerIcon(this.getTextureName() + "_top");
        this.field_150115_b = reg.registerIcon(this.getTextureName() + "_bottom");
    }
}
