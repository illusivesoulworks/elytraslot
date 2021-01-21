/*
 * Copyright (c) 2019-2020 C4
 *
 * This file is part of Curious Elytra, a mod made for Minecraft.
 *
 * Curious Elytra is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Curious Elytra is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Curious Elytra.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.curiouselytra.loader;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import top.theillusivec4.caelus.api.CaelusApi;
import top.theillusivec4.caelus.api.event.RenderElytraCallback;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class ClientCuriousElytraMod implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    RenderElytraCallback.EVENT.register(
        ((playerEntity, renderElytraInfo) -> CuriosApi.getCuriosHelper()
            .getCuriosHandler(playerEntity).ifPresent(handler -> {
              Set<String> tags = CuriosApi.getCuriosHelper().getCurioTags(Items.ELYTRA);
              AtomicBoolean flag = new AtomicBoolean(false);

              for (String id : tags) {
                handler.getStacksHandler(id).ifPresent(stacksHandler -> {
                  IDynamicStackHandler stackHandler = stacksHandler.getStacks();

                  for (int i = 0; i < stackHandler.size(); i++) {
                    ItemStack stack = stackHandler.getStack(i);
                    Identifier itemId = Registry.ITEM.getId(stack.getItem());

                    if (CaelusApi.getInstance().isElytra(stack.getItem()) && stacksHandler.getRenders().get(i)) {
                      renderElytraInfo.activateRender();

                      if (itemId.equals(new Identifier("enderitemod:enderite_elytra_seperated"))) {
                        renderElytraInfo.setTextureOverride(new Identifier("minecraft:textures/entity/enderite_elytra.png"));
                      } else if (itemId.equals(new Identifier("netherite_plus:netherite_elytra"))) {
                        renderElytraInfo.setTextureOverride(new Identifier("netherite_plus:textures/entity/netherite_elytra.png"));
                      }

                      if (stack.hasGlint()) {
                        renderElytraInfo.activateGlow();
                      }
                      flag.set(true);
                      return;
                    }
                  }
                });

                if (flag.get()) {
                  return;
                }
              }
            })));
  }
}
