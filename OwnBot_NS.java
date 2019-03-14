public class OwnBot_NS extends Creature {
	@Override
	public void run() {
		while (true) {
			if (!moveForward()) {
				attack();
				turnLeft();
			}
		}
	}

	@Override
	public String getAuthorName() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}

	@Override
	public String getDescription() {
		// TODO Automatisch generierter Methodenstub
		return null;
	}
}
