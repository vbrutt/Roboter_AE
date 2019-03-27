import java.util.Random;

public class OwnBot_NS extends Creature {

	@Override
	public void run() {
		while (true) {

			Observation obs = observe()[0];
			int d = distance(obs.position) - 1;
			// Move until the far edge
			for (int i = 0; i < d; ++i) {
				if (!(moveForward())) {
					enemySight(obs);
					// Hit something unexpected!
					attack();
					break;
				}
			}

			if (obs.classId == Creature.APPLE_CLASS_ID) { // APPLE EATER
				while (distance(obs.position) > 1) {
					if (!moveForward()) {
						attack();
					}
				}
				attack();
			}

			attack();
			turn(obs);

		}
	}

	private void turn(Observation obs) {
		Random r = new Random();
		int i = r.nextInt(2) + 1;
		switch (i) {
		case 1:
			turnLeft();
			attack();
			break;
		case 2:
			turnRight();
			attack();
			break;
		default:
			break;
		}
	}

	protected boolean myMoveForward() {
		if (!moveForward()) {
			attack();
			return false;
		}
		return true;
	}

	public void enemySight(Observation enemy) {
		int enemyDistance = distance(enemy.position);

		if (enemyDistance == 2) {
			delay();
		} else if (enemyDistance == 1) {
			attack();
		} else {
			moveForward();
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