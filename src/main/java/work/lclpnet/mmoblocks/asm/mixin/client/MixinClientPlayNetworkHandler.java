package work.lclpnet.mmoblocks.asm.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.mmoblocks.entity.StoolEntity;
import work.lclpnet.mmoblocks.module.StoolsModule;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private ClientWorld world;

    @Inject(
            method = "onEntitySpawn(Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;)V",
            at = @At("TAIL")
    )
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci) {
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();
        EntityType<?> type = packet.getEntityTypeId();
        if (type == StoolsModule.stoolEntity) {
            StoolEntity entity = new StoolEntity(type, this.world);

            int i = packet.getId();
            entity.updateTrackedPosition(x, y, z);
            entity.refreshPositionAfterTeleport(x, y, z);
            entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
            entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
            entity.setEntityId(i);
            entity.setUuid(packet.getUuid());
            this.world.addEntity(i, entity);
        }
    }

}
