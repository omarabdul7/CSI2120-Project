// CSI 2120 Project Part 1
// Done by Omar Abdul - 300228700 and Anas Taimah - 300228842

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;

public class SimilaritySearch {

    
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.exit(1);
        }

        String findImageFilename = args[0];
        String datasetDirectory = args[1];
        int d = 3; // Color depth for histogram
        int k = 5; // Number of most similar images to find


        ColorImage findImage = new ColorImage(findImageFilename);
        ColorHistogram findHistogram = new ColorHistogram(d);
        findHistogram.setImage(findImage);

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
            double similarity = findHistogram.compare(datasetHistogram);
            pq.offer(new SimpleEntry<>(histogramFile.getName(), similarity));

            if (pq.size() > k) {
                pq.poll();
            }
        }

        List<String> mostSimilarImages = new ArrayList<>();
        while (!pq.isEmpty()) {
            // Extract the base name without .txt
            String baseName = pq.poll().getKey().replace(".jpg.txt", "");
            String imageFileName = baseName + ".jpg";
            mostSimilarImages.add(0, imageFileName);
        }

        System.out.println("The 5 most similar images to " + findImageFilename + " are:");
        for (String imageName : mostSimilarImages) {
            System.out.println(imageName);
        }

    }
}

