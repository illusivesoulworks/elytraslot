package top.theillusivec4.curiouselytra.client;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.caelus.api.RenderCapeEvent;
import top.theillusivec4.curiouselytra.CuriousElytraMod;

public class CuriousElytraClientMod {

  public static void setup() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(CuriousElytraClientMod::addLayers);
    MinecraftForge.EVENT_BUS.addListener(CuriousElytraClientMod::renderCape);
  }

  private static void addLayers(final EntityRenderersEvent.AddLayers evt) {
    addPlayerLayer(evt, "default");
    addPlayerLayer(evt, "slim");
    addEntityLayer(evt, EntityType.ARMOR_STAND);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void addPlayerLayer(EntityRenderersEvent.AddLayers evt, String skin) {
    EntityRenderer<? extends Player> renderer = evt.getSkin(skin);

    if (renderer instanceof LivingEntityRenderer livingRenderer) {
      livingRenderer.addLayer(new CurioElytraLayer(livingRenderer, evt.getEntityModels()));
    }
  }

  private static <T extends LivingEntity, M extends HumanoidModel<T>, R extends LivingEntityRenderer<T, M>> void addEntityLayer(
      EntityRenderersEvent.AddLayers evt, EntityType<? extends T> entityType) {
    R renderer = evt.getRenderer(entityType);

    if (renderer != null) {
      renderer.addLayer(new CurioElytraLayer<>(renderer, evt.getEntityModels()));
    }
  }

  private static void renderCape(final RenderCapeEvent evt) {

    if (CuriousElytraMod.getElytraProvider(evt.getPlayer(), false).isPresent()) {
      evt.setCanceled(true);
    }
  }
}
