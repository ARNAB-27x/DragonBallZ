import java.util.*;

class Character {
    String name;
    int health = 100, mana = 100;
    int maxHealth = 100;
    int form = 0;

    boolean stunned = false;
    boolean powerBoost = false;

    Character(String name) {
        this.name = name;
    }

    // 🔥 DAMAGE MULTIPLIER BASED ON FORM
    double getMultiplier() {
        return switch (name) {
            case "Goku" -> switch (form) {
                case 1 -> 1.3; // SSJ1
                case 2 -> 1.6; // SSJ2
                case 3 -> 2.0; // SSJ3
                case 4 -> 2.5; // Ultra Instinct
                default -> 1.0;
            };
            case "Vegeta" -> switch (form) {
                case 1 -> 1.3;
                case 2 -> 1.7;
                case 3 -> 2.2; // Majin
                default -> 1.0;
            };
            case "Gohan" -> switch (form) {
                case 1 -> 1.2;
                case 2 -> 1.8;
                case 3 -> 2.3; // Ultimate
                default -> 1.0;
            };
            case "Frieza" -> switch (form) {
                case 1 -> 1.2;
                case 2 -> 1.5;
                case 3 -> 1.9;
                case 4 -> 2.6; // Golden
                default -> 1.0;
            };
            default -> 1.0;
        };
    }

    int applyDamage(int base) {
        double dmg = base * getMultiplier();
        if (powerBoost) dmg *= 1.5;
        return (int) dmg;
    }

    void punch(Character enemy) {
        int dmg = applyDamage(10);
        enemy.health -= dmg;
        System.out.println(name + " punches! (-" + dmg + ")");
    }

    void kick(Character enemy) {
        int dmg = applyDamage(15);
        enemy.health -= dmg;
        System.out.println(name + " kicks! (-" + dmg + ")");
    }

    void charge() {
        mana = Math.min(100, mana + 25);
        System.out.println(name + " is charging energy...");
    }

    void transform() {
        switch (name) {
            case "Goku" -> gokuForms();
            case "Vegeta" -> vegetaForms();
            case "Gohan" -> gohanForms();
            case "Frieza" -> friezaForms();
        }
    }

    void gokuForms() {
        String[] forms = {"SSJ1", "SSJ2", "SSJ3", "ULTRA INSTINCT"};
        int[] cost = {30, 50, 70, 100};

        if (form < forms.length && mana >= cost[form]) {
            mana -= cost[form];
            form++;
            powerUp(forms[form - 1]);
        } else System.out.println("Cannot transform!");
    }

    void vegetaForms() {
        String[] forms = {"SSJ1", "SSJ2", "MAJIN"};
        int[] cost = {30, 50, 80};

        if (form < forms.length && mana >= cost[form]) {
            mana -= cost[form];
            form++;
            powerUp(forms[form - 1]);
        } else System.out.println("Cannot transform!");
    }

    void gohanForms() {
        String[] forms = {"SSJ", "SSJ2", "ULTIMATE"};
        int[] cost = {30, 50, 80};

        if (form < forms.length && mana >= cost[form]) {
            mana -= cost[form];
            form++;
            powerUp(forms[form - 1]);
        } else System.out.println("Cannot transform!");
    }

    void friezaForms() {
        String[] forms = {"FORM2", "FORM3", "FINAL", "GOLDEN"};
        int[] cost = {30, 50, 70, 100};

        if (form < forms.length && mana >= cost[form]) {
            mana -= cost[form];
            form++;
            powerUp(forms[form - 1]);
        } else System.out.println("Cannot transform!");
    }

    void powerUp(String formName) {
        maxHealth += 20;
        health = maxHealth;
        System.out.println(name + " transformed into " + formName + "!!!");
    }

    void specialMove(Character enemy) {
        if (mana < 30) {
            System.out.println("Not enough mana!");
            return;
        }

        mana -= 30;

        switch (name) {
            case "Goku" -> {
                int dmg = applyDamage(35);
                System.out.println("KAMEHAMEHA!!! (-" + dmg + ")");
                enemy.health -= dmg;
            }
            case "Vegeta" -> {
                int dmg = applyDamage(40);
                System.out.println("FINAL FLASH!!! (-" + dmg + ")");
                enemy.health -= dmg;
            }
            case "Gohan" -> {
                int dmg = applyDamage(30);
                System.out.println("MASENKO!!! STUN (-" + dmg + ")");
                enemy.health -= dmg;
                enemy.stunned = true;
            }
            case "Frieza" -> {
                int dmg = applyDamage(28);
                System.out.println("DEATH BEAM!!! BOOST (-" + dmg + ")");
                enemy.health -= dmg;
                powerBoost = true;
            }
        }
    }

    boolean isAlive() {
        return health > 0;
    }
}

public class DBZGame {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[32m";
    static final String BLUE = "\u001B[34m";
    static final String YELLOW = "\u001B[33m";
    static final String RED = "\u001B[31m";

    public static void main(String[] args) {

        Character p1 = choose("Player 1");
        Character p2 = choose("Player 2");

        System.out.println("1. PvP\n2. Vs AI");
        int mode = sc.nextInt();

        while (p1.isAlive() && p2.isAlive()) {

            turn(p1, p2);
            if (!p2.isAlive()) break;

            if (mode == 2) aiTurn(p2, p1);
            else turn(p2, p1);
        }

        System.out.println("\n" + (p1.isAlive() ? p1.name : p2.name) + " WINS!");
    }

    static Character choose(String player) {
        System.out.println(player + " choose:");
        System.out.println("1.Goku 2.Vegeta 3.Gohan 4.Frieza");

        int c = sc.nextInt();

        return switch (c) {
            case 1 -> new Character("Goku");
            case 2 -> new Character("Vegeta");
            case 3 -> new Character("Gohan");
            default -> new Character("Frieza");
        };
    }

    static void turn(Character p, Character enemy) {

        if (p.stunned) {
            System.out.println(p.name + " is stunned!");
            p.stunned = false;
            return;
        }

        drawScreen(p, enemy);

        System.out.println(p.name + " Turn:");
        System.out.println("1.Punch 2.Kick 3.Special 4.Transform 5.Charge");

        int c = sc.nextInt();

        switch (c) {
            case 1 -> p.punch(enemy);
            case 2 -> p.kick(enemy);
            case 3 -> p.specialMove(enemy);
            case 4 -> p.transform();
            case 5 -> p.charge();
        }
    }

    static void aiTurn(Character ai, Character enemy) {
        int c = rand.nextInt(5) + 1;

        switch (c) {
            case 1 -> ai.punch(enemy);
            case 2 -> ai.kick(enemy);
            case 3 -> ai.specialMove(enemy);
            case 4 -> ai.transform();
            case 5 -> ai.charge();
        }
    }

    static void drawScreen(Character p1, Character p2) {

        System.out.println("\n================================================================");

        String p1HP = GREEN + p1.name + " HP: [" + bar(p1.health) + "] " + p1.health + RESET;
        String p2HP = GREEN + p2.name + " HP: [" + bar(p2.health) + "] " + p2.health + RESET;

        String p1MP = BLUE + p1.name + " MP: [" + bar(p1.mana) + "] " + p1.mana + RESET;
        String p2MP = BLUE + p2.name + " MP: [" + bar(p2.mana) + "] " + p2.mana + RESET;

        System.out.printf("%-45s %45s\n", p1HP, p2HP);
        System.out.printf("%-45s %45s\n", p1MP, p2MP);

        System.out.println();

        System.out.printf("%-45s %45s\n", "           " + face(p1), face(p2) + "           ");
        System.out.printf("%-45s %45s\n", "          /|\\", "/|\\          ");
        System.out.printf("%-45s %45s\n", "          / \\", "/ \\          ");

        System.out.println("================================================================");
    }

    static String bar(int val) {
        int total = 20;
        int filled = (val * total) / 100;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < filled; i++) sb.append("|");
        for (int i = filled; i < total; i++) sb.append(" ");

        return sb.toString();
    }

    static String face(Character c) {
        if (c.form == 0) return "O";
        if (c.form == 1) return YELLOW + "O" + RESET;
        if (c.form == 2) return YELLOW + "0" + RESET;
        if (c.form == 3) return RED + "O" + RESET;
        return BLUE + "O" + RESET;
    }
}