package dungeonmania.entities.playerState;

public class InvisibleState implements PlayerState {

    @Override
    public boolean isInvincible() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return true;
    }
}
