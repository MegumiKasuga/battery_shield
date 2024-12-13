package cc.xypp.battery_shield.utils;

import cc.xypp.battery_shield.Config;
import cc.xypp.battery_shield.api.IDamageSourceA;
import cc.xypp.battery_shield.data.DamageNumberType;
import cc.xypp.battery_shield.data.ShieldType;
import cc.xypp.battery_shield.helper.AssetsManager;
import cc.xypp.battery_shield.items.Register;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ShieldUtil {

    public static final ResourceLocation CHESTPLATES = new ResourceLocation("forge","armors/chestplates");
    public static ShieldType getShieldTypeByMaxValue(float value) {
        if (value <= 0) return ShieldType.RAW;
        for (int i = 4; i > 0; i--) {
            if (value > ShieldType.values()[i].getMaxValue())
                return ShieldType.values()[i];
        }
        return ShieldType.RAW;
    }
    public  static ItemStack getShieldArmorNonCore (LivingEntity entity){
        for (ItemStack armorSlot : entity.getArmorSlots()) {
            if(armorSlot.is(ItemTags.create(CHESTPLATES))) {
                return armorSlot;
            }
        }
        return null;
    }
    public static @Nullable ItemStack getShieldArmor(LivingEntity entity) {
        for (ItemStack armorSlot : entity.getArmorSlots()) {
            if(!armorSlot.is(ItemTags.create(CHESTPLATES)))
                continue;
            CompoundTag tag = armorSlot.getTag();
            if (tag == null || !tag.contains("shield_type")) return null;
            ShieldType type = ShieldType.fromString(tag.getString("shield_type"));
//            if(!tag.contains("shield_type")) {
                // TODO: Changed here from FLOAT value to RESOURCE_LOCATION shield type.
                // tag.putFloat("shield_max", getMaxShieldByType(ShieldType.values()[tag.getInt("core_level")]));
//                tag.putString("shield_type", type.getSerializedName());
//            }
            if(!tag.contains("shield_value") && type != null) {
                tag.putFloat("shield_value", type.getMaxValue());
            }
            return armorSlot;
        }
        return null;
    }
    public static float getMaxShieldByType(ShieldType type) {
        return type.getMaxValue();
    }
    public static String getShieldIdByType(ShieldType type) {
        return type.getSerializedName();
    }

    public static AssetsManager.ImageAssets getShieldTypeByValue(float value) {
        if (value > Config.shield_pre_level*4) {
            return AssetsManager.SHIELD_RED;
        } else if (value > Config.shield_pre_level*3) {
            return AssetsManager.SHIELD_PERPLE;
        } else if (value > Config.shield_pre_level*2) {
            return AssetsManager.SHIELD_BLUE;
        } else {
            return AssetsManager.SHIELD_WHITE;
        }
    }
    public static int getColor(DamageNumberType type) {
        return switch (type) {
            case RAW -> 0x5d4037;
            case NORMAL -> 0xf44336;
            case SHIELD_WHITE -> 0xcfd8dc;
            case SHIELD_BLUE -> 0x2196f3;
            case SHIELD_PERP -> 0xab47bc;
            case SHIELD_RED -> 0xff1744;
            default -> 0xFFFFFF;
        };
    }
    public static DamageNumberType getTypeBySourceValue(DamageSource source, float value) {
        if (((IDamageSourceA) source).battery_shield$isByBatteryShield()) {
            if (value > Config.shield_pre_level*4) {
                return DamageNumberType.SHIELD_RED;
            } else if (value > Config.shield_pre_level*3) {
                return DamageNumberType.SHIELD_PERP;
            } else if (value > Config.shield_pre_level*2) {
                return DamageNumberType.SHIELD_BLUE;
            } else {
                return DamageNumberType.SHIELD_WHITE;
            }
        } else {
            return DamageNumberType.RAW;
        }
    }
    @Nullable
    public static AssetsManager.ImageAssets getIconByType(DamageNumberType type) {
        return switch (type) {
            case SHIELD_WHITE -> AssetsManager.SHIELD_ICON_WHITE;
            case SHIELD_BLUE -> AssetsManager.SHIELD_ICON_BLUE;
            case SHIELD_PERP -> AssetsManager.SHIELD_ICON_PERPLE;
            case SHIELD_RED -> AssetsManager.SHIELD_ICON_RED;
            default -> null;
        };
    }
    public static int getMaxShieldValue(LivingEntity entity) {
        return Mth.clamp((int)Math.ceil(entity.getHealth()/10) * 5, 0, 50);
    }
}
