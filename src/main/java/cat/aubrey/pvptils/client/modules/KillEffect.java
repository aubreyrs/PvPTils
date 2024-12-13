package cat.aubrey.pvptils.client.modules;

import cat.aubrey.pvptils.client.Config;
import cat.aubrey.pvptils.client.Pvptils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;

public class KillEffect {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static int fakeEntityIdCounter = Integer.MAX_VALUE / 2;

    public static void triggerKillEffect(Entity entity) {
//        Pvptils.LOGGER.info("triggerKillEffect called for entity: {}", entity.getName().getString());
        if (!Config.get().isLightningKillEffectEnabled()) return;
        if (client.world == null || client.getNetworkHandler() == null) return;
        LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, client.world);
        double x = entity.getX();
        double y = entity.getY() - 1;
        double z = entity.getZ();
        lightning.setPos(x, y, z);
        int entityId = fakeEntityIdCounter--;
        lightning.setId(entityId);
        UUID uuid = UUID.randomUUID();
        lightning.setUuid(uuid);
        EntitySpawnS2CPacket packet = new EntitySpawnS2CPacket(entityId, uuid, x, y, z, 0.0f, 0.0f, EntityType.LIGHTNING_BOLT, 0, Vec3d.ZERO, 0.0);
        ClientPlayNetworkHandler networkHandler = client.getNetworkHandler();
//        Pvptils.LOGGER.info("Sending fake EntitySpawnS2CPacket to client for lightning at ({}, {}, {}).", x, y, z);
        networkHandler.onEntitySpawn(packet);
        client.world.playSound(x, y, z, SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 10.0F, 0.8F + client.world.random.nextFloat() * 0.2F, false);
        client.world.playSound(x, y, z, SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, SoundCategory.WEATHER, 5.0F, 0.8F + client.world.random.nextFloat() * 0.2F, false);
    }
}
