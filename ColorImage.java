// CSI 2120 Project Part 1
// Done by Omar Abdul - 300228700 and Anas Taimah - 300228842

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ColorImage {
    private int w;
    private int h;
    private int dep; 
    private int[][][] pixels; 

    public ColorImage(String filename) throws IOException {
        loadPPMImage(filename);
    }

    public int getWidth() {
        return w;
    }

    public int getHeight() {
        return h;
    }

    public int getDepth() {
        return dep;
    }

    // Returns the RGB coords
    public int[] getPixel(int x, int y) {
        return pixels[x][y];
    }

    public void reduceColor(int d) {
        int shiftAmount = 8 - d;
        
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pixels[i][j][0] = pixels[i][j][0] >> shiftAmount; // Red
                pixels[i][j][1] = pixels[i][j][1] >> shiftAmount; // Green
                pixels[i][j][2] = pixels[i][j][2] >> shiftAmount; // Blue
            }
        }
    }

    private void loadPPMImage(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine();

        // Skip comment lines (beginning with hashtag)
        do {
            line = br.readLine();
        } while (line.startsWith("#"));

 
        String[] dimensions = line.split("\\s+");
        w = Integer.parseInt(dimensions[0]);
        h = Integer.parseInt(dimensions[1]);
        dep = Integer.parseInt(br.readLine()); 

        pixels = new int[w][h][3];

        // Read RGB values
        String[] rgbValues = br.readLine().trim().split("\\s+");
        int index = 0;
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                // make sure there is enough values in the buffer
                if (index >= rgbValues.length) {
                    rgbValues = br.readLine().trim().split("\\s+");
                    index = 0;
                }
                pixels[i][j][0] = Integer.parseInt(rgbValues[index++]); // Red
                pixels[i][j][1] = Integer.parseInt(rgbValues[index++]); // Green
                pixels[i][j][2] = Integer.parseInt(rgbValues[index++]); // Blue
            }
        }

        br.close(); 
    }
}
