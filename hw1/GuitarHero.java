public class GuitarHero {
    private static String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";

    public static void main(String[] args) {
        /* create array of guitar strings */
        synthesizer.GuitarString[] stringKEYS = new synthesizer.GuitarString[37];
        for (int i = 0; i < 37; i += 1){
            double CONCERT = 440 * Math.pow(2, (i - 24) / 12);
            stringKEYS[i] = new synthesizer.GuitarString(CONCERT);
        }

        int keyindex = 0;

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                try {
                    keyindex = keyboard.indexOf(key);
                    stringKEYS[keyindex].pluck();
                } catch(Exception e){
                    System.out.println("KEY does not exist");
                }
            }

            /* compute the superposition of samples */
            double sample = stringKEYS[keyindex].sample();

            /* play the sample on standard audio */
            StdAudio.play(sample);

            /* advance the simulation of each guitar string by one step */
            stringKEYS[keyindex].tic();
        }
    }
}
