package com.emc.it.airwatchplugin;

import java.io.Serializable;

/**
 * @author elumam
 *
 */

public class AirwatchDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String transactionId;

	private String chunkData;
	private Integer chunkSequenceNumber;
	private Long totalApplicationSize;
	private int chunkSize;
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	/**
	 * @return the chunkData
	 */
	public String getChunkData() {
		return chunkData;
	}
	/**
	 * @param chunkData the chunkData to set
	 */
	public void setChunkData(String chunkData) {
		this.chunkData = chunkData;
	}
	/**
	 * @return the chunkSequenceNumber
	 */
	public Integer getChunkSequenceNumber() {
		return chunkSequenceNumber;
	}
	/**
	 * @param chunkSequenceNumber the chunkSequenceNumber to set
	 */
	public void setChunkSequenceNumber(Integer chunkSequenceNumber) {
		this.chunkSequenceNumber = chunkSequenceNumber;
	}
	/**
	 * @return the totalApplicationSize
	 */
	public Long getTotalApplicationSize() {
		return totalApplicationSize;
	}
	/**
	 * @param totalApplicationSize the totalApplicationSize to set
	 */
	public void setTotalApplicationSize(Long totalApplicationSize) {
		this.totalApplicationSize = totalApplicationSize;
	}
	/**
	 * @return the chunkSize
	 */
	public int getChunkSize() {
		return chunkSize;
	}
	/**
	 * @param length the chunkSize to set
	 */
	public void setChunkSize(int length) {
		this.chunkSize = length;
	}
		

}
