package ozmud.world;


public class SkillValue {


    private static final int EXP_ORDER = 2;

    private final Skill skill;

    private int level = 1;

    private int exp = 0;


    public SkillValue(Skill skill) {
        this.skill = skill;
    }


    public Skill getSkill() {
        return skill;
    }


    public int getLevel() {
        return level;
    }


    public double getProgress() {
        return (double) exp / getExpNeededForNextLevel();
    }


    public boolean addExperience(int exp) {
        int oldLevel = level;
        if (level < Skill.MAX_LEVEL) {
            this.exp += exp;
            int goal = getExpNeededForNextLevel();
            if (exp >= goal) {
                level++;
                if (level == Skill.MAX_LEVEL) {
                    exp = 0;
                } else {
                    addExperience(exp - goal);
                }
            }
        }
        return (level > oldLevel);
    }


    private int getExpNeededForNextLevel() {
        return (int) Math.ceil(Math.pow(level, EXP_ORDER)
                / Math.pow(Skill.MAX_LEVEL, EXP_ORDER) * skill.getCost());
    }


}
