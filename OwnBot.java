import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class OwnBot extends Creature {
	@Override
	public void run() {
		while (true) {
			Observation obs = observe()[0];

			int d = distance(obs.position) - 1;
			// Move until the far edge
			for (int i = 0; i < d; ++i) {
				moveForward();
				observeAround(obs);

			}
			if (isEnemy(obs)) {
				// Attack whatever we observed
				attack();
			}

			turnRight();
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
		System.out.println("current Dir: " + currentDirection);
		do {
			turnRight();
			obs = observe()[0];
			System.out.println(getDirection());
			if (distance(obs.position) - 1 > 0 && !(getDirection().equals(getOppositeDirection(currentDirection)))) {
				observed.add(obs.position);
				System.out.println(getDirection());
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