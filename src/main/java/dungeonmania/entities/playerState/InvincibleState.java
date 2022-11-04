package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public class InvincibleState implements PlayerState {
    private Player player;

    public InvincibleState(Player player) {
        this.player = player;
    }

    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void transitionInvisible() {
        player.changeState(new InvisibleState(player));
    }

    @Override
    public void transitionInvincible() {
        player.changeState(new InvincibleState(player));
    }

    @Override
    public void transitionBase() {
        return;
    }
}
