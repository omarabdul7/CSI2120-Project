import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class SimilaritySearch {

    
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java SimilaritySearch <query_image.ppm> <dataset_directory>");
            System.exit(1);
        }

        String queryImageFilename = args[0];
        String datasetDirectory = args[1];
        int d = 3; // Color depth for histogram
        int k = 5; // Number of most similar images to find

        try {
            ColorImage queryImage = new ColorImage(queryImageFilename);
            ColorHistogram queryHistogram = new ColorHistogram(d);
            queryHistogram.setImage(queryImage);

            File dir = new File(datasetDirectory);
            File[] datasetFiles = dir.listFiles(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.toLowerCase().endsWith(".jpg.txt");
                }
            });

            PriorityQueue<Entry<String, Double>> pq = new PriorityQueue<>(
                (a, b) -> Double.compare(b.getValue(), a.getValue())
            );

            for (File histogramFile : datasetFiles) {
                ColorHistogram datasetHistogram = new ColorHistogram(histogramFile.getAbsolutePath());
                double similarity = queryHistogram.compare(datasetHistogram);
                pq.offer(new SimpleEntry<>(histogramFile.getName(), similarity));

                if (pq.size() > k) {
                    pq.poll();
                }
            }

            List<String> mostSimilarImages = new ArrayList<>();
            while (!pq.isEmpty()) {
                // Extract the base name without ".jpg.txt" and append ".jpg" to get the image filename
                String baseName = pq.poll().getKey().replace(".jpg.txt", "");
                String imageFileName = baseName + ".jpg";
                mostSimilarImages.add(0, imageFileName);
            }

            System.out.println("The 5 most similar images to " + queryImageFilename + " are:");
            for (String imageName : mostSimilarImages) {
                System.out.println(imageName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
