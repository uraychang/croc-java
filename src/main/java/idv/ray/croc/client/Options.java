package idv.ray.croc.client;

public class Options {

	private int relayPort = 9001;
	private String relayAddress = "localhost";
	private boolean TransferOnlyOnLocal = false;
	private String downloadPath = "download";
	private boolean OverwriteFile = true;

	/* socket timeout */
	public static final int WAIT_RECEIVER_TIMEOUT = 1000 * 60 * 60 * 2;// 2hrs
	public static final int SOCKET_TIMEOUT = 1000 * 30;
	/* buffer sizes */
	public static final int FILE_BUFFER_SIZE = 1024 * 4;

	public int getRelayPort() {
		return relayPort;
	}

	public void setRelayPort(int relayPort) {
		this.relayPort = relayPort;
	}

	public String getRelayAddress() {
		return relayAddress;
	}

	public void setRelayAddress(String relayAddress) {
		this.relayAddress = relayAddress;
	}

	public boolean isTransferOnlyOnLocal() {
		return TransferOnlyOnLocal;
	}

	public void setTransferOnlyOnLocal(boolean transferOnlyOnLocal) {
		TransferOnlyOnLocal = transferOnlyOnLocal;
	}

	public boolean isOverwriteFile() {
		return OverwriteFile;
	}

	public void setOverwriteFile(boolean overwriteFile) {
		OverwriteFile = overwriteFile;
	}

	public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}

}
