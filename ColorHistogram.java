import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class ColorHistogram {
    private double[] bins;
    private int depth;

    // Constructor for a d-bit image
    public ColorHistogram(int d) {
        this.depth = d;
        int binCount = (int) Math.pow(2, d * 3);
        this.bins = new double[binCount];
    }

    // Constructor that constructs a ColorHistogram from a text file
    public ColorHistogram(String filename) throws IOException {
        loadHistogramFromFile(filename);
    }

    // Method to associate an image with this histogram instance
    public void setImage(ColorImage image) {
        computeHistogram(image);
    }

    // Returns the normalized histogram of the image
    public double[] getHistogram() {
        return bins;
    }

    // Method to compare two histograms and return their intersection
    public double compare(ColorHistogram other) {
        double intersection = 0.0;
        for (int i = 0; i < this.bins.length; i++) {
            intersection += Math.min(this.bins[i], other.bins[i]);
        }
        return intersection;
    }

    // Method to save the histogram into a text file
    public void save(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (double bin : bins) {
                writer.write(bin + "\n");
            }
        }
    }

    // Private helper method to compute the histogram from a ColorImage
    private void computeHistogram(ColorImage image) {
        Arrays.fill(bins, 0); // Reset bins to 0 before computing the histogram
        int colorRange = (int) Math.pow(2, depth);
        int colorMaxValue = image.getDepth(); // Use the depth from the image
        int scaleFactor = colorMaxValue / (colorRange - 1);

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] rgb = image.getPixel(i, j);
                int r = rgb[0] / scaleFactor;
                int g = rgb[1] / scaleFactor;
                int b = rgb[2] / scaleFactor;
                int index = (r << (2 * depth)) + (g << depth) + b;
                bins[index]++;
            }
        }

        // Normalize the histogram
        int totalPixels = image.getWidth() * image.getHeight();
        for (int i = 0; i < bins.length; i++) {
            bins[i] /= totalPixels;
        }
    }

    // Private helper method to load the histogram from a text file
    private void loadHistogramFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            this.bins = reader.lines()
                              .skip(1) // Assuming the first line might be metadata like total bin count
                              .flatMap(line -> Arrays.stream(line.trim().split("\\s+")))
                              .mapToDouble(Double::parseDouble)
                              .toArray();
            this.depth = (int) (Math.log(bins.length) / Math.log(2) / 3); // Recalculate the depth based on the number of bins
        }
    }
}
