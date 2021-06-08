package top.theillusivec4.elytratrinket.client;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketsApi;
import java.util.List;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

public class ElytraTrinketRenderer<T extends LivingEntity, M extends EntityModel<T>>
    extends ElytraFeatureRenderer<T, M> {

  private static final Identifier SKIN = new Identifier("textures/entity/elytra.png");
  private final ElytraEntityModel<T> elytra;

  public ElytraTrinketRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
    super(context, loader);
    this.elytra = new ElytraEntityModel<>(loader.getModelPart(EntityModelLayers.ELYTRA));
  }

  public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i,
                     T livingEntity, float f, float g, float h, float j, float k, float l) {
    TrinketsApi.getTrinketComponent(livingEntity).ifPresent(component -> {
      List<Pair<SlotReference, ItemStack>> res = component.getEquipped(stack -> stack.isOf(Items.ELYTRA));

      if (res.size() == 0) {
        return;
      }
      Identifier identifier4;
      if (livingEntity instanceof AbstractClientPlayerEntity abstractClientPlayerEntity) {
        if (abstractClientPlayerEntity.canRenderElytraTexture() &&
            abstractClientPlayerEntity.getElytraTexture() != null) {
          identifier4 = abstractClientPlayerEntity.getElytraTexture();
        } else if (abstractClientPlayerEntity.canRenderCapeTexture() &&
            abstractClientPlayerEntity.getCapeTexture() != null &&
            abstractClientPlayerEntity.isPartVisible(
                PlayerModelPart.CAPE)) {
          identifier4 = abstractClientPlayerEntity.getCapeTexture();
        } else {
          identifier4 = SKIN;
        }
      } else {
        identifier4 = SKIN;
      }
      matrixStack.push();
      matrixStack.translate(0.0D, 0.0D, 0.125D);
      this.getContextModel().copyStateTo(this.elytra);
      this.elytra.setAngles(livingEntity, f, g, j, k, l);
      VertexConsumer vertexConsumer = ItemRenderer
          .getArmorGlintConsumer(vertexConsumerProvider,
              RenderLayer.getArmorCutoutNoCull(identifier4), false, res.get(0).getRight().hasGlint());
      this.elytra
          .render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F,
              1.0F);
      matrixStack.pop();
    });
  }
}
