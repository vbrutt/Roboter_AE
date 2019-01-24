
public class OwnBot extends Creature {
	@Override
	public void run() {
		while (true) {
			Observation obs = observe()[0];

			int d = distance(obs.position) - 1;
			// Move until the far edge
			for (int i = 0; i < d; ++i) {
				if (!moveForward() && isEnemy(obs)) {
					// Hit something unexpected!
					attack();
					break;
				}else if (!moveForward()) {
					turnLeft();
				}
			}
			// if (isEnemy(obs)) {
			// // Attack whatever we observed
			// attack();
			// }
			//
			// // Turn
			// turnRight();
			// emitPheromone("ph");
			// if(this.getPheromone().equals("ph")) {
			// turnLeft();
			// }
			
			
			// turnLeft();

		}
	}

	@Override
	public String getAuthorName() {
		return "Darwin SDK";
	}

	@Override
	public String getDescription() {
		return "A rover that looks before it moves.";
	}

}
