import java.util.Random;

public class Population {
	public static final int MAX = 10;

	private String id;
	private int attack;
	private int deffence;
	private int hitPoint;
	private int speed;
	private int damage;
	protected Population enemy;
	private int win;
	private int attackCount;
	private int absorbCount;
	private int lose;
	private String isMutation;

	public Population(String id) {
		int max = 200 + Main.generation - 1;

		this.id = id;

		Random random = new Random();
		this.attack = random.nextInt(30)+1;
		this.deffence = random.nextInt(20);
		this.hitPoint = random.nextInt(50)+1;
		this.speed = random.nextInt(100)+1;

		this.offset(max - (this.attack + this.deffence + this.hitPoint + this.speed));

		this.damage = 0;
		this.enemy = null;
		this.win = 0;
		this.lose = 0;
		this.isMutation = "";
	}

	public Population(String id, int a, int d, int h, int s) {
		Random random = new Random();
		int max = 200 + Main.generation - 1;

		this.id = id;
		this.attack = a + (random.nextInt(11) - 5);
		if (this.attack <= 0) {
			this.attack = 1;
		}

		this.deffence = d + (random.nextInt(11) - 5);
		if (this.deffence < 0) {
			this.deffence = 0;
		} 

		this.hitPoint = h + (random.nextInt(11) - 5);
		if (this.hitPoint <= 0) {
			this.hitPoint = 1;
		}

		this.speed = s + (random.nextInt(11) - 5);
		if (this.speed <= 0) {
			this.speed = 1;
		}

		this.offset(max - (this.attack + this.deffence + this.hitPoint + this.speed));

		this.damage = 0;
		this.enemy = null;
		this.win = 0;
		this.attackCount = 0;
		this.absorbCount = 0;
		this.lose = 0;
		this.isMutation = "";
	}

	private void offset(int offset) {
		Random random = new Random();
		int flag = offset < 0 ? -1 : 1;
		while (offset != 0) {
			int type = random.nextInt(4);
			int delta = offset;
			if (Math.abs(delta) > 10) {
				delta = flag * (random.nextInt(10) + 1);
			}

			if (type == 0) {
				int tmp = this.attack;
				this.attack += delta;
				if (this.attack <= 0) {
					this.attack = 1;
				}
				offset += tmp - this.attack;
			} else if (type == 1) {
				int tmp = this.deffence;
				this.deffence += delta;
				if (this.deffence < 0) {
					this.deffence = 0;
				}
				offset += tmp - this.deffence;
			} else if (type == 2) {
				int tmp = this.hitPoint;
				this.hitPoint += delta;
				if (this.hitPoint <= 0) {
					this.hitPoint = 1;
				}
				offset += tmp - this.hitPoint;
			} else {
				int tmp = this.speed;
				this.speed += delta;
				if (this.speed <= 0) {
					this.speed = 1;
				}
				offset += tmp - this.speed;
			}
		}
	}

	public void attack() {
		if (this.enemy == null) {
			return;
		}

		if (this.enemy.getHitPoint() <= 0) {
			this.enemy = null;
			return;
		}

		int enemyHitPoint = this.enemy.getHitPoint();
		this.enemy.addDamage(this.getAttack());
		System.out.println(this.id + " attack -> " + this.enemy.getId() + "(" + enemyHitPoint + " -> " + this.enemy.getHitPoint() + ")");

		if (enemyHitPoint == this.enemy.getHitPoint()) {
			System.out.println("\t" + this.id + " could not cause damage to " + this.enemy.getId());
			this.enemy = null;
			return;
		}

		this.addAttackCount();

		if (this.enemy.getHitPoint() <= 0) {
			System.out.println("\t" + this.id + " Win : Lose " + this.enemy.getId());
			this.addWin();
			this.enemy.addLose();
			if (this.getClass().getName().equals("Alpha")) {
				Beta.survivor--;
			} else {
				Alpha.survivor--;
			}
			this.enemy = null;
		} else {
			this.enemy.addAbsorbCount();
		}
	}

	public void mutation(int type) {
		Random random = new Random();
		int max = 200 + Main.generation - 1;

		if ((type & 0x1) == 1) {
			this.attack = random.nextInt(max) + 1;
		}

		if ((type & 0x2) == 2) {
			this.deffence = random.nextInt(max + 1);
		}

		if ((type & 0x4) == 4) {
			this.hitPoint = random.nextInt(max) + 1;
		}

		if ((type & 0x8) == 8) {
			this.speed = random.nextInt(max) + 1;
		}

		this.offset(max - (this.attack + this.deffence + this.hitPoint + this.speed));

		this.isMutation = "◯";
	}

	public String getId() {
		return this.id;
	}

	public int getAttack() {
		return this.attack;
	}

	public int getDeffence() {
		return this.deffence;
	}

	public int getHitPoint() {
		int tmp = this.hitPoint - this.damage;
		return tmp < 0 ? 0 : tmp;
	}

	public int getOriginalHitPoint() {
		return this.hitPoint;
	}

	public int getSpeed() {
		return this.speed;
	}

	public void addDamage(int d) {
		int tmp = d - deffence;
		this.damage += tmp < 0 ? 0 : tmp;
	}

	public void addWin() {
		this.win++;
	}

	public void addAttackCount() {
		this.attackCount++;
	}

	public void addAbsorbCount() {
		this.absorbCount++;
	}

	public void addLose() {
		this.lose++;
	}

	public int getScore() {
		return this.win*10 + this.attackCount*2 + this.absorbCount - this.lose*10;
	}

	public String toString() {
		return this.id + "\t" + this.attack + "\t" + this.deffence + "\t" +
			this.hitPoint + "\t" + this.speed + "\t" + this.isMutation;
	}
}
