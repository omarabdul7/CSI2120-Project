import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ColorImage {
    private int width;
    private int height;
    private int depth; // Maximum color value
    private int[][][] pixels; // 3D array for RGB values

    public ColorImage(String filename) throws IOException {
        loadPPMImage(filename);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }

    // Returns the RGB values of the pixel at the specified coordinates
    public int[] getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void reduceColor(int d) {
        int shiftAmount = 8 - d;
        
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                pixels[i][j][0] = pixels[i][j][0] >> shiftAmount; // Red
                pixels[i][j][1] = pixels[i][j][1] >> shiftAmount; // Green
                pixels[i][j][2] = pixels[i][j][2] >> shiftAmount; // Blue
            }
        }
    }

    private void loadPPMImage(String filename) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            if (!"P3".equals(line)) {
                throw new IOException("Unsupported PPM format: " + line);
            }

            // Skip comment lines
            do {
                line = br.readLine();
            } while (line.startsWith("#"));

            // 'line' now contains the dimensions
            String[] dimensions = line.split("\\s+");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);
            depth = Integer.parseInt(br.readLine()); // Read the maximum color value (depth)

            pixels = new int[width][height][3];

            // Read RGB values
            String[] rgbValues = br.readLine().trim().split("\\s+");
            int index = 0;
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    // Ensure that there are enough values in the buffer
                    if (index >= rgbValues.length) {
                        rgbValues = br.readLine().trim().split("\\s+");
                        index = 0;
                    }
                    pixels[i][j][0] = Integer.parseInt(rgbValues[index++]); // Red
                    pixels[i][j][1] = Integer.parseInt(rgbValues[index++]); // Green
                    pixels[i][j][2] = Integer.parseInt(rgbValues[index++]); // Blue
                }
            }
        }
    }

}
