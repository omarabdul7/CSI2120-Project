// CSI 2120 Project Part 1
// Done by Omar Abdul - 300228700 and Anas Taimah - 300228842


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.IOException;

public class ColorHistogram {
    private int d; 
    private double[] histogram; 

    public ColorHistogram(int d) {
        this.d = d;
        int size = (int) Math.pow(2, this.d) * 3;
        this.histogram = new double[size];
    }

    public ColorHistogram(String filename) throws IOException{
        load(filename);
    }

    // Setting image with histogram
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

        // normalizing the histogram
        for (int i = 0; i < this.histogram.length; i++) {
            this.histogram[i] /= totalPixels;
        }
    }

    // Method to return the normalized histogram of the image
    public double[] getHistogram() {
        return this.histogram;
    }

    // Retrun similarity between histograms
    public double compare(ColorHistogram hist) {
        double sum = 0.0;
        for (int i = 0; i < this.histogram.length; i++) {
            sum += Math.min(this.histogram[i], hist.histogram[i]);
        }
        return sum;
    }

    // Save histogram
    public void save(String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(filename)) {
            // Write the number of bins on the first line
            out.println(this.histogram.length);
            
            // Start the histogram values on the next line
            // Use a StringBuilder to construct the line for efficiency
            StringBuilder histogramValues = new StringBuilder();
            for (double val : this.histogram) {
                // Assuming values were normalized to [0,1], scale to [0,255]
                // and convert to integer. Append each value followed by a space
                histogramValues.append((int)Math.round(val * 255)).append(" ");
            }
    
            // Trim the StringBuilder to remove the last space and print the line
            if (histogramValues.length() > 0) { // Check if the StringBuilder is not empty
                // Remove the last space
                histogramValues.setLength(histogramValues.length() - 1);
            }
            out.println(histogramValues.toString());
        }
    }
    

    // Load Histogram
    private void load(String filename) throws IOException {
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(filename));
        String line = br.readLine(); //Read starting for the first line
        if (line != null) {
            int numberOfBins = Integer.parseInt(line.trim());
            this.histogram = new double[numberOfBins];

            int indexB = 0; // Index of bin
            while ((line = br.readLine()) != null) {

                String[] numbers = line.split("\\s+"); 

                for (String number : numbers) {
                    if (indexB < this.histogram.length) {
                        this.histogram[indexB] = Double.parseDouble(number.trim()) / 255.0; // Normalizing the count
                        indexB++;
                    }
                }
            }

        }
    }
}
