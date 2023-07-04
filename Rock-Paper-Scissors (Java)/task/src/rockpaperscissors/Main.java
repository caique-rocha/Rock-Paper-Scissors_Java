package rockpaperscissors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        String[] defaultList = {"rock", "paper", "scissors"};

        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        String input;
        String computer;

        Player player = new Player();

        System.out.println("Enter your name: ");
        player.setName(scanner.nextLine());
        System.out.printf("Hello, %s\n", player.getName());

        String[] list = scanner.nextLine().split(",");
        System.out.println("Okay, let's start");

        File file = new File("rating.txt");

        getPlayerRating(player, file);

        while (true) {

            if (list.length < 3) { // jodada padrão (rock, paper, scissors)

                computer = defaultList[random.nextInt(3)];
            } else {
                computer = list[random.nextInt(list.length)];
            }

            input = scanner.nextLine();

            if (input.equals("!exit")) { // encerra a partida

                System.out.println("Bye!");
                try (PrintWriter printWriter = new PrintWriter(file)) {
                    printWriter.printf("%s %d", player.getName(), player.getScore());

                } catch (IOException e) {
                    System.out.printf("An exception occurred %s", e.getMessage());
                }
                break;

            } else if (input.equals("rock") || input.equals("scissors") || input.equals("paper") ||
                    List.of(list).contains(input)) { // verifica se a entrada é valida e verifica o resultado

                checkWinner(input, computer, player);

            } else if (input.equals("!rating")) { // obtem a pontuação do jogador
                System.out.printf("Your rating: %d\n", player.getScore());

            } else { // entrada inválida

                System.out.println("Invalid input");
            }
        }
    }

    /**
     * Obtem a pontuação do jogador armazenada em um arquivo e imprime o valor
     * @param player jogador atual
     * @param file arquivo onde as pontuações estão armazenadas
     */
    public static void getPlayerRating(Player player, File file) {

        try (Scanner scannerFile = new Scanner(file)) {
            String name;
            int score;

            while (scannerFile.hasNext()) {
                String[] line = scannerFile.nextLine().split(" ");
                name = line[0];
                score = Integer.parseInt(line[1]);

                if (player.getName().equals(name)) { // verifica o nome na lista
                    player.setScore(score);
                    break;
                }
            }


            try (PrintWriter printWriter = new PrintWriter(file)) {
                printWriter.printf("%s %d", player.getName(), player.getScore());

            } catch (IOException e) {
                System.out.printf("An exception occurred %s", e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.printf("File not found: %s", file.getName());
        }
    }

    /**
     * Verifica qual jogador venceu ou se houve empate e imprime a informação
     *
     * @param play a escolha do jogador
     * @param computer a escolha da máquina
     * @param player o jogador atual
     */
    public static void checkWinner(String play, String computer, Player player) {

        String[] listOfOptions = ("rock,gun,lightning,devil,dragon,water,air,paper,sponge,wolf," +
                "tree,human,snake,scissors,fire").split(",");

        String[] shiftList = new String[listOfOptions.length];
        String playChoose = play.toLowerCase();


        int index = getIndex(playChoose, listOfOptions);
        int shift = listOfOptions.length + listOfOptions.length / 2 + index + 1;


        // desloca os itens da lista de opções em um novo array de acordo o valor de shift
        for (int i = 0; i < listOfOptions.length; i++)
            shiftList[i] = listOfOptions[(shift + i) % listOfOptions.length];


        if (play.equals(computer)) {

            System.out.printf("There is a draw (%s)\n", playChoose);
            player.scorePlus50();

        } else if (getIndex(playChoose, shiftList) < getIndex(computer, shiftList)) {
            System.out.printf("Sorry, but the computer chose %s\n", computer);

        } else {

            System.out.printf("Well done. The computer chose %s and failed\n", computer);
            player.scorePlus100();
        }
    }

    /**
     * Obtem o indice onde uma palavra se localiza em um array
     * @param word item desejado de se obtem o indice
     * @param listOfOptions Array de busca
     * @return o indice da palavra e -1 caso não encontre
     */
    public static int getIndex(String word, String[] listOfOptions) {

        for (int i = 0; i < listOfOptions.length; i++) {

            if (word.equals(listOfOptions[i])) {
                return i;
            }
        }

        return -1;
    }

    public static class Player {

        private String name;
        private int score = 0;

        public Player() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public void scorePlus50() {
            score += 50;
        }

        public void scorePlus100() {
            score += 100;
        }
    }

}
