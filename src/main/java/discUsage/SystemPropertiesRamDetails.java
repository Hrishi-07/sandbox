package discUsage;


public class SystemPropertiesRamDetails {
	private long totalMemory;
	private long freeMemory;
	private long consumedMemory;
	private long applicationRamUsage;

	public SystemPropertiesRamDetails() {
		super();
	}

	public SystemPropertiesRamDetails(long totalMemory, long freeMemory, long consumedMemory,
			long applicationRamUsage) {
		super();
		this.totalMemory = totalMemory;
		this.freeMemory = freeMemory;
		this.consumedMemory = consumedMemory;
		this.applicationRamUsage = applicationRamUsage;
	}

	public long getTotalMemory() {
		return totalMemory;
	}

	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}

	public long getFreeMemory() {
		return freeMemory;
	}

	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}

	public long getConsumedMemory() {
		return consumedMemory;
	}

	public void setConsumedMemory(long consumedMemory) {
		this.consumedMemory = consumedMemory;
	}

	public long getApplicationRamUsage() {
		return applicationRamUsage;
	}

	public void setApplicationRamUsage(long applicationRamUsage) {
		this.applicationRamUsage = applicationRamUsage;
	}

	@Override
	public String toString() {
		return "SystemPropertiesRamDetails [totalMemory=" + totalMemory + ", freeMemory=" + freeMemory
				+ ", consumedMemory=" + consumedMemory + ", applicationRamUsage=" + applicationRamUsage + "]";
	}

}
