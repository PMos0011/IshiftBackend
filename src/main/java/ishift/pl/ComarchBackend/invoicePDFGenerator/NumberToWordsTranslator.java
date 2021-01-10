package ishift.pl.ComarchBackend.invoicePDFGenerator;

public class NumberToWordsTranslator {

    private static String inWords ="";

    protected static String translateNumberToWords(long number) {
        final String[] UNITY = {"", "jeden ", "dwa ", "trzy ", "cztery ",
                "pięć ", "sześć ", "siedem ", "osiem ", "dziewięć "};

        final String[] TEENS = {"", "jedenaście ", "dwanaście ", "trzynaście ",
                "czternaście ", "piętnaście ", "szesnaście ", "siedemnaście ",
                "osiemnaście ", "dziewiętnaście "};

        final String[] DOZENS = {"", "dziesięć ", "dwadzieścia ",
                "trzydzieści ", "czterdzieści ", "pięćdziesiąt ",
                "sześćdziesiąt ", "siedemdziesiąt ", "osiemdziesiąt ",
                "dziewięćdziesiąt "};

        final String[] HUNDREDS = {"", "sto ", "dwieście ", "trzysta ", "czterysta ",
                "pięćset ", "sześćset ", "siedemset ", "osiemset ",
                "dziewięćset "};

        final String[][] GROUPS = {{"", "", ""},
                {"tysiąc ", "tysiące ", "tysięcy "},
                {"milion ", "miliony ", "milionów "},
                {"miliard ", "miliardy ", "miliardów "},
                {"bilion ", "biliony ", "bilionów "},
                {"biliard ", "biliardy ", "biliardów "},
                {"trylion ", "tryliony ", "trylionów "}};

        long unityAmount,
                teensAmount,
                dozensAmount,
                hundredsAmount,
                groupColumn,
                groupRow = 0;

        number = Math.abs(number);

        if (number == 0)
            inWords = "zero";

        while (number != 0) {
            hundredsAmount = number % 1000 / 100;
            dozensAmount = number % 100 / 10;
            unityAmount = number % 10;

            if (dozensAmount == 1 & unityAmount > 0) {
                teensAmount = unityAmount;
                dozensAmount = 0;
                unityAmount = 0;
            } else
                teensAmount = 0;

            if (unityAmount == 1 & hundredsAmount + dozensAmount + teensAmount == 0) {
                groupColumn = 0;

                if (hundredsAmount + dozensAmount == 0 && groupRow > 0) {
                    unityAmount = 0;
                    inWords = GROUPS[(int) groupRow][(int) groupColumn]
                            + inWords;
                }

            } else if (unityAmount > 1 && unityAmount < 5)
                groupColumn = 1;
            else
                groupColumn = 2;

            if (hundredsAmount + dozensAmount + teensAmount + unityAmount > 0)
                inWords = HUNDREDS[(int) hundredsAmount]
                        + DOZENS[(int) dozensAmount]
                        + TEENS[(int) teensAmount]
                        + UNITY[(int) unityAmount]
                        + GROUPS[(int) groupRow][(int) groupColumn]
                        + inWords;

            number /= 1000;
            groupRow++;
        }

        return inWords;
    }
}
