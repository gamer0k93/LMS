/**
 * Created by KOT on 17.12.2015.
 */
public class Main {
    public static void main(String[] args) {
        new Pypamid(3,5,' '); //1- deapazon chisel, 2- kolechestvo urovney, 3- semvol v pustote
    }
}

        /*
        * @param   amountOfNumbers  range of numbers (not greater than 9).
        * @param   levels           the number of pyramid levels (geometric progression).
        * @param   character        symbol of filling the void.
        */
class Pypamid {
    private int amountOfNumbers, levels, sumLevel;
    private char character;

    public Pypamid(int amountOfNumbers, int levels, char character) {
        if (amountOfNumbers < 1 || amountOfNumbers > 9) {
            System.out.println("Number must be from 1 to 9!");
            System.exit(0);
        }
        double l = Math.pow(2, levels - 1);
        sumLevel = (int) l;
        this.amountOfNumbers = amountOfNumbers;
        this.levels = levels;
        this.character = character;
        for (int numberLevel = 0; numberLevel < sumLevel; numberLevel++) {
            for (int row = 1; row <= amountOfNumbers; row++) {
                space(numberLevel);                                                    //indent the line
                for (int l1 = 0; l1 < numberLevel / 4 + 1; l1++) {
                    int lol = 0;
                    for(int lil=1; lil <= levels; lil++){                              //gaps in the pyramid
                        double sqr1 = Math.pow(2,lil);
                        int sqr2 = (int) sqr1;
                        if       ((numberLevel / (sqr2*2)) % 4 == 2 && l1%sqr2 >= sqr2/2) {
                            for (int i = 0; i < amountOfNumbers * 8; i++) System.out.print(character);
                            lol++;
                            break;
                        } else if((numberLevel / (sqr2*2)) % 4 == 0 && l1%sqr2 >= sqr2/2){
                            for (int i = 0; i < amountOfNumbers * 8; i++) System.out.print(character);
                            lol++;
                            break;
                        }
                    }
                    if(lol != 1) {
                        if (numberLevel % 4 == 0) {                                                        //level1
                            craftPiramid(row);                                                            //pypamid 1
                        } else if (numberLevel % 4 == 1) {
                            craftPiramid(row);                                                           //pypamid 1
                            craftPiramid(row);                                                           //pypamid 2
                        } else if (numberLevel % 4 == 2) {
                            craftPiramid(row);                                                           //pypamid 1
                            for (int i = 0; i < amountOfNumbers * 2; i++) System.out.print(character);//ne trogat
                            craftPiramid(row);
                        } else if (numberLevel % 4 == 3) {
                            craftPiramid(row);                                                           //pypamid 2
                            craftPiramid(row);
                            craftPiramid(row);                                                           //pypamid 3
                            craftPiramid(row);
                        }
                        if (l1 == numberLevel / 4) {
                        } else {
                            for (int i = 0; i < amountOfNumbers * (6 - numberLevel % 4 * 2); i++) System.out.print(character);
                        }
                    }
                }
                space(numberLevel);
                System.out.println();
            }
        }
    }

    private void  space(int x){
        for(int i=0; i < amountOfNumbers * (sumLevel - (x / 4 * 4 + (x % 4 + 1))); i++) System.out.print(character);
    }
    private void craftPiramid(int row){
        int number = 0;
        for (int col = 0; col < amountOfNumbers * 2; col++) {
            if (col < amountOfNumbers - row) {
                System.out.print(character);
            } else if (col < amountOfNumbers) {
                System.out.print(++number);
            } else if (number != 1) {
                System.out.print(--number);
            } else {
                System.out.print(character);
            }
        }
    }
}