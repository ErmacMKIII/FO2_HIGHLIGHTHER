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
public class CustomObj implements Obj {

    private final Color color;
    private boolean labeled = false;

    public CustomObj(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor(Configuration config) {
        return color;
    }

    @Override
    public boolean isLabeled() {
        return labeled;
    }

    @Override
    public void setLabeled(boolean labeled) {
        this.labeled = labeled;
    }

}
