package dungeonmania.entities.playerState;
public class BaseState implements PlayerState {
    @Override
    public boolean isInvincible() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return false;
    }
}
