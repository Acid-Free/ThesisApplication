public class TerrainData{
    private float[][] terrainData;
    private String terrainPath;
    private String terrainName;

    public float getHighestLevel(){
        return highestLevel;
    }

    public void setHighestLevel(float highestLevel){
        this.highestLevel = highestLevel;
    }

    public float getAverageLevel(){
        return averageLevel;
    }

    public void setAverageLevel(float averageLevel){
        this.averageLevel = averageLevel;
    }

    public float getLowestLevel(){
        return lowestLevel;
    }

    public void setLowestLevel(float lowestLevel){
        this.lowestLevel = lowestLevel;
    }

    private float highestLevel;
    private float averageLevel;
    private float lowestLevel;

    public float[][] getTerrainData(){
        return terrainData;
    }

    public void setTerrainData(float[][] terrainData){
        this.terrainData = terrainData;
    }

    public String getTerrainPath(){
        return terrainPath;
    }

    public void setTerrainPath(String terrainPath){
        this.terrainPath = terrainPath;
    }

    public String getTerrainName(){
        return terrainName;
    }

    public void setTerrainName(String terrainName){
        this.terrainName = terrainName;
    }

    public int getWidth() {
        try{
            return terrainData[0].length;
        } catch(NullPointerException e) {
            return 0;
        }
    }

    public int getHeight() {
        try{
            return terrainData.length;
        } catch(NullPointerException e) {
            return 0;
        }
    }
}
