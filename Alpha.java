public class Alpha extends Population {
	public static int survivor = Population.MAX;
	public static int gid = 1;
	public static int id = 1;

	public Alpha() {
		super(
			"Alpha-" + String.format("%03d", Alpha.gid) + String.format("-%02d", Alpha.id)
		);
		Alpha.id++;
		Alpha.survivor = Population.MAX;
	}

	public Alpha(int a, int d, int h, int s) {
		super(
			"Alpha-" + String.format("%03d", Alpha.gid) + String.format("-%02d", Alpha.id),
			a, d, h, s);
		Alpha.id++;
		Alpha.survivor = Population.MAX;
	}

	public Alpha(Alpha a1, Alpha a2) {
		this(
			(a1.getAttack() + a2.getAttack()) / 2,
			(a1.getDeffence() + a2.getDeffence()) / 2,
			(a1.getOriginalHitPoint() + a2.getOriginalHitPoint()) / 2,
			(a1.getSpeed() + a2.getSpeed()) / 2
		);
	}

	public void setEnemy(Beta[] enemyList, int index) {
		for (int i=0; i<Population.MAX; i++) {
			int tmpIndex = (index+i) % Population.MAX;
			Beta enemy = enemyList[tmpIndex];
			if (enemy.getHitPoint() > 0) {
				if (enemy.getEnemy() == null) {
					enemy.setEnemy(this);
				}
				this.enemy = enemy;
				break;
			}
		}
	}

	public void setEnemy(Beta enemy) {
		this.enemy = enemy;
	}

	public Beta getEnemy() {
		return (Beta)this.enemy;
	}

	public void subSurvivor() {
		Alpha.survivor--;
	}
}
