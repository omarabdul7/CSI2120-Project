import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class SimilaritySearch {

    private static final int COLOR_DEPTH = 3; // Assuming 3-bit color depth for histogram comparison
    private static final int TOP_K = 5; // Number of top similar images to find

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimilaritySearch <query_image_path> <image_dataset_directory>");
            return;
        }

        String queryImagePath = args[0];
        String imageDatasetDirectory = args[1];

        try {
            // Compute the histogram for the query image
            ColorImage queryImage = new ColorImage(queryImagePath);
            queryImage.reduceColor(COLOR_DEPTH);
            ColorHistogram queryHistogram = new ColorHistogram(3); // Assuming 512 bins for a 3-bit depth histogram
            queryHistogram.setImage(queryImage);

            // Directory containing the dataset histograms
            File dir = new File(imageDatasetDirectory);
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                PriorityQueue<ImageSimilarity> pq = new PriorityQueue<>(Collections.reverseOrder());

                for (File child : directoryListing) {
                    if (child.getName().endsWith(".txt")) { // Process only .txt files
                        ColorHistogram datasetHistogram = new ColorHistogram(child.getAbsolutePath());

                        // Calculate the similarity score
                        double similarityScore = queryHistogram.compare(datasetHistogram);

                        // Add to the priority queue
                        pq.add(new ImageSimilarity(child.getName(), similarityScore));

                        // Ensure the priority queue never holds more than TOP_K elements
                        while (pq.size() > TOP_K) {
                            pq.poll();
                        }
                    }
                }

                // Print the top K similar images
                List<ImageSimilarity> topKSimilarImages = new ArrayList<>(pq);
                Collections.sort(topKSimilarImages, Collections.reverseOrder());
                
                System.out.println("Top " + TOP_K + " most similar images:");
                for (ImageSimilarity sim : topKSimilarImages) {
                    System.out.println(sim.getImageName() + " - Similarity Score: " + sim.getSimilarityScore());
                }
            } else {
                System.out.println("Image dataset directory does not exist or is not a directory.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ImageSimilarity implements Comparable<ImageSimilarity> {
        private final String imageName;
        private final double similarityScore;

        public ImageSimilarity(String imageName, double similarityScore) {
            this.imageName = imageName;
            this.similarityScore = similarityScore;
        }

        public String getImageName() {
            return imageName.replace(".txt", ""); // Remove .txt extension for display
        }

        public double getSimilarityScore() {
            return similarityScore;
        }

        @Override
        public int compareTo(ImageSimilarity other) {
            return Double.compare(this.similarityScore, other.similarityScore);
        }
    }
}
