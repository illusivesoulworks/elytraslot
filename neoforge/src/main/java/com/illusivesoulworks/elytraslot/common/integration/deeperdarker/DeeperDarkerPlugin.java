package com.illusivesoulworks.elytraslot.common.integration.deeperdarker;

import com.illusivesoulworks.elytraslot.ElytraSlotConstants;
import com.kyanite.deeperdarker.DeeperDarker;
import com.kyanite.deeperdarker.DeeperDarkerConfig;
import com.kyanite.deeperdarker.content.DDItems;
import com.kyanite.deeperdarker.network.SoulElytraClientPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import top.theillusivec4.curios.api.event.CurioChangeEvent;

public class DeeperDarkerPlugin {

  public static void setup(IEventBus eventBus) {
    eventBus.addListener(DeeperDarkerPlugin::registerPayloads);
    NeoForge.EVENT_BUS.addListener(DeeperDarkerPlugin::onCurioChange);
  }

  private static void registerPayloads(final RegisterPayloadHandlersEvent evt) {
    evt.registrar(ElytraSlotConstants.MOD_ID)
        .playToServer(SoulElytraBoostPayload.TYPE, SoulElytraBoostPayload.STREAM_CODEC,
            SoulElytraBoostPayload::handle);
  }

  private static void onCurioChange(final CurioChangeEvent evt) {

    if (!evt.getTo().is(DDItems.SOUL_ELYTRA.get()) || evt.getFrom().is(DDItems.SOUL_ELYTRA.get())) {
      return;
    }
    if (evt.getEntity() instanceof ServerPlayer player) {
      PacketDistributor.sendToPlayer(player, new SoulElytraClientPacket(true));
    }
  }

  public static void tick(Level level, Entity entity) {

    if (level.isClientSide() && entity instanceof Player player) {

      if (player.getCooldowns().isOnCooldown(DDItems.SOUL_ELYTRA.get())) {
        float percent = player.getCooldowns().getCooldownPercent(DDItems.SOUL_ELYTRA.get(), 0);
        player.displayClientMessage(
            Component.translatable("item." + DeeperDarker.MOD_ID + ".soul_elytra.cooldown",
                (int) Math.ceil(percent * DeeperDarkerConfig.CONFIG.soulElytraCooldown.getAsInt() / 20)), true);
      }
    }
  }
}
