package projekt;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Path;

public class Meteor {

	ImageView imageView = new ImageView();
	boolean valid = true;
	PathTransition transition = new PathTransition();
	Path path = new Path();

	Meteor(Image meteorImage) {
		imageView.setImage(meteorImage);
		transition.setNode(imageView);
		transition.setPath(path);

		transition.setOnFinished(event -> {
			valid = true;
			//System.out.println("Meteor finished");
			imageView.setTranslateX(0);
			imageView.setTranslateY(40);
		});
	}


}
