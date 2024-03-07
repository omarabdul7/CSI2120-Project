import java.io.File;
import java.io.IOException;

public class HistogramGenerator {

    public static void main(String[] args) {
        String queryImagesDirectoryPath = "queryImages"; // Directory with images
        String histogramsDirectoryPath = "histograms"; // Directory where histograms will be saved
        int d = 3; // Color depth for histogram
        
        File queryImagesDir = new File(queryImagesDirectoryPath);
        File histogramsDir = new File(histogramsDirectoryPath);
        
        // Create the histograms directory if it does not exist
        if (!histogramsDir.exists()) {
            histogramsDir.mkdirs();
        }
        
        // Filter to select only the desired image files, adjust as necessary
        File[] imageFiles = queryImagesDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".ppm")); // or any other image format
        
        if (imageFiles != null) {
            for (File imageFile : imageFiles) {
                try {
                    // Load each image and compute its histogram
                    ColorImage image = new ColorImage(imageFile.getAbsolutePath());
                    ColorHistogram histogram = new ColorHistogram(d);
                    histogram.setImage(image);
                    
                    // Generate a filename for the histogram
                    String histogramFilename = histogramsDir.getAbsolutePath() + File.separator + 
                                               imageFile.getName().replaceAll("\\.ppm$", ".txt"); // Change extension
                    
                    // Save the histogram
                    histogram.save(histogramFilename);
                    System.out.println("Saved histogram for " + imageFile.getName() + " as " + histogramFilename);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.err.println("No image files found in " + queryImagesDirectoryPath);
        }
    }
}
