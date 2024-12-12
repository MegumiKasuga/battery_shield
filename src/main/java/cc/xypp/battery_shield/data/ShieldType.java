package cc.xypp.battery_shield.data;

import net.minecraft.util.StringRepresentable;

import javax.annotation.Nullable;

public enum ShieldType implements StringRepresentable {
    RAW,
    SHIELD_WHITE,
    SHIELD_BLUE,
    SHIELD_PERP,
    SHIELD_RED;

    @Override
    public String getSerializedName() {
        return switch (this) {
            case RAW -> "raw";
            case SHIELD_RED -> "shield_red";
            case SHIELD_BLUE -> "shield_blue";
            case SHIELD_PERP -> "shield_perp";
            case SHIELD_WHITE -> "shield_white";
        };
    }

    public static @Nullable ShieldType fromString(String str) {
        return switch (str) {
            case "raw" -> RAW;
            case "shield_red" -> SHIELD_RED;
            case "shield_blue" -> SHIELD_BLUE;
            case "shield_perp" -> SHIELD_PERP;
            case "shield_white" -> SHIELD_WHITE;
            default -> null;
        };
    }
}
