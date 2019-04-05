import java.time.Instant;

public class Test{
    public static void main(String[] args) {
        Instant now = Instant.now();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long i = Instant.now().getEpochSecond() - now.getEpochSecond();
        System.out.println(i);
    }
}