import java.awt.Dimension;
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
			if (isEnemy(obs)) {
				// Attack whatever we observed
				attack();
			}
			Random r = new Random();
			int i = r.nextInt(2) + 1;
			switch (i) {
			case 1:
				turnLeft();
				break;
			case 2:
				turnRight();
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

	private void decideDirection(Point orginalPositon, List<Direction> noDirections) {
		Random r = new Random();
		int i = r.nextInt(noDirections.size());

		chosenDirection.put(orginalPositon, noDirections.get(i));
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
			possibleDirections.put(orginalPositon, directions); // hier sind alle mögilche Directions von diesem Punkt
			// decideDirection(orginalPositon, directions);
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

	private Direction checkHowManyWays(Observation obs, Direction originalDirection) {
		// Direction originalDirection = getDirection();

		obs = observe()[0];
		Direction direction = null;
		// if (distance(obs.position) - 1 > 1) {
		if (!(obs.type == Type.WALL)) {
			if (obs.classId == Creature.TREASURE_CLASS_ID || obs.classId == Creature.APPLE_CLASS_ID) {
				goGetTreasure(obs);
			}
			if (checkExistance(obs)) {
				direction = chosenDirection.get(obs.position);
				Point p = turnRightDirection(direction, obs.position);
				// Direction d = directionToPoint(p);
				turn(getDirection(), originalDirection);
			} else {
				// mann kann hierlang weiter laufen
				emitPheromone(obs.position.toString());
				visitedJunctions.add(obs.position);
				direction = getDirection();
				chosenDirection.put(obs.position, direction);
			}
		}
		return direction;
	}

	/** Returns the primary direction of travel to reach this point. */
	protected Direction directionToPoint(Point loc) {
		Point pos = getPosition();

		int dx = loc.x - pos.x;
		int dy = loc.y - pos.y;

		if (Math.abs(dx) >= Math.abs(dy)) {
			// Mostly off on the horizontal
			if (dx < 0) {
				return Direction.WEST;
			} else {
				return Direction.EAST;
			}
		} else if (dy < 0) {
			return Direction.NORTH;
		} else {
			return Direction.SOUTH;
		}
	}

	private void defineDirection() {
		Dimension d = getMapDimensions();
		Observation[][] data = new Observation[d.width][d.height];
		Point p = new Point(d.width, d.height);

		Observation N = data[p.x][p.y - 1];
		Observation S = data[p.x][p.y + 1];
		Observation E = data[p.x + 1][p.y];
		Observation W = data[p.x - 1][p.y];

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

	/** Turn in a random direction 90 degrees. */
	protected void turnRandom() {
		Random random = new Random();
		if (random.nextInt(2) == 0) {
			turnLeft();
		} else {
			turnRight();
		}
	}

	private Point turnRightDirection(Direction direction, Point p) {
		switch (direction.toInt()) {
		case 0:
			return new Point(p.x, p.y - 1);
		case 2:
			return new Point(p.x, p.y + 1);
		case 1:
			return new Point(p.x + 1, p.y);
		case 3:
			return new Point(p.x - 1, p.y);
		default:
			return null;
		}
	}

	private Direction getOppositeDirection(Direction currentDirection) {
		switch (currentDirection) {
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		case EAST:
			return Direction.WEST;
		case WEST:
			return Direction.EAST;
		default:
			break;
		}
		return null;
	}

	private void observeAround(Observation obs) {
		int count = 0;
		List<Point> observed = new ArrayList<>();
		Direction currentDirection = getDirection();
		do {
			turnRight();
			obs = observe()[0];
			if (distance(obs.position) - 1 > 0 && !(getDirection().equals(getOppositeDirection(currentDirection)))) {
				observed.add(obs.position);
			}
			count++;
		} while (count < 4);
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