package top.theillusivec4.curiouselytra.loader;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

                    if (stack.getItem() == Items.ELYTRA && stacksHandler.getRenders().get(i)) {
                      renderElytraInfo.activateRender();

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
