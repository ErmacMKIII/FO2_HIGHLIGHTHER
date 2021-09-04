/*
 * Copyright (C) 2021 Alexander Stojanovich <coas91@rocketmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package rs.alexanderstojanovich.fo2h.highlighter;

import java.awt.Color;

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public enum PredefObj implements Obj {
    UNUSED,
    IMPLANTS, T4_WEAPONS, T4_ARMORS, T4_AMMO, T4_ITEMS,
    T3_WEAPONS, T3_ARMORS, T3_AMMO, T3_ITEMS,
    T2_WEAPONS, T2_ARMORS, T2_AMMO, T2_ITEMS,
    T1_WEAPONS, T1_ARMORS, T1_AMMO, T1_ITEMS,
    T0_WEAPONS, T0_ARMORS, T0_AMMO, T0_ITEMS,
    RESOURCES, BOOKS, ORES, COLLECTION, CONTAINERS;

    private boolean labeled = false;

    @Override
    public boolean isLabeled() {
        return labeled;
    }

    @Override
    public void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

    @Override
    public Color getColor(Configuration config) {
        Color result = null;
        switch (this) {
            case IMPLANTS:
                result = config.getImplantColor();
                break;
            case T4_WEAPONS:
            case T4_ARMORS:
            case T4_AMMO:
            case T4_ITEMS:
                result = config.getT4Color();
                break;
            case T3_WEAPONS:
            case T3_ARMORS:
            case T3_AMMO:
            case T3_ITEMS:
                result = config.getT3Color();
                break;
            case T2_WEAPONS:
            case T2_ARMORS:
            case T2_AMMO:
            case T2_ITEMS:
                result = config.getT2Color();
                break;
            case T1_WEAPONS:
            case T1_ARMORS:
            case T1_AMMO:
            case T1_ITEMS:
                result = config.getT1Color();
                break;
            case T0_WEAPONS:
            case T0_ARMORS:
            case T0_AMMO:
            case T0_ITEMS:
                result = config.getT0Color();
                break;
            case BOOKS:
                result = config.getBookColor();
                break;
            case ORES:
                result = config.getOreColor();
                break;
            case RESOURCES:
                result = config.getResourcesColor();
                break;
            case CONTAINERS:
                result = config.getContainerColor();
                break;
            case COLLECTION:
                result = config.getCollectionColor();
                break;
            case UNUSED:
            default:
                result = config.getUnusedColor();
                break;
        }

        return result;
    }

}
