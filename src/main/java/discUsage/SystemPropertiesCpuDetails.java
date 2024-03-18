package discUsage;
public class SystemPropertiesCpuDetails {

	private int threadCount;
	private double totalThreads;
	private int handleCount;
	private double totalHandles;
	private int totalProcess;

	public SystemPropertiesCpuDetails() {
		super();
	}

	public SystemPropertiesCpuDetails(int threadCount, double totalThreads, int handleCount, double totalHandles,
			int totalProcess) {
		super();
		this.threadCount = threadCount;
		this.totalThreads = totalThreads;
		this.handleCount = handleCount;
		this.totalHandles = totalHandles;
		this.totalProcess = totalProcess;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public double getTotalThreads() {
		return totalThreads;
	}

	public void setTotalThreads(double totalThreads) {
		this.totalThreads = totalThreads;
	}

	public int getHandleCount() {
		return handleCount;
	}

	public void setHandleCount(int handleCount) {
		this.handleCount = handleCount;
	}

	public double getTotalHandles() {
		return totalHandles;
	}

	public void setTotalHandles(double totalHandles) {
		this.totalHandles = totalHandles;
	}

	public int getTotalProcess() {
		return totalProcess;
	}

	public void setTotalProcess(int totalProcess) {
		this.totalProcess = totalProcess;
	}

	@Override
	public String toString() {
		return "SystemPropertiesCpuDetails [threadCount=" + threadCount + ", totalThreads=" + totalThreads
				+ ", handleCount=" + handleCount + ", totalHandles=" + totalHandles + ", totalProcess=" + totalProcess
				+ "]";
	}

}
