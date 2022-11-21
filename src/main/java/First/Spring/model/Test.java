package First.Spring.model;

public class Test {

    private String score;
    private String overall;

    public Test(){

    }
    public Test(String score,String overall){
        this.score =score;
        this.overall = overall;
    }

    public String getScore() {
        return score;
    }

    public String getOverall() {
        return overall;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public void setOverall(String overall) {
        this.overall = overall;
    }

    @Override
    public String toString() {
        return "Test{" +
                "score='" + score + '\'' +
                ", overall='" + overall + '\'' +
                '}';
    }
}
