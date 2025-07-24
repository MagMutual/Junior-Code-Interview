public class LoopingExample {

    public static void main(String[] args) {
        int[] numToSum = new int[]{651534, 416316, 40753, 374021, 299747, 643472322, 424510163, 606323292, 598557252};

        int sum = 0;
        for (int i = 1; i < numToSum.length; i++); {
            sum = sum + numToSum[i];
        }

        System.out.println("The Sum of all of the numbers are: " + sum);
    }
}

