package tk.meowmc.doorknockmod.mixin;

import net.minecraft.block.TrapdoorBlock;
import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.BlockStateParticleEffect;

@Mixin(TrapdoorBlock.class)
public abstract class TrapDoorKnockMixins extends Block {

    public TrapDoorKnockMixins(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World w, BlockPos blockPos, PlayerEntity p) {
        if (!w.isClient) {
            ServerWorld world = (ServerWorld)w;
            ServerPlayerEntity player = (ServerPlayerEntity)p;
            HitResult hit = player.raycast(player.isCreative() ? 5.0F : 4.5F, 1F, false);

            Vec3d hitPos = hit.getPos();
            Vec3d pos = Vec3d.ofCenter(blockPos);

            double xDist = Math.abs(pos.x - hitPos.x);
            double yDist = Math.abs(pos.y - hitPos.y);
            double zDist = Math.abs(pos.z - hitPos.z);

            int x = 0;
            if (xDist <= 0.2) x++;
            if (yDist <= 0.2) x++;
            if (zDist <= 0.2) x++;

            if (x == 2) {
                world.playSound(null, hitPos.x, hitPos.y, hitPos.z, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1F, 1F);
                world.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, state), hitPos.x, hitPos.y, hitPos.z, 10, 0.1, 0.1, 0.1, 0.0);
            }
        }
    }
}
