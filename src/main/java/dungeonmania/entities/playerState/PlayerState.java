package dungeonmania.entities.playerState;

public interface PlayerState {
    public boolean isInvincible();
    public boolean isInvisible();
    public void transitionInvisible();
    public void transitionInvincible();
    public void transitionBase();
}
