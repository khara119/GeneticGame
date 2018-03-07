import java.util.Arrays;
import java.util.Random;

public class Main {
	public static int generation = 1;

	public static void main(String[] args) throws Exception {
		int[] expectations = new int[100];
		for (int i=0; i<50; i++) {
			expectations[i] = 0;
		}
		for (int i=50; i<75; i++) {
			expectations[i] = 1;
		}
		for (int i=75; i<88; i++) {
			expectations[i] = 2;
		}
		for (int i=88; i<94; i++) {
			expectations[i] = 3;
		}
		for (int i=94; i<97; i++) {
			expectations[i] = 4;
		}
		for (int i=97; i<99; i++) {
			expectations[i] = 5;
		}
		expectations[99] = 6;

		Population[] population = new Population[Population.MAX*2];
		Alpha[] alpha = new Alpha[Population.MAX];
		Beta[] beta = new Beta[Population.MAX];

		// Create initial populations
		for (int j=0; j<Population.MAX*2; j+=2) {
			population[j] = new Alpha();
			alpha[j/2] = (Alpha)population[j];

			population[j+1] = new Beta();
			beta[j/2] = (Beta)population[j+1];
		}

		Alpha.gid++;
		Beta.gid++;

		double alpha_average = 0.0;
		double beta_average = 0.0;
		int alpha_count = 0;
		int beta_count = 0;

		// Battle loops 100 generations
		for (int i=0; i<100; i++) {
			// Sort by speed
			Arrays.sort(population, (a, b) -> b.getSpeed() - a.getSpeed());

			System.out.println("======= " + (i+1) + " =======");
			System.out.println("Poplation ID\tATK\tDEF\tHP\tSPD\tMTT");
			for (int j=0; j<Population.MAX*2; j++) {
				System.out.println(population[j]);
			}

			double alpha_win = 0;
			double beta_win = 0;
			if (i!=0) {
				alpha_win = (double)alpha_count / i * 100.0;
				beta_win = (double)beta_count / i * 100.0;
			}
			System.out.println("\tAlpha: " + String.format("%.2f", alpha_win) + "%(" + String.format("%.2f", alpha_average) + ")" +
				"\tBeta: " + String.format("%.2f", beta_win) + "%(" + String.format("%.2f", beta_average) + ")");
			System.out.println("================");
			Thread.sleep(700);

			int count = 10;
			// Loops until suvivor is 0 on Alpha or Beta
			while (Alpha.survivor > 0 && Beta.survivor > 0 && count > 0) {
				// Each population attacks
				for (int j=0; j<population.length; j++) {
					// do nothing if population is dead
					if (population[j].getHitPoint() <= 0) {
						continue;
					}

					Random random = new Random();
					// Attack
					if (population[j].getClass().getName().equals("Alpha")) {
						Alpha a = (Alpha)population[j];
						// If no enemy, population decide target
						if (a.getEnemy() == null) {
							a.setEnemy(beta, random.nextInt(Population.MAX));
						}
					} else {
						Beta b = (Beta)population[j];
						// If no enemy, population decide target
						if (b.getEnemy() == null) {
							b.setEnemy(alpha, random.nextInt(Population.MAX));
						}
					}

					population[j].attack();
					Thread.sleep(500);
				}

				count--;
				System.out.println("Alpha: " + Alpha.survivor + " vs " + Beta.survivor + " :Beta");
				System.out.println();
				Thread.sleep(500);
			}

			alpha_average = (alpha_average * i + Alpha.survivor) / (i+1);
			beta_average = (beta_average * i + Beta.survivor) / (i+1);

			if (Alpha.survivor < Beta.survivor) {
				beta_count++;
			} else if (Alpha.survivor > Beta.survivor) {
				alpha_count++;
			}

			Arrays.sort(alpha, (a, b) -> b.getScore() - a.getScore());
			Arrays.sort(beta, (a, b) -> b.getScore() - a.getScore());

			Population[] nextPopulation = new Population[Population.MAX*2];
			Alpha[] nextAlpha = new Alpha[Population.MAX];
			Beta[] nextBeta = new Beta[Population.MAX];

			// Select two populations who has best score from each team
			nextPopulation[0] = new Alpha(alpha[0], alpha[0]);
			nextPopulation[1] = new Alpha(alpha[1], alpha[1]);
			nextPopulation[2] = new Beta(beta[2], beta[2]);
			nextPopulation[3] = new Beta(beta[3], beta[3]);
			nextAlpha[0] = (Alpha)nextPopulation[0];
			nextAlpha[1] = (Alpha)nextPopulation[1];
			nextBeta[0] = (Beta)nextPopulation[2];
			nextBeta[1] = (Beta)nextPopulation[3];

			for (int j=2; j<Population.MAX; j+=2) {
				Random random = new Random();

				Alpha p_alpha1 = alpha[expectations[random.nextInt(100)]];
				Alpha p_alpha2 = alpha[expectations[random.nextInt(100)]];
				Beta p_beta1 = beta[expectations[random.nextInt(100)]];
				Beta p_beta2 = beta[expectations[random.nextInt(100)]];

				if (Beta.survivor == 0) {
					p_alpha1 = alpha[random.nextInt(10)];
					p_alpha2 = alpha[random.nextInt(10)];
				}

				if (Alpha.survivor == 0) {
					p_beta1 = beta[random.nextInt(10)];
					p_beta2 = beta[random.nextInt(10)];
				}

				Alpha c_alpha1 = new Alpha(p_alpha1.getAttack(), p_alpha2.getDeffence(), p_alpha1.getOriginalHitPoint(), p_alpha2.getSpeed());
				Alpha c_alpha2 = new Alpha(p_alpha2.getAttack(), p_alpha1.getDeffence(), p_alpha2.getOriginalHitPoint(), p_alpha1.getSpeed());

				Beta c_beta1 = new Beta(p_beta1.getAttack(), p_beta2.getDeffence(), p_beta1.getOriginalHitPoint(), p_beta2.getSpeed());
				Beta c_beta2 = new Beta(p_beta2.getAttack(), p_beta1.getDeffence(), p_beta2.getOriginalHitPoint(), p_beta1.getSpeed());

				if (random.nextDouble() < 0.05) {
					c_alpha1.mutation(random.nextInt(16));
				}

				if (random.nextDouble() < 0.05) {
					c_alpha2.mutation(random.nextInt(16));
				}

				if (random.nextDouble() < 0.05) {
					c_beta1.mutation(random.nextInt(16));
				}

				if (random.nextDouble() < 0.05) {
					c_beta2.mutation(random.nextInt(16));
				}

				nextAlpha[j] = c_alpha1;
				nextAlpha[j+1] = c_alpha2;
				nextBeta[j] = c_beta1;
				nextBeta[j+1] = c_beta2;
				nextPopulation[j*2] = nextAlpha[j];
				nextPopulation[j*2+1] = nextAlpha[j+1];
				nextPopulation[j*2+2] = nextBeta[j];
				nextPopulation[j*2+3] = nextBeta[j+1];

				System.out.println(c_alpha1.getId() + " and " + c_alpha2.getId() + " are created from " + p_alpha1.getId() + " and " + p_alpha2.getId());
				System.out.println(c_beta1.getId() + " and " + c_beta2.getId() + " are created from " + p_beta1.getId() + " and " + p_beta2.getId());
			}

			population = nextPopulation;
			alpha = nextAlpha;
			beta = nextBeta;

			Alpha.id = 1;
			Beta.id = 1;
			Alpha.gid++;
			Beta.gid++;
			Main.generation++;
		}
	}
} 

