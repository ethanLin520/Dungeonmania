package dungeonmania.entities.playerState;

public class InvincibleState implements PlayerState {
    @Override
    public boolean isInvincible() {
        return true;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }
}
