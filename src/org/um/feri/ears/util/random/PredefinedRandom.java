package org.um.feri.ears.util.random;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PredefinedRandom extends RandomGenerator {

    public static void main(String[] args) {
        RNG.setSelectedRandomGenerator(RNG.RngType.PREDEFINED_RANDOM);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            System.out.println(RNG.nextDouble());
        }
    }

    private List<Double> predefinedNumbers;
    private int currentIndex;

    public PredefinedRandom() {
        this.predefinedNumbers = Arrays.asList(0.9050525774669225, 0.2709422160471633, 0.9938821719372665,
                0.9020385351876491, 0.5296578325961951, 0.5455313704461174, 0.6598445006145756, 0.6428127428383863,
                0.7180826722425252, 0.0063484503409309, 0.2791891703816942, 0.215124143995853, 0.9502722197484932,
                0.3483929972336235, 0.08050949842928701, 0.05017505316285198, 0.7662947104605, 0.3444196554274741,
                0.718769481325461, 0.8007104274850757, 0.17007502438324584, 0.48227347410773436, 0.9614591422029354,
                0.6318875918918209, 0.9847588333386067, 0.6977539295440046, 0.9584753625385458, 0.141747432555698,
                0.09601705756942391, 0.46866292799693654, 0.06768334315245794, 0.16961850211503182, 0.036202244018972984,
                0.2788565520689362, 0.7403390680897094, 0.13457399622188304, 0.16011955621981955, 0.7355341732486469,
                0.2840303582646937, 0.9611016626745491, 0.7422770323761118, 0.42866256102371336, 0.41552087026066564,
                0.09543429384667512, 0.6858311188038186, 0.9583469231872995, 0.3918570787313479, 0.5935293715909289,
                0.17504512937483063, 0.42412441446324023, 0.165325948089469, 0.7704733051987637, 0.5259298156618439,
                0.06957033946189595, 0.4463265676693875, 0.4411271402102892, 0.6938368359154905, 0.7062707713760932,
                0.7779463388174521, 0.3531174546755502, 0.3821272559976948, 0.003129710797692198, 0.7270085460749394,
                0.01581690843362793, 0.46263928542238997, 0.7201427890688616, 0.07858430125094107, 0.7627469241810217,
                0.46653030956553787, 0.4901838015500386, 0.4844396174688258, 0.549874756696444, 0.23317066671454745,
                0.8080140695133502, 0.36061323515801547, 0.6885655597269382, 0.4151171682702276, 0.4726390267534968,
                0.95529907174284, 0.43948976033613363, 0.4287970507023934, 0.8473004802676307, 0.4154324237551569,
                0.15257486279525923, 0.6133836915668683, 0.9508065842823168, 0.36885655947599505, 0.4281296671344438,
                0.81444746118513, 0.5745663475281276, 0.008671959470888169, 0.5955582632017986, 0.3443165197126229,
                0.33160900628920187, 0.9173482297384907, 0.6586201554311661, 0.2876839066324026, 0.7870328669441157,
                0.9004039039744101, 0.7399817696182611);
        this.currentIndex = 0;
    }

    private double getNextPredefinedNumber() {
        double number = predefinedNumbers.get(currentIndex);
        currentIndex = (currentIndex + 1) % predefinedNumbers.size();
        return number;
    }
    @Override
    public float nextFloat() {
        return (float) getNextPredefinedNumber();
    }

    @Override
    public int nextInt() {
        double normalized = (getNextPredefinedNumber() % 1.0);
        return (int)(Integer.MIN_VALUE + (long)(normalized * ((long)Integer.MAX_VALUE - Integer.MIN_VALUE)));
    }

    @Override
    public int nextInt(int upper) {
        double normalized = (getNextPredefinedNumber() % 1.0);
        return (int) (normalized * upper);
    }

    @Override
    public long nextLong() {
        return (long) getNextPredefinedNumber();
    }

    @Override
    public double nextDouble() {
        return getNextPredefinedNumber();
    }

    @Override
    void nextBytes(byte[] bytes, int start, int len) {
        for (int i = start; i < start + len; i++) {
            bytes[i] = (byte) getNextPredefinedNumber();
        }
    }
}
