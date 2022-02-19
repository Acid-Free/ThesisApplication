public class ComparisonData{
    private String featureName;
    private float upperRange;
    private float lowerRange;
    private int weight;
    private float terrain1Result;
    private float terrain2Result;
    private float likeliness;

    public ComparisonData(String featureName) {
        setFeatureName(featureName);
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
}
