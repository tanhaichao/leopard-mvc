package io.leopard.web.upload;

/**
 * 文件上传.
 * 
 * @author 阿海
 *
 */
public class Upload {

	/**
	 * 文件路径.
	 */
	private String path;

	private byte[] data;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
