package com.brocode.models;

import java.util.List;

public class NotificationPojo {
	public String key;

	public String packageName;
	public String timeStamp ;
	public String title;
	public String text;

	public byte[] smallIconBytes;
	public byte[] largIconBytes;

	public List<String> actions;

	private NotificationPojo(Builder builder) {
		key = builder.key;
		packageName = builder.packageName;
		timeStamp = builder.timeStamp;
		title = builder.title;
		text = builder.text;
		smallIconBytes = builder.smallIconBytes;
		largIconBytes = builder.largIconBytes;
		actions = builder.actions;
	}

	public static final class Builder {
		private String key;
		private String packageName;
		private String timeStamp;
		private String title;
		private String text;
		private byte[] smallIconBytes;
		private byte[] largIconBytes;
		private List<String> actions;

		private Builder() {
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		public Builder setKey(String val) {
			key = val;
			return this;
		}

		public Builder setPackageName(String val) {
			packageName = val;
			return this;
		}

		public Builder setTimeStamp(String val) {
			timeStamp = val;
			return this;
		}

		public Builder setTitle(String val) {
			title = val;
			return this;
		}

		public Builder setText(String val) {
			text = val;
			return this;
		}

		public Builder setSmallIconBytes(byte[] val) {
			smallIconBytes = val;
			return this;
		}

		public Builder setLargIconBytes(byte[] val) {
			largIconBytes = val;
			return this;
		}

		public Builder setActions(List<String> val) {
			actions = val;
			return this;
		}

		public NotificationPojo build() {
			return new NotificationPojo(this);
		}
	}
}


