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

/**
 *
 * @author Alexander Stojanovich <coas91@rocketmail.com>
 */
public enum Obj {
    UNUSED,
    IMPLANTS, T4_WEAPONS, T4_ARMORS, T4_AMMO, T4_ITEMS,
    T3_WEAPONS, T3_ARMORS, T3_AMMO, T3_ITEMS,
    T2_WEAPONS, T2_ARMORS, T2_AMMO, T2_ITEMS,
    T1_WEAPONS, T1_ARMORS, T1_AMMO, T1_ITEMS,
    T0_WEAPONS, T0_ARMORS, T0_AMMO, T0_ITEMS,
    RESOURCES, BOOKS, ORES, CONTAINERS;

    private boolean labeled = false;

    public boolean isLabeled() {
        return labeled;
    }

    public void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

}
