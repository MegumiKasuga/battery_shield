package cc.xypp.battery_shield.items.ShieldCore;

import cc.xypp.battery_shield.data.ShieldType;

import javax.annotation.Nullable;

public interface IShieldCore {
    ShieldType getCoreLevel();
    boolean isRequired(@Nullable ShieldType type);
}
