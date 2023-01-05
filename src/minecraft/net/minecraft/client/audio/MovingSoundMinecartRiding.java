package net.minecraft.client.audio;

import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class MovingSoundMinecartRiding extends MovingSound
{
    private final EntityPlayer field_147672_k;
    private final EntityMinecart field_147671_l;
    private static final String __OBFID = "CL_00001119";

    public MovingSoundMinecartRiding(EntityPlayer p_i45106_1_, EntityMinecart p_i45106_2_)
    {
        super(new ResourceLocation("minecraft:minecart.inside"));
        this.field_147672_k = p_i45106_1_;
        this.field_147671_l = p_i45106_2_;
        this.attenuationType = ISound.AttenuationType.NONE;
        this.repeat = true;
        this.repeatDelay = 0;
    }

    public void update()
    {
        if (!this.field_147671_l.isDead && this.field_147672_k.isRiding() && this.field_147672_k.ridingEntity == this.field_147671_l)
        {
            float var1 = MathHelper.sqrt_double(this.field_147671_l.motionX * this.field_147671_l.motionX + this.field_147671_l.motionZ * this.field_147671_l.motionZ);

            if ((double)var1 >= 0.01D)
            {
                this.volume = 0.0F + MathHelper.clamp_float(var1, 0.0F, 1.0F) * 0.75F;
            }
            else
            {
                this.volume = 0.0F;
            }
        }
        else
        {
            this.donePlaying = true;
        }
    }
}
