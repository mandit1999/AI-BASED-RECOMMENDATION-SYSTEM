import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.File;
import java.util.List;

public class RecommenderSystem {
    public static void main(String[] args) {
        try {
            // Load the dataset from a CSV file (format: userID,itemID,rating)
            DataModel model = new FileDataModel(new File("data.csv"));

            // Calculate how similar users are to each other using Pearson correlation
            UserSimilarity similarity = new PearsonCorrelationSimilarity(model);

            // Find the 2 users most similar to each target user
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // Create a user-based recommendation system using the model, similarity, and neighborhood
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Generate 3 recommendations for user with ID 1
            List<RecommendedItem> recommendations = recommender.recommend(1, 3);

            // Print the recommended items
            System.out.println("Recommended items for User 1:");
            for (RecommendedItem item : recommendations) {
                System.out.println("Item ID: " + item.getItemID() +
                                   " | Estimated Rating: " + item.getValue());
            }

        } catch (Exception e) {
            // If something goes wrong (e.g., file not found or parse error), show the error
            e.printStackTrace();
        }
    }
}
