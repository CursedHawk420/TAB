package me.neznamy.tab.platforms.krypton;

import lombok.NonNull;
import me.neznamy.tab.shared.chat.IChatBaseComponent;
import me.neznamy.tab.shared.platform.PlatformScoreboard;
import org.kryptonmc.api.scoreboard.Objective;
import org.kryptonmc.api.scoreboard.ObjectiveRenderType;
import org.kryptonmc.api.scoreboard.Scoreboard;
import org.kryptonmc.api.scoreboard.Team;
import org.kryptonmc.api.scoreboard.Visibility;
import org.kryptonmc.api.scoreboard.criteria.Criteria;

import java.util.Collection;

public class KryptonScoreboard extends PlatformScoreboard<KryptonTabPlayer> {

    public KryptonScoreboard(@NonNull KryptonTabPlayer player) {
        super(player);
        // Create a new, blank scoreboard for each player to avoid conflicts
        player.getPlayer().showScoreboard(Scoreboard.create());
    }

    @Override
    public void setDisplaySlot(@NonNull DisplaySlot slot, @NonNull String objectiveName) {
        Objective objective = getScoreboard().getObjective(objectiveName);
        if (objective != null) getScoreboard().updateSlot(objective, convertDisplaySlot(slot));
    }

    private org.kryptonmc.api.scoreboard.DisplaySlot convertDisplaySlot(DisplaySlot slot) {
        return org.kryptonmc.api.scoreboard.DisplaySlot.values()[slot.ordinal()];
    }

    @Override
    public void registerObjective0(@NonNull String objectiveName, @NonNull String title, boolean hearts) {
        Objective ignored = getScoreboard().createObjectiveBuilder()
                .name(objectiveName)
                .criterion(Criteria.DUMMY.get())
                .displayName(IChatBaseComponent.optimizedComponent(title).toAdventureComponent())
                .renderType(hearts ? ObjectiveRenderType.HEARTS : ObjectiveRenderType.INTEGER)
                .buildAndRegister();
    }

    @Override
    public void unregisterObjective0(@NonNull String objectiveName) {
        Objective objective = getScoreboard().getObjective(objectiveName);
        if (objective != null) getScoreboard().removeObjective(objective);
    }

    @Override
    public void updateObjective0(@NonNull String objectiveName, @NonNull String title, boolean hearts) {
        Objective objective = getScoreboard().getObjective(objectiveName);
        if (objective == null) return;
        objective.setDisplayName(IChatBaseComponent.optimizedComponent(title).toAdventureComponent());
        objective.setRenderType(hearts ? ObjectiveRenderType.HEARTS : ObjectiveRenderType.INTEGER);
    }

    @Override
    public void registerTeam0(@NonNull String name, @NonNull String prefix, @NonNull String suffix, @NonNull NameVisibility visibility, @NonNull CollisionRule collision, @NonNull Collection<String> players, int options) {
        Team team = getScoreboard().createTeamBuilder(name)
                .displayName(IChatBaseComponent.optimizedComponent(name).toAdventureComponent())
                .prefix(IChatBaseComponent.optimizedComponent(prefix).toAdventureComponent())
                .suffix(IChatBaseComponent.optimizedComponent(suffix).toAdventureComponent())
                .friendlyFire((options & 0x01) != 0)
                .canSeeInvisibleMembers((options & 0x02) != 0)
                .collisionRule(org.kryptonmc.api.scoreboard.CollisionRule.valueOf(collision.name()))
                .nameTagVisibility(Visibility.valueOf(visibility.name()))
                .buildAndRegister();
        for (String member : players) {
            team.addMember(IChatBaseComponent.optimizedComponent(member).toAdventureComponent());
        }
    }

    @Override
    public void unregisterTeam0(@NonNull String name) {
        Team team = getScoreboard().getTeam(name);
        if (team != null) getScoreboard().removeTeam(team);
    }

    @Override
    public void updateTeam0(@NonNull String name, @NonNull String prefix, @NonNull String suffix, @NonNull NameVisibility visibility, @NonNull CollisionRule collision, int options) {
        Team team = getScoreboard().getTeam(name);
        if (team == null) return;
        team.setDisplayName(IChatBaseComponent.optimizedComponent(name).toAdventureComponent());
        team.setPrefix(IChatBaseComponent.optimizedComponent(prefix).toAdventureComponent());
        team.setSuffix(IChatBaseComponent.optimizedComponent(suffix).toAdventureComponent());
        team.setAllowFriendlyFire((options & 0x01) != 0);
        team.setCanSeeInvisibleMembers((options & 0x02) != 0);
        team.setCollisionRule(org.kryptonmc.api.scoreboard.CollisionRule.valueOf(collision.name()));
        team.setNameTagVisibility(Visibility.valueOf(visibility.name()));
    }

    @Override
    public void setScore0(@NonNull String objectiveName, @NonNull String playerName, int score) {
        Objective objective = getScoreboard().getObjective(objectiveName);
        if (objective != null) objective.getOrCreateScore(IChatBaseComponent.optimizedComponent(playerName).toAdventureComponent()).setScore(score);
    }

    @Override
    public void removeScore0(@NonNull String objectiveName, @NonNull String playerName) {
        Objective objective = getScoreboard().getObjective(objectiveName);
        if (objective != null) objective.removeScore(IChatBaseComponent.optimizedComponent(playerName).toAdventureComponent());
    }

    private Scoreboard getScoreboard() {
        return player.getPlayer().getScoreboard();
    }
}