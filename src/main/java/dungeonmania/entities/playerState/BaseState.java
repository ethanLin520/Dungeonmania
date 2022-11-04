package dungeonmania.entities.playerState;

import dungeonmania.entities.Player;

public class BaseState implements PlayerState {
    private Player player;

    public BaseState(Player player) {
        this.player = player;
    }

    @Override
    public boolean isInvincible() {
        return false;
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
