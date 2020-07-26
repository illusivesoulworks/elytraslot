package top.theillusivec4.curiouselytra.loader;

import nerdhub.cardinal.components.api.event.ItemComponentCallbackV2;
import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Items;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosComponent;
import top.theillusivec4.curios.api.SlotTypeInfo.BuildScheme;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curiouselytra.core.CurioElytra;

public class CuriousElytraMod implements ModInitializer {

  @Override
  public void onInitialize() {
    CuriosApi.enqueueSlotType(BuildScheme.REGISTER, SlotTypePreset.BACK.getInfoBuilder().build());
    ItemComponentCallbackV2.event(Items.ELYTRA).register(
        ((item, itemStack, componentContainer) -> componentContainer
            .put(CuriosComponent.ITEM, new CurioElytra(itemStack))));
  }
}
