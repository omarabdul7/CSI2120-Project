
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ColorHistogram {
    private int d; // The number of bits per channel
    private double[] histogram; // The normalized histogram

    // Constructor for a d-bit image
    public ColorHistogram(int d) {
        this.d = d;
        int size = (int) Math.pow(2, this.d * 3);
        this.histogram = new double[size];
    }

    // Constructor that constructs a ColorHistogram from a text file
    public ColorHistogram(String filename) throws IOException {
        this.load(filename);
    }

    // Method to associate an image with a histogram instance
    public void setImage(ColorImage image) {
        int shift = 8 - this.d;
        int size = (int) Math.pow(2, this.d * 3);
        this.histogram = new double[size];
        int totalPixels = image.getWidth() * image.getHeight();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                int[] pixel = image.getPixel(i, j);
                int r = pixel[0] >> shift;
                int g = pixel[1] >> shift;
                int b = pixel[2] >> shift;
                int index = (r << (2 * this.d)) + (g << this.d) + b;
                this.histogram[index]++;
            }
        }

        // Normalize the histogram
        for (int i = 0; i < this.histogram.length; i++) {
            this.histogram[i] /= totalPixels;
        }
    }

    // Method to return the normalized histogram of the image
    public double[] getHistogram() {
        return this.histogram;
    }

    // Method that returns the intersection between two histograms
    public double compare(ColorHistogram hist) {
        double sum = 0.0;
        for (int i = 0; i < this.histogram.length; i++) {
            sum += Math.min(this.histogram[i], hist.histogram[i]);
        }
        return sum;
    }

    // Method that saves the histogram into a text file
    public void save(String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(filename)) {
            for (double val : this.histogram) {
                out.println(val);
            }
        }
    }

// Method to load the histogram from a text file
private void load(String filename) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
        String line = br.readLine(); // Read the first line to  the number of bins
        if (line != null) {
            int numberOfBins = Integer.parseInt(line.trim());
            this.histogram = new double[numberOfBins]; // Initialize the histogram array
        }
        
        int binIndex = 0; // Index for placing counts into the histogram
        while ((line = br.readLine()) != null) {
            String[] numbers = line.split("\\s+"); // Split the line into individual numbers
            for (String number : numbers) {
                if (binIndex < this.histogram.length) {
                    this.histogram[binIndex] = Double.parseDouble(number.trim()) / 255.0; // Normalizing the count by dividing by 255
                    binIndex++;
                }
            }
        }
        
        if (binIndex != this.histogram.length) {
            throw new IOException("Histogram file format is incorrect. Expected " + this.histogram.length + " bins, but found " + binIndex);
        }
    }
}
}