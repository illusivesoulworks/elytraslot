package com.illusivesoulworks.elytraslot.common.integration.deeperdarker;

import com.illusivesoulworks.elytraslot.ElytraSlotConstants;
import com.illusivesoulworks.elytraslot.platform.Services;
import com.kyanite.deeperdarker.DeeperDarker;
import com.kyanite.deeperdarker.DeeperDarkerConfig;
import com.kyanite.deeperdarker.content.DDItems;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SoulElytraBoostPayload(boolean bool) implements CustomPacketPayload {

  public static final StreamCodec<ByteBuf, SoulElytraBoostPayload> STREAM_CODEC =
      StreamCodec.composite(
          ByteBufCodecs.BOOL, SoulElytraBoostPayload::bool,
          SoulElytraBoostPayload::new
      );

  public static final ResourceLocation ID =
      ResourceLocation.fromNamespaceAndPath(ElytraSlotConstants.MOD_ID, "soul_elytra_boost");
  public static final Type<SoulElytraBoostPayload> TYPE = new Type<>(ID);

  @Nonnull
  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public void handle(IPayloadContext context) {
    context.enqueueWork(() -> {
      Player player = context.player();
      Level level = player.level();

      if (DeeperDarkerConfig.CONFIG.soulElytraCooldown.getAsInt() == -1) {
        player.displayClientMessage(
            Component.translatable("item." + DeeperDarker.MOD_ID + ".soul_elytra.no_cooldown"),
            true);
        return;
      }

      if (player.isFallFlying() && Services.ELYTRA.getEquipped(player).is(DDItems.SOUL_ELYTRA) &&
          !player.getCooldowns().isOnCooldown(DDItems.SOUL_ELYTRA.get())) {
        FireworkRocketEntity rocket =
            new FireworkRocketEntity(level, new ItemStack(Items.FIREWORK_ROCKET), player);
        level.addFreshEntity(rocket);
        player.getCooldowns()
            .addCooldown(DDItems.SOUL_ELYTRA.get(), DeeperDarkerConfig.CONFIG.soulElytraCooldown.getAsInt());
      }
    });
  }
}
