public class TerrainComparisonHelper{


    public TerrainComparisonHelper(){

    }

    void computeFeature(ComparisonData comparisonData, TerrainData terrain1, TerrainData terrain2) {
        try{
            // Check if the ranges are valid
            if (comparisonData.getLowerRange() == -1 || comparisonData.getUpperRange() == -1) {
                System.out.println("Invalid range value/s, aborting computations");
                return;
            }

            switch(comparisonData.getFeatureName()){
                case "Level":
                    comparisonData.setTerrain1Result(computeLevelFeature(comparisonData,terrain1));
                    comparisonData.setTerrain2Result(computeLevelFeature(comparisonData,terrain2));
                    System.out.println(comparisonData.toString());
                    break;
                case "Exclude Level":

                    break;
                case "Max Horizontal":

                    break;
                case "Max Vertical":

                    break;
                case "Max Diagonal":

                    break;
                case "Max Area":

                    break;
                default:
                    System.out.println("Invalid feature name detected when trying to compute feature: " + comparisonData.getFeatureName());
            }
        } catch(NullPointerException e) {
            System.out.println("Insufficient terrain data, aborting computations");
        }
    }


    float computeLevelFeature(ComparisonData comparisonData, TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        float min = comparisonData.getLowerRange();
        float max = comparisonData.getUpperRange();

        int counter = 0;
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[0].length; ++j) {
                if (data[i][j] >= min && data[i][j] <= max)
                    ++counter;
            }
        }

        return ((float)counter / (data.length * data[0].length)) * 100;
    }

    float computeExcludeLevelFeature(float min, float max) {
        return 0.0f;
    }

    float computeMaxHorizontalFeature(float min, float max) {
        return 0.0f;
    }

    float computeMaxVerticalFeature(float min, float max) {
        return 0.0f;
    }

    float computeMaxDiagonalFeature(float min, float max) {
        return 0.0f;
    }

    float computeMaxAreaFeature(float min, float max) {
        return 0.0f;
    }
}
