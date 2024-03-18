package discUsage;
public class DiscDetails {
	Long totalSpace;
	Long usableSpace;
	
	public DiscDetails() {
		super();
	}
	
	public DiscDetails(Long totalSpace, Long usableSpace) {
		super();
		this.totalSpace = totalSpace;
		this.usableSpace = usableSpace;
	}

	public Long getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(Long totalSpace) {
		this.totalSpace = totalSpace;
	}
	public Long getUsableSpace() {
		return usableSpace;
	}
	public void setUsableSpace(Long usableSpace) {
		this.usableSpace = usableSpace;
	}

	@Override
	public String toString() {
		return "DiscDetails [totalSpace=" + totalSpace + ", usableSpace=" + usableSpace + "]";
	}
	
}
