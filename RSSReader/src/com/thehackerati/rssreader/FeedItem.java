package com.thehackerati.rssreader;

import java.io.Serializable;

public class FeedItem implements Serializable {
	
	private String appTitle;
	private String appSummary;
	private String appPrice;
	private String appDeveloper;
	private String appLink;
	
	public FeedItem() {
		
	}
	
	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

	public String getAppSummary() {
		return appSummary;
	}

	public void setAppSummary(String appSummary) {
		this.appSummary = appSummary;
	}

	
	public String getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(String appPrice) {
		this.appPrice = appPrice;
	}

	public String getAppDeveloper() {
		return appDeveloper;
	}

	public void setAppDeveloper(String appDeveloper) {
		this.appDeveloper = appDeveloper;
	}

	public String getAppLink() {
		return appLink;
	}

	public void setAppLink(String appLink) {
		this.appLink = appLink;
	}

	@Override
	public String toString() {
		return getAppTitle();
	}
}
