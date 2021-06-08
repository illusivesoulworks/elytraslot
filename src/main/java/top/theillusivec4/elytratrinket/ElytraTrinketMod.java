/*
 * Copyright (c) 2019-2021 C4
 *
 * This file is part of Elytra Trinket, a mod made for Minecraft.
 *
 * Elytra Trinket is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Elytra Trinket is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Elytra Trinket.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.elytratrinket;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;

public class ElytraTrinketMod implements ModInitializer {

  @Override
  public void onInitialize() {
    TrinketsApi.registerTrinket(Items.ELYTRA, new ElytraTrinket());
  }
}
