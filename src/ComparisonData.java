// Class serving as data container for the comparison data of two terrains
public class ComparisonData{
    private String featureName;
    private float upperRange;
    private float lowerRange;
    private int weight;
    private float terrain1Result;
    private float terrain2Result;
    private float likeliness;

    // First constructor
    public ComparisonData(String featureName) {
        setFeatureName(featureName);
    }

    // Second constructor with more parameters
    public ComparisonData(String featureName, float lowerRangeValue, float upperRangeValue, int weight) {
        this(featureName);
        setLowerRange(lowerRangeValue);
        setUpperRange(upperRangeValue);
        setWeight(weight);
    }

    // Getter function; returns the first terrain comparison result
    public float getTerrain1Result(){
        return terrain1Result;
    }

    // Setter function; assigns the first terrain comparison result
    public void setTerrain1Result(float terrain1Result){
        this.terrain1Result = terrain1Result;
    }

    // Getter function; returns the second terrain comparison result
    public float getTerrain2Result(){
        return terrain2Result;
    }

    // Setter function; assigns the second terrain comparison result
    public void setTerrain2Result(float terrain2Result){
        this.terrain2Result = terrain2Result;
    }

    // Getter function; returns the terrain comparison likeliness
    public float getLikeliness(){
        return likeliness;
    }

    // Setter function; assigns the terrain comparison likeliness
    public void setLikeliness(float likeliness){
        this.likeliness = likeliness;
    }

    // Getter function; returns the terrain comparison feature name
    public String getFeatureName(){
        return featureName;
    }

    // Setter function; assignst the terrain comparison feature name
    public void setFeatureName(String featureName){
        this.featureName = featureName;
    }

    // Getter function; returns the terrain comparison upper range
    public float getUpperRange(){
        return upperRange;
    }

    // Setter function; assigns the terrain comparison upper range
    public void setUpperRange(float upperRange){
        this.upperRange = upperRange;
    }

    // Getter function; returns the terrain comparison lower range
    public float getLowerRange(){
        return lowerRange;
    }

    // Setter function; assignst he terrain comparison lower range
    public void setLowerRange(float lowerRange){
        this.lowerRange = lowerRange;
    }

    // Getter function; returns the terrain comparison weight
    public int getWeight(){
        return weight;
    }

    // Setter function; assigns the terrain comparison weight
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
