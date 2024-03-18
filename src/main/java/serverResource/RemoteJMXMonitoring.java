package serverResource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import  com.sun.management.OperatingSystemMXBean; 

public class RemoteJMXMonitoring {

    public static void main(String[] args) {
        while (true) {
            printSystemInfo();
            try {
                Thread.sleep(5000); // Sleep for 5 seconds (adjust as needed)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void printSystemInfo() {
//        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        java.lang.management.OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

        if (osBean instanceof OperatingSystemMXBean) {
            OperatingSystemMXBean sunOsBean = (OperatingSystemMXBean) osBean;

            // Print CPU usage
            System.out.println("CPU Usage: " + (int) (sunOsBean.getSystemCpuLoad() * 100) + "%");
        } else {
            System.out.println("CPU Usage: Not supported on this platform.");
        }

        // Print Memory usage
        Runtime runtime = Runtime.getRuntime();

        // Get the total memory currently available to the JVM
        long totalMemory = runtime.totalMemory();

        // Get the amount of free memory in the JVM
        long freeMemory = runtime.freeMemory();

        // Calculate used memory
        long usedMemory = totalMemory - freeMemory;

        // Convert memory sizes from bytes to megabytes for easier readability
        long totalMemoryInMB = totalMemory / (1024 * 1024);
        long usedMemoryInMB = usedMemory / (1024 * 1024);
        long freeMemoryInMB = freeMemory / (1024 * 1024);

        System.out.println("Total Memory: " + totalMemoryInMB + " MB");
        System.out.println("Used Memory: " + usedMemoryInMB + " MB");
        System.out.println("Free Memory: " + freeMemoryInMB + " MB");

        System.out.println("------------------------");
    }
}
