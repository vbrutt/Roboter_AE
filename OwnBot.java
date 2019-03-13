import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OwnBot extends Creature {
	private Map<Point, Direction> chosenDirection = new HashMap<>();
	private List<Point> visitedJunctions = new ArrayList<>();
	private Map<Point, List<Direction>> possibleDirections = new HashMap<>();

	// TODO Hazards vermeiden
	@Override
	public void run() {
		while (true) {
			Observation obs = observe()[0];
			int d = distance(obs.position) - 1;
			// Move until the far edge
			for (int i = 0; i < d; ++i) {
				moveForward();
				lookRightAndLeft(obs);
				// obs = observe()[0];
			}
			if (isEnemy(obs) || obs.type == Type.HAZARD) {
				// Attack whatever we observed
				attack();
			}
			Random r = new Random();
			int i = r.nextInt(2) + 1;
			switch (i) {
			case 1:
				turnLeft();
				chosenDirection.put(obs.position, getDirection());
				break;
			case 2:
				turnRight();
				chosenDirection.put(obs.position, getDirection());
				break;
			default:
				break;
			}
		}
	}

	private boolean checkExistance(Observation obs) {
		if (visitedJunctions.contains(obs.position)) {
			return true;
		}
		return false;
	}

	private void lookRightAndLeft(Observation obs) {
		Direction originalDirection = getDirection();
		Point orginalPositon = obs.position;
		List<Direction> directions = new ArrayList<>();

		for (int i = 0; i < 3; i++) {
			if (i < 1) {
				turnLeft();
				Direction d = checkHowManyWays(obs, originalDirection);
				if (d != null) {
					directions.add(d);
				}

			} else if (i == 1) {
				turnRight();
			} else {
				turnRight();
				Direction d = checkHowManyWays(obs, originalDirection);
				if (d != null) {
					directions.add(d);
				}
				turnLeft();
			}
		}
		if (!(directions.isEmpty())) {
			possibleDirections.put(orginalPositon, directions);
		}
	}

	private void goGetTreasure(Observation obs) {
		while (distance(obs.position) > 1) {
			if (!moveForward()) {
				attack();
			}
		}
		attack();
	}

	private void checkIfTreasure(Observation obs) {
		if (obs.classId == Creature.TREASURE_CLASS_ID || obs.classId == Creature.APPLE_CLASS_ID) {
			goGetTreasure(obs);
		}
	}

	private Direction checkHowManyWays(Observation obs, Direction originalDirection) {

		obs = observe()[0];
		Direction direction = null;
		if (distance(obs.position) - 1 > 1) {
			checkIfTreasure(obs);
			if (checkExistance(obs)) {
				direction = chosenDirection.get(obs.position);
				turn(getDirection(), originalDirection);
				visitedJunctions.remove(obs.position);
			} else {
				emitPheromone(obs.position.toString());
				visitedJunctions.add(obs.position);
				direction = getDirection();
			}
		}
		return direction;
	}

	/** Rotate to face this direction, returning true if actually rotated. */
	protected boolean turn(Direction currentDirection, Direction original) {
		// final Direction dir = getDirection();

		if (currentDirection == original) {
			return false;
		}

		int me = original.toInt();
		int it = currentDirection.toInt();

		if ((me + 1) % 4 == it) {
			turnLeft();
		} else {
			turnRight();
		}
		return true;
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