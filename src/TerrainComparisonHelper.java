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
                    comparisonData.setLikeliness(computePercentageLikeliness(comparisonData));
                    // Debug
                    System.out.println(comparisonData);
                    break;
                case "Exclude Level":
                    comparisonData.setTerrain1Result(computeExcludeLevelFeature(comparisonData,terrain1));
                    comparisonData.setTerrain2Result(computeExcludeLevelFeature(comparisonData,terrain2));
                    comparisonData.setLikeliness(computePercentageLikeliness(comparisonData));
                    // Debug
                    System.out.println(comparisonData);
                    break;
                case "Max Horizontal":
                    comparisonData.setTerrain1Result(computeMaxHorizontalFeature(comparisonData,terrain1));
                    comparisonData.setTerrain2Result(computeMaxHorizontalFeature(comparisonData,terrain2));
                    comparisonData.setLikeliness(computeLengthLikeliness("horizontal", comparisonData, terrain1,
                            terrain2));
                    // Debug
                    System.out.println(comparisonData);
                    break;
                case "Max Vertical":
                    comparisonData.setTerrain1Result(computeMaxVerticalFeature(comparisonData,terrain1));
                    comparisonData.setTerrain2Result(computeMaxVerticalFeature(comparisonData,terrain2));
                    comparisonData.setLikeliness(computeLengthLikeliness("vertical", comparisonData, terrain1,
                            terrain2));
                    // Debug
                    System.out.println(comparisonData);
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

    float computePercentageLikeliness(ComparisonData comparisonData) {
        return 100 - Math.abs(comparisonData.getTerrain1Result() - comparisonData.getTerrain2Result());
    }

    float computeLengthLikeliness(String type, ComparisonData comparisonData, TerrainData terrain1,
                                  TerrainData terrain2) {
        int terrain1Max = 0;
        int terrain2Max = 0;

        int terrain1Width = terrain1.getWidth();
        int terrain2Width = terrain2.getWidth();
        int terrain1Height = terrain1.getHeight();
        int terrain2Height = terrain2.getHeight();
        switch (type) {
            case "vertical":
                terrain1Max = terrain1Height;
                terrain2Max = terrain2Height;
                break;
            case "horizontal":
                terrain1Max = terrain1Width;
                terrain2Max = terrain2Width;
                break;
            case "diagonal":
                terrain1Max = (Math.min(terrain1Width,terrain1Height));
                terrain2Max = (Math.min(terrain2Width,terrain2Height));
                break;
            default:
                System.out.println("Invalid comparison type (horizontal, vertical, and diagonal)");
        }
        return (1 - Math.abs(comparisonData.getTerrain1Result() / terrain1Max - comparisonData.getTerrain2Result() / terrain2Max)) * 100;
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

    float computeExcludeLevelFeature(ComparisonData comparisonData, TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        float min = comparisonData.getLowerRange();
        float max = comparisonData.getUpperRange();

        int counter = 0;
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[0].length; ++j) {
                if (data[i][j] < min || data[i][j] > max)
                    ++counter;
            }
        }

        return ((float)counter / (data.length * data[0].length)) * 100;
    }

    float computeMaxHorizontalFeature(ComparisonData comparisonData, TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        float min = comparisonData.getLowerRange();
        float max = comparisonData.getUpperRange();

        int longest = 0;
        for (int i = 0; i < data.length; ++i) {
            int counter = 0;
            for (int j = 0; j < data[0].length; ++j) {
                if (data[i][j] >= min && data[i][j] <= max) {
                    ++counter;
                }
                else if (counter > longest)
                    longest = counter;
            }
            if (counter > longest)
                longest = counter;
        }

        return longest;
    }

    float computeMaxVerticalFeature(ComparisonData comparisonData, TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        float min = comparisonData.getLowerRange();
        float max = comparisonData.getUpperRange();

        int longest = 0;
        for (int i = 0; i < data[0].length; ++i) {
            int counter = 0;
            for (int j = 0; j < data.length; ++j) {
                if (data[j][i] >= min && data[j][i] <= max) {
                    ++counter;
                }
                else if (counter > longest)
                    longest = counter;
            }
            if (counter > longest)
                longest = counter;
        }

        return longest;
    }

    float computeMaxDiagonalFeature(ComparisonData comparisonData, TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        float min = comparisonData.getLowerRange();
        float max = comparisonData.getUpperRange();

        int longest = 0;
        for (int i = 0; i < data.length; ++i) {
            int counter = 0;
            for (int j = 0; j < data[0].length; ++j) {
                if (data[i][j] >= min && data[i][j] <= max) {
                    ++counter;
                }
                else if (counter > longest)
                    longest = counter;
            }
            if (counter > longest)
                longest = counter;
        }

        return longest;
    }

    float computeMaxAreaFeature(float min, float max) {
        return 0.0f;
    }
}
