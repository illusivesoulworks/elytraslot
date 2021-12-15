package top.theillusivec4.curiouselytra.integration;

import com.gildedgames.aether.common.item.accessories.cape.CapeItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.caelus.api.RenderElytraEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.util.ICuriosHelper;

public class AetherCape implements ICustomElytra {

  @Override
  public boolean renderElytra(ItemStack stack, RenderElytraEvent evt) {
    ICuriosHelper curios = CuriosApi.getCuriosHelper();
    PlayerEntity player = evt.getPlayer();
    return curios.findEquippedCurio(curio -> curio.getItem() instanceof CapeItem, player)
        .map(triple -> {
              curios.getCuriosHandler(player)
                  .ifPresent(capes -> capes.getStacksHandler(triple.getLeft())
                      .ifPresent(capeStacks -> {
                        CapeItem cape = (CapeItem) triple.getRight().getItem();
                        if (cape.getCapeTexture() != null &&
                            capeStacks.getRenders().get(triple.getMiddle())) {
                          evt.setResourceLocation(cape.getCapeTexture());
                        }
                      }));
              return true;
            }
        ).orElse(false);
  }
}
