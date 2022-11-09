package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;

public class AndGoal implements Goal, ComplexGoal {
    private Goal goal1;
    private Goal goal2;

    public AndGoal(Goal goal1, Goal goal2) {
        this.goal1 = goal1;
        this.goal2 = goal2;
    }

    @Override
    public boolean achieved(Game game) {
        return goal1.achieved(game) && goal2.achieved(game);
    }

    @Override
    public String toString(Game game) {
        if (this.achieved(game)) return "";
        return "(" + goal1.toString(game) + " AND " + goal2.toString(game) + ")";
    }

    @Override
    public String goalType() {
        return "AND";
    }

    @Override
    public List<Goal> getSubgoal() {
        return List.of(goal1, goal2);
    }
}
