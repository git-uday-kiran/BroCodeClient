package com.brocode.utils;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.List;

public class NotificationPojo {

	public String key;
	public String channelId;
	public String packageName;
	public String categoryName;
	public String timestamp;
	public Object title;
	public Object text;
	public Object subText;
	public Object bigText;
	public Object summeryText;

	public List<String> actions;
	public byte[] smallIconBytes;
	public byte[] largeIconBytes;

	private NotificationPojo(Builder builder) {
		key = builder.key;
		channelId = builder.channelId;
		packageName = builder.packageName;
		categoryName = builder.categoryName;
		timestamp = builder.timestamp;
		title = builder.title;
		text = builder.text;
		subText = builder.subText;
		bigText = builder.bigText;
		summeryText = builder.summeryText;
		actions = builder.actions;
		smallIconBytes = builder.smallIconBytes;
		largeIconBytes = builder.largeIconBytes;
	}

	@NonNull
	@Override
	public String toString() {
		return "NotificationPojo{" +
				"key='" + key + '\'' +
				", channelId='" + channelId + '\'' +
				", packageName='" + packageName + '\'' +
				", categoryName='" + categoryName + '\'' +
				", timestamp='" + timestamp + '\'' +
				", title=" + title +
				", text=" + text +
				", subText=" + subText +
				", bigText=" + bigText +
				", summeryText=" + summeryText +
				", actions=" + actions +
				", smallIconBytes=" + Arrays.toString(smallIconBytes) +
				", largeIconBytes=" + Arrays.toString(largeIconBytes) +
				'}';
	}

	public static final class Builder {
		private String key;
		private String channelId;
		private String packageName;
		private String categoryName;
		private String timestamp;
		private Object title;
		private Object text;
		private Object subText;
		private Object bigText;
		private Object summeryText;
		private List<String> actions;
		private byte[] smallIconBytes;
		private byte[] largeIconBytes;

		private Builder() {
		}

		public static Builder newBuilder() {
			return new Builder();
		}

		public Builder setKey(String val) {
			key = val;
			return this;
		}

		public Builder setChannelId(String val) {
			channelId = val;
			return this;
		}

		public Builder setPackageName(String val) {
			packageName = val;
			return this;
		}

		public Builder setCategoryName(String val) {
			categoryName = val;
			return this;
		}

		public Builder setTimestamp(String val) {
			timestamp = val;
			return this;
		}

		public Builder setTitle(Object val) {
			title = val;
			return this;
		}

		public Builder setText(Object val) {
			text = val;
			return this;
		}

		public Builder setSubText(Object val) {
			subText = val;
			return this;
		}

		public Builder setBigText(Object val) {
			bigText = val;
			return this;
		}

		public Builder setSummeryText(Object val) {
			summeryText = val;
			return this;
		}

		public Builder setActions(List<String> val) {
			actions = val;
			return this;
		}

		public Builder setSmallIconBytes(byte[] val) {
			smallIconBytes = val;
			return this;
		}

		public Builder setLargeIconBytes(byte[] val) {
			largeIconBytes = val;
			return this;
		}

		public NotificationPojo build() {
			return new NotificationPojo(this);
		}
	}
}


