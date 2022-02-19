import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TerrainImageHelper{

    ImageIcon getTerrainImage(String name, int width, int height) {
        ImageIcon image = new ImageIcon(name);
        float terrainImageRatio = (float)image.getIconWidth() / image.getIconHeight();
        int finalWidth1 = (terrainImageRatio < 1 ? (int)(width * terrainImageRatio) : width);
        int finalHeight1 = (terrainImageRatio > 1 ? (int)(height / terrainImageRatio) : height);
        image = new ImageIcon(new ImageIcon(name).getImage().getScaledInstance(finalWidth1,finalHeight1,
                Image.SCALE_DEFAULT));

        return image;
    }

    float[][] readImageData(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        float[][] result = new float[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // As the image inputs are grayscale, any color channel will work
                result[row][col] = new Color(image.getRGB(col, row)).getRed() / 255.0f;
            }
        }

        return result;
    }

    void convertTerrainData(BufferedImage image,TerrainData terrainData) {
        terrainData.setTerrainData(readImageData(image));
    }

    // Debug: Warning: Printing thousands of floats is slow
    void showTerrainData(TerrainData terrainData) {
        float[][] data = terrainData.getTerrainData();
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[0].length; ++j) {
                System.out.print(data[i][j] + " ");
            }
            System.out.println();
        }
    }

    void computeTerrainLevels(TerrainData terrainData) {
        float highestLevel = 0.0f;
        float averageLevel = 0.0f;
        float lowestLevel = 1.0f;
        float[][] data = terrainData.getTerrainData();

        float levelTotal = 0.0f;
        int totalCount = data.length * data[0].length;
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < data[0].length; ++j) {
                if (data[i][j] < lowestLevel)
                    lowestLevel = data[i][j];
                if (data[i][j] > highestLevel) {
                    highestLevel = data[i][j];
                }
                levelTotal += data[i][j];
            }
        }
        averageLevel = levelTotal / totalCount;

        terrainData.setHighestLevel(highestLevel);
        terrainData.setAverageLevel(averageLevel);
        terrainData.setLowestLevel(lowestLevel);
    }
}
