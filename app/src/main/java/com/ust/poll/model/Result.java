// CSIT 6000B    #  CHAN Shing Chuen     20286820     scchanak@connect.ust.hk
// CSIT 6000B    #  MA Ka Kin            20286533     kkmaab@connect.ust.hk

package com.ust.poll.model;

public class Result implements Comparable {


	private String title;
	private Integer count;

	public Result(int count, String title) {
		this.count = count;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String pollId) {
		this.title = title;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	//@Override
	//public int compareTo(Result compareresult) {
	//	int compareage=((Result)compareresult).getCount();
        /* For Ascending order*/
	//	return this.count-compareage;

        /* For Descending order do like this */
		//return compareage-this.studentage;
	///}

	@Override
	public String toString() {
		return "Option:" + title + ", voted:" + count + "";
	}


	@Override
	public int compareTo(Object another) {
		int compareage=((Result)another).getCount();
		return compareage-this.count;
	}
}
