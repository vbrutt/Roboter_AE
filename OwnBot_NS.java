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

			if(isEnemy(obs)) {
				if(obs.className == "Apple") {
					while(myMoveForward()) {
						
					}
				}
				killEnemy(obs);
			}
			turn(obs);

		}
	}

	private void killEnemy(Observation obs) {
		int enemyId = obs.id;
		if (distance(obs.position) == 1) {
			attack();
		} else {
			while (obs.id == enemyId && distance(obs.position) > 2) {
				moveForward();
				obs = look();
			}

			if (distance(obs.position) == 1)
				attack();
			else {
				// attack strategy
				while (obs.id == enemyId && distance(obs.position) == 2 && !isVulnerable(obs)) {
					// wait
					obs = look();
				}
				// if the loop broke because the enemy became vulnerable,
				// kill it
				if (distance(obs.position) == 1)
					attack();
				else if (isVulnerable(obs)) {
					moveForward();
					attack();
				}
			}
		}
	}

	private boolean isVulnerable(Observation o) {
		return (isEnemy(o) && distance(o.position) == 2 && !getDirection().equals(o.direction.opposite()));
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