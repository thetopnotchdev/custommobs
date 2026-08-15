package dev.topnotch.custommobs.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {
    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public boolean isOnCooldown(UUID playerId, String abilityId) {
        return remainingMillis(playerId, abilityId) > 0;
    }

    public long remainingMillis(UUID playerId, String abilityId) {
        Map<String, Long> playerCooldowns = cooldowns.get(playerId);
        if (playerCooldowns == null) return 0;
        long until = playerCooldowns.getOrDefault(abilityId, 0L);
        long remaining = until - System.currentTimeMillis();
        if (remaining <= 0) {
            playerCooldowns.remove(abilityId);
            return 0;
        }
        return remaining;
    }

    public void start(UUID playerId, String abilityId, long seconds) {
        cooldowns.computeIfAbsent(playerId, ignored -> new ConcurrentHashMap<>())
                .put(abilityId, System.currentTimeMillis() + seconds * 1000L);
    }

    public long remainingSeconds(UUID playerId, String abilityId) {
        return (long) Math.ceil(remainingMillis(playerId, abilityId) / 1000.0);
    }

    public void clear(UUID playerId) {
        cooldowns.remove(playerId);
    }
}
