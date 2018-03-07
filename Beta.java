import java.util.Random;

public class Beta extends Population {
	public static int survivor = Population.MAX;
	public static int gid = 1;
	public static int id = 1;

	public Beta() {
		super(
			"Beta-" + String.format("%03d", Beta.gid) + String.format("-%02d", Beta.id)
		);
		Beta.id++;
		Beta.survivor = Population.MAX;
	}

	public Beta(int type, int a, int d, int h, int s) {
		super(
			"Beta-" + String.format("%03d", Beta.gid) + String.format("-%02d", Beta.id),
			type, a, d, h, s);
		Beta.id++;
		Beta.survivor = Population.MAX;
	}

	public Beta(Beta b1, Beta b2) {
		this(
			new Random().nextInt(2) == 0 ? b1.getType() : b2.getType(),
			(b1.getAttack() + b2.getAttack()) / 2,
			(b1.getDeffence() + b2.getDeffence()) / 2,
			(b1.getOriginalHitPoint() + b2.getOriginalHitPoint()) / 2,
			(b1.getSpeed() + b2.getSpeed()) / 2
		);
	}

	public void setEnemy(Alpha[] enemyList, int index) {
		for (int i=0; i<Population.MAX; i++) {
			int tmpIndex = (index+i) % Population.MAX;
			Alpha enemy = enemyList[tmpIndex];
			if (enemy.getHitPoint() > 0) {
				this.enemy = enemy;
				break;
			}
		}
	}

	public void setEnemy(Alpha enemy) {
		this.enemy = enemy;
	}

	public Alpha getEnemy() {
		return (Alpha)this.enemy;
	}

	public void subSurvivor() {
		Beta.survivor--;
	}
}
