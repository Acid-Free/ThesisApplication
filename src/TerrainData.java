// Class that serves as a data container for every terrain
public class TerrainData{
    private float[][] terrainData;
    private String terrainPath;
    private String terrainName;
    private float highestLevel;
    private float averageLevel;
    private float lowestLevel;

    // Getter function; returns the terrain highest level
    public float getHighestLevel(){
        return highestLevel;
    }

    // Setter function; assigns the terrain highest level
    public void setHighestLevel(float highestLevel){
        this.highestLevel = highestLevel;
    }

    // Getter function; returns the terrain average level
    public float getAverageLevel(){
        return averageLevel;
    }

    // Setter function; assigns the terrain average level
    public void setAverageLevel(float averageLevel){
        this.averageLevel = averageLevel;
    }

    // Getter function; returns the terrain lowest level
    public float getLowestLevel(){
        return lowestLevel;
    }

    // Setter function; assigns the terrain lowest level
    public void setLowestLevel(float lowestLevel){
        this.lowestLevel = lowestLevel;
    }

    // Getter function; returns the terrain data
    public float[][] getTerrainData(){
        return terrainData;
    }

    // Setter function; assigns the terrain data
    public void setTerrainData(float[][] terrainData){
        this.terrainData = terrainData;
    }

    // Getter function; returns the terrain path
    public String getTerrainPath(){
        return terrainPath;
    }

    // Setter function; assigns the terrain path 
    public void setTerrainPath(String terrainPath){
        this.terrainPath = terrainPath;
    }

    // Getter function; Returns the terrain name
    public String getTerrainName(){
        return terrainName;
    }

    // Setter function; assigns the terrain name
    public void setTerrainName(String terrainName){
        this.terrainName = terrainName;
    }

    // Getter function; returns the terrain data width
    public int getWidth() {
        try{
            return terrainData[0].length;
        } catch(NullPointerException e) {
            return 0;
        }
    }

    // Getter function; returns the terrain data height
    public int getHeight() {
        try{
            return terrainData.length;
        } catch(NullPointerException e) {
            return 0;
        }
    }
}
