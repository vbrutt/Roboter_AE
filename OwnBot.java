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
				obs = observe()[0];
				System.out.println("Type" + obs.type);

			}
			if (isEnemy(obs)) {
				// Attack whatever we observed
				attack();
			}

			turnRight();
		}
	}

	private void observeAround(Observation obs) {
		int count = 0;
		List<Observation> observed = new ArrayList<>();
		do {
			turnRight();
			obs = observe()[0];
			if (distance(obs.position) - 1 > 0) {
				System.out.println("oi");
				observed.add(obs);
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