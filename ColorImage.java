
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ColorImage {
    private int width;
    private int height;
    private int depth; // Added to store the depth value from the PPM file
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
    public int[] getPixel(int i, int j) {
        return pixels[i][j];
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
            br.readLine(); // Skip "P3"
            br.readLine(); // Skip comment line
            String[] dimensions = br.readLine().split("\\s+");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);
            depth = Integer.parseInt(br.readLine()); // Read the maximum color value

            pixels = new int[width][height][3];

            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    String line = br.readLine();
                    if (line != null) {
                        String[] rgb = line.split("\\s+");
                        pixels[i][j][0] = Integer.parseInt(rgb[0]); // Red
                        pixels[i][j][1] = Integer.parseInt(rgb[1]); // Green
                        pixels[i][j][2] = Integer.parseInt(rgb[2]); // Blue
                    }
                }
            }
        }
    }
}
