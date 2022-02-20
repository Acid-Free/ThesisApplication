public class ComparisonData{
    private String featureName;
    private float upperRange;
    private float lowerRange;
    private int weight;
    private float terrain1Result;
    private float terrain2Result;
    private float likeliness;

    public float getTerrain1Result(){
        return terrain1Result;
    }

    public void setTerrain1Result(float terrain1Result){
        this.terrain1Result = terrain1Result;
    }

    public float getTerrain2Result(){
        return terrain2Result;
    }

    public void setTerrain2Result(float terrain2Result){
        this.terrain2Result = terrain2Result;
    }

    public float getLikeliness(){
        return likeliness;
    }

    public void setLikeliness(float likeliness){
        this.likeliness = likeliness;
    }

    public ComparisonData(String featureName) {
        setFeatureName(featureName);
    }

    public ComparisonData(String featureName,float lowerRangeValue,float upperRangeValue,int weight) {
        this(featureName);
        setLowerRange(lowerRangeValue);
        setUpperRange(upperRangeValue);
        setWeight(weight);
    }

    public String getFeatureName(){
        return featureName;
    }

    public void setFeatureName(String featureName){
        this.featureName = featureName;
    }

    public float getUpperRange(){
        return upperRange;
    }

    public void setUpperRange(float upperRange){
        this.upperRange = upperRange;
    }

    public float getLowerRange(){
        return lowerRange;
    }

    public void setLowerRange(float lowerRange){
        this.lowerRange = lowerRange;
    }

    public int getWeight(){
        return weight;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    @Override
    public String toString(){
        return "ComparisonData{" +
                "featureName='" + featureName + '\'' +
                ", lowerRange=" + lowerRange +
                ", upperRange=" + upperRange +
                ", weight=" + weight +
                ", terrain1Result=" + terrain1Result +
                ", terrain2Result=" + terrain2Result +
                ", likeliness=" + likeliness +
                '}';
    }
}
